package com.hetongre4per.ita.config.security;

import com.hetongre4per.ita.modules.user.entity.domain.User;
import com.hetongre4per.ita.modules.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String accountType = username.contains("::") ? username.split("::")[0] : "normal";
        String usernameReal = username.contains("::") ? username.split("::")[1] : username;
        User user = userMapper.selectByUsername(usernameReal);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户没找到: " + usernameReal);
        }
        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUsername(user.getUsername());
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        if ("admin".equals(accountType)) {
            authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorityList.add(new SimpleGrantedAuthority("ROLE_NORMAL"));
        }
        userDetails.setAuthorities(authorityList);
        return userDetails;
    }
}
