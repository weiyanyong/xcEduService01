package com.xuecheng.framework.domain.cms.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
/*@Data注解在类上，会为类的所有属性自动生成setter/getter、
 equals、canEqual、hashCode、toString方法，如为final属性，
 则不会为该属性生成setter方法。*/
public class QueryPageRequest {
    //@ApiModelProperty：用对象接收参数时，描述对象的一个字段
    @ApiModelProperty("站点id")
    private  String siteId;
    private  String pageId;
    private  String pageName;
    private  String pageAliase;
    private  String templateId;

}
