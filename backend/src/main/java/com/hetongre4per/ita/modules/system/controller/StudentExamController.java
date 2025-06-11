package com.hetongre4per.ita.modules.system.controller;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.hetongre4per.ita.modules.base.controller.BaseController;
import com.hetongre4per.ita.modules.system.entity.domain.Exam;
import com.hetongre4per.ita.modules.system.entity.domain.StudentExam;
import com.hetongre4per.ita.modules.system.entity.dto.StudentExamDTO;
import com.hetongre4per.ita.modules.system.service.ExamService;
import com.hetongre4per.ita.vo.Result;
import com.hetongre4per.ita.modules.system.service.StudentExamService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/manage/studentExam")
public class StudentExamController extends BaseController {
    
    @Autowired
    private StudentExamService studentExamService;
    @Autowired
    private ExamService examService;
    
    @ApiOperation(value = "获取学生考试成绩列表")
    @GetMapping("/list")
    public Result<IPage<StudentExam>> list(@ApiParam(name = "order", value = "排序字段") @RequestParam(value = "order", required = false, defaultValue = "id") String order,
                                    @ApiParam(name = "pageNo", value = "页码") @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                    @ApiParam(name = "pageSize", value = "分页大小") @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                    HttpServletRequest request) {
        QueryWrapper<StudentExam> queryWrapper = QueryWrapperUtil.build(StudentExam.class, request.getParameterMap());
        queryWrapper.orderByDesc(order);
        Page<StudentExam> page = new Page<>(pageNo, pageSize);
        return Result.success(studentExamService.page(page, queryWrapper));
    }
    
    @ApiOperation(value = "获取学生考试成绩信息")
    @GetMapping("/queryById")
    public Result<StudentExamDTO> queryById(@ApiParam(name = "id", value = "编号") @RequestParam(value = "id") Serializable id, HttpServletRequest request) {
        StudentExam studentExam = studentExamService.getById(id);
        StudentExamDTO studentExamDTO = new StudentExamDTO();
        BeanUtil.copyProperties(studentExam, studentExamDTO, CopyOptions.create().setIgnoreNullValue(true));
        return Result.success(studentExamDTO);
    }
    
    @ApiOperation(value = "保存学生考试成绩")
    @PostMapping("/add")
    public Result<?> add(@ApiParam(name = "studentExamDTO", value = "学生考试成绩信息") @RequestBody StudentExamDTO studentExamDTO, HttpServletRequest request) {
        StudentExam studentExam = new StudentExam();
        BeanUtil.copyProperties(studentExamDTO, studentExam);
        boolean saved = studentExamService.save(studentExam);
        return saved ? Result.success("添加成功") : Result.error("添加失败");
    }
    
    @ApiOperation(value = "更新学生考试成绩")
    @PostMapping("/edit")
    public Result<?> edit(@ApiParam(name = "studentExamDTO", value = "学生考试成绩信息") @RequestBody StudentExamDTO studentExamDTO, HttpServletRequest request) {
        StudentExam studentExam = new StudentExam();
        BeanUtil.copyProperties(studentExamDTO, studentExam, CopyOptions.create().setIgnoreNullValue(true));
        boolean saved = studentExamService.saveOrUpdate(studentExam);
        return saved ? Result.success("更新成功") : Result.error("更新失败");
    }
    
    @ApiOperation(value = "删除学生考试成绩")
    @GetMapping("/deleteById")
    public Result<?> deleteById(@ApiParam(name = "id", value = "编号", required = true) @RequestParam(name = "id") Serializable id, HttpServletRequest request) {
        boolean removed = studentExamService.removeById(id);
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }
    
    @ApiOperation(value = "批量添加学生考试成绩")
    @PostMapping("/batchAdd")
    public Result<?> batchAdd(@ApiParam(name = "courseId", value = "课程编号", required = true) @RequestParam(name = "examId") Integer examId, @ApiParam(name = "studentIds", value = "编号", required = true) @RequestParam(name = "studentIds") String studentIds, HttpServletRequest request) {
        Exam exam = examService.getById(examId);
        String[] studentIdStrs = studentIds.split(",");
        List<StudentExam> studentExams = new ArrayList<>();
        for (String studentIdStr : studentIdStrs) {
            StudentExam studentExam = new StudentExam();
            studentExam.setStudentId(Integer.valueOf(studentIdStr));
            studentExam.setExamId(examId);
            studentExam.setStartTime(exam.getStartTime());
            studentExam.setEndTime(exam.getEndTime());
            studentExams.add(studentExam);
        }
        boolean saved = studentExamService.saveBatch(studentExams);
        return saved ? Result.success("添加成功") : Result.error("添加失败");
    }
    
    @ApiOperation(value = "批量删除学生考试成绩")
    @GetMapping("/batchDeleteById")
    public Result<?> batchDeleteById(@ApiParam(name = "ids", value = "编号", required = true) @RequestParam(name = "ids") String ids, HttpServletRequest request) {
        boolean removed = studentExamService.removeBatchByIds(Arrays.asList(ids.split(",")));
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }

    @ApiOperation(value = "学生考试切屏计数")
    @PostMapping("/switchScreen")
    public Result<?> switchScreen(@RequestParam Integer id) {
        StudentExam exam = studentExamService.getById(id);
        if (exam == null) return Result.error("未找到考试记录");
        exam.setSwitchCount(exam.getSwitchCount() == null ? 1 : exam.getSwitchCount() + 1);
        studentExamService.updateById(exam);
        return Result.success("计数加一");
    }
    
}
