package com.example.club_management.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author xinn
 * @since 2023-09-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User_club implements Serializable {

    private static final long serialVersionUID=1L;

      private Integer userId;

    private Integer clubId;

    @TableField(exist = false)
    private String clubName;

    private String role;

    private LocalDateTime joinTime;


}
