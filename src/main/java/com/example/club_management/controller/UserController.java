package com.example.club_management.controller;


import com.example.club_management.entity.User;
import com.example.club_management.service.UserService;
import com.example.club_management.utils.JWTUtils;
import com.example.club_management.utils.Response;
import com.example.club_management.utils.ResponseCode;
import io.jsonwebtoken.Claims;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xinn
 * @since 2023-09-27
 */
@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/login")
    public Response login(@RequestBody User user){
        return userService.login(user);
    }
    @PostMapping("/logout")
    public Response logout(){
        return Response.ok();
    }

    @GetMapping("/info")
    public Response getUserInfo(@RequestParam("token") String token){
        return userService.getUserInfo(token);

    }

    @PostMapping("")
    public Response register(@RequestBody User user){
        return userService.register(user);
    }
    @PutMapping("")
    public Response update(@RequestBody User user){
        return userService.update(user);
    }
    @PutMapping("/password")
    public Response updatePassword(@RequestBody PasswordUpdateRequest request){
        int uid = request.getUid();
        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();

        return userService.updatePassword(uid,oldPassword,newPassword);
    }
    @Data public static class PasswordUpdateRequest {
        private int uid;
        private String oldPassword;
        private String newPassword;

    }



}

