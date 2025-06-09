package com.hetongre4per.ita.modules.system.entity.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserRoleDTO {

    private Integer id;

    private Integer userId;

    private Integer roleId;

    private Date createdTime;

    private Date updatedTime;

    
}
