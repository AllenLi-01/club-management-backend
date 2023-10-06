package com.example.club_management.controller;


import com.example.club_management.entity.Application;
import com.example.club_management.service.ClubService;
import com.example.club_management.utils.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.internal.dynalink.beans.StaticClass;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  社团接口
 * </p>
 *
 * @author xinn
 * @since 2023-09-27
 */
@CrossOrigin
@RestController
@RequestMapping("/api/clubs")
public class ClubController {
    @Autowired
    ClubService clubService;


    /**
     * 获取所有社团类型
     *
     * @return {@link Response}
     */
    @GetMapping("/types")
    public Response getType(){
        return clubService.getType();
    }


    //测试类，给我啥返回啥
    @PostMapping("/test")
    public Response test(@RequestBody JsonNode request) {
        return Response.ok().data("request",request);

    }

    @GetMapping("")
    public Response getList(@RequestParam("page") int page,@RequestParam("limit") int limit,@RequestParam("userId") int userId){
        return clubService.getList(page,limit,userId);
    }
    @GetMapping("/id")
    public Response getClubByID(@RequestParam("id") int id){
        return clubService.getClubByID(id);
    }


    @GetMapping("/user/state")
    public Response checkUserJoinedClub(@RequestParam("userId") int userID,@RequestParam("clubId") int clubID){
        return clubService.checkUserJoinedClub(userID,clubID);
    }



    @Data
    public static class DeleteObject{
        int admin_id;
        int club_id;
    }

    @DeleteMapping("")
    public Response delete(@RequestBody DeleteObject deleteObject){
        return clubService.delete(deleteObject.getAdmin_id(),deleteObject.getClub_id());

    }



    @DeleteMapping("/member")
   public Response deleteMember(@RequestBody JsonNode request){
        int admin_id = request.get("admin_id").asInt();
        int mem_id = request.get("mem_id").asInt();
        int club_id = request.get("club_id").asInt();


        return clubService.deleteMember(admin_id,mem_id,club_id);
   }



}

