package com.hetongre4per.ita.modules.system.controller;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.hetongre4per.ita.modules.base.controller.BaseController;
import com.hetongre4per.ita.modules.system.entity.domain.Permission;
import com.hetongre4per.ita.modules.system.entity.dto.PermissionDTO;
import com.hetongre4per.ita.vo.Result;
import com.hetongre4per.ita.modules.system.service.PermissionService;
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
@RequestMapping("/manage/permission")
public class PermissionController extends BaseController {
    
    @Autowired
    private PermissionService permissionService;
    
    @ApiOperation(value = "获取权限列表")
    @GetMapping("/list")
    public Result<IPage<Permission>> list(@ApiParam(name = "order", value = "排序字段") @RequestParam(value = "order", required = false, defaultValue = "id") String order,
                                    @ApiParam(name = "pageNo", value = "页码") @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                    @ApiParam(name = "pageSize", value = "分页大小") @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                    HttpServletRequest request) {
        QueryWrapper<Permission> queryWrapper = QueryWrapperUtil.build(Permission.class, request.getParameterMap());
        queryWrapper.orderByDesc(order);
        Page<Permission> page = new Page<>(pageNo, pageSize);
        return Result.success(permissionService.page(page, queryWrapper));
    }
    
    @ApiOperation(value = "获取父权限列表")
    @GetMapping("/listParent")
    public Result<IPage<Permission>> listParent(@ApiParam(name = "order", value = "排序字段") @RequestParam(value = "order", required = false, defaultValue = "id") String order,
                                    @ApiParam(name = "pageNo", value = "页码") @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                    @ApiParam(name = "pageSize", value = "分页大小") @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                    HttpServletRequest request) {
        QueryWrapper<Permission> queryWrapper = QueryWrapperUtil.build(Permission.class, request.getParameterMap());
        queryWrapper.isNull("parent_id");
        queryWrapper.orderByDesc(order);
        Page<Permission> page = new Page<>(pageNo, pageSize);
        return Result.success(permissionService.page(page, queryWrapper));
    }
    
    @ApiOperation(value = "获取权限信息")
    @GetMapping("/queryById")
    public Result<PermissionDTO> queryById(@ApiParam(name = "id", value = "编号") @RequestParam(value = "id") Serializable id, HttpServletRequest request) {
        Permission permission = permissionService.getById(id);
        PermissionDTO permissionDTO = new PermissionDTO();
        BeanUtil.copyProperties(permission, permissionDTO, CopyOptions.create().setIgnoreNullValue(true));
        return Result.success(permissionDTO);
    }
    
    @ApiOperation(value = "保存权限")
    @PostMapping("/add")
    public Result<?> add(@ApiParam(name = "permissionDTO", value = "权限信息") @RequestBody PermissionDTO permissionDTO, HttpServletRequest request) {
        Permission permission = new Permission();
        BeanUtil.copyProperties(permissionDTO, permission);
        boolean saved = permissionService.save(permission);
        return saved ? Result.success("添加成功") : Result.error("添加失败");
    }
    
    @ApiOperation(value = "更新权限")
    @PostMapping("/edit")
    public Result<?> edit(@ApiParam(name = "permissionDTO", value = "权限信息") @RequestBody PermissionDTO permissionDTO, HttpServletRequest request) {
        Permission permission = new Permission();
        BeanUtil.copyProperties(permissionDTO, permission, CopyOptions.create().setIgnoreNullValue(true));
        boolean saved = permissionService.saveOrUpdate(permission);
        return saved ? Result.success("更新成功") : Result.error("更新失败");
    }
    
    @ApiOperation(value = "删除权限")
    @GetMapping("/deleteById")
    public Result<?> deleteById(@ApiParam(name = "id", value = "编号", required = true) @RequestParam(name = "id") Serializable id, HttpServletRequest request) {
        boolean removed = permissionService.removeById(id);
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }
    
    @ApiOperation(value = "批量删除权限")
    @GetMapping("/batchDeleteById")
    public Result<?> batchDeleteById(@ApiParam(name = "ids", value = "编号", required = true) @RequestParam(name = "ids") String ids, HttpServletRequest request) {
        boolean removed = permissionService.removeBatchByIds(Arrays.asList(ids.split(",")));
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }
    
}
