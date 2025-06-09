package com.hetongre4per.ita.modules.system.entity.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExamQuestionDTO {

    private Integer id;

    private Integer teacherId;

    private Integer examId;

    private Integer type;

    private JSONObject content;

    private Integer score;

    private Integer sequence;

    private Date createdTime;

    private Date updatedTime;

    
}
