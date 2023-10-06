package com.example.club_management.mapper;

import com.example.club_management.entity.Application;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xinn
 * @since 2023-09-27
 */
public interface ApplicationMapper extends BaseMapper<Application> {

    /**
     * 获取用户加入社团的申请
     *
     * @param uid 用户id
     * @param cid 社团id
     * @return {@link Application}
     */
    @Select("select * from application where club_id=#{cid} and applicant_id =#{uid} and status='PENDING' and type='JOIN_CLUB'")
    Application getJoinApplication(int uid, int cid);

    /**
     * 根据application_detail的字段查询记录
     *
     * @param key   Json字段key
     * @param value Json字段Value
     * @return {@link Application}
     */
    @Select("SELECT * FROM Application WHERE JSON_EXTRACT(application_detail, concat('$.',#{key})) = #{value}")
    List<Application> getApplyByDetail(String key, String value);


}
