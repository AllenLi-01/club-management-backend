package com.example.club_management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.club_management.entity.Message;
import com.example.club_management.entity.User;
import com.example.club_management.mapper.ClubMapper;
import com.example.club_management.mapper.MessageMapper;
import com.example.club_management.mapper.UserMapper;
import com.example.club_management.service.MessageService;
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
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ClubMapper clubMapper;
    Map<String,Object> resmap =new HashMap<String,Object>();
    public Response getMessageList(int club_id,int page,int limit){
        // 创建一个查询条件构造器
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("club_id", club_id); // 设置查询条件，匹配社团ID
        queryWrapper.orderByDesc("id");

        Page<Message> mypage =new Page<>(page,limit);
        IPage iPage =messageMapper.selectPage(mypage,queryWrapper);
        if(iPage.getTotal()==0) return Response.failure("该社团暂无推送");

        List<Message> messageList = iPage.getRecords();
        for(Message message:messageList){
            //根据publisher_id查user记录
            User user = userMapper.selectById(message.getPublisherId());
            if(user!=null){
                message.setEditorName(user.getName());
            }
        }

        return Response.ok()
                .data("items",iPage.getRecords())
                .data("total",iPage.getTotal())
                .data("page",iPage.getPages());
    }
    public Response getMessageContent(int id){
        resmap.put("title",messageMapper.selectById(id).getTitle());
        resmap.put("content",messageMapper.selectById(id).getContent());
        resmap.put("time",messageMapper.selectById(id).getTime());
        resmap.put("publisherDept",messageMapper.selectById(id).getPublisherDept());
        resmap.put("editorName",userMapper.selectById(messageMapper.selectById(id).getPublisherId()).getName());
        resmap.put("publishClub",clubMapper.selectById(messageMapper.selectById(id).getClubId()).getName());
        return Response.ok().data(resmap);
    }
    public Response modifyMessage(Message message){
        int publisher_id = message.getPublisherId();
        String role = userMapper.selectById(publisher_id).getRole();
        if(!role.equals("ADMIN"))   return Response.failure("没有修改权限");
        boolean isOK =  this.updateById(message);
        if(isOK){
            return Response.ok();
        }
        return Response.failure();
    }
    public Response deleteMessage(Message message){
        int publisher_id = message.getPublisherId();
        String role = userMapper.selectById(publisher_id).getRole();
        if(!role.equals("ADMIN"))   return Response.failure("没有删除权限");
        boolean isOK = this.removeById(message.getId());
        if(isOK){
            return Response.ok();
        }
        return Response.failure();
    }
    public Response postMessage(Message message){
        int publisher_id = message.getPublisherId();
        String role = userMapper.selectById(publisher_id).getRole();
        if(!role.equals("ADMIN"))   return Response.failure("没有发布权限");
        boolean isOK = this.save(message);
        if(isOK){
            return Response.ok().data("id",message.getId());
        }
        return Response.failure();
    }
}
