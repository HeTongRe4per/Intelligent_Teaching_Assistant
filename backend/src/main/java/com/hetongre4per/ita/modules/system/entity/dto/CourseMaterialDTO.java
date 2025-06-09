package com.hetongre4per.ita.modules.system.entity.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CourseMaterialDTO {

    private Integer id;

    private Integer teacherId;

    private Integer courseId;

    private String name;

    private String url;

    private Date createdTime;

    private Date updatedTime;

    
}
