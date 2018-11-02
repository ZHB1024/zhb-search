package com.zhb.forever.search.lucene;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;

import com.zhb.forever.framework.page.Page;
import com.zhb.forever.search.lucene.vo.DocumentVo;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年11月2日下午2:49:07
*/

public interface LuceneClient {
    
    void initLuceneIndex(List<Document> documents) throws IOException;
    
    Page<DocumentVo> luceneSearch(String keyword, int start ,int pageSize) throws Exception;

}


