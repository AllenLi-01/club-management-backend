package com.example.club_management.service;

import com.example.club_management.entity.User;
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
public interface UserService extends IService<User> {
    /**
     * 用户登录。
     * @return 返回Response类型。成功code返回20000，并返回token；失败code返回20004.
     * */
    Response login(User user);
    /**
     * 请求用户信息。根据token解析出用户id，据此查询用户信息。
     * @param token
     * @return 用户的uid、账号名、姓名、头像、权限角色、加入的社团、在社团中的职位。
     * */
    Response getUserInfo(String token);


    Response register(User user);

    Response update(User user);

    Response updatePassword(int uid,String old_passwd,String new_passwd);

}
