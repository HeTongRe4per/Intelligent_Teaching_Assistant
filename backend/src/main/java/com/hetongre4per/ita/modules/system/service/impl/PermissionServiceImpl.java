package com.hetongre4per.ita.modules.system.service.impl;

import com.hetongre4per.ita.modules.system.entity.domain.Permission;
import com.hetongre4per.ita.modules.system.mapper.PermissionMapper;
import com.hetongre4per.ita.modules.system.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

}
