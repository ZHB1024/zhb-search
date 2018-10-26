package com.zhb.forever.search.solr.server;

import org.apache.solr.client.solrj.impl.HttpSolrClient;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年10月26日下午5:17:52
*/

public class AttachmentSolrServer extends HttpSolrClient {

    public AttachmentSolrServer(Builder builder) {
        super(builder);
    }
    
}


