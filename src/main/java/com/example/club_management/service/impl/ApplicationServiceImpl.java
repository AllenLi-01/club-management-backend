package com.example.club_management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.club_management.entity.Application;
import com.example.club_management.entity.User_club;
import com.example.club_management.mapper.*;
import com.example.club_management.service.ApplicationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.club_management.service.ClubService;
import com.example.club_management.utils.Response;
import com.example.club_management.utils.ResponseCode;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xinn
 * @since 2023-09-27
 */
@Service
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Application> implements ApplicationService {

    @Autowired
    ClubMapper clubMapper;
    @Autowired
    User_clubMapper userClubMapper;
    @Autowired
    ApplicationMapper applicationMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ClubService clubService;
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    MessageMapper messageMapper;

    public Response processJoinApplication(Application application){
        int id = application.getId();
        if(applicationMapper.selectById(application.getId())==null) return Response.failure("申请记录不存在！"+application.getId());
        String status = application.getStatus();
        int reviewerId = application.getReviewerId();
        //根据申请编号获取申请详情
        application = applicationMapper.selectById(id);
        if(!application.getStatus().equals("PENDING"))  return Response.failure("该申请已被处理！");

        User_club reviewerRecord = userClubMapper
                .selectOne(new QueryWrapper<User_club>().eq("user_id",reviewerId).eq("club_id",application.getClubId()));
        if(reviewerRecord==null)  return Response.failure("您不是此社团成员。");
        String reviewerRole =  reviewerRecord.getRole();
        if(reviewerRole.equals("STUDENT"))   return Response.failure("您不具有处理此申请的权限。原因：您不是社团管理员。");
        LocalDateTime applyTime = application.getApplyTime();
        Response response = new Response();
        if(status.equals("APPROVED")){//如果批准，则执行加入成员方法
            User_club userClub = new User_club();
            userClub.setClubId(application.getClubId()).setUserId(application.getApplicantId()).setJoinTime(applyTime);
            response =  clubService.addMember(userClub);
            if (!response.isSuccess()) return response;
        }else {
            response.setSuccess(true)
                    .setCode(ResponseCode.LOGIN_SUCCESS);
        }
        application.setReviewTime(LocalDateTime.now())
                .setReviewerId(reviewerId)
                .setStatus(status);
        applicationMapper.updateById(application);
        return response;
    }

    public Response getApplyList(int userId,int page,int limit){
        Page<Application> mypage = new Page<>(page,limit);
        QueryWrapper<Application> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("applicant_id",userId);
        queryWrapper.orderByDesc("id");
        IPage iPage = applicationMapper.selectPage(mypage,queryWrapper);
        List<Application> applicationList = iPage.getRecords();
        for(Application application :applicationList){
            if (application.getClubId() != null) {
                application.setClubName(clubMapper.selectById(application.getClubId()).getName());
            }

        }


        return Response.ok()
                .data("total",iPage.getTotal())
                .data("items",iPage.getRecords());
    }

    public Response join(Application application){
        int club_id = application.getClubId();
        int applicant_id = application.getApplicantId();
        //查询用户id是否在数据库中
        if(userMapper.selectById(applicant_id)==null)   return Response.failure("用户ID不存在，请重新登录或联系管理员！");
        //先查询社团是否存在
        if(clubMapper.selectById(club_id)==null)    return Response.failure("申请加入的社团不存在！");
        //查看是否已在此社团中
        if(userClubMapper.checkJoined(applicant_id,club_id)!=null)    return Response.failure("已加入此社团了！");
        //查看是否已经申请，且申请正在审核中
        if(applicationMapper.getJoinApplication(applicant_id,club_id)!=null)   return Response.failure("已经向此社团递交了申请。");
        application.setType("JOIN_CLUB");

        this.save(application);
        int applyID = application.getId();

        return Response.ok().data("apply_id",applyID);

    }
    public Response createClub(Application application){
        //首先检查已注册的社团中有无重名
        JsonNode detail = application.getApplicationDetail();
        String name = detail.get("name").asText();
        if(clubMapper.selectByName(name)!=null)   return Response.failure("此社团名已被使用。请尝试换一个社团名。");

        //再检查提交申请中的社团有无重名
        List<Application> applyingList = applicationMapper.getApplyByDetail("name",name);
        if(applyingList!=null) {
            for(Application apply : applyingList){
                if(apply.getStatus().equals("PENDING")) return Response.failure("申请列表中存在此社团名。请尝试换一个社团名。");
            }
        }


        int applicant_id = application.getApplicantId();
        //查询用户id是否在数据库中
        if(userMapper.selectById(applicant_id)==null)   return Response.failure("用户ID不存在，请重新登录或联系管理员！");

        this.save(application);
        int appId = application.getId();

        return Response.ok().data("apply_id",appId);
    }
}
