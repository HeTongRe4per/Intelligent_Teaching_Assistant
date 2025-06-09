package com.hetongre4per.ita.config.filter;

import cn.hutool.core.util.StrUtil;
import com.hetongre4per.ita.config.security.CustomUserDetails;
import com.hetongre4per.ita.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtRequestFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // 获取Refer
        String refer = request.getHeader("Referer");
        String uri = request.getRequestURI();
        if (StrUtil.contains(refer, "/manage/")) {
            // 管理系统
            checkAuthorization(request, response, chain);
        } else {
            // 前台界面
            if (uri.endsWith("/add") || uri.endsWith("/edit") || uri.endsWith("/delete")) {
                checkAuthorization(request, response, chain);
            } else {
                CustomUserDetails userDetails = new CustomUserDetails();
                userDetails.setUsername("游客");
                userDetails.setAuthorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_NORMAL")));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
            }
        }
    }
    
    private void checkAuthorization(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (StrUtil.isBlank(authorizationHeader)) {
            authorizationHeader = request.getParameter("Authorization");
        }
        if (authorizationHeader == null) {
            SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
            return;
        }
        String username = JwtUtils.getUsernameFromToken(authorizationHeader);
        if (username != null && JwtUtils.validateToken(authorizationHeader)) {
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                chain.doFilter(request, response);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
   