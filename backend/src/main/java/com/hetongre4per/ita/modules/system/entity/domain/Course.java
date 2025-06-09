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
@TableName("t_course")
public class Course  {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("teacher_id")
    private Integer teacherId;
    
    @TableField("type")
    private String type;

    @TableField("grade")
    private String grade;

    @TableField("name")
    private String name;

    @TableField("code")
    private String code;

    @TableField("img")
    private String img;

    @TableField("description")
    private String description;

    @TableField("credit")
    private Integer credit;

    @TableField("created_time")
    private Date createdTime;

    @TableField("updated_time")
    private Date updatedTime;


    @Getter
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public enum GradeEnum {
        GRADE21("grade21", "21级"),
                GRADE22("grade22", "22级"),
                GRADE23("grade23", "23级"),
                GRADE24("grade24", "24级"),
        ;
        private String code;
        private String desc;
    }

}