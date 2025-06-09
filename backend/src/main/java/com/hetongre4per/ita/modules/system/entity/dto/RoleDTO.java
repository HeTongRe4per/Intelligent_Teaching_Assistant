package com.hetongre4per.ita.modules.system.entity.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RoleDTO {

    private Integer id;

    private String name;

    private String code;

    private Date createdTime;

    private Date updatedTime;

    
}
