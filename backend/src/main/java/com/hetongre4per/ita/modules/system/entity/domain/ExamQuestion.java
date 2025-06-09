package com.hetongre4per.ita.modules.system.entity.domain;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_exam_question")
public class ExamQuestion  {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("teacher_id")
    private Integer teacherId;

    @TableField("exam_id")
    private Integer examId;

    @TableField("type")
    private Integer type;

    @TableField("content")
    private JSONObject content;

    @TableField("score")
    private Integer score;

    @TableField("sequence")
    private Integer sequence;

    @TableField("created_time")
    private Date createdTime;

    @TableField("updated_time")
    private Date updatedTime;


    @Getter
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public enum TypeEnum {
        SINGLE(1, "单选"),
                SHORT(2, "简答"),
        ;
        private Integer code;
        private String desc;
    }

}