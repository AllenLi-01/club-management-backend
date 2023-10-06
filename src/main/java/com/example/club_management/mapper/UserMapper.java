package com.example.club_management.mapper;

import com.example.club_management.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.club_management.utils.Response;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xinn
 * @since 2023-09-27
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 用户账号名、密码校验。
     * @param username 账号名
     * @param password 密码
     * @return 返回账号名、密码是否匹配
     * */
    @Select("SELECT COUNT(*) FROM user WHERE username = #{username} AND password = #{password}")
    boolean loginCheck(String username,String password);


    /**
     * 根据用户名获取ID.
     * @param username 用户名
     * @return uid
     * */
    @Select("SELECT uid from user where username=#{username}")
    int getUID(String username);

    @Select("select * from user where username=#{username};")
    User findByUsername(String username);

    @Update("UPDATE user SET password = #{passwd} WHERE uid = #{uid}")
    boolean changePassword(int uid,String passwd);

}
