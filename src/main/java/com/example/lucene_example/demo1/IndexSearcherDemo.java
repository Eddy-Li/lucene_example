package com.example.lucene_example.demo1;

import com.example.lucene_example.ikAdapter.IKAnalyzer6x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

public class IndexSearcherDemo {
    //索引存放目录
    private static final String PATH = "D:\\examples\\lucene";

    public static void main(String[] args) throws Exception {
        //Directory
        Directory directory = FSDirectory.open(Paths.get(PATH));
        //IndexReader
        IndexReader indexReader = DirectoryReader.open(directory);
        //IndexSearcher
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);


        //the default field for query terms
        String defaultField = "id";
        //Analyzer
        Analyzer analyzer = new IKAnalyzer6x(false);
        QueryParser queryParser = new QueryParser(defaultField, analyzer);

        //如果这里没有指定搜索字段(搜索字段:关键词，例如content:scala)，则会使用默认搜索字段即上面的defaultField
        String queryStr = "content:啊";
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
            System.out.println(document.get("title"));
            System.out.println(document.get("content"));
            System.out.println("=======================================================");
        }
    }
}
