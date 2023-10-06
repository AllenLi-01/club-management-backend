package com.example.club_management.controller;


import com.example.club_management.entity.Message;
import com.example.club_management.service.MessageService;
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
@RestController
@CrossOrigin
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    MessageService messageService;
    @GetMapping("/club")
    public Response getMessageList(@RequestParam("club_id") int club_id, @RequestParam("page") int page, @RequestParam("limit") int limit){
        return messageService.getMessageList(club_id,page,limit);
    }
    @GetMapping("")
    public Response getMessageContent(@RequestParam("id") int id){
        return messageService.getMessageContent(id);
    }
    @PutMapping("")
    public Response modifyMessage(@RequestBody Message message){
        return messageService.modifyMessage(message);
    }
    @DeleteMapping("")
    public Response deleteMessage(@RequestBody Message message){
        return messageService.deleteMessage(message);
    }
    @PostMapping("")
    public Response postMessage(@RequestBody Message message){
        return messageService.postMessage(message);
    }

}

