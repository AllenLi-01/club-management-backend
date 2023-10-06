package com.example.club_management.mapper;

import com.example.club_management.entity.User_club;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xinn
 * @since 2023-09-28
 */
public interface User_clubMapper extends BaseMapper<User_club> {
    @Select("select * from user_club where user_id = #{uid}")
    List<User_club> getByUid(int uid);
    @Select("select * from user_club where club_id = #{cid}")
    List<User_club> getByCid(int cid);
    @Select("select * from user_club where club_id = #{cid} and user_id = #{uid}")
    User_club getByUidAndCid(int uid,int cid);

    /**
     * 查看是否已加入社团
     * */
    @Select("select * from user_club where club_id=#{cid} and user_id = #{uid}")
    User_club checkJoined(int uid,int cid);

    /**
     * 删除指定社团的记录
     * */
    @Delete("DELETE FROM user_club WHERE club_id = #{cid}")
    int removeByClubId(int cid);

    @Delete("delete from user_club where user_id=#{uid}")
    int removeByUid(int uid);

    /**
     * 获取指定社团成员数
     *
     * @param ClubId 社团id
     * @return int
     */

    @Select("select count(*) from user_club where club_id = #{ClubId}")
    int getClubMemNums(int ClubId);

}
