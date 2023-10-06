package com.example.club_management.mapper;

import com.example.club_management.entity.Club;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xinn
 * @since 2023-09-27
 */
public interface ClubMapper extends BaseMapper<Club> {

    String getClubTypes();


    @Select("select * from club where name=#{name}")
    Club selectByName(String name);
}
