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
@TableName("t_course_material")
public class CourseMaterial  {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("teacher_id")
    private Integer teacherId;

    @TableField("course_id")
    private Integer courseId;

    @TableField("name")
    private String name;

    @TableField("url")
    private String url;

    @TableField("created_time")
    private Date createdTime;

    @TableField("updated_time")
    private Date updatedTime;



}