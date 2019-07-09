package com.example.lucene_example.demo2;

import com.example.lucene_example.ikAdapter.IKAnalyzer6x;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

//高亮
public class HighLighterDemo {
    //索引存放目录
    private static final String PATH = "D:\\examples\\lucene";

    @Test
    public void test1() throws Exception {
        FileUtils.deleteDirectory(new File(PATH));
        FileUtils.forceMkdir(new File(PATH));

        //Directory
        Directory directory = FSDirectory.open(Paths.get(PATH));
        //Analyzer,使用自己重写IKAnalyzer6x,true:粗粒度分词, false:细粒度分词
        Analyzer analyzer = new IKAnalyzer6x(true);
        //IndexWriterConfig
        IndexWriterConfig conf = new IndexWriterConfig(analyzer);
        //IndexWriter
        IndexWriter indexWriter = new IndexWriter(directory, conf);

        //DocumentDemo
        Document doc1 = new Document();
        doc1.add(new TextField("content", "习近平在深改总结会上的讲话引领航向", Field.Store.YES));
        indexWriter.addDocument(doc1);
        Document doc2 = new Document();
        doc2.add(new TextField("content", "书写好中华民族伟大复兴的“三农”新篇章", Field.Store.YES));
        indexWriter.addDocument(doc2);
        Document doc3 = new Document();
        doc3.add(new TextField("content", "在正定三年多的时间里，他住在办公室，吃在大食堂，和大家一起排队打饭，一起蹲在树下聊天。他在大街上临时摆桌子，听取群众意见；敞开县委、县政府机关大门，请背着粪筐的老农进来交谈；让县委干部走上街头搞随机问卷调查", Field.Store.YES));
        indexWriter.addDocument(doc3);

        indexWriter.commit();

        indexWriter.close();
    }


    @Test
    public void test2() throws IOException, ParseException, InvalidTokenOffsetsException {
        //Directory
        Directory directory = FSDirectory.open(Paths.get(PATH));
        //IndexReader
        IndexReader indexReader = DirectoryReader.open(directory);
        //IndexSearcher
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);


        //the default field for query terms
        String defaultField = "content";
        //Analyzer
        Analyzer analyzer = new IKAnalyzer6x(true);
        QueryParser queryParser = new QueryParser(defaultField, analyzer);

        //如果这里没有指定搜索字段(搜索字段:关键词，例如content:scala)，则会使用默认搜索字段即上面的defaultField
        String queryStr = "群众意见";
        Query query = queryParser.parse(queryStr);
        TopDocs topDocs = indexSearcher.search(query, 10);
        System.out.println("总命中数:" + topDocs.totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;//命中的记录数组

        SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<b style=\"color: red\">", "</b>");
        QueryScorer scorer = new QueryScorer(query);
        Highlighter highlighter = new Highlighter(htmlFormatter, scorer);
        /*
         * 创建Fragmenter 作用是将原始字符串拆分成独立的片段
         * SimpleSpanFragmenter 是尝试将让片段永远包含跨度匹配的文档
         * SimpleFragmenter 是负责将文本拆分封固定字符长度的片段，但它并处理子边界（默认100）
         * NullFragmenter 整个字符串作为单个片段返回，这适合于处理title域和前台文本较短的域
         */
        highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer, 40));

        for (ScoreDoc scoreDoc : scoreDocs) {
            int docId = scoreDoc.doc;//命中的文档id
            System.out.println(docId);
            Document document = indexSearcher.doc(docId);
            System.out.println(document.get("content"));
            String bestFragment = highlighter.getBestFragment(analyzer, defaultField, document.get(defaultField));
            System.out.println(bestFragment);
            System.out.println("=======================================================");
        }

        indexReader.close();
    }


}
