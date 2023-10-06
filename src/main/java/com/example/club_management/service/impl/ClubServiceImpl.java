package com.example.club_management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.club_management.entity.Application;
import com.example.club_management.entity.Club;
import com.example.club_management.entity.User_club;
import com.example.club_management.mapper.*;
import com.example.club_management.service.ApplicationService;
import com.example.club_management.service.ClubService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.club_management.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xinn
 * @since 2023-09-27
 */
@Service
public class ClubServiceImpl extends ServiceImpl<ClubMapper, Club> implements ClubService {
    @Autowired
    ClubMapper clubMapper;
    @Autowired
    User_clubMapper userClubMapper;
    @Autowired
    ApplicationMapper applicationMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    MessageMapper messageMapper;

    public Response getMemberListByClubId(int id,int page,int limit){
        Page<User_club> mypage = new Page<>(page,limit);
        QueryWrapper<User_club> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("club_id",id);
        IPage iPage = userClubMapper.selectPage(mypage,queryWrapper);
        List<User_club> list = iPage.getRecords();
        for(User_club uc: list){
            uc.setUserName(userMapper.selectById(uc.getUserId()).getName());
        }

        Map<String,Object> resMap = new HashMap<>();
        resMap.put("total",iPage.getTotal());
        resMap.put("items",list);
        return Response.ok().data(resMap);
    }

    public Response getList(int page, int limit,int userId){
        Page<Club> mypage = new Page<>(page,limit);
        IPage iPage = clubMapper.selectPage(mypage,null);
        List<Club> list = iPage.getRecords();
        for(Club club:list){
            club.setSupervisorName(userMapper.selectById(club.getSupervisorID()).getName());
            club.setMemberCnt(getClubMemNums(club.getId()));
            club.setJoinState(getUserJoinState(userId,club.getId()));
        }

        Map<String,Object> resMap = new HashMap<>();
        resMap.put("total",iPage.getTotal());
        resMap.put("items",list);
        return Response.ok().data(resMap);
    }

   public Response checkUserJoinedClub(int userId,int clubId){
       //查询用户id是否在数据库中
       if(userMapper.selectById(userId)==null)   return Response.failure("用户ID不存在，请重新登录或联系管理员！");
       //先查询社团是否存在
       if(clubMapper.selectById(clubId)==null)    return Response.failure("社团不存在！");
       return Response.ok().data("state",getUserJoinState(userId,clubId));
    }

    public String getUserJoinState(int userId,int clubId){
        String state;
        //查看是否已加入社团
        if(userClubMapper.checkJoined(userId,clubId)!=null) {
            state = "joined";
        }
        //查看是否已递交申请
        else if(applicationMapper.getJoinApplication(userId,clubId)!=null)    {
            state="applying";
        }else {
            state = "no";
        }
        return state;
    }



    public Response delete(int admin_id,int club_id){
        //确认撤销者权限
        String role = userMapper.selectById(admin_id).getRole();
        if(!role.equals("ADMIN"))   return Response.failure("权限不足！");
        //确认社团存在
        if(clubMapper.selectById(club_id)==null)    return Response.failure("欲撤销的社团不存在！");
        //清除此社团的活动信息
        activityMapper.removeByClubId(club_id);
        //清除此社团的所有推送信息
        messageMapper.removeByClubId(club_id);
        //清除此社团所有用户社团联表信息
        userClubMapper.removeByClubId(club_id);
        //删除社团信息
        if(this.removeById(club_id))    return Response.ok().data("club_id",club_id);
        return Response.failure("未知原因删除失败！");
    }

    public Response deleteMember(int admin_id,int mem_id,int club_id){
        //确认成员和执行者是否属于该社团
        if(userClubMapper.checkJoined(admin_id,club_id)==null)   return Response.failure("您已不在此社团！");
        if(userClubMapper.checkJoined(mem_id,club_id)==null)   return Response.failure("欲开除的成员已不在此社团！");
        //首先确认其是否具有删除权限
        String admin_role = userClubMapper.getByUidAndCid(admin_id,club_id).getRole();
        String mem_role = userClubMapper.getByUidAndCid(mem_id,club_id).getRole();
        if(admin_role.equals("STUDENT") || mem_role.equals("SUPERVISOR"))    return  Response.failure("您没有权限开除该用户！");
        if(admin_role.equals("ADMIN") && mem_role.equals("ADMIN"))  return Response.failure("您没有权限开除该用户！");

        userClubMapper.removeByUid(mem_id);
        return Response.ok();


    }

    public Response getClubByID(int id){
        if(clubMapper.selectById(id)==null)   return Response.failure("社团不存在！");
        Club club = clubMapper.selectById(id);
        String name =club.getName();
        String description = club.getDescription();
        LocalDate createDate = club.getCreateDate();
        int supervisorID = club.getSupervisorID();
        String supervisorName = userMapper.selectById(supervisorID).getName();
        return Response.ok()
                .data("name",name)
                .data("description",description)
                .data("createDate",createDate)
                .data("supervisorID",supervisorID)
                .data("supervisorName",supervisorName);



    }

    public Response getType(){

        String enumString = clubMapper.getClubTypes();
            // 去除括号并按逗号分隔字符串
            String[] enumArray = enumString.replace("enum(", "").replace(")", "").split(",");
            List<String> enumList = new ArrayList<>();
        for (String item : enumArray) {
            // 去除单引号并添加到新列表中
            String cleanedItem = item.replaceAll("'", "");
            enumList.add(cleanedItem);
        }
        return Response.ok()
                .data("type",enumList);
    }

    public int getClubMemNums(int ClubId){
        return userClubMapper.getClubMemNums(ClubId);
    }


}
