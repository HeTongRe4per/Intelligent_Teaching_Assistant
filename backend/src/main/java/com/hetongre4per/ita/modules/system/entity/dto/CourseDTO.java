package com.hetongre4per.ita.modules.system.entity.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CourseDTO {

    private Integer id;

    private Integer teacherId;
    
    private String type;

    private String grade;

    private String name;

    private String code;

    private String img;

    private String description;

    private Integer credit;

    private Date createdTime;

    private Date updatedTime;

    
}
