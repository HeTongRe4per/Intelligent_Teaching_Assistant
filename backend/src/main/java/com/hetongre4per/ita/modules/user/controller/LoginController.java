package com.hetongre4per.ita.modules.user.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.hetongre4per.ita.modules.base.controller.BaseController;
import com.hetongre4per.ita.modules.user.entity.domain.BaseUser;
import com.hetongre4per.ita.modules.user.entity.domain.User;
import com.hetongre4per.ita.modules.user.entity.dto.UserDTO;
import com.hetongre4per.ita.modules.user.service.UserService;
import com.hetongre4per.ita.utils.JwtUtils;
import com.hetongre4per.ita.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController extends BaseController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/login")
    public Result<String> login(@ApiParam(name = "username", value = "用户名", required = true) @RequestParam(name = "username") String username,
                           @ApiParam(name = "password", value = "密码", required = true) @RequestParam(name = "password") String password,
                           HttpServletRequest request) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        String usernameReal = username.contains("::") ? username.split("::")[1] : username;
        queryWrapper.eq("username", usernameReal);
        queryWrapper.eq("password", DigestUtils.md5DigestAsHex(password.getBytes()));
        User user = userService.getOne(queryWrapper);
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        return Result.success("登录成功", JwtUtils.generateToken(usernameReal));
    }
    
    @GetMapping("/manage/logout")
    public Result<?> logout(HttpServletRequest request) {
        return Result.ok();
    }
    
    @GetMapping("/manage/getUser")
    public Result<?> getUser(HttpServletRequest request) {
        BaseUser user = getUser();
        return Result.success(user);
    }
    
    @PostMapping("/manage/changePass")
    public Result<?> changePass(@ApiParam(name = "userId", value = "用户编号", required = true) @RequestParam(name = "userId") Integer userId,
                                @ApiParam(name = "oldPassword", value = "密码", required = true) @RequestParam(name = "oldPassword") String oldPassword,
                                @ApiParam(name = "newPassword", value = "密码", required = true) @RequestParam(name = "newPassword") String newPassword,
                                HttpServletRequest request) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        queryWrapper.eq("password", DigestUtils.md5DigestAsHex(oldPassword.getBytes()));
        User user = userService.getOne(queryWrapper);
        if (user == null) {
            return Result.error("原密码错误");
        }
        user.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
        userService.updateById(user);
        return Result.success("修改成功");
    }
    
    @PostMapping("/register")
    public Result<?> register(@ApiParam(name = "userDTO", value = "用户信息") @RequestBody UserDTO userDTO, HttpServletRequest request) {
        User user = new User();
        BeanUtil.copyProperties(userDTO, user, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        user.setPassword(DigestUtils.md5DigestAsHex(userDTO.getPassword().toString().getBytes()));
        userService.save(user);
        return Result.success("注册成功");
    }
    
}
