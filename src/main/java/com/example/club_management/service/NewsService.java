package com.example.club_management.service;

import com.example.club_management.entity.News;
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
public interface NewsService extends IService<News> {

    Response getNewsList(int page, int limit);
    Response getNewsContent(int id);
    Response modifyNews(News news);
    Response deleteNews(News news);
    Response postNews(News news);
}
