package com.example.club_management.controller;


import com.example.club_management.entity.Application;
import com.example.club_management.service.ApplicationService;
import com.example.club_management.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xinn
 * @since 2023-09-27
 */
@CrossOrigin
@RestController
@RequestMapping("/api/application")
public class ApplicationController {
    @Autowired
    ApplicationService applicationService;

    @GetMapping("/user")
    public Response getApplyList(@RequestParam("userId") int userId,@RequestParam("page") int page,@RequestParam("limit") int limit){
        return applicationService.getApplyList(userId,page,limit);
    }
    @PostMapping("/clubs/join")
    public Response join(@RequestBody Application application){
        return applicationService.join(application);
    }

    @PostMapping("clubs/create")
    public Response createClub(@RequestBody Application application){
        return applicationService.createClub(application);
    }

    @DeleteMapping("")
    public Response cancel(@RequestBody Application application){
        if(applicationService.removeById(application.getId()))   return Response.ok();
        return Response.failure();

    }

}

