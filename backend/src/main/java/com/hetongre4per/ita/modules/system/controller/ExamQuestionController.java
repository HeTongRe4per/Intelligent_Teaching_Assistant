package com.hetongre4per.ita.modules.system.controller;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.hetongre4per.ita.modules.base.controller.BaseController;
import com.hetongre4per.ita.modules.system.entity.domain.ExamQuestion;
import com.hetongre4per.ita.modules.system.entity.dto.ExamQuestionDTO;
import com.hetongre4per.ita.vo.Result;
import com.hetongre4per.ita.modules.system.service.ExamQuestionService;
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
@RequestMapping("/manage/examQuestion")
public class ExamQuestionController extends BaseController {
    
    @Autowired
    private ExamQuestionService examQuestionService;
    
    @ApiOperation(value = "获取试题列表")
    @GetMapping("/list")
    public Result<IPage<ExamQuestion>> list(@ApiParam(name = "order", value = "排序字段") @RequestParam(value = "order", required = false, defaultValue = "id") String order,
                                    @ApiParam(name = "pageNo", value = "页码") @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                    @ApiParam(name = "pageSize", value = "分页大小") @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                    HttpServletRequest request) {
        QueryWrapper<ExamQuestion> queryWrapper = QueryWrapperUtil.build(ExamQuestion.class, request.getParameterMap());
        queryWrapper.orderByDesc(order);
        Page<ExamQuestion> page = new Page<>(pageNo, pageSize);
        return Result.success(examQuestionService.page(page, queryWrapper));
    }
    
    @ApiOperation(value = "获取试题信息")
    @GetMapping("/queryById")
    public Result<ExamQuestionDTO> queryById(@ApiParam(name = "id", value = "编号") @RequestParam(value = "id") Serializable id, HttpServletRequest request) {
        ExamQuestion examQuestion = examQuestionService.getById(id);
        ExamQuestionDTO examQuestionDTO = new ExamQuestionDTO();
        BeanUtil.copyProperties(examQuestion, examQuestionDTO, CopyOptions.create().setIgnoreNullValue(true));
        return Result.success(examQuestionDTO);
    }
    
    @ApiOperation(value = "保存试题")
    @PostMapping("/add")
    public Result<?> add(@ApiParam(name = "examQuestionDTO", value = "试题信息") @RequestBody ExamQuestionDTO examQuestionDTO, HttpServletRequest request) {
        ExamQuestion examQuestion = new ExamQuestion();
        BeanUtil.copyProperties(examQuestionDTO, examQuestion);
        examQuestion.setTeacherId(getUser().getId());
        boolean saved = examQuestionService.save(examQuestion);
        return saved ? Result.success("添加成功") : Result.error("添加失败");
    }
    
    @ApiOperation(value = "更新试题")
    @PostMapping("/edit")
    public Result<?> edit(@ApiParam(name = "examQuestionDTO", value = "试题信息") @RequestBody ExamQuestionDTO examQuestionDTO, HttpServletRequest request) {
        ExamQuestion examQuestion = new ExamQuestion();
        BeanUtil.copyProperties(examQuestionDTO, examQuestion, CopyOptions.create().setIgnoreNullValue(true));
        boolean saved = examQuestionService.saveOrUpdate(examQuestion);
        return saved ? Result.success("更新成功") : Result.error("更新失败");
    }
    
    @ApiOperation(value = "删除试题")
    @GetMapping("/deleteById")
    public Result<?> deleteById(@ApiParam(name = "id", value = "编号", required = true) @RequestParam(name = "id") Serializable id, HttpServletRequest request) {
        boolean removed = examQuestionService.removeById(id);
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }
    
    @ApiOperation(value = "批量删除试题")
    @GetMapping("/batchDeleteById")
    public Result<?> batchDeleteById(@ApiParam(name = "id", value = "编号", required = true) @RequestParam(name = "ids") String ids, HttpServletRequest request) {
        boolean removed = examQuestionService.removeBatchByIds(Arrays.asList(ids.split(",")));
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }
    
}
