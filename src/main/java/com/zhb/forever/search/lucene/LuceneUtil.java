package com.zhb.forever.search.lucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.SortField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.zhb.forever.framework.util.StringUtil;
/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年11月2日下午2:51:41
*/

public class LuceneUtil {

private static Logger log = LoggerFactory.getLogger(LuceneUtil.class);
    
    public static String INDEX_DIR ;
    public static IKAnalyzer ikAnalyzer;
    public static StandardAnalyzer standardAnalyzer;
    
    static {
        String property = System.getenv("propertyPath");
        if (StringUtil.isNotBlank(property)) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(property);
                Properties properties = new Properties();
                properties.load(fis);
                INDEX_DIR = properties.getProperty("sys.lucene.index.path");
            }catch(Exception e) {
                INDEX_DIR = "D:/java/lucene/index";
                e.printStackTrace();
                log.info("LuceneUtil load property fail ......");
            }
        }else {
            INDEX_DIR = "D:/java/lucene/index";
            log.info("环境变量未配置propertyPath");
        }
        
    }
    
    public static Directory createDirectory(String path){
        if (StringUtil.isBlank(path)) {
            return new RAMDirectory();
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            return FSDirectory.open(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        return null;
    }
    
    public static Directory openDirectory(String path){
        if (StringUtil.isBlank(path)) {
            return new RAMDirectory();
        }
        File file = new File(path);
        try {
            return FSDirectory.open(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        return null;
    }
    
    public static Analyzer createStandardAnalyzer(){
        if (null == standardAnalyzer) {
            standardAnalyzer = new StandardAnalyzer();
        }
        return standardAnalyzer;
    }
    
    public static Analyzer createIKAnalyzer(){
        if (ikAnalyzer == null) {
            ikAnalyzer = new IKAnalyzer();
        }
        return ikAnalyzer;
    }
    
    public static IndexWriterConfig createIndexWriterConfig(Analyzer analyzer){
        if (null == analyzer) {
            analyzer = new StandardAnalyzer();
        }
        return new IndexWriterConfig(analyzer);
    }
    
    public static IndexWriter createIndexWriter(Directory directory,IndexWriterConfig indexWriterConfig){
        if (null == directory || null == indexWriterConfig) {
            return null;
        }
        try {
            indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
            /*if (IndexWriter.isLocked(directory)) {
                IndexWriter.unlock(directory);
            }*/
            return new IndexWriter(directory,indexWriterConfig);
        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        return null;
    }
    
    public static Document createDocument(){
        return new Document();
    }
    
    public static void addStringFieldToDocument(Document doc,String field,String value,Field.Store store){
        doc.add(new TextField(field, valueNotNull(value),store));
    }
    
    public static DirectoryReader createDirectoryReader(Directory directory) {
        try {
            return DirectoryReader.open(directory);
        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        return null;
    }
    
    public static IndexReader createIndexReader(Directory directory){
        try {
            return DirectoryReader.open(directory);
        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        return null;
    }
    
    public static IndexSearcher createIndexSearcher(IndexReader indexReader){
        return new IndexSearcher(indexReader);
    }
    
    public static SortField createSortField(String field,SortField.Type type,boolean reverse){
        return new SortField(field,type,reverse);
    }
    
    /**
     * 更新索引
     * 
     * 例如：Term term = new Term("id","1234567");
     * 先去索引文件里查找id为1234567的Document，如果有就更新它(如果有多条，最后更新后只有一条)，如果没有就新增。
     * 数据库更新的时候，我们可以只针对某个列来更新，而lucene只能针对一行数据更新。
     * 
     * @param field Document的Field(类似数据库的字段)
     * @param value Field中的一个关键词
     * @param doc
     * @return
     */
    public static boolean updateIndex(IndexWriter indexWriter, Document doc,Term term) {
        try {
            indexWriter.updateDocument(term, doc);
            log.info("lucene update success.");
            return true;
        } catch (Exception e) {
            log.error("lucene update failure.", e);
            return false;
        }
    }
    
    /**
     * 删除整个索引库
     * 
     * @return
     */
    public static void deleteAllIndex(IndexWriter indexWriter){
        try {
            indexWriter.deleteAll();
            log.info("lucene delete all success.");
        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
    }
    
    /**
     * 判断索引库是否已创建
     * 
     * @return true:存在，false：不存在
     * @throws Exception
     */
    public static boolean existsIndex() throws Exception {
        File file = new File(LuceneUtil.INDEX_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        String indexSufix = "/segments.gen";
        // 根据索引文件segments.gen是否存在判断是否是第一次创建索引
        File indexFile = new File(LuceneUtil.INDEX_DIR + indexSufix);
        return indexFile.exists();
    }
    
    public static String valueNotNull(String value){
        if (StringUtil.isBlank(value)) {
            return "--";
        }
        
        return value;
    }

}


