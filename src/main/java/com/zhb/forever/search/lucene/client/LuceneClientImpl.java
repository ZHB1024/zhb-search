package com.zhb.forever.search.lucene.client;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;

import com.zhb.forever.framework.page.Page;
import com.zhb.forever.framework.page.PageUtil;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.forever.search.lucene.LuceneClient;
import com.zhb.forever.search.lucene.LuceneUtil;
import com.zhb.forever.search.lucene.vo.DocumentVo;

/**
 * @author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
 * @createDate 2018年11月2日下午2:50:10
 */

public class LuceneClientImpl implements LuceneClient {

    @Override
    public void initLuceneIndex(List<Document> documents) {
        IndexWriter indexWriter = null;
        if (null != documents && documents.size() > 0) {
            try {
                // Analyzer analyzer = LuceneUtil.createIKAnalyzer();
                Analyzer analyzer = LuceneUtil.createStandardAnalyzer();

                Directory directory = LuceneUtil.createDirectory(LuceneUtil.INDEX_DIR);

                IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);

                indexWriter = LuceneUtil.createIndexWriter(directory, indexWriterConfig);
                for (Document document : documents) {
                    indexWriter.addDocument(document);
                }
                // indexWriter.commit();
                // indexWriter.forceMerge(1); // forceMerge代替optimize

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != indexWriter) {
                    try {
                        indexWriter.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

        }
    }

    @Override
    public Page<DocumentVo> luceneSearch(String keyword, int start, int pageSize) throws Exception {

        List<DocumentVo> results = new ArrayList<DocumentVo>();

        Analyzer analyzer = LuceneUtil.createStandardAnalyzer();
        Directory directory = LuceneUtil.openDirectory(LuceneUtil.INDEX_DIR);
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        ;

        /* 1用于对单个域搜索 */
        QueryParser queryParser = new QueryParser("content", analyzer);
        Query query = queryParser.parse(keyword);

        /* 2用于多个域搜索 */
        String[] fields = { "content", "title" };
        MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(fields, analyzer);
        Query multiFieldQuery = multiFieldQueryParser.parse(keyword);

        // 3单个分词查询,term(最小的索引块，包含一个field名和值)
        Query tempQuery1 = new TermQuery(new Term("content", "咨询"));
        Query tempQuery2 = new TermQuery(new Term("content", "数据"));

        /* 4前缀查询 */
        PrefixQuery prefixQuery = new PrefixQuery(new Term("content", keyword));

        /* 5通配符搜索,有* ? 这两个通配符，*表示匹配任意多个字符，?表示匹配一个任意字符 */
        WildcardQuery wildcardQuery = new WildcardQuery(new Term("content", keyword + "?"));

        /* 6短语搜索，它可以指定关键词之间的最大距离 */
        PhraseQuery phraseQuery = new PhraseQuery(4, "content", "咨询", "数据");
        /*
         * phraseQuery.setSlop(4); phraseQuery.add(new Term("content","咨询"));
         * phraseQuery.add(new Term("content","数据"));
         */

        /* 7查询“计张”、“计钦”组合的关键词，先指定一个前缀关键字，然后其他的关键字加在此关键字之后，组成词语进行搜索 */
        /*
         * MultiPhraseQuery multiPhraseQuery=new MultiPhraseQuery("content",); Term
         * term=new Term("content", "数据"); //前置关键字 Term term1=new Term("content",
         * "统计表"); //搜索关键字 Term term2=new Term("content", "通知"); //搜索关键字
         * multiPhraseQuery.add(term); multiPhraseQuery.add(new Term[]{term1, term2});
         */

        /* 8模糊搜索,模糊度是小于2的整数 */
        Term termFu = new Term("content", keyword);
        FuzzyQuery fuzzyquery = new FuzzyQuery(termFu, 1);

        /* 9正则表达式搜索 */
        Term termRe = new Term("name", keyword);
        // RegexQuery regexQuery = new RegexQuery(termRe);

        /* 10字符串范围搜索 */
        TermRangeQuery termRangeQuery = TermRangeQuery.newStringRange("content", "咨询", "数据", true, true);

        /* 11数字范围搜索 */
        // NumericRangeQuery numericRangeQuery = NumericRangeQuery.newIntRange("咨询", 0,
        // 4, true, true);;

        /* 12组合搜索(允许多个域不同关键字组合搜索,MUST、SHOULD、MUST_NOT) */
        /*
         * BooleanQuery booleanQuery = new BooleanQuery();
         * 
         * booleanQuery.add(fuzzyquery, Occur.MUST);
         */
        // booleanQuery.add(tempQuery2, Occur.MUST);
        // booleanQuery.add(multiFieldQuery, Occur.MUST_NOT);

        /* 过滤 */
        // QueryWrapperFilter filter = new QueryWrapperFilter(new TermQuery(new
        // Term("content", "2009")));

        /* 组合排序字段 */
        SortField sortField01 = new SortField("id", SortField.Type.STRING, false);
        SortField sortField02 = new SortField("content", SortField.Type.STRING, false);
        Sort sort = new Sort(new SortField[] { sortField01, sortField02 });
        TopFieldCollector collector = TopFieldCollector.create(sort, start + pageSize, false, false, false);

        // 总记录数
        // int totalCount =
        // indexSearcher.search(termRangeQuery,null,Integer.MAX_VALUE).totalHits;
        /*
         * int totalCount = indexSearcher.count(termRangeQuery); //搜索
         * indexSearcher.search(termRangeQuery,collector); ScoreDoc[] scoreDocs =
         * collector.topDocs(start,pageSize).scoreDocs;
         */

        TopDocs topDocs = indexSearcher.search(multiFieldQuery, 10);
        long totalCount = topDocs.totalHits;
        System.out.println("检索总条数：" + totalCount);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        // ScoreDoc[] scoreDocs = indexSearcher.search(query,
        // null,Integer.MAX_VALUE,sort).scoreDocs;
        // 高亮显示
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
        Highlighter highlighter = new Highlighter(formatter, new QueryScorer(multiFieldQuery));
        for (ScoreDoc scoreDoc : scoreDocs) {
            Document document = indexSearcher.doc(scoreDoc.doc);
            DocumentVo vo = new DocumentVo();

            /*
             * highlighter.setTextFragmenter(new
             * SimpleFragmenter(document.get("content").length())); String text =
             * highlighter.getBestFragment(analyzer, "content",document.get("content"));
             */

            TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(document.get("content")));
            String text = highlighter.getBestFragment(tokenStream, document.get("content"));
            if (StringUtil.isBlank(text)) {
                text = document.get("content");
            }
            vo.setId(document.get("id"));
            vo.setContent(text);
            vo.setType(document.get("type"));
            vo.setSystemId(document.get("title"));
            vo.setScore(scoreDoc.score);
            results.add(vo);
        }

        return PageUtil.getPage(results.iterator(), start, pageSize, totalCount);
    }

}
