package com.example.club_management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.club_management.entity.Application;
import com.example.club_management.mapper.*;
import com.example.club_management.service.ApplicationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.club_management.utils.Response;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    ActivityMapper activityMapper;
    @Autowired
    MessageMapper messageMapper;

    public Response getApplyList(int userId,int page,int limit){
        Page<Application> mypage = new Page<>(page,limit);
        QueryWrapper<Application> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("applicant_id",userId);
        queryWrapper.orderByDesc("id");
        IPage iPage = applicationMapper.selectPage(mypage,queryWrapper);
        List<Application> applicationList = iPage.getRecords();
        for(Application application :applicationList){
            if (application.getClub_id() != null) {
                application.setClub_name(clubMapper.selectById(application.getClub_id()).getName());
            }

        }


        return Response.ok()
                .data("total",iPage.getTotal())
                .data("items",iPage.getRecords());
    }

    public Response join(Application application){
        int club_id = application.getClub_id();
        int applicant_id = application.getApplicant_id();
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
        JsonNode detail = application.getApplication_detail();
        String name = detail.get("name").asText();
        if(clubMapper.selectByName(name)!=null)   return Response.failure("此社团名已被使用。请尝试换一个社团名。");

        //再检查提交申请中的社团有无重名
        List<Application> applyingList = applicationMapper.getApplyByDetail("name",name);
        if(applyingList!=null) {
            for(Application apply : applyingList){
                if(apply.getStatus().equals("PENDING")) return Response.failure("申请列表中存在此社团名。请尝试换一个社团名。");
            }
        }


        int applicant_id = application.getApplicant_id();
        //查询用户id是否在数据库中
        if(userMapper.selectById(applicant_id)==null)   return Response.failure("用户ID不存在，请重新登录或联系管理员！");

        this.save(application);
        int appId = application.getId();

        return Response.ok().data("apply_id",appId);
    }
}
