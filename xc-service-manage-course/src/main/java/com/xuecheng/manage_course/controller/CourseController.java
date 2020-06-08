package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {
    @Autowired
    CourseService courseService;

    @Override
    @GetMapping("/courseview/{id}")
    public CourseView courseview(@PathVariable("id") String id) {
        return courseService.getCoruseView(id);
    }

    @Override
    @PostMapping("/preview/{id}")
    public CoursePublishResult preview(@PathVariable("id") String id) {
        return courseService.preview(id);

    }
    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        TeachplanNode teachplanList = courseService.findTeachplanList(courseId);
        return teachplanList;
    }

    @Override
    @PostMapping("/teachplan/add")
    //RequestBody将json解析成java对象
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
       return  courseService.addTeachplan(teachplan);

    }
@PostMapping("/coursepic/add")
    @Override
    public ResponseResult addCoursePic(@RequestParam("courseId") String courseId,@RequestParam("pic") String pic ) {
        return courseService.addCoursePic(courseId,pic);
    }

    @Override
    @PostMapping("/publish/{id}")
    public CoursePublishResult publish(@PathVariable("id")String id) {
        return courseService.publish(id);//4028e581617f945f01617f9dabc40000
    }
    //url传参：{courseId}-----@PathVariable("courseId")
// key-value传参：@RequestParam("courseId"）
    @Override
    @DeleteMapping("/coursepic/delete")
    public ResponseResult deleteCoursePic(@RequestParam("courseId") String courseId) {
        return courseService.deleteCoursePic(courseId);
    }

    @Override
    @GetMapping("/coursepic/list/{courseId}")
    public CoursePic findCoursePic(@PathVariable("courseId") String courseId) {
        return courseService.findCoursePic(courseId);
    }

    @Override
    public QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest) {
        return null;
    }

    @Override
    public CourseBase getCourseBaseById(String courseId) throws RuntimeException {
        return null;
    }

    @Override
    public ResponseResult updateCourseBase(String id, CourseBase courseBase) {
        return null;
    }

    @Override
    public CourseMarket getCourseMarketById(String id) {
        return null;
    }

    @Override
    public ResponseResult updateCourseMarket(String id, CourseMarket courseMarket) {
        return null;
    }

}
