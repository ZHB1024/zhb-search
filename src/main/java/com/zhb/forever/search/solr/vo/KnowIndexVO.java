package com.zhb.forever.search.solr.vo;

import java.util.List;

import org.apache.solr.client.solrj.beans.Field;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年10月25日下午12:08:41
*/

public class KnowIndexVO {

    @Field
    private String id;

    @Field("key_words")
    private String keywords;

    @Field("title")
    private String title;

    @Field("content")
    private String content;

    @Field("system_ids")
    private List<String> systemIds;

    @Field("type")
    private String type;

    @Field("sort")
    private int sort;

    @Field("update_time")
    private long updateTime;

    @Field("creater")
    private String creater;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSort() {
        return this.sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getSystemIds() {
        return this.systemIds;
    }

    public void setSystemIds(List<String> systemIds) {
        this.systemIds = systemIds;
    }

    public long getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreater() {
        return this.creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

}


