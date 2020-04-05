package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PageService {
    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    CmsConfigRepository cmsConfigRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    RabbitTemplate rabbitTemplate;


//分页查询
    public QueryResponseResult findList( int page, int size, QueryPageRequest queryPageRequest) {
        if(queryPageRequest == null){
            queryPageRequest = new QueryPageRequest();
        }
        //自定义条件查询,页面别名模糊查询，需要自定义字符串的匹配器实现模糊查询
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("pageAliase",
                ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cmsPage = new CmsPage();
        //设置站点id作为查询条件
        if(StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if(StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        if(StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);

        if(page <= 0){
            page = 1;
        }
        page = page - 1;
        if(size <= 0 ){
            size = 10;
        }
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());
        queryResult.setTotal(all.getTotalElements());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }

//添加页面
    public CmsPageResult add(CmsPage cmsPage){
        //校验pageName、siteId、pageWebPath的唯一性，已建立唯一索引pageName_1_pageWebPath_1_siteId_1
        if(cmsPage == null){

        }
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(),cmsPage.getSiteId(),cmsPage.getPageWebPath());
        if(cmsPage1 != null){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }

            //mongodb会自动生成主键
            cmsPage.setPageId(null);
            cmsPageRepository.save(cmsPage);
            return  new CmsPageResult(CommonCode.SUCCESS,cmsPage);

    }

//更新页面，根据id查询，findById来自于CurdRepository
    public CmsPage getById(String id){
        //Optional 类是一个可以为null的容器对象。如果值存在则isPresent()方法会返回true，调用get()方法会返回该对象。
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if(optional.isPresent()){
            CmsPage cmsPage = optional.get();
            return  cmsPage;
        }
        return null;
    }
    //更新页面
    public CmsPageResult update(String id,CmsPage cmsPage){
       CmsPage one = getById(id);
        if(one != null){
                   one.setTemplateId(cmsPage.getTemplateId());
                   one.setSiteId(cmsPage.getSiteId());
                   one.setPageAliase(cmsPage.getPageAliase());
                   one.setPageName(cmsPage.getPageName());
                   one.setPageWebPath(cmsPage.getPageWebPath());
                   one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
                   one.setDataUrl(cmsPage.getDataUrl());
        }
       CmsPage save =  cmsPageRepository.save(one);
        if(save != null){
            CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS,save);
            return cmsPageResult;
        }
        return  new CmsPageResult(CommonCode.FAIL,null);
    }

    //删除页面
    public ResponseResult delete(String id){
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if(optional.isPresent()){
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);

    }

   public CmsConfig getConfigById(String id){
            Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
            if(optional.isPresent()){
                return optional.get();
            }
            return  null;
        }



  //页面静态化
    public String getPageHtml(String pageId){
        //获取数据
        Map model = getModelByPageId(pageId);
        //获取模板
        String templateByPageId = getTemplateByPageId(pageId);
        if(model == null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        if(templateByPageId == null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //执行静态化
        String html = generateHtml(templateByPageId, model);
        return html;
    }

    //执行静态化
    private  String generateHtml(String templateContent,Map model){
        //创建配置类
        Configuration configuration=new Configuration(Configuration.getVersion());
        //模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",templateContent);
        configuration.setTemplateLoader(stringTemplateLoader);
        try {
            Template template = configuration.getTemplate("template");
            //静态化
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            //静态化内容
            return content;
        }catch (Exception e){
         e.printStackTrace();
        }
        return null;
    }

//获取页面的模板,根据pageid获取templateId；
// 通过templateId再cms_template表找到TemplateFileId；
// 通过TemplateFileId关联object_id找到fs.files;TemplateFileId关联fs.chunk找到files_id得到data字段模板内容返回（运用到了gridFsTemplate）
    private  String getTemplateByPageId(String pageId){
        CmsPage cmsPage = this.getById(pageId);
        if(cmsPage == null){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String templateId = cmsPage.getTemplateId();
        if(StringUtils.isEmpty(templateId)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if(optional.isPresent()){
            CmsTemplate cmsTemplate = optional.get();
            //获取模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();
//根据文件id查文件
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
          try {
                String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                return content;
            }catch (IOException e){
                e.printStackTrace();
            }

        }
return  null;
    }
   //根据pageid获取数据
    private Map getModelByPageId(String pageId){
        CmsPage cmsPage = this.getById(pageId);
        if(cmsPage == null){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String dataUrl = cmsPage.getDataUrl();
        if(StringUtils.isEmpty(dataUrl)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }

        //通过resttemplate请求dataurl获取数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;
    }

    //保存html到GridFS,将html文件id更新到cmsPage中,返回cmspage
    private CmsPage saveHtml(String pageId,String htmlContent){
        //先得到页面信息
        CmsPage cmsPage = this.getById(pageId);
        if(cmsPage == null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        ObjectId objectId = null;
        try {
            //将htmlContent内容转成输入流
            InputStream inputStream = IOUtils.toInputStream(htmlContent, "utf-8");
            //将html文件内容保存到GridFS
            objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //将html文件id更新到cmsPage中
        cmsPage.setHtmlFileId(objectId.toHexString());
        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }

    //页面发布
    public ResponseResult post(String pageId){
        //执行页面静态化
        String pageHtml = this.getPageHtml(pageId);
        //将页面静态化文件存储到GridFs中
        CmsPage cmsPage = saveHtml(pageId, pageHtml);
        //向MQ发消息
        sendPostPage(pageId);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //向mq 发送消息
    private void sendPostPage(String pageId){
        //得到页面信息
        CmsPage cmsPage = this.getById(pageId);
        if(cmsPage == null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //创建消息对象
        Map<String,String> msg = new HashMap<>();
        msg.put("pageId",pageId);
        //运用JSON将map转成json串
        String jsonString = JSON.toJSONString(msg);
        //发送给mq
        //站点id
        String siteId = cmsPage.getSiteId();
        //json格式发送，siteId为routingkey，
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE,siteId,jsonString);
    }
}

