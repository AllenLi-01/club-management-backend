package com.example.club_management.mapper;

import com.example.club_management.entity.Activity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xinn
 * @since 2023-09-27
 */
public interface ActivityMapper extends BaseMapper<Activity> {

    @Delete("DELETE FROM activity WHERE club_id = #{cid}")
    int removeByClubId(int cid);


}
