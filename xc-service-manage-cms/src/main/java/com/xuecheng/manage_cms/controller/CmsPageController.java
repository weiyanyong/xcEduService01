package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {
    @Autowired
    PageService pageService;
    //Ctrl  + I  :重写接口中的方法
    //{page}/{size}使用url地址传参
    /*@PathVariable以及@RequestParam*/

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult  findList(@PathVariable int page, @PathVariable int size, QueryPageRequest queryPageRequest) {

        return pageService.findList(page,size,queryPageRequest);
    }

    @Override
    @PostMapping("/add")
    /*
    * @RequestBody和@ResponseBody注解的区别
@ResponseBody 注解表示该方法返回的结果直接写入Http响应正文，一般在异步获得数据时使用；
在使用@RequestMapping后，返回值通常被解析为跳转路径，加上@ResponseBody后返回值不会被解析为跳转路径，而是直接写入HTTP响应正文中。
例如，异步获得Json数据，加上@ResponseBody后  直接可以返回Json数据。
@RequestBody注解将Http请求正文插入方法中，使用合适的HttpMessageCoverter将请求体写入某一个对象。
*/
    //@RequestBody会把请求过来的json数据转换为对象
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return pageService.add(cmsPage);
    }

@GetMapping("/get/{id}")
    public CmsPage findById(@PathVariable String id){
      return  pageService.getById(id);
    }

    @DeleteMapping("/del/{id}")
    @Override
    public ResponseResult delete(@PathVariable String id) {
        return pageService.delete(id);
    }

    @Override
    @PostMapping("/postPage/{pageId}")
    public ResponseResult post(@PathVariable("pageId") String pageId) {
        return pageService.post(pageId);
    }

    @Override
    @PutMapping("/edit/{id}")
    public  CmsPageResult edit(@PathVariable("id") String id,@RequestBody CmsPage cmsPage){
        return pageService.update(id,cmsPage);
    }
}
