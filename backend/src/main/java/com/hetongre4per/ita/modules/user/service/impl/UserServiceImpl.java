package com.hetongre4per.ita.modules.user.service.impl;

import com.hetongre4per.ita.modules.user.entity.domain.User;
import com.hetongre4per.ita.modules.user.mapper.UserMapper;
import com.hetongre4per.ita.modules.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
