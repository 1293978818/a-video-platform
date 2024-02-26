package com.ygh.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 视频类
 * @author ygh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Video {
    
    private String id;

    private String userId;

    private String videoUrl;

    private String coverUrl;

    private String title;

    private String description;

    private Integer visitCount;

    private Integer likeCount;

    private Integer commentCount;

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
