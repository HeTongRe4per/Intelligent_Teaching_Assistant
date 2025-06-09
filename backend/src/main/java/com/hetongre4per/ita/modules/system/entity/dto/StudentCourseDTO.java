package com.hetongre4per.ita.modules.system.entity.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StudentCourseDTO {

    private Integer id;

    private Integer studentId;

    private Integer courseId;

    private Date createdTime;

    private Date updatedTime;

    
}
