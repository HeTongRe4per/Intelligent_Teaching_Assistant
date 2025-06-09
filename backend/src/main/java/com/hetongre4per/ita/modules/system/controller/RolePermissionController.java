package com.hetongre4per.ita.modules.system.controller;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.hetongre4per.ita.modules.base.controller.BaseController;
import com.hetongre4per.ita.modules.system.entity.domain.RolePermission;
import com.hetongre4per.ita.modules.system.entity.dto.RolePermissionDTO;
import com.hetongre4per.ita.vo.Result;
import com.hetongre4per.ita.modules.system.service.RolePermissionService;
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
@RequestMapping("/manage/rolePermission")
public class RolePermissionController extends BaseController {
    
    @Autowired
    private RolePermissionService rolePermissionService;
    
    @ApiOperation(value = "获取角色权限关联表列表")
    @GetMapping("/list")
    public Result<IPage<RolePermission>> list(@ApiParam(name = "order", value = "排序字段") @RequestParam(value = "order", required = false, defaultValue = "id") String order,
                                    @ApiParam(name = "pageNo", value = "页码") @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                    @ApiParam(name = "pageSize", value = "分页大小") @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                    HttpServletRequest request) {
        QueryWrapper<RolePermission> queryWrapper = QueryWrapperUtil.build(RolePermission.class, request.getParameterMap());
        queryWrapper.orderByDesc(order);
        Page<RolePermission> page = new Page<>(pageNo, pageSize);
        return Result.success(rolePermissionService.page(page, queryWrapper));
    }
    
    @ApiOperation(value = "获取角色权限关联表信息")
    @GetMapping("/queryById")
    public Result<RolePermissionDTO> queryById(@ApiParam(name = "id", value = "编号") @RequestParam(value = "id") Serializable id, HttpServletRequest request) {
        RolePermission rolePermission = rolePermissionService.getById(id);
        RolePermissionDTO rolePermissionDTO = new RolePermissionDTO();
        BeanUtil.copyProperties(rolePermission, rolePermissionDTO, CopyOptions.create().setIgnoreNullValue(true));
        return Result.success(rolePermissionDTO);
    }
    
    @ApiOperation(value = "保存角色权限关联表")
    @PostMapping("/add")
    public Result<?> add(@ApiParam(name = "rolePermissionDTO", value = "角色权限关联表信息") @RequestBody RolePermissionDTO rolePermissionDTO, HttpServletRequest request) {
        RolePermission rolePermission = new RolePermission();
        BeanUtil.copyProperties(rolePermissionDTO, rolePermission);
        boolean saved = rolePermissionService.save(rolePermission);
        return saved ? Result.success("添加成功") : Result.error("添加失败");
    }
    
    @ApiOperation(value = "更新角色权限关联表")
    @PostMapping("/edit")
    public Result<?> edit(@ApiParam(name = "rolePermissionDTO", value = "角色权限关联表信息") @RequestBody RolePermissionDTO rolePermissionDTO, HttpServletRequest request) {
        RolePermission rolePermission = new RolePermission();
        BeanUtil.copyProperties(rolePermissionDTO, rolePermission, CopyOptions.create().setIgnoreNullValue(true));
        boolean saved = rolePermissionService.saveOrUpdate(rolePermission);
        return saved ? Result.success("更新成功") : Result.error("更新失败");
    }
    
    @ApiOperation(value = "删除角色权限关联表")
    @GetMapping("/deleteById")
    public Result<?> deleteById(@ApiParam(name = "id", value = "编号", required = true) @RequestParam(name = "id") Serializable id, HttpServletRequest request) {
        boolean removed = rolePermissionService.removeById(id);
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }
    
    @ApiOperation(value = "批量删除角色权限关联表")
    @GetMapping("/batchDeleteById")
    public Result<?> batchDeleteById(@ApiParam(name = "id", value = "编号", required = true) @RequestParam(name = "ids") String ids, HttpServletRequest request) {
        boolean removed = rolePermissionService.removeBatchByIds(Arrays.asList(ids.split(",")));
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }
    
}
