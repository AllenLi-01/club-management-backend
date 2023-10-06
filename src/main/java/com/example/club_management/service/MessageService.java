package com.example.club_management.service;

import com.example.club_management.entity.Message;
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
public interface MessageService extends IService<Message> {

    Response getMessageList(int club_id,int page, int limit);
    Response getMessageContent(int id);
    Response modifyMessage(Message message);
    Response deleteMessage(Message message);
    Response postMessage(Message message);

}
