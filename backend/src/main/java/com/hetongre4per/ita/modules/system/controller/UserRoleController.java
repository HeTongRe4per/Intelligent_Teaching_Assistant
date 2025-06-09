package com.hetongre4per.ita.modules.system.controller;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.hetongre4per.ita.modules.base.controller.BaseController;
import com.hetongre4per.ita.modules.system.entity.domain.UserRole;
import com.hetongre4per.ita.modules.system.entity.dto.UserRoleDTO;
import com.hetongre4per.ita.vo.Result;
import com.hetongre4per.ita.modules.system.service.UserRoleService;
import com.hetongre4per.ita.utils.QueryWrapperUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Arrays;

@RestController
@RequestMapping("/manage/userRole")
public class UserRoleController extends BaseController {
    
    @Autowired
    private UserRoleService userRoleService;
    
    @ApiOperation(value = "获取用户角色关联表列表")
    @GetMapping("/list")
    public Result<IPage<UserRole>> list(@ApiParam(name = "order", value = "排序字段") @RequestParam(value = "order", required = false, defaultValue = "id") String order,
                                    @ApiParam(name = "pageNo", value = "页码") @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                    @ApiParam(name = "pageSize", value = "分页大小") @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                    HttpServletRequest request) {
        QueryWrapper<UserRole> queryWrapper = QueryWrapperUtil.build(UserRole.class, request.getParameterMap());
        queryWrapper.orderByDesc(order);
        Page<UserRole> page = new Page<>(pageNo, pageSize);
        return Result.success(userRoleService.page(page, queryWrapper));
    }
    
    @ApiOperation(value = "获取用户角色关联表信息")
    @GetMapping("/queryById")
    public Result<UserRoleDTO> queryById(@ApiParam(name = "id", value = "编号") @RequestParam(value = "id") Serializable id, HttpServletRequest request) {
        UserRole userRole = userRoleService.getById(id);
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        BeanUtil.copyProperties(userRole, userRoleDTO, CopyOptions.create().setIgnoreNullValue(true));
        return Result.success(userRoleDTO);
    }
    
    @ApiOperation(value = "保存用户角色关联表")
    @PostMapping("/add")
    public Result<?> add(@ApiParam(name = "userRoleDTO", value = "用户角色关联表信息") @RequestBody UserRoleDTO userRoleDTO, HttpServletRequest request) {
        UserRole userRole = new UserRole();
        BeanUtil.copyProperties(userRoleDTO, userRole);
        boolean saved = userRoleService.save(userRole);
        return saved ? Result.success("添加成功") : Result.error("添加失败");
    }
    
    @ApiOperation(value = "更新用户角色关联表")
    @PostMapping("/edit")
    public Result<?> edit(@ApiParam(name = "userRoleDTO", value = "用户角色关联表信息") @RequestBody UserRoleDTO userRoleDTO, HttpServletRequest request) {
        UserRole userRole = new UserRole();
        BeanUtil.copyProperties(userRoleDTO, userRole, CopyOptions.create().setIgnoreNullValue(true));
        boolean saved = userRoleService.saveOrUpdate(userRole);
        return saved ? Result.success("更新成功") : Result.error("更新失败");
    }
    
    @ApiOperation(value = "删除用户角色关联表")
    @GetMapping("/deleteById")
    public Result<?> deleteById(@ApiParam(name = "id", value = "编号", required = true) @RequestParam(name = "id") Serializable id, HttpServletRequest request) {
        boolean removed = userRoleService.removeById(id);
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }
    
    @ApiOperation(value = "批量删除用户角色关联表")
    @GetMapping("/batchDeleteById")
    public Result<?> batchDeleteById(@ApiParam(name = "id", value = "编号", required = true) @RequestParam(name = "ids") String ids, HttpServletRequest request) {
        boolean removed = userRoleService.removeBatchByIds(Arrays.asList(ids.split(",")));
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }
    
}
