package com.ygh.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @author ygh
 */
@Data
public class Comment {
    
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String userId;

    private String videoId;

    private String parentId;

    private Integer likeCount;

    private Integer childCount;

    private String content;

    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;

    private Date deletedAt;

    @TableLogic
    @JsonIgnore
    private Integer deleted;

    @Version
    @JsonIgnore
    private Integer version;
}
