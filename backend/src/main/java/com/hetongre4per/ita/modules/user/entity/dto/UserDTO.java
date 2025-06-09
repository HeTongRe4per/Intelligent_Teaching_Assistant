package com.hetongre4per.ita.modules.user.entity.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserDTO {

    private Integer id;

    private String username;

    private String password;

    private String realName;

    private String email;

    private String phone;

    private String avatar;

    private String role;

    private String stuNum;

    private String grade;

    private String clazz;

    private String department;

    private Date createdTime;

    private Date updatedTime;

    
}
