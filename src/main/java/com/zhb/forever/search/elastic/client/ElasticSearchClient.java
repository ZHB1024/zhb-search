package com.zhb.forever.search.elastic.client;

import java.net.UnknownHostException;
import java.util.List;

import com.zhb.forever.framework.page.Page;
import com.zhb.forever.search.elastic.vo.ElasticSearchIndexData;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年11月14日上午9:01:26
*/

public interface ElasticSearchClient {
    
    void getConnect() throws UnknownHostException;
    
    void initIndex(String index,String type ,ElasticSearchIndexData data) ;
    
    void initIndex(String index,String type ,List<ElasticSearchIndexData> datas);
    
    ElasticSearchIndexData getIndexById(String id,String index,String type);
    
    boolean updateIndexById(ElasticSearchIndexData data,String index,String type);
    
    boolean delIndexByIndex(String index);
    
    boolean delIndexById(String id,String index,String type);
    
    Page<ElasticSearchIndexData> query(String index,String type ,String keyWord , int start,int pageSize) throws Exception;
    
    void closeConnect();

}


