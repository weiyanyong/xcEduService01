package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

//@SpringBootTest找启动类，扫描bean
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {
    @Autowired
    CmsPageRepository cmsPageRepository;

    @Test
    public void testFindAll() {
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);
    }

    //不加Test,会报：java.lang.Exception: No tests found matching Method
    // testFindPage(com.xuecheng.manage_cms.dao.CmsPageRepositoryTest) from org.junit.internal.requests.ClassRequest@73a2e526
    @Test
    public void testFindPage() {
        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }

    @Test
    public void testUpdate() {
        //Optional,jdk1.8容器
        Optional<CmsPage> optional  = cmsPageRepository.findById("5abefd525b05aa293098fca6");
        if(optional.isPresent()){
            CmsPage cmsPage = optional.get();
            cmsPage.setPageAliase("WYY");
           CmsPage save =  cmsPageRepository.save(cmsPage);
            System.out.println(save);
        }
    }
        @Test
        public void testFindByName() {
            CmsPage cmspage = cmsPageRepository.findByPageName("index2.html");
            System.out.println(cmspage);
        }


    //自定义条件查询测试   
  @Test
  public void testFindAllByExample() {
    //条件匹配器，模糊查询
    ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher = exampleMatcher.withMatcher("pageAliase",
                ExampleMatcher.GenericPropertyMatchers.contains());
     //页面别名模糊查询，需要自定义字符串的匹配器实现模糊查询
    //ExampleMatcher.GenericPropertyMatchers.contains() 包含
//ExampleMatcher.GenericPropertyMatchers.startsWith()//开头匹配     
    //条件值
    CmsPage cmsPage = new CmsPage();
    //站点ID
    //cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
    //模板ID
    //cmsPage.setTemplateId("5a925be7b00ffc4b3c1578b5");
   cmsPage.setPageAliase("轮播");
    //创建条件实例，泛型放存放条件的对象
    Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
    Pageable pageable = new PageRequest(0, 10);
    Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
      List<CmsPage> content = all.getContent();
    System.out.println(content);
  }
}
