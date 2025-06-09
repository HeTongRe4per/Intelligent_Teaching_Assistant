package com.hetongre4per.ita.modules.system.service.impl;

import com.hetongre4per.ita.modules.system.entity.domain.Role;
import com.hetongre4per.ita.modules.system.mapper.RoleMapper;
import com.hetongre4per.ita.modules.system.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
