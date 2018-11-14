package com.zhb.forever.search.elastic.client.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhb.forever.framework.page.Page;
import com.zhb.forever.framework.page.PageUtil;
import com.zhb.forever.framework.util.RandomUtil;
import com.zhb.forever.search.elastic.client.ElasticSearchClient;
import com.zhb.forever.search.elastic.vo.ElasticSearchIndexData;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年11月14日上午9:01:49
*/

public class ElasticSearchClientImpl implements ElasticSearchClient {

    protected final Logger logger = LoggerFactory.getLogger(ElasticSearchClientImpl.class);

    private TransportClient client = null;

    public final static int PORT = 9300; // http请求的端口是9200，客户端是9300

    public final static String HOST = "127.0.0.1";
    public final static String HOST2 = "172.16.10.215";

    @Override
    @SuppressWarnings("resource")
    public void getConnect() throws UnknownHostException {
        if (null == client || client.connectedNodes().isEmpty()) {
            Settings settings = Settings.builder().put("client.transport.sniff", true)
                    .put("cluster.name", "my-application").build();
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddresses(new TransportAddress(InetAddress.getByName(HOST), PORT));
        }
    }

    @Override
    public void initIndex(String index, String type, ElasticSearchIndexData data) {
        try {
            Map<String, Object> json = new HashMap<String, Object>();
            String id = RandomUtil.getRandomUUID();
            json.put("id", id);
            json.put("name", data.getName());
            json.put("sex", data.getSex());
            json.put("age", data.getAge());
            json.put("birthday", data.getBirthday());
            json.put("phone", data.getPhone());
            json.put("email", data.getEmail());
            client.prepareIndex(index, type, id).setSource(json).get();
            logger.info("initIndex data total 1  个");
        } catch (Exception e) {
            e.printStackTrace();
            client.close();
            logger.info("initIndex data fail.....");
        }
    }

    @Override
    public void initIndex(String index, String type, List<ElasticSearchIndexData> datas) {
        try {
            if (null != datas && datas.size() > 0) {
                for (ElasticSearchIndexData data : datas) {
                    Map<String, Object> json = new HashMap<String, Object>();
                    String id = RandomUtil.getRandomUUID();
                    json.put("id", id);
                    json.put("name", data.getName());
                    json.put("sex", data.getSex());
                    json.put("age", data.getAge());
                    json.put("birthday", data.getBirthday());
                    json.put("phone", data.getPhone());
                    json.put("email", data.getEmail());
                    client.prepareIndex(index, type, id).setSource(json).get();
                }
                logger.info("initIndex data total " + datas.size() + " 个");
            }
        } catch (Exception e) {
            e.printStackTrace();
            client.close();
            logger.info("initIndex data fail.....");
        }

    }

    @Override
    public ElasticSearchIndexData getIndexById(String id, String index, String type) {
        ElasticSearchIndexData data = null;
        GetResponse response = client.prepareGet(index, type, id).get();
        Map<String, DocumentField> fields = response.getFields();
        Map<String, Object> map = response.getSource();
        if (null != map) {
            data = new ElasticSearchIndexData();
            data.setData(map);
        }
        return data;
    }

    public boolean updateIndexById(ElasticSearchIndexData data, String index, String type) {
        try {
            Map<String, Object> json = new HashMap<String, Object>();
            json.put("id", data.getId());
            json.put("name", data.getName());
            json.put("sex", data.getSex());
            json.put("age", data.getAge());
            json.put("birthday", data.getBirthday());
            json.put("phone", data.getPhone());
            json.put("email", data.getEmail());

            UpdateRequest updateRequest = new UpdateRequest();
            updateRequest.index(index);
            updateRequest.type(type);
            updateRequest.id(data.getId());
            /*updateRequest.doc(XContentFactory.jsonBuilder().startObject().field("name", data.getName())
                    .field("sex", data.getSex()).field("age", data.getAge())
                    .field("birthday", data.getBirthday())
                    .field("phone", data.getPhone()).field("email", data.getEmail()).endObject());*/
            
            UpdateResponse response1 = client.update(updateRequest).get();

            /*
             * client.prepareUpdate(index,type,data.getId())
             * .setDoc(json).execute().get();
             */
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("updateIndex fail.......");
            return false;
        }
        return true;
    }

    public boolean delIndexByIndex(String index) {
        try {
            IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(index);
            IndicesExistsResponse inExistsResponse = client.admin().indices().exists(inExistsRequest).actionGet();
            if (inExistsResponse.isExists()) {
                DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(index).execute().actionGet();
                return dResponse.isAcknowledged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delIndexById(String id, String index, String type) {
        try {
            DeleteResponse dResponse = client.prepareDelete(index, type, id).execute().actionGet();
            boolean temp = dResponse.forcedRefresh();
            boolean temp01 = dResponse.isFragment();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Page<ElasticSearchIndexData> query(String index, String type, String keyWord, int start, int pageSize)
            throws Exception {
        List<ElasticSearchIndexData> results = null;
        try {
            QueryBuilder all = QueryBuilders.matchAllQuery();// 全匹配
            QueryBuilder matchQueryName = QueryBuilders.matchQuery("name", keyWord); // 模糊匹配
                                                                                        // 单个匹配
            QueryBuilder wildCardQuery = QueryBuilders.wildcardQuery("name", "*" + keyWord + "*");// 模糊匹配
            QueryBuilder rangeQuery = QueryBuilders.rangeQuery("age").gt(0); // 大于1
            QueryBuilder multiQuery = QueryBuilders.multiMatchQuery(keyWord, "name", "age");// 多字段单值匹配
            QueryBuilder term = QueryBuilders.termQuery("name", keyWord);

            // 复合查询类型 and or
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                    .must(QueryBuilders.matchQuery("name", keyWord))
                    .should(QueryBuilders.matchQuery("age", 100));

            HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);
            highlightBuilder.preTags("<span style=\"color:red\">");
            highlightBuilder.postTags("</span>");

            SearchResponse sr = client.prepareSearch(index).setTypes(type)
                    // 设置查询类型 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询
                    // 2.SearchType.SCAN = 扫描查询,无序
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(boolQuery)
                    // .setPostFilter(QueryBuilders.rangeQuery("age").from(20).to(30))
                    .addSort("age", SortOrder.DESC) // 降序
                    .setFrom(start)
                    .setSize(pageSize)// 不设置的话，默认取10条数据
                    .setExplain(true)// 设置是否按查询匹配度排序
                    .highlighter(highlightBuilder)
                    .get();
            SearchHits hits = sr.getHits();
            logger.info("查到记录数：" + hits.getTotalHits());
            long totalCount = hits.getTotalHits();
            if (totalCount > 0) {
                results = new ArrayList<ElasticSearchIndexData>();
                SearchHit[] searchHists = hits.getHits();
                if (searchHists.length > 0) {
                    for (SearchHit hit : searchHists) {
                        Map<String, Object> map = hit.getSourceAsMap();
                        if (null != map) {
                            ElasticSearchIndexData data = new ElasticSearchIndexData();
                            data.setId(null == map.get("id") ? "" : map.get("id").toString());
                            data.setAge(null == map.get("age") ? 0 : Integer.parseInt(map.get("age").toString()));
                            data.setSex(null == map.get("sex") ? "" : map.get("sex").toString());
                            data.setBirthday(null == map.get("birthday") ? null : (Calendar) map.get("birthday"));
                            data.setPhone(null == map.get("phone") ? "" : map.get("phone").toString());
                            data.setEmail(null == map.get("email") ? "" : map.get("email").toString());
                            // 高亮显示
                            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                            // name高亮
                            HighlightField nameField = highlightFields.get("name");
                            if (null != nameField) {
                                StringBuilder sb = new StringBuilder();
                                Text[] fragments = nameField.fragments();
                                for (Text text : fragments) {
                                    sb.append(text);
                                }
                                data.setName(sb.toString());
                            }
                            results.add(data);
                        }

                    }
                }
                return PageUtil.getPage(results.iterator(), start, pageSize, totalCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
            client.close();
            logger.info("query index fail......");
        }
        return Page.EMPTY_PAGE;
    }

    @Override
    public void closeConnect() {
        if (null != client && !client.connectedNodes().isEmpty()) {
            client.close();
            logger.info("执行关闭连接操作...");
        }
    }

    /**
     * 转换成json对象
     *
     * @param user
     * @return
     */
    private String generateJson(ElasticSearchIndexData user) {
        String json = "";
        try {
            XContentBuilder contentBuilder = XContentFactory.jsonBuilder().startObject();
            contentBuilder.field("id", user.getId());
            contentBuilder.field("name", user.getName());
            contentBuilder.field("age", user.getAge());
            json = contentBuilder.endObject().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

}


