package com.example.club_management.service;

import com.example.club_management.entity.Application;
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
public interface ApplicationService extends IService<Application> {
    Response join(Application application);
    Response createClub(Application application);
    Response getApplyList(int userId,int page,int limit);


    /**
     * 处理入社申请
     *
     * @param application 申请实体类
     * @return {@link Response}
     */
    Response processJoinApplication(Application application);

}
