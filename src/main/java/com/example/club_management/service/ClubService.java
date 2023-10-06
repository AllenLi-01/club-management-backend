package com.example.club_management.service;

import com.example.club_management.entity.Application;
import com.example.club_management.entity.Club;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.club_management.utils.Response;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xinn
 * @since 2023-09-27
 */
public interface ClubService extends IService<Club> {
    Response getList(int page,int limit,int userId);
    Response getType();

    /**
     * 获取社团目前成员数量
     * @param ClubId 社团ID
     * @return int
     */
    int getClubMemNums(int ClubId);
    Response getClubByID(int id);
    Response delete(int admin_id,int club_id);
    Response deleteMember(int admin_id,int mem_id,int club_id);

    Response checkUserJoinedClub(int userId,int clubId);
    Response getMemberListByClubId(int id,int page,int limit);
}
