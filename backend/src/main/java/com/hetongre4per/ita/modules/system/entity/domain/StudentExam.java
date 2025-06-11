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
@TableName("t_student_exam")
public class StudentExam  {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("student_id")
    private Integer studentId;

    @TableField("exam_id")
    private Integer examId;

    @TableField("answers")
    private JSONObject answers;

    @TableField("score")
    private Integer score;

    @TableField("start_time")
    private Date startTime;

    @TableField("end_time")
    private Date endTime;

    @TableField("state")
    private Integer state;

    @TableField("created_time")
    private Date createdTime;

    @TableField("updated_time")
    private Date updatedTime;

    @TableField("switch_count")
    private Integer switchCount;

    @Getter
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public enum StateEnum {
        CREATED(0, "未提交"),
                WAITTING(1, "待批改"),
                FINISED(2, "已完成"),
        ;
        private Integer code;
        private String desc;
    }

}