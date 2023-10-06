package com.example.club_management.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.club_management.entity.Club;
import com.example.club_management.entity.User;
import com.example.club_management.entity.User_club;
import com.example.club_management.mapper.ClubMapper;
import com.example.club_management.mapper.UserMapper;
import com.example.club_management.mapper.User_clubMapper;
import com.example.club_management.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.club_management.utils.JWTUtils;
import com.example.club_management.utils.Response;
import com.example.club_management.utils.ResponseCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xinn
 * @since 2023-09-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    User_clubMapper userClubMapper;
    @Autowired
    ClubMapper clubMapper;
    public Response login(User user){
        String username = user.getUsername();
        String password = user.getPassword();
        if(!userMapper.loginCheck(username,password))   return Response.failure("登录失败，用户名或密码错误！");
        int uid = userMapper.getUID(username);
        String token = JWTUtils.generateToken(uid);
        return Response.ok().data("token",token);

    }
    public Response getUserInfo(String token){
        Map<String,Object> claimsMap = new HashMap<>();
        claimsMap = JWTUtils.getClaimsByToken(token);
        Claims claims = (Claims) claimsMap.get("claims");
        int code = (int)claimsMap.get("code");
        if(code!= ResponseCode.LOGIN_SUCCESS) {
            String msg;
            if(code == ResponseCode.TOKEN_EXPIRED){
                msg = "token已过期！";
            }else if(code == ResponseCode.ILLEGAL_TOKEN){
                msg = "token非法！";
            }else {
                msg = "token其他异常！";
            }
            return Response.failure(code,msg);
        }
        int uid = Integer.parseInt(claims.getSubject());
        User user= userMapper.selectById(uid);
        String username = user.getUsername();
        String name = user.getName();
        String role = user.getRole();
        String avatar = user.getAvatar();
        List<User_club> userClub = userClubMapper.getByUid(uid);


        // 遍历 userClub 列表，为每个对象设置 clubName
        for (User_club club : userClub) {
            int clubId = club.getClubId();
            Club clubInfo = clubMapper.selectById(clubId);
            if (clubInfo != null) {
                club.setClubName(clubInfo.getName());
            }
        }

        return Response.ok()
                .data("uid",uid)
                .data("username",username)
                .data("name",name)
                .data("role",role)
                .data("avatar",avatar)
                .data("clubs",userClub);
    }

    public Response register(User user){
        String username = user.getUsername();

        user.setRole("STUDENT");
        if(userMapper.findByUsername(username)!=null){
            return Response.failure("用户名已存在。");
        }
        if(this.save(user)){
            return Response.ok().data("uid",userMapper.getUID(username));

        }else {
            return Response.failure();
        }

    }

    public Response update(User user){
        if(this.updateById(user))   return Response.ok();
        return Response.failure();
    }

    public Response updatePassword(int uid,String old_passwd,String new_passwd){
        String username = userMapper.selectById(uid).getUsername();
        if(!userMapper.loginCheck(username,old_passwd)) return Response.failure("原密码错误，请重试！");
        if(!userMapper.changePassword(uid,new_passwd))  return Response.failure("未知原因更改失败！");
        return Response.ok();
    }
}
