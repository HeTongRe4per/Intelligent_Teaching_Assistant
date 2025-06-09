package com.hetongre4per.ita.modules.system.controller;

import com.hetongre4per.ita.config.CourseRecommendationComponent;
import com.hetongre4per.ita.modules.system.entity.domain.Course;
import com.hetongre4per.ita.vo.Result;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recommend")
public class RecommendationController {
    
    @Autowired
    private CourseRecommendationComponent recommendationService;
    
    @GetMapping("/recommendByUser")
    public Result<List<Course>> recommendByUser(@ApiParam(name = "studentId", value = "学生编号") @RequestParam(value = "studentId", required = true) Integer studentId,
                                                  @RequestParam(defaultValue= "4") int topN) {
        List<Course> courses = recommendationService.recommendCoursesByUser(studentId, topN);
        return Result.success(courses);
    }
    
    @GetMapping("/recommendByItem")
    public Result<List<Course>> recommendByItem(@ApiParam(name = "studentId", value = "学生编号") @RequestParam(value = "studentId", required = true) Integer studentId,
                                  @RequestParam(defaultValue= "4") int topN) {
        List<Course> courses = recommendationService.recommendCoursesByItem(studentId, topN);
        return Result.success(courses);
    }
    
    @GetMapping("/hybridRecommend")
    public Result<List<Course>> hybridRecommend(@ApiParam(name = "studentId", value = "学生编号") @RequestParam(value = "studentId", required = true) Integer studentId,
                                @RequestParam(defaultValue= "4") int topN) {
        // 混合推荐策略：结合用户和物品的结果
        List<Course> userCFCourses = recommendationService.recommendCoursesByUser(studentId, topN);
        List<Course> itemCFCourses = recommendationService.recommendCoursesByItem(studentId, topN);
        
        // 简单合并去重
        Set<Course> recommendedCourses = new LinkedHashSet<>();
        recommendedCourses.addAll(userCFCourses);
        recommendedCourses.addAll(itemCFCourses);
        
        return Result.success(recommendedCourses.stream().limit(topN).collect(Collectors.toList()));
    }
}