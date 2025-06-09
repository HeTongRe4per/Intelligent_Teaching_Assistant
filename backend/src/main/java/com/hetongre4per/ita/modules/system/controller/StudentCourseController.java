package com.hetongre4per.ita.modules.system.controller;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.hetongre4per.ita.modules.base.controller.BaseController;
import com.hetongre4per.ita.modules.system.entity.domain.StudentCourse;
import com.hetongre4per.ita.modules.system.entity.dto.StudentCourseDTO;
import com.hetongre4per.ita.vo.Result;
import com.hetongre4per.ita.modules.system.service.StudentCourseService;
import com.hetongre4per.ita.utils.QueryWrapperUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
@RequestMapping("/manage/studentCourse")
public class StudentCourseController extends BaseController {
    
    @Autowired
    private StudentCourseService studentCourseService;
    
    @ApiOperation(value = "获取学生选课列表")
    @GetMapping("/list")
    public Result<IPage<StudentCourse>> list(@ApiParam(name = "order", value = "排序字段") @RequestParam(value = "order", required = false, defaultValue = "id") String order,
                                    @ApiParam(name = "pageNo", value = "页码") @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                    @ApiParam(name = "pageSize", value = "分页大小") @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                    HttpServletRequest request) {
        QueryWrapper<StudentCourse> queryWrapper = QueryWrapperUtil.build(StudentCourse.class, request.getParameterMap());
        queryWrapper.orderByDesc(order);
        Page<StudentCourse> page = new Page<>(pageNo, pageSize);
        return Result.success(studentCourseService.page(page, queryWrapper));
    }
    
    @ApiOperation(value = "获取学生选课信息")
    @GetMapping("/queryById")
    public Result<StudentCourseDTO> queryById(@ApiParam(name = "id", value = "编号") @RequestParam(value = "id") Serializable id, HttpServletRequest request) {
        StudentCourse studentCourse = studentCourseService.getById(id);
        StudentCourseDTO studentCourseDTO = new StudentCourseDTO();
        BeanUtil.copyProperties(studentCourse, studentCourseDTO, CopyOptions.create().setIgnoreNullValue(true));
        return Result.success(studentCourseDTO);
    }
    
    @ApiOperation(value = "保存学生选课")
    @PostMapping("/add")
    public Result<?> add(@ApiParam(name = "studentCourseDTO", value = "学生选课信息") @RequestBody StudentCourseDTO studentCourseDTO, HttpServletRequest request) {
        StudentCourse studentCourse = new StudentCourse();
        BeanUtil.copyProperties(studentCourseDTO, studentCourse);
        studentCourse.setStudentId(getUser().getId());
        StudentCourse courseServiceOne = studentCourseService.getOne(new LambdaQueryWrapper<StudentCourse>().eq(StudentCourse::getStudentId, studentCourse.getStudentId()).eq(StudentCourse::getCourseId, studentCourse.getCourseId()));
        if (courseServiceOne != null) {
            return Result.error("你已经选过该课");
        }
        boolean saved = studentCourseService.save(studentCourse);
        return saved ? Result.success("选课成功") : Result.error("选课失败");
    }
    
    @ApiOperation(value = "更新学生选课")
    @PostMapping("/edit")
    public Result<?> edit(@ApiParam(name = "studentCourseDTO", value = "学生选课信息") @RequestBody StudentCourseDTO studentCourseDTO, HttpServletRequest request) {
        StudentCourse studentCourse = new StudentCourse();
        BeanUtil.copyProperties(studentCourseDTO, studentCourse, CopyOptions.create().setIgnoreNullValue(true));
        boolean saved = studentCourseService.saveOrUpdate(studentCourse);
        return saved ? Result.success("更新成功") : Result.error("更新失败");
    }
    
    @ApiOperation(value = "删除学生选课")
    @GetMapping("/deleteById")
    public Result<?> deleteById(@ApiParam(name = "id", value = "编号", required = true) @RequestParam(name = "id") Serializable id, HttpServletRequest request) {
        boolean removed = studentCourseService.removeById(id);
        return removed ? Result.success("退课成功") : Result.error("退课失败");
    }
    
    @ApiOperation(value = "批量删除学生选课")
    @GetMapping("/batchDeleteById")
    public Result<?> batchDeleteById(@ApiParam(name = "id", value = "编号", required = true) @RequestParam(name = "ids") String ids, HttpServletRequest request) {
        boolean removed = studentCourseService.removeBatchByIds(Arrays.asList(ids.split(",")));
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }
    
}
