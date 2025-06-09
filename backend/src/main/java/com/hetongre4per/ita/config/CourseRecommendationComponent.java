package com.hetongre4per.ita.config;

import com.hetongre4per.ita.modules.system.entity.domain.Course;
import com.hetongre4per.ita.modules.system.entity.domain.StudentCourse;
import com.hetongre4per.ita.modules.system.mapper.CourseMapper;
import com.hetongre4per.ita.modules.system.mapper.StudentCourseMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CourseRecommendationComponent {
    
    @Autowired
    private StudentCourseMapper studentCourseMapper;
    
    @Autowired
    private CourseMapper courseMapper;
    
    /**
     * 基于用户的协同过滤推荐课程
     * @param studentId 目标学生ID
     * @param topN 推荐数量
     * @return 推荐课程列表
     */
    public List<Course> recommendCoursesByUser(Integer studentId, int topN) {
        // 1. 获取所有学生的选课数据
        List<StudentCourse> allStudentCourses = studentCourseMapper.selectList(null);
        
        // 2. 构建用户-课程矩阵
        Map<Integer, Set<Integer>> userCourseMap = new HashMap<>();
        for (StudentCourse sc : allStudentCourses) {
            userCourseMap.computeIfAbsent(sc.getStudentId(), k -> new HashSet<>())
                         .add(sc.getCourseId());
        }
        
        // 3. 计算目标用户与其他用户的相似度
        Map<Integer, Double> similarityMap = new HashMap<>();
        Set<Integer> targetCourses = userCourseMap.getOrDefault(studentId, new HashSet<>());
        
        for (Map.Entry<Integer, Set<Integer>> entry : userCourseMap.entrySet()) {
            if (entry.getKey().equals(studentId)) continue; // 跳过自己
            
            Set<Integer> otherCourses = entry.getValue();
            double similarity = jaccardSimilarity(targetCourses, otherCourses);
            similarityMap.put(entry.getKey(), similarity);
        }
        
        // 4. 找出最相似的topK个用户
        List<Integer> similarUsers = similarityMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .limit(5) // 取前5个最相似用户
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        
        // 5. 获取这些相似用户选过但目标用户没选过的课程
        Set<Integer> recommendedCourseIds = new HashSet<>();
        for (Integer userId : similarUsers) {
            Set<Integer> courses = userCourseMap.get(userId);
            for (Integer courseId : courses) {
                if (!targetCourses.contains(courseId)) {
                    recommendedCourseIds.add(courseId);
                }
            }
        }
        
        // 6. 获取课程详细信息并返回
        if (recommendedCourseIds.isEmpty()) {
            return getPopularCourses(topN); // 如果没有推荐，返回热门课程
        }
        
        return courseMapper.selectBatchIds(recommendedCourseIds).stream()
                .limit(topN)
                .collect(Collectors.toList());
    }
    
    // 计算Jaccard相似度
    private double jaccardSimilarity(Set<Integer> set1, Set<Integer> set2) {
        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        
        Set<Integer> union = new HashSet<>(set1);
        union.addAll(set2);
        
        return union.isEmpty() ? 0 : (double) intersection.size() / union.size();
    }
    
    /**
     * 基于物品的协同过滤推荐课程
     * @param studentId 目标学生ID
     * @param topN 推荐数量
     * @return 推荐课程列表
     */
    public List<Course> recommendCoursesByItem(Integer studentId, int topN) {
        // 1. 获取所有学生的选课数据
        List<StudentCourse> allStudentCourses = studentCourseMapper.selectList(null);
        
        // 2. 构建课程-用户矩阵
        Map<Integer, Set<Integer>> courseUserMap = new HashMap<>();
        for (StudentCourse sc : allStudentCourses) {
            courseUserMap.computeIfAbsent(sc.getCourseId(), k -> new HashSet<>())
                    .add(sc.getStudentId());
        }
        
        // 3. 获取目标学生已选的课程
        Set<Integer> targetCourses = allStudentCourses.stream()
                .filter(sc -> sc.getStudentId().equals(studentId))
                .map(StudentCourse::getCourseId)
                .collect(Collectors.toSet());
        
        if (targetCourses.isEmpty()) {
            return getPopularCourses(topN); // 如果学生没有选过任何课程，返回热门课程
        }
        
        // 4. 计算目标课程与其他课程的相似度
        Map<Integer, Double> courseSimilarityMap = new HashMap<>();
        
        for (Integer courseId : courseUserMap.keySet()) {
            
            double maxSimilarity = 0;
            for (Integer targetCourseId : targetCourses) {
                double similarity = cosineSimilarity(
                        courseUserMap.get(targetCourseId),
                        courseUserMap.get(courseId)
                );
                maxSimilarity = Math.max(maxSimilarity, similarity);
            }
            courseSimilarityMap.put(courseId, maxSimilarity);
        }
        
        // 5. 获取最相似的前topN个课程
        List<Integer> recommendedCourseIds = courseSimilarityMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        
        return courseMapper.selectBatchIds(recommendedCourseIds);
    }
    
    // 计算余弦相似度
    private double cosineSimilarity(Set<Integer> set1, Set<Integer> set2) {
        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        
        double dotProduct = intersection.size();
        double norm1 = Math.sqrt(set1.size());
        double norm2 = Math.sqrt(set2.size());
        
        return (norm1 * norm2) == 0 ? 0 : dotProduct / (norm1 * norm2);
    }
    
    // 获取热门课程
    private List<Course> getPopularCourses(int topN) {
        QueryWrapper<StudentCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("course_id, count(*) as count")
                   .groupBy("course_id")
                   .orderByDesc("count")
                   .last("limit " + topN);
        
        List<Map<String, Object>> popularCourseMaps = studentCourseMapper.selectMaps(queryWrapper);
        List<Integer> popularCourseIds = popularCourseMaps.stream()
                .map(map -> (Integer) map.get("course_id"))
                .collect(Collectors.toList());
        
        return courseMapper.selectBatchIds(popularCourseIds);
    }
}