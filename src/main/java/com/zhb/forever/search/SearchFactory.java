package com.zhb.forever.search;

import com.zhb.forever.framework.spring.bean.locator.SpringBeanLocator;
import com.zhb.forever.search.solr.SolrClient;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年10月25日上午11:50:16
*/

public class SearchFactory {
    
    public static SolrClient getSolrClientBean() {
        Object bean = SpringBeanLocator.getInstance(
                Constants.SOLR_CLIENT_CONF).getBean(
                        Constants.SOLR_CLIENT);
        return (SolrClient) bean;
    }

}


