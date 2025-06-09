package com.hetongre4per.ita.modules.system.entity.dto;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PermissionDTO {

    private Integer id;

    private Integer parentId;

    private Integer type;

    private String name;

    private String code;

    private String path;

    private String icon;
    
    private Integer sequence;

    private String description;

    private Date createdTime;

    private Date updatedTime;

    
}
