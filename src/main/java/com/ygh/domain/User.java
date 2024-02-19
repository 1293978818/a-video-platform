package com.ygh.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ygh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String username;
    private String password;

    @TableField(value = "avatar_url")
    private String avatarUrl;

    @TableField(value = "created_at",fill = FieldFill.INSERT)
    private Date createdAt;

    @TableField(value = "updated_at",fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;

    @TableField(value = "deleted_at",fill = FieldFill.UPDATE)
    private Date deletedAt;

    @TableLogic
    private Integer deleted;

    @Version
    @TableField(value = "version", fill = FieldFill.UPDATE, update = "%s+1")
    private Integer version;
}
