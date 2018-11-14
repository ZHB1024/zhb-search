package com.zhb.forever.search.solr.client;

import java.util.List;

import com.zhb.forever.framework.page.Page;
import com.zhb.forever.search.solr.param.AttachmentInfoSolrIndexParam;
import com.zhb.forever.search.solr.vo.AttachmentInfoSolrData;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年11月14日上午8:58:14
*/

public interface SolrClient {
    
    /**
     * *添加附件索引
     * @param datas
     * 
     */
    void addAttachment(AttachmentInfoSolrData datas);
    
    /**
     * *批量添加附件索引
     * @param datas
     * 
     */
    void addAttachments(List<AttachmentInfoSolrData> datas);
    
    /**
     * *查询附件索引，分页
     * @param keyword，orderField，start，pageSize
     * 
     * @return
     */
    List<AttachmentInfoSolrData> getAttachments(String keyword,String orderField, int start,int pageSize);
    
    /**
     * *查询附件索引，分页
     * @param AttachmentInfoSolrIndexParam
     * 
     * @return
     */
    Page<AttachmentInfoSolrData> searchAttachmentsForPage(AttachmentInfoSolrIndexParam param);
    
    /**
     * *删除附件索引
     * @param id
     * 
     */
    void deleteAttachmentById(String id);
    
    /**
     * *删除附件索引
     * @param param
     * 
     * @return
     */
    void deleteAttachmentsByIds(List<String> ids);
    
    /**
     * *删除所有附件索引
     * @param param
     * 
     * @return
     */
    void deleteAllAttachments();

}


