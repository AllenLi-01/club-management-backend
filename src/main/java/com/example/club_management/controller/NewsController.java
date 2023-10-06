package com.example.club_management.controller;


import com.example.club_management.entity.News;
import com.example.club_management.service.NewsService;
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
@RequestMapping("/api/news")
public class NewsController {
    @Autowired
    NewsService newsService;
    @GetMapping("")
    public Response getNewsList(@RequestParam("page") int page, @RequestParam("limit") int limit){
        return newsService.getNewsList(page,limit);

    }
    @GetMapping("/id")
    public Response getNewsContent(@RequestParam("id") int id){
        return newsService.getNewsContent(id);
    }
    @PutMapping("")
    public Response modifyNews(@RequestBody News news){
        return newsService.modifyNews(news);
    }
    @DeleteMapping("")
    public Response deleteNews(@RequestBody News news){
        return newsService.deleteNews(news);
    }
    @PostMapping("")
    public Response postNews(@RequestBody News news){
        return newsService.postNews(news);
    }
}

