package com.example.club_management.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author xinn
 * @since 2023-09-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Club implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String description;

    @TableField("supervisorID")
    private Integer supervisorID;

    @TableField(exist = false)
    private String supervisorName;

    @TableField(exist=false)
    private Integer memberCnt;

    @TableField(exist = false)
    private String joinState;

    private LocalDate createDate;

    private String type;



}
