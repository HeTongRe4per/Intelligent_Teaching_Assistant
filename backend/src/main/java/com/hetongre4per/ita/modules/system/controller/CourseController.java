package com.hetongre4per.ita.modules.system.controller;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.hetongre4per.ita.modules.base.controller.BaseController;
import com.hetongre4per.ita.modules.system.entity.domain.Course;
import com.hetongre4per.ita.modules.system.entity.dto.CourseDTO;
import com.hetongre4per.ita.vo.Result;
import com.hetongre4per.ita.modules.system.service.CourseService;
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
@RequestMapping("/manage/course")
public class CourseController extends BaseController {
    
    @Autowired
    private CourseService courseService;
    
    @ApiOperation(value = "获取课程列表")
    @GetMapping("/list")
    public Result<IPage<Course>> list(@ApiParam(name = "order", value = "排序字段") @RequestParam(value = "order", required = false, defaultValue = "id") String order,
                                    @ApiParam(name = "pageNo", value = "页码") @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                    @ApiParam(name = "pageSize", value = "分页大小") @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                    HttpServletRequest request) {
        QueryWrapper<Course> queryWrapper = QueryWrapperUtil.build(Course.class, request.getParameterMap());
        queryWrapper.orderByDesc(order);
        Page<Course> page = new Page<>(pageNo, pageSize);
        return Result.success(courseService.page(page, queryWrapper));
    }
    
    @ApiOperation(value = "获取课程信息")
    @GetMapping("/queryById")
    public Result<CourseDTO> queryById(@ApiParam(name = "id", value = "编号") @RequestParam(value = "id") Serializable id, HttpServletRequest request) {
        Course course = courseService.getById(id);
        CourseDTO courseDTO = new CourseDTO();
        BeanUtil.copyProperties(course, courseDTO, CopyOptions.create().setIgnoreNullValue(true));
        return Result.success(courseDTO);
    }
    
    @ApiOperation(value = "保存课程")
    @PostMapping("/add")
    public Result<?> add(@ApiParam(name = "courseDTO", value = "课程信息") @RequestBody CourseDTO courseDTO, HttpServletRequest request) {
        Course course = new Course();
        BeanUtil.copyProperties(courseDTO, course);
        boolean saved = courseService.save(course);
        return saved ? Result.success("添加成功") : Result.error("添加失败");
    }
    
    @ApiOperation(value = "更新课程")
    @PostMapping("/edit")
    public Result<?> edit(@ApiParam(name = "courseDTO", value = "课程信息") @RequestBody CourseDTO courseDTO, HttpServletRequest request) {
        Course course = new Course();
        BeanUtil.copyProperties(courseDTO, course, CopyOptions.create().setIgnoreNullValue(true));
        boolean saved = courseService.saveOrUpdate(course);
        return saved ? Result.success("更新成功") : Result.error("更新失败");
    }
    
    @ApiOperation(value = "删除课程")
    @GetMapping("/deleteById")
    public Result<?> deleteById(@ApiParam(name = "id", value = "编号", required = true) @RequestParam(name = "id") Serializable id, HttpServletRequest request) {
        boolean removed = courseService.removeById(id);
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }
    
    @ApiOperation(value = "批量删除课程")
    @GetMapping("/batchDeleteById")
    public Result<?> batchDeleteById(@ApiParam(name = "ids", value = "编号", required = true) @RequestParam(name = "ids") String ids, HttpServletRequest request) {
        boolean removed = courseService.removeBatchByIds(Arrays.asList(ids.split(",")));
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }
    
}
