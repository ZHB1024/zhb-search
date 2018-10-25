package com.zhb.forever.search.solr.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.SolrParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhb.forever.framework.page.Page;
import com.zhb.forever.framework.page.PageUtil;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.forever.search.solr.SolrClient;
import com.zhb.forever.search.solr.vo.KnowIndexVO;
import com.zhb.forever.search.solr.vo.KnowledgeVO;
import com.zhb.forever.search.solr.vo.NewsIndexVO;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年10月25日上午11:51:15
*/

public class SolrClientImpl implements SolrClient {
    
    private Logger logger = LoggerFactory.getLogger(SolrClientImpl.class);
    
    private SolrServer solrServer;
    
    
    @Override
    public void addNews(String id, String title, String content) {
        NewsIndexVO news = new NewsIndexVO();
        news.setId(id);
        news.setTitle(title);
        news.setContent(content);
        try {
            this.solrServer.addBean(news);
            this.solrServer.commit();
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
        QueryResponse rsp = query(this.solrServer, query);
        return rsp.getBeans(NewsIndexVO.class);
    }
    
    @Override
    public void addKnowledge(List<NewsIndexVO> knowIndexDatas) throws SolrServerException, IOException {
        if (null != knowIndexDatas && knowIndexDatas.size() > 0) {
            for (NewsIndexVO knowIndexData : knowIndexDatas) {
                this.solrServer.addBean(knowIndexData);
            }
            this.solrServer.commit();
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
        QueryResponse rsp = query(this.solrServer, query);
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
    
    private QueryResponse query(SolrServer solr, SolrParams params) {
        int i = 0;
        while (i++ < 5) {
            try {
                return solr.query(params);
            } catch (SolrServerException e) {
                this.logger.info("solr query exception,time:{},msg:{}", Integer.valueOf(i), e.getMessage());
            }
        }
        throw new RuntimeException("query exception");
    }

    
    public SolrServer getSolrServer() {
        return solrServer;
    }

    public void setSolrServer(SolrServer solrServer) {
        this.solrServer = solrServer;
    }
}

