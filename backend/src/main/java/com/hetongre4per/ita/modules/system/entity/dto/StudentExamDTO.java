package com.hetongre4per.ita.modules.system.entity.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StudentExamDTO {

    private Integer id;

    private Integer studentId;

    private Integer examId;

    private JSONObject answers;

    private Integer score;

    private Date startTime;

    private Date endTime;

    private Integer state;

    private Date createdTime;

    private Date updatedTime;

    
}
