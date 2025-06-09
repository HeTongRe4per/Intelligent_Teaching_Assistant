package com.hetongre4per.ita.modules.base.controller;

import com.hetongre4per.ita.modules.user.entity.domain.BaseUser;
import com.hetongre4per.ita.modules.user.mapper.UserMapper;
import com.hetongre4per.ita.modules.user.service.UserService;
import com.hetongre4per.ita.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
public class BaseController {
    @Autowired
    private UserService userService;
    
    protected String getUsername() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return JwtUtils.getUsernameFromToken(requestAttributes.getRequest().getHeader("Authorization"));
    }
    
    protected BaseUser getUser() {
        return ((UserMapper) userService.getBaseMapper()).selectByUsername(getUsername());
    }
    
}
