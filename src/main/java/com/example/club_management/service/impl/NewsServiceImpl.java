package com.example.club_management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.club_management.entity.News;
import com.example.club_management.entity.User;
import com.example.club_management.mapper.NewsMapper;
import com.example.club_management.mapper.UserMapper;
import com.example.club_management.service.NewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.club_management.utils.Response;
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
 * @author jy
 * @since 2023-09-27
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {
    @Autowired
    NewsMapper newsMapper;
    @Autowired
    UserMapper userMapper;
    Map<String,Object> resmap =new HashMap<String,Object>();
    public Response getNewsList(int page, int limit){
        Page<News> mypage =new Page<>(page,limit);
        // 创建一个排序对象，按照 ID 逆序排序
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");

        IPage iPage =newsMapper.selectPage(mypage,queryWrapper);

        // 遍历查询结果，设置editor_name
        List<News> newsList = iPage.getRecords();
        for (News news : newsList) {
            // 根据 publisher_id 查询对应的 user 记录
            User user = userMapper.selectById(news.getPublisherId());
            if (user != null) {
                // 如果找到了对应的 user 记录，设置 editor_name
                news.setEditorName(user.getName());
            }
        }
        return Response.ok()
                .data("items",iPage.getRecords())
                .data("total",iPage.getTotal())
                .data("page",iPage.getPages());

    }
    public Response getNewsContent(int id){
        if(newsMapper.selectById(id)==null) return Response.failure("没有此公告！");
        resmap.put("title",newsMapper.selectById(id).getTitle());
        resmap.put("content",newsMapper.selectById(id).getContent());
        resmap.put("time",newsMapper.selectById(id).getTime());
        resmap.put("publisherDept",newsMapper.selectById(id).getPublisherDept());
        resmap.put("editorName",userMapper.selectById(newsMapper.selectById(id).getPublisherId()).getName());
        return Response.ok().data(resmap);
    }
    public Response modifyNews(News news){
        int publisher_id = news.getPublisherId();
        String role = userMapper.selectById(publisher_id).getRole();
        if(!role.equals("ADMIN"))   return Response.failure("没有修改权限");
        boolean isOK =  this.updateById(news);
        if(isOK){
            return Response.ok();
        }
        return Response.failure();
    }
    public Response deleteNews(News news){
        int publisher_id = news.getPublisherId();
        String role = userMapper.selectById(publisher_id).getRole();
        if(!role.equals("ADMIN"))   return Response.failure("没有删除权限");
        boolean isOK = this.removeById(news.getId());
        if(isOK){
            return Response.ok();
        }
        return Response.failure();
    }
    public Response postNews(News news){
        int publisher_id = news.getPublisherId();
        String role = userMapper.selectById(publisher_id).getRole();
        if(!role.equals("ADMIN"))   return Response.failure("没有发布权限");
        boolean isOK = this.save(news);
        if(isOK){
            return Response.ok().data("id",news.getId());
        }
        return Response.failure();
    }

}
