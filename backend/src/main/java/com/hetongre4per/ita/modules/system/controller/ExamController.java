package com.hetongre4per.ita.modules.system.controller;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.hetongre4per.ita.modules.base.controller.BaseController;
import com.hetongre4per.ita.modules.system.entity.domain.Exam;
import com.hetongre4per.ita.modules.system.entity.dto.ExamDTO;
import com.hetongre4per.ita.vo.Result;
import com.hetongre4per.ita.modules.system.service.ExamService;
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
@RequestMapping("/manage/exam")
public class ExamController extends BaseController {
    
    @Autowired
    private ExamService examService;
    
    @ApiOperation(value = "获取考试列表")
    @GetMapping("/list")
    public Result<IPage<Exam>> list(@ApiParam(name = "order", value = "排序字段") @RequestParam(value = "order", required = false, defaultValue = "id") String order,
                                    @ApiParam(name = "pageNo", value = "页码") @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                    @ApiParam(name = "pageSize", value = "分页大小") @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                    HttpServletRequest request) {
        QueryWrapper<Exam> queryWrapper = QueryWrapperUtil.build(Exam.class, request.getParameterMap());
        queryWrapper.orderByDesc(order);
        Page<Exam> page = new Page<>(pageNo, pageSize);
        return Result.success(examService.page(page, queryWrapper));
    }
    
    @ApiOperation(value = "获取考试信息")
    @GetMapping("/queryById")
    public Result<ExamDTO> queryById(@ApiParam(name = "id", value = "编号") @RequestParam(value = "id") Serializable id, HttpServletRequest request) {
        Exam exam = examService.getById(id);
        ExamDTO examDTO = new ExamDTO();
        BeanUtil.copyProperties(exam, examDTO, CopyOptions.create().setIgnoreNullValue(true));
        return Result.success(examDTO);
    }
    
    @ApiOperation(value = "保存考试")
    @PostMapping("/add")
    public Result<?> add(@ApiParam(name = "examDTO", value = "考试信息") @RequestBody ExamDTO examDTO, HttpServletRequest request) {
        Exam exam = new Exam();
        BeanUtil.copyProperties(examDTO, exam);
        exam.setTeacherId(getUser().getId());
        boolean saved = examService.save(exam);
        return saved ? Result.success("添加成功") : Result.error("添加失败");
    }
    
    @ApiOperation(value = "更新考试")
    @PostMapping("/edit")
    public Result<?> edit(@ApiParam(name = "examDTO", value = "考试信息") @RequestBody ExamDTO examDTO, HttpServletRequest request) {
        Exam exam = new Exam();
        BeanUtil.copyProperties(examDTO, exam, CopyOptions.create().setIgnoreNullValue(true));
        boolean saved = examService.saveOrUpdate(exam);
        return saved ? Result.success("更新成功") : Result.error("更新失败");
    }
    
    @ApiOperation(value = "删除考试")
    @GetMapping("/deleteById")
    public Result<?> deleteById(@ApiParam(name = "id", value = "编号", required = true) @RequestParam(name = "id") Serializable id, HttpServletRequest request) {
        boolean removed = examService.removeById(id);
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }
    
    @ApiOperation(value = "批量删除考试")
    @GetMapping("/batchDeleteById")
    public Result<?> batchDeleteById(@ApiParam(name = "id", value = "编号", required = true) @RequestParam(name = "ids") String ids, HttpServletRequest request) {
        boolean removed = examService.removeBatchByIds(Arrays.asList(ids.split(",")));
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }
    
}
