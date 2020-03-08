package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
}
