package com.hetongre4per.ita.modules.user.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class BaseUser {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @TableField("username")
    private String username;
    
    @TableField("password")
    private String password;
    
}
