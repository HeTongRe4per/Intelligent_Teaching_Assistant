package com.hetongre4per.ita.modules.system.service.impl;

import com.hetongre4per.ita.modules.system.entity.domain.UserRole;
import com.hetongre4per.ita.modules.system.mapper.UserRoleMapper;
import com.hetongre4per.ita.modules.system.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
