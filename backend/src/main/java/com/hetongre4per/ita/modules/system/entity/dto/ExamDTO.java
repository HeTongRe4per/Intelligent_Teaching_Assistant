package com.hetongre4per.ita.modules.system.entity.dto;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExamDTO {

    private Integer id;

    private Integer teacherId;

    private Integer courseId;

    private String name;

    private Date startTime;

    private Date endTime;

    private String description;
    
    private Integer state;

    private Date createdTime;

    private Date updatedTime;

    
}
