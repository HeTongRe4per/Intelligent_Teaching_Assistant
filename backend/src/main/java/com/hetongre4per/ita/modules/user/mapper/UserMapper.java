package com.hetongre4per.ita.modules.user.mapper;

import com.hetongre4per.ita.modules.user.entity.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<User> {

    @Select("select * from t_user where username = #{username}")
    User selectByUsername(String username);

}