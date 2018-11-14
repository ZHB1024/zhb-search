package com.zhb.forever.search;

import com.zhb.forever.framework.spring.bean.locator.SpringBeanLocator;
import com.zhb.forever.search.elastic.client.ElasticSearchClient;
import com.zhb.forever.search.lucene.client.LuceneClient;
import com.zhb.forever.search.solr.client.SolrClient;

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
    
    public static ElasticSearchClient getElasticSearchClientBean() {
        Object bean = SpringBeanLocator.getInstance(
                Constants.ELASTICSEARCH_CLIENT_CONF).getBean(
                        Constants.ELASTICSEARCH_CLIENT);
        return (ElasticSearchClient) bean;
    }
    
    public static LuceneClient getLuceneClientBean() {
        Object bean = SpringBeanLocator.getInstance(
                Constants.LUCENE_CLIENT_CONF).getBean(
                        Constants.LUCENE_CLIENT);
        return (LuceneClient) bean;
    }

}


