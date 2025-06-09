package com.hetongre4per.ita.modules.system.controller;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.hetongre4per.ita.modules.base.controller.BaseController;
import com.hetongre4per.ita.modules.system.entity.domain.Role;
import com.hetongre4per.ita.modules.system.entity.dto.RoleDTO;
import com.hetongre4per.ita.vo.Result;
import com.hetongre4per.ita.modules.system.service.RoleService;
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
@RequestMapping("/manage/role")
public class RoleController extends BaseController {
    
    @Autowired
    private RoleService roleService;
    
    @ApiOperation(value = "获取角色列表")
    @GetMapping("/list")
    public Result<IPage<Role>> list(@ApiParam(name = "order", value = "排序字段") @RequestParam(value = "order", required = false, defaultValue = "id") String order,
                                    @ApiParam(name = "pageNo", value = "页码") @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                    @ApiParam(name = "pageSize", value = "分页大小") @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                    HttpServletRequest request) {
        QueryWrapper<Role> queryWrapper = QueryWrapperUtil.build(Role.class, request.getParameterMap());
        queryWrapper.orderByDesc(order);
        Page<Role> page = new Page<>(pageNo, pageSize);
        return Result.success(roleService.page(page, queryWrapper));
    }
    
    @ApiOperation(value = "获取角色信息")
    @GetMapping("/queryById")
    public Result<RoleDTO> queryById(@ApiParam(name = "id", value = "编号") @RequestParam(value = "id") Serializable id, HttpServletRequest request) {
        Role role = roleService.getById(id);
        RoleDTO roleDTO = new RoleDTO();
        BeanUtil.copyProperties(role, roleDTO, CopyOptions.create().setIgnoreNullValue(true));
        return Result.success(roleDTO);
    }
    
    @ApiOperation(value = "保存角色")
    @PostMapping("/add")
    public Result<?> add(@ApiParam(name = "roleDTO", value = "角色信息") @RequestBody RoleDTO roleDTO, HttpServletRequest request) {
        Role role = new Role();
        BeanUtil.copyProperties(roleDTO, role);
        boolean saved = roleService.save(role);
        return saved ? Result.success("添加成功") : Result.error("添加失败");
    }
    
    @ApiOperation(value = "更新角色")
    @PostMapping("/edit")
    public Result<?> edit(@ApiParam(name = "roleDTO", value = "角色信息") @RequestBody RoleDTO roleDTO, HttpServletRequest request) {
        Role role = new Role();
        BeanUtil.copyProperties(roleDTO, role, CopyOptions.create().setIgnoreNullValue(true));
        boolean saved = roleService.saveOrUpdate(role);
        return saved ? Result.success("更新成功") : Result.error("更新失败");
    }
    
    @ApiOperation(value = "删除角色")
    @GetMapping("/deleteById")
    public Result<?> deleteById(@ApiParam(name = "id", value = "编号", required = true) @RequestParam(name = "id") Serializable id, HttpServletRequest request) {
        boolean removed = roleService.removeById(id);
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }
    
    @ApiOperation(value = "批量删除角色")
    @GetMapping("/batchDeleteById")
    public Result<?> batchDeleteById(@ApiParam(name = "id", value = "编号", required = true) @RequestParam(name = "ids") String ids, HttpServletRequest request) {
        boolean removed = roleService.removeBatchByIds(Arrays.asList(ids.split(",")));
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }
    
}
