package com.example.lucene_example.demo1;

import com.example.lucene_example.ikAdapter.IKAnalyzer6x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class QueryDemo {
    //索引存放目录
    private static final String PATH = "D:\\examples\\lucene";

    @Test
    public void test1() throws IOException {
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
        doc1.add(new TextField("content", "习近平用三“好”道出中俄深厚友谊 友谊之路", Field.Store.YES));
        indexWriter.addDocument(doc1);

        Document doc2 = new Document();
        doc2.add(new TextField("id", "0002", Field.Store.YES));
        doc2.add(new TextField("content", "习近平绘出\"天蓝山绿水清\"的江山丽景图 ", Field.Store.YES));
        indexWriter.addDocument(doc2);

        Document doc3 = new Document();
        doc3.add(new TextField("id", "0003", Field.Store.YES));
        doc3.add(new TextField("content", "习近平抵达莫斯科开始对俄罗斯进行国事访问", Field.Store.YES));
        indexWriter.addDocument(doc3);


        indexWriter.commit();

        indexWriter.close();
    }

    //QueryParser: 先分词再查询，字段中只要有一个分词字就满足查询条件
    @Test
    public void test2() throws IOException, ParseException {
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
        String queryStr = "俄罗斯上海"; //只要有"俄罗斯"或者"上海"就满足查询条件
        Query query = queryParser.parse(queryStr);

        TopDocs topDocs = indexSearcher.search(query, 10);
        System.out.println("总命中数:" + topDocs.totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;//命中的记录数组
        for (ScoreDoc scoreDoc : scoreDocs) {
            float score = scoreDoc.score;
            int shardIndex = scoreDoc.shardIndex;
            int docId = scoreDoc.doc;//命中的文档id
            System.out.println(docId);
            //获取文档,如果创建索引时Field.Store.NO，即不存储，此时就会获取不到文档的数据，下面全部会显示为空
            Document document = indexSearcher.doc(docId);
            System.out.println(document.get("id"));
            System.out.println(document.get("content"));
            System.out.println("=======================================================");
        }
    }


    //TermQuery:关键词查询(不会对传入的term分词)
    @Test
    public void test3() throws IOException, ParseException {
        //Directory
        Directory directory = FSDirectory.open(Paths.get(PATH));
        //IndexReader
        IndexReader indexReader = DirectoryReader.open(directory);
        //IndexSearcher
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        TermQuery termQuery = new TermQuery(new Term("content","友谊之路"));//不会对"友谊之路"分词，只要字段中有"友谊之路"就满足查询

        TopDocs topDocs = indexSearcher.search(termQuery, 10);
        System.out.println("总命中数:" + topDocs.totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;//命中的记录数组
        for (ScoreDoc scoreDoc : scoreDocs) {
            int docId = scoreDoc.doc;//命中的文档id
            System.out.println(docId);
            //获取文档,如果创建索引时Field.Store.NO，即不存储，此时就会获取不到文档的数据，下面全部会显示为空
            Document document = indexSearcher.doc(docId);
            System.out.println(document.get("id"));
            System.out.println(document.get("content"));
            System.out.println("=======================================================");
        }

    }


}
