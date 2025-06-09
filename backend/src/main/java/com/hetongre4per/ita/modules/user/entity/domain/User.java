package com.hetongre4per.ita.modules.user.entity.domain;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
@TableName("t_user")
public class User extends BaseUser {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("real_name")
    private String realName;

    @TableField("email")
    private String email;

    @TableField("phone")
    private String phone;

    @TableField("avatar")
    private String avatar;

    @TableField("role")
    private String role;

    @TableField("stu_num")
    private String stuNum;

    @TableField("grade")
    private String grade;

    @TableField("clazz")
    private String clazz;

    @TableField("department")
    private String department;

    @TableField("created_time")
    private Date createdTime;

    @TableField("updated_time")
    private Date updatedTime;


    @Getter
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public enum RoleEnum {
        NORMAL("normal", "学生"),
                TEACHER("teacher", "教师"),
                ADMIN("admin", "系统管理员"),
        ;
        private String code;
        private String desc;
    }
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