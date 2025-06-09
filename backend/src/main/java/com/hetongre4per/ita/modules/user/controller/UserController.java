package com.hetongre4per.ita.modules.user.controller;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.hetongre4per.ita.modules.base.controller.BaseController;
import com.hetongre4per.ita.modules.user.entity.domain.User;
import com.hetongre4per.ita.modules.user.entity.dto.UserDTO;
import com.hetongre4per.ita.vo.Result;
import com.hetongre4per.ita.modules.user.service.UserService;
import com.hetongre4per.ita.utils.QueryWrapperUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Arrays;

@RestController
@RequestMapping("/manage/user")
public class UserController extends BaseController {
    
    @Autowired
    private UserService userService;
    
    @ApiOperation(value = "获取用户列表")
    @GetMapping("/list")
    public Result<IPage<User>> list(@ApiParam(name = "order", value = "排序字段") @RequestParam(value = "order", required = false, defaultValue = "id") String order,
                                    @ApiParam(name = "pageNo", value = "页码") @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                    @ApiParam(name = "pageSize", value = "分页大小") @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                    HttpServletRequest request) {
        QueryWrapper<User> queryWrapper = QueryWrapperUtil.build(User.class, request.getParameterMap());
        queryWrapper.orderByDesc(order);
        Page<User> page = new Page<>(pageNo, pageSize);
        return Result.success(userService.page(page, queryWrapper));
    }
    
    @ApiOperation(value = "获取用户信息")
    @GetMapping("/queryById")
    public Result<UserDTO> queryById(@ApiParam(name = "id", value = "编号") @RequestParam(value = "id") Serializable id, HttpServletRequest request) {
        User user = userService.getById(id);
        UserDTO userDTO = new UserDTO();
        BeanUtil.copyProperties(user, userDTO, CopyOptions.create().setIgnoreNullValue(true));
        return Result.success(userDTO);
    }
    
    @ApiOperation(value = "保存用户")
    @PostMapping("/add")
    public Result<?> add(@ApiParam(name = "userDTO", value = "用户信息") @RequestBody UserDTO userDTO, HttpServletRequest request) {
        User user = new User();
        BeanUtil.copyProperties(userDTO, user);
        if (StrUtil.isNotBlank(user.getPassword()) && !Validator.isMatchRegex("^[a-fA-F0-9]{32}$", user.getPassword())) {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().toString().getBytes()));
        }
        boolean saved = userService.save(user);
        return saved ? Result.success("添加成功") : Result.error("添加失败");
    }
    
    @ApiOperation(value = "更新用户")
    @PostMapping("/edit")
    public Result<?> edit(@ApiParam(name = "userDTO", value = "用户信息") @RequestBody UserDTO userDTO, HttpServletRequest request) {
        User user = new User();
        BeanUtil.copyProperties(userDTO, user, CopyOptions.create().setIgnoreNullValue(true));
        if (StrUtil.isNotBlank(user.getPassword()) && !Validator.isMatchRegex("^[a-fA-F0-9]{32}$", user.getPassword())) {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().toString().getBytes()));
        }
        boolean saved = userService.saveOrUpdate(user);
        return saved ? Result.success("更新成功") : Result.error("更新失败");
    }
    
    @ApiOperation(value = "删除用户")
    @GetMapping("/deleteById")
    public Result<?> deleteById(@ApiParam(name = "id", value = "编号", required = true) @RequestParam(name = "id") Serializable id, HttpServletRequest request) {
        boolean removed = userService.removeById(id);
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }
    
    @ApiOperation(value = "批量删除用户")
    @GetMapping("/batchDeleteById")
    public Result<?> batchDeleteById(@ApiParam(name = "id", value = "编号", required = true) @RequestParam(name = "ids") String ids, HttpServletRequest request) {
        boolean removed = userService.removeBatchByIds(Arrays.asList(ids.split(",")));
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }
    
}
