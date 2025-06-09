package com.hetongre4per.ita.modules.system.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.hetongre4per.ita.modules.base.controller.BaseController;
import com.hetongre4per.ita.modules.system.entity.domain.CourseMaterial;
import com.hetongre4per.ita.modules.system.entity.dto.CourseMaterialDTO;
import com.hetongre4per.ita.modules.system.service.CourseMaterialService;
import com.hetongre4per.ita.utils.QueryWrapperUtil;
import com.hetongre4per.ita.vo.Result;
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
@RequestMapping("/manage/courseMaterial")
public class CourseMaterialController extends BaseController {
    
    @Autowired
    private CourseMaterialService courseMaterialService;
    
    @ApiOperation(value = "获取课程资料列表")
    @GetMapping("/list")
    public Result<IPage<CourseMaterial>> list(@ApiParam(name = "order", value = "排序字段") @RequestParam(value = "order", required = false, defaultValue = "id") String order,
                                    @ApiParam(name = "pageNo", value = "页码") @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                    @ApiParam(name = "pageSize", value = "分页大小") @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                    HttpServletRequest request) {
        QueryWrapper<CourseMaterial> queryWrapper = QueryWrapperUtil.build(CourseMaterial.class, request.getParameterMap());
        queryWrapper.orderByDesc(order);
        Page<CourseMaterial> page = new Page<>(pageNo, pageSize);
        return Result.success(courseMaterialService.page(page, queryWrapper));
    }
    
    @ApiOperation(value = "获取课程资料信息")
    @GetMapping("/queryById")
    public Result<CourseMaterialDTO> queryById(@ApiParam(name = "id", value = "编号") @RequestParam(value = "id") Serializable id, HttpServletRequest request) {
        CourseMaterial courseMaterial = courseMaterialService.getById(id);
        CourseMaterialDTO courseMaterialDTO = new CourseMaterialDTO();
        BeanUtil.copyProperties(courseMaterial, courseMaterialDTO, CopyOptions.create().setIgnoreNullValue(true));
        return Result.success(courseMaterialDTO);
    }
    
    @ApiOperation(value = "保存课程资料")
    @PostMapping("/add")
    public Result<?> add(@ApiParam(name = "courseMaterialDTO", value = "课程资料信息") @RequestBody CourseMaterialDTO courseMaterialDTO, HttpServletRequest request) {
        CourseMaterial courseMaterial = new CourseMaterial();
        BeanUtil.copyProperties(courseMaterialDTO, courseMaterial);
        courseMaterial.setTeacherId(getUser().getId());
        boolean saved = courseMaterialService.save(courseMaterial);
        return saved ? Result.success("添加成功") : Result.error("添加失败");
    }
    
    @ApiOperation(value = "更新课程资料")
    @PostMapping("/edit")
    public Result<?> edit(@ApiParam(name = "courseMaterialDTO", value = "课程资料信息") @RequestBody CourseMaterialDTO courseMaterialDTO, HttpServletRequest request) {
        CourseMaterial courseMaterial = new CourseMaterial();
        BeanUtil.copyProperties(courseMaterialDTO, courseMaterial, CopyOptions.create().setIgnoreNullValue(true));
        boolean saved = courseMaterialService.saveOrUpdate(courseMaterial);
        return saved ? Result.success("更新成功") : Result.error("更新失败");
    }
    
    @ApiOperation(value = "删除课程资料")
    @GetMapping("/deleteById")
    public Result<?> deleteById(@ApiParam(name = "id", value = "编号", required = true) @RequestParam(name = "id") Serializable id, HttpServletRequest request) {
        boolean removed = courseMaterialService.removeById(id);
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }
    
    @ApiOperation(value = "批量删除课程资料")
    @GetMapping("/batchDeleteById")
    public Result<?> batchDeleteById(@ApiParam(name = "id", value = "编号", required = true) @RequestParam(name = "ids") String ids, HttpServletRequest request) {
        boolean removed = courseMaterialService.removeBatchByIds(Arrays.asList(ids.split(",")));
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }
    
}
