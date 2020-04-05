package com.xuecheng.manage_cms;


import com.xuecheng.manage_cms.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PageServiceTest {

    @Autowired
    PageService pageService;

    @Test
    public void  getPageHtml(){
        String pageHtml = pageService.getPageHtml("5e6cd2dfecde8e43ec81f7c4");
        System.out.println(pageHtml);

    }
}
