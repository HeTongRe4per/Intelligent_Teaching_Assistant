package com.hetongre4per.ita.modules.system.service.impl;

import com.hetongre4per.ita.modules.system.entity.domain.Course;
import com.hetongre4per.ita.modules.system.mapper.CourseMapper;
import com.hetongre4per.ita.modules.system.service.CourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

}
