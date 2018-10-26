package com.zhb.forever.search.solr.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhb.forever.framework.page.Page;
import com.zhb.forever.framework.page.PageUtil;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.forever.search.solr.SolrClient;
import com.zhb.forever.search.solr.param.AttachmentInfoSolrIndexParam;
import com.zhb.forever.search.solr.vo.AttachmentInfoSolrData;
import com.zhb.forever.search.solr.vo.KnowIndexVO;
import com.zhb.forever.search.solr.vo.KnowledgeVO;
import com.zhb.forever.search.solr.vo.NewsIndexVO;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年10月25日上午11:51:15
*/

public class SolrClientImpl implements SolrClient {
    
    private Logger logger = LoggerFactory.getLogger(SolrClientImpl.class);
    
    private HttpSolrClient zhbSolrServer = new HttpSolrClient.Builder("http://localhost:8983/solr/zhb")
                                            .withConnectionTimeout(10000)//设置连接超时时间
                                            .withSocketTimeout(10000).build();
    private HttpSolrClient attachmentSolrServer = new HttpSolrClient.Builder("http://localhost:8983/solr/attachment")
                                            .withConnectionTimeout(5000)//设置连接超时时间
                                            .withSocketTimeout(5000).build();;
    
    
    @Override
    public void addAttachment(AttachmentInfoSolrData data) {
        if (null != data) {
            try {
                this.attachmentSolrServer.addBean(data);
                this.attachmentSolrServer.commit();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SolrServerException e) {
                e.printStackTrace();
            }
        }
        
    }
    
    @Override
    public void addAttachments(List<AttachmentInfoSolrData> datas) {
        if (null != datas && datas.size() > 0) {
            try {
                this.attachmentSolrServer.addBeans(datas);
                this.attachmentSolrServer.commit();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SolrServerException e) {
                e.printStackTrace();
            }
        }
        
    }
    
    @Override
    public List<AttachmentInfoSolrData> getAttachments(String keyword,String orderField, int start,int pageSize){
        SolrQuery query = new SolrQuery();
        
        query.setQuery(String.format("\"%s\"", new Object[] { keyword }));
        
        query.setStart(start);
        query.setRows(pageSize);

        if (StringUtil.isNotBlank(orderField)) {
            query.setSort(orderField, SolrQuery.ORDER.asc);
        }
        QueryResponse rsp = query(this.attachmentSolrServer, query);
        return rsp.getBeans(AttachmentInfoSolrData.class);
    }
    
    @Override
    public Page<AttachmentInfoSolrData> searchAttachmentsForPage(AttachmentInfoSolrIndexParam param){
        if (null == param) {
            return null;
        }
        
        SolrQuery query = new SolrQuery();
        if (StringUtil.isNotBlank(param.getKeyWord())) {
            query.setQuery(String.format("\"%s\"", new Object[] { param.getKeyWord() }));
        }
        
        
        if (null != param.getParams()) {
            Map<String,String> params = param.getParams();
            for (Map.Entry<String, String> entry: params.entrySet()) {
                query.setParam(entry.getKey(), new String[] { entry.getValue() });
            }
        }
        
        if (StringUtil.isBlank(param.getKeyWord()) && null == param.getParams() ) {
            query.setQuery("*:*");
        }
        
        query.setStart(param.getStart());
        query.setRows(param.getPageSize());

        if (null != param.getSorts()) {
            query.setSorts(param.getSorts());
        }
        
        QueryResponse rsp = query(this.attachmentSolrServer, query);
        List<AttachmentInfoSolrData> datas = rsp.getBeans(AttachmentInfoSolrData.class);
        
        long count = rsp.getResults().getNumFound();
        long start = rsp.getResults().getStart();
        
        return PageUtil.getPage(datas.iterator(), start, param.getPageSize(), count);
    }
    
    @Override
    public void deleteAttachmentById(String id) {
        if (StringUtil.isNotBlank(id)) {
            try {
                UpdateResponse rsp = this.attachmentSolrServer.deleteById(id);
                UpdateResponse result = this.attachmentSolrServer.commit();
                logger.info(rsp.toString());
                logger.info(result.toString());
            } catch (SolrServerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteAttachmentsByIds(List<String> ids) {
        if (null != ids) {
            try {
                UpdateResponse rsp = this.attachmentSolrServer.deleteById(ids);
                UpdateResponse result = this.attachmentSolrServer.commit();
                logger.info(rsp.toString());
                logger.info(result.toString());
            } catch (SolrServerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void deleteAllAttachments() {
        try {
            attachmentSolrServer.deleteByQuery("*:*");
            attachmentSolrServer.commit();  
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  
        
    }
    
    @Override
    public void addNews(String id, String title, String content) {
        NewsIndexVO news = new NewsIndexVO();
        news.setId(id);
        news.setTitle(title);
        news.setContent(content);
        try {
            this.zhbSolrServer.addBean(news);
            this.zhbSolrServer.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public List<NewsIndexVO> getNews(String keyword, String orderField, int start, int pageSize) {
        SolrQuery query = new SolrQuery();
        /*if (userTypes.size() > 1) {
            StringBuilder strBuild = new StringBuilder();
            boolean first = true;
            for (int i = 0; i < userTypes.size(); ++i) {
                if (first) {
                    first = false;
                    strBuild.append("(");
                }
                String userType = (String) userTypes.get(i);
                strBuild.append(userType);
                if (i != userTypes.size() - 1) {
                    strBuild.append(" ");
                }
            }
            strBuild.append(")");
            query.addFilterQuery(
                    new String[] { new StringBuilder().append("user_type:").append(strBuild.toString()).toString() });
        } else if (1 == userTypes.size()) {
            query.addFilterQuery(new String[] {
                    new StringBuilder().append("user_type:").append((String) userTypes.get(0)).toString() });
        }*/
        
        //query.add("q", String.format("\"%s\"", keyword));
        query.setQuery(String.format("\"%s\"", new Object[] { keyword }));
        //query.set("text", "title:" + String.format("\"%s\"", new Object[] { keyword }));
        
        query.setStart(start);
        query.setRows(pageSize);

        if (StringUtil.isNotBlank(orderField)) {
            query.setSort(orderField, SolrQuery.ORDER.asc);
        }
        QueryResponse rsp = query(this.zhbSolrServer, query);
        return rsp.getBeans(NewsIndexVO.class);
    }
    
    @Override
    public void addKnowledge(List<NewsIndexVO> knowIndexDatas) throws SolrServerException, IOException {
        if (null != knowIndexDatas && knowIndexDatas.size() > 0) {
            for (NewsIndexVO knowIndexData : knowIndexDatas) {
                this.zhbSolrServer.addBean(knowIndexData);
            }
            this.zhbSolrServer.commit();
        }
        
    }
    
    @Override
    public Page<KnowledgeVO> searchKnow(String keywords, String systemId, Map<String, String> queryParams, int start,
            int pageSize) {
        SolrQuery query = new SolrQuery();
        query.setQuery(keywords);
        //query.addFilterQuery(new String[] { new StringBuilder().append("system_id:").append(systemId).toString() });
        query.setStart(Integer.valueOf(start));
        query.setRows(Integer.valueOf(pageSize));

        if (null != queryParams) {
            for (String type : queryParams.keySet()) {
                query.setParam(type, new String[] { (String) queryParams.get(type) });
            }
            
        }
        QueryResponse rsp = query(this.zhbSolrServer, query);
        List<KnowIndexVO> knowIndexList = rsp.getBeans(KnowIndexVO.class);
        List<KnowledgeVO> knowList = new ArrayList<KnowledgeVO>();
        for (KnowIndexVO knowIndex : knowIndexList) {
            KnowledgeVO vo = new KnowledgeVO();
            vo.setTitle(knowIndex.getTitle());
            vo.setContent(knowIndex.getContent());
            vo.setKeywords(knowIndex.getKeywords());
            vo.setType(knowIndex.getType());
            vo.setUpdateTime(new Date(knowIndex.getUpdateTime()));
            knowList.add(vo);
        }

        long count = rsp.getResults().getNumFound();
        long startReturn = rsp.getResults().getStart();
        return PageUtil.getPage(knowList.iterator(), startReturn, pageSize, count);
    }
    
    private QueryResponse query(HttpSolrClient solr, SolrQuery params) {
        int i = 0;
        while (i++ < 5) {
            try {
                return solr.query(params);
            } catch (SolrServerException | IOException e) {
                this.logger.info("solr query exception,time:{},msg:{}", Integer.valueOf(i), e.getMessage());
            }
        }
        throw new RuntimeException("query exception");
    }
    

    public HttpSolrClient getZhbSolrServer() {
        return zhbSolrServer;
    }

    public void setZhbSolrServer(HttpSolrClient zhbSolrServer) {
        this.zhbSolrServer = zhbSolrServer;
    }

    public HttpSolrClient getAttachmentSolrServer() {
        return attachmentSolrServer;
    }

    public void setAttachmentSolrServer(HttpSolrClient attachmentSolrServer) {
        this.attachmentSolrServer = attachmentSolrServer;
    }

}


