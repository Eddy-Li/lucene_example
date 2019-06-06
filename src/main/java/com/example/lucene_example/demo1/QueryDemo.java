package com.example.lucene_example.demo1;

import com.example.lucene_example.ikAdapter.IKAnalyzer6x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
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

    //1.MatchAllDocQuery:匹配所有文档

    //2.QueryParser: 先分词再查询，字段中只要有一个分词字就满足查询条件
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


    //3.TermQuery:关键词查询(不会对传入的term分词)
    @Test
    public void test3() throws IOException, ParseException {
        //Directory
        Directory directory = FSDirectory.open(Paths.get(PATH));
        //IndexReader
        IndexReader indexReader = DirectoryReader.open(directory);
        //IndexSearcher
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        TermQuery termQuery = new TermQuery(new Term("content", "友谊之路"));//不会对"友谊之路"分词，只要字段中有"友谊之路"就满足查询

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

    //4.WildcardQuery:通配符匹配,搜索文档字段的分词中能匹配的,*：0或N个字符,?:代表1个字符
    @Test
    public void test4() throws IOException {
        //Directory
        Directory directory = FSDirectory.open(Paths.get(PATH));
        //IndexReader
        IndexReader indexReader = DirectoryReader.open(directory);
        //IndexSearcher
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        WildcardQuery wildcardQuery = new WildcardQuery(new Term("content", "俄*斯"));

        TopDocs topDocs = indexSearcher.search(wildcardQuery, 10);
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

    //5.范围查询:LongPoint IntPoint DoublePoint FloatPoint BigIntegerPoint
    @Test
    public void test5() throws IOException {
        Query query1 = LongPoint.newExactQuery("name", 100);//查询值等于100的字段
        Query query2 = LongPoint.newSetQuery("name", 100, 200, 300);//查询值等于100、200、300的字段,还有一个重载为集合的方法，两个一样
        Query query3 = LongPoint.newRangeQuery("name", 100, 200);//查询值在[100,200]的字段
        Query query4 = LongPoint.newRangeQuery("name", Long.MIN_VALUE, 200);//查询值在(-∞,200]的字段
        Query query5 = LongPoint.newRangeQuery("name", 100, Long.MAX_VALUE);//查询值在[100,+∞)的字段
        Query query6 = LongPoint.newRangeQuery("name", 100, Math.addExact(200, 1));//查询值在[100,200)的字段
        Query query7 = LongPoint.newRangeQuery("name", Math.addExact(100, -1), 200);//查询值在(100,200]的字段
        Query query8 = LongPoint.newRangeQuery("name", new long[]{1, 2, 3}, new long[]{7, 8, 9});//多维数据时，x在[1,7],y在[2,8],z在[3,9]

        //LongRange用于（最大值、最小值）或者多对（最大值、最小值）的索引，以及查询方法，还有IntRange DoubleRange...
        //LongRange longRange = new LongRange(String name, final long[] min, final long[] max)
        //LongRange.newContainsQuery(String field, final long[] min, final long[] max) //包含
        //LongRange.newCrossesQuery(String field, final long[] min, final long[] max) //相交
        //LongRange.newWithinQuery(String field, final long[] min, final long[] max) //包含
        //LongRange.newIntersectsQuery(String field, final long[] min, final long[] max) //相交
    }


    //6.RegexQuery—正则表达式的查询

    //7.PrefixQuery:前缀搜索（搜索起始位置符合要求的结果）

    //8.FuzzyQuery:模糊查询查询
    //在FuzzyQuery中，默认的匹配度是0.5，当这个值越小时，通过模糊查找出的文档的匹配程度就越低，查出的文档量就越多,反之亦然.
    //模糊搜索的三种构造函数，具体讲一下参数的用法（以第三个为例）；
    //第一个参数当然是词条对象，第二个参数指的是levenshtein算法的最小相似度，第三个参数指的是要有多少个前缀字母完全匹配：
    @Test
    public void test8() throws IOException {
        //Directory
        Directory directory = FSDirectory.open(Paths.get(PATH));
        //IndexReader
        IndexReader indexReader = DirectoryReader.open(directory);
        //IndexSearcher
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term("content", "莫斯可"));//可以搜索到"莫斯科"

        TopDocs topDocs = indexSearcher.search(fuzzyQuery, 10);
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

    //9.BooleanQuery:组合查询（布尔查询）
    @Test
    public void test9() throws IOException {
        //Directory
        Directory directory = FSDirectory.open(Paths.get(PATH));
        //IndexReader
        IndexReader indexReader = DirectoryReader.open(directory);
        //IndexSearcher
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        TermQuery termQuery1 = new TermQuery(new Term("content", "莫斯科"));
        TermQuery termQuery2 = new TermQuery(new Term("content", "习近平"));

        BooleanQuery booleanQuery = new BooleanQuery.Builder()
                .add(termQuery1, BooleanClause.Occur.MUST)
                .add(termQuery2, BooleanClause.Occur.MUST)
                .build();

        TopDocs topDocs = indexSearcher.search(booleanQuery, 10);
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

    //10.TermRangeQuery:字符串的范围搜索,TermRangeQuery无法查询数字的结果

}
