package com.example.lucene_example.demo2;

import com.example.lucene_example.ikAdapter.IKAnalyzer6x;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class PageDemo {
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
        for (int i = 0; i < 20; i++) {
            Document doc = new Document();
            doc.add(new LongPoint("title", i));
            doc.add(new StoredField("title", i));
            doc.add(new TextField("content", "hello java world" + i, Field.Store.YES));
            indexWriter.addDocument(doc);
        }

        indexWriter.commit();

        indexWriter.close();

    }

    //分页方法一
    @Test
    public void test2() throws IOException {
        //Directory
        Directory directory = FSDirectory.open(Paths.get(PATH));
        //IndexReader
        IndexReader indexReader = DirectoryReader.open(directory);
        //IndexSearcher
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        int pageNumber = 2;
        int pageSize = 5;
        int begin = (pageNumber - 1) * pageSize;
        int end = pageSize * pageNumber;

        TermQuery query = new TermQuery(new Term("content", "java"));
        //这里的参数需要传end，以满足分页查询数量
        TopDocs topDocs = indexSearcher.search(query, end);
        //totalHits与上面的end参数无关，totalHits指满足查询条件的数量，上面end参数指返回搜索结果的前end条
        System.out.println("总命中数:" + topDocs.totalHits);//总命中数:20
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;//命中的记录数组

        for (int i = begin; i < end; i++) {
            int docId = scoreDocs[i].doc;
            System.out.println(docId);
            Document document = indexSearcher.doc(docId);
            System.out.println(document.get("title"));
            System.out.println(document.get("content"));
            System.out.println("=======================================================");
        }

        indexReader.close();
    }
}
