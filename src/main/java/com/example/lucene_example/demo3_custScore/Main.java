package com.example.lucene_example.demo3_custScore;

import com.example.lucene_example.ikAdapter.IKAnalyzer6x;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Main {
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
        doc1.add(new TextField("id", "0001", Field.Store.YES));
        doc1.add(new TextField("title", "java", Field.Store.YES));
        doc1.add(new TextField("content", "hello java world", Field.Store.YES));//0.3472057 0.6944114
        //_click_num要为NumericDocValuesField字段
        doc1.add(new NumericDocValuesField("_click_num", 1));
        indexWriter.addDocument(doc1);

        Document doc2 = new Document();
        doc2.add(new TextField("id", "0002", Field.Store.YES));
        doc2.add(new TextField("title", "scala", Field.Store.YES));
        doc2.add(new TextField("content", "hello scala world", Field.Store.YES));//0.3472057 1.0416172
        doc2.add(new NumericDocValuesField("_click_num", 2));
        indexWriter.addDocument(doc2);

        Document doc3 = new Document();
        doc3.add(new TextField("id", "0003", Field.Store.YES));
        doc3.add(new TextField("title", "java", Field.Store.YES));
        doc3.add(new TextField("content", "hello the java world", Field.Store.YES)); //0.3472057 1.3888228
        doc3.add(new NumericDocValuesField("_click_num", 3));
        indexWriter.addDocument(doc3);

        Document doc4 = new Document();
        doc4.add(new TextField("id", "0004", Field.Store.YES));
        doc4.add(new TextField("title", "ikAdapter", Field.Store.YES));
        doc4.add(new TextField("content", "我们是中国人,我们家在中国", Field.Store.YES));
        doc4.add(new NumericDocValuesField("_click_num", 4));
        indexWriter.addDocument(doc4);

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
        String queryStr = "hello";
        Query query = queryParser.parse(queryStr);

        MyCustScoreQuery myCustScoreQuery = new MyCustScoreQuery(query);

        TopDocs topDocs = indexSearcher.search(myCustScoreQuery, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;//命中的记录数组

        for (ScoreDoc scoreDoc : scoreDocs) {
            int docId = scoreDoc.doc;//命中的文档id
            System.out.println("id: " + docId);
            Document document = indexSearcher.doc(docId);
            System.out.println("content: " + document.get("content"));
            System.out.println("score: " + scoreDoc.score);
            System.out.println("=======================================================");
        }

        indexReader.close();
    }



}
