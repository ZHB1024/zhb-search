package com.zhb.forever.search.solr.param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.SortClause;

import com.zhb.forever.framework.vo.OrderVO;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年10月26日下午1:45:19
*/

public class AttachmentInfoSolrIndexParam {

    public AttachmentInfoSolrIndexParam() {
        // TODO Auto-generated constructor stub
    }
    
    private String keyWord;
    private Map<String, String> params;
    
    private Integer start = 0;
    private Integer pageSize = 20;
    private Integer currentPage;
    
    private List<SortClause> sorts;
    
    
    public void addParams(String key,String value) {
        if (null == params) {
            params = new HashMap<>();
            params.put(key, value);
        }else {
            params.put(key, value);
        }
    }
    
    public void addSort(String field,SolrQuery.ORDER order) {
        if (null == sorts) {
            sorts = new ArrayList<>();
            SortClause sort = new SortClause(field,order);
            sorts.add(sort);
        }else {
            SortClause sort = new SortClause(field,order);
            sorts.add(sort);
        }
    }
    
    public String getKeyWord() {
        return keyWord;
    }
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
    public Map<String, String> getParams() {
        return params;
    }
    public void setParams(Map<String, String> params) {
        this.params = params;
    }
    public Integer getStart() {
        return start;
    }
    public void setStart(Integer start) {
        this.start = start;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<SortClause> getSorts() {
        return sorts;
    }

    public void setSorts(List<SortClause> sorts) {
        this.sorts = sorts;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
    
}


