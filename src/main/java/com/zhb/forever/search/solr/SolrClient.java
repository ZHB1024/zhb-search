package com.zhb.forever.search.solr;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;

import com.zhb.forever.framework.page.Page;
import com.zhb.forever.search.solr.vo.AttachmentInfoSolrData;
import com.zhb.forever.search.solr.vo.KnowledgeVO;
import com.zhb.forever.search.solr.vo.NewsIndexVO;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年10月25日上午11:51:03
*/

public interface SolrClient {
    
    void addAttachments(List<AttachmentInfoSolrData> datas);
    
    List<AttachmentInfoSolrData> getAttachments(String keyword,String orderField, int start,int pageSize);
    
    
    List<NewsIndexVO> getNews(String keyword,String orderField, int start,int pageSize);
    
    void addNews(String id, String title,String content);
    
    void addKnowledge(List<NewsIndexVO> knowIndexDatas) throws SolrServerException, IOException;
    
    Page<KnowledgeVO> searchKnow(String keywords, String systemId, Map<String, String> queryParams, int start,int pageSize);

}


