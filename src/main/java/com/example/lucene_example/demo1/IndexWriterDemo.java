package com.example.lucene_example.demo1;


import com.example.lucene_example.ikAdapter.IKAnalyzer6x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

public class IndexWriterDemo {

    //索引存放目录
    private static final String PATH = "D:\\examples\\lucene";

    public static void main(String[] args) throws Exception {
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
        doc1.add(new TextField("content", "hello java world", Field.Store.YES));
        indexWriter.addDocument(doc1);

        Document doc2 = new Document();
        doc2.add(new TextField("id", "0002", Field.Store.YES));
        doc2.add(new TextField("title", "scala", Field.Store.YES));
        doc2.add(new TextField("content", "hello scala world", Field.Store.YES));
        indexWriter.addDocument(doc2);

        Document doc3 = new Document();
        doc3.add(new TextField("id", "0003", Field.Store.YES));
        doc3.add(new TextField("title", "java", Field.Store.YES));
        doc3.add(new TextField("content", "hello the java world", Field.Store.YES));
        indexWriter.addDocument(doc3);

        Document doc4 = new Document();
        doc4.add(new TextField("id", "0004", Field.Store.YES));
        doc4.add(new TextField("title", "ikAdapter", Field.Store.YES));
        doc4.add(new TextField("content", "我们是中国人,我们家在中国", Field.Store.YES));
        indexWriter.addDocument(doc4);

        indexWriter.commit();

        indexWriter.close();
    }


}
