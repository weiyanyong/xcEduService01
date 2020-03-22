package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;
//<CmsPage,String> 指定模型类和主键类型
/*
* HibernateRepository类似，通过继承MongoRepository接口，我们可以非常方便地实现对一个对象的增删改查，要使
用Repository的功能，先继承MongoRepository<T, 
TD>接口，其中T为仓库保存的bean类，TD为该bean的唯一标识的类型，一般为ObjectId。之后在service中注入该接口就可以
使用，无需实现里面的方法，spring会根据定义的规则自动生成。
但是MongoRepository实现了的只是最基本的增删改查的功能，要想增加额外的查询方法，可以按照以下规则定义接口的方法。自定义查询方
法，格式为“findBy+字段名+方法后缀”，方法传进的参数即字段的值，此外还支持分页查询，通过传进一个Pageable对象，返回Page集合。
*/
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
    /*
    * 同Spring Data JPA一样Spring Data mongodb也提供自定义方法的规则，如下：
按照findByXXX，findByXXXAndYYY、countByXXXAndYYY等规则定义方法，实现查询操作。
    * */
    CmsPage findByPageName(String pageName);
    CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName,String siteId,String pageWebPath);
}
