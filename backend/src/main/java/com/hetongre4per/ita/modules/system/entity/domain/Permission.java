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
@TableName("t_permission")
public class Permission  {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("parent_id")
    private Integer parentId;

    @TableField("type")
    private Integer type;

    @TableField("name")
    private String name;

    @TableField("code")
    private String code;

    @TableField("path")
    private String path;

    @TableField("icon")
    private String icon;
    
    @TableField("sequence")
    private Integer sequence;

    @TableField("description")
    private String description;

    @TableField("created_time")
    private Date createdTime;

    @TableField("updated_time")
    private Date updatedTime;


    @Getter
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public enum TypeEnum {
        MENU(1, "菜单"),
                BTN(2, "按钮"),
                API(3, "API"),
        ;
        private Integer code;
        private String desc;
    }
    @Getter
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public enum IconEnum {
        FOLDEROPENED("folderOpened", "课程图标"),
                DOCUMENT("document", "考试图标"),
                SETTING("setting", "系统图标"),
        ;
        private String code;
        private String desc;
    }

}