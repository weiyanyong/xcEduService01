package com.xuecheng.test.freemarker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/freemarker")
@Controller
public class FreemarkerController {
    @RequestMapping("/banner")
    public String index_banner(Map<String,Object> map){
        return "index_banner";
    }

    @RequestMapping("/test1")
    public String test1(Map<String,Object> map){
        map.put("name","weiyy");
        return "test1";
    }
}
