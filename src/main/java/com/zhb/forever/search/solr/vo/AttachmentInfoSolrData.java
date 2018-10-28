package com.zhb.forever.search.solr.vo;

import java.util.Calendar;
import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年10月26日上午8:59:48
*/

public class AttachmentInfoSolrData {
    
    @Field("id")
    private String id;
    
    @Field("fileName")
    private String fileName;
    
    @Field("type")
    private String type;
    
    @Field("likeDegree")
    private String likeDegree;
    
    @Field("createUserId")
    private String createUserId;
    
    @Field("createTime")
    private Date createTime;
    
    @Field("filePath")
    private String filePath;
    
    @Field("thumbnailPath")
    private String thumbnailPath;
    
    public AttachmentInfoSolrData() {
        
    }

    public AttachmentInfoSolrData(String id,String fileName,String type) {
        this.id = id;
        this.fileName = fileName;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLikeDegree() {
        return likeDegree;
    }

    public void setLikeDegree(String likeDegree) {
        this.likeDegree = likeDegree;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

}


