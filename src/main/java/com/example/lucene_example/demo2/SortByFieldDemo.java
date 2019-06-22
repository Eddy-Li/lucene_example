package com.example.lucene_example.demo2;

import com.example.lucene_example.ikAdapter.IKAnalyzer6x;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class SortByFieldDemo {
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


        String[] tags = {"b", "A", "c", "123", "啊", "b", "a", "b", "B", "!"};

        //DocumentDemo
        for (int i = 0; i < 10; i++) {
            Document doc = new Document();

            doc.add(new LongPoint("title", i));
            doc.add(new StoredField("title", i));
            //如果要对"title"字段排序，则需要对"title"字段开启DocValues功能，即如下代码new NumericDocValuesField("title", i)
            //排序的字段不一定需要索引、存储
            //字段是Long时用NumericDocValuesField，Double时DoubleDocValuesField，Float时FloatDocValuesField
            doc.add(new NumericDocValuesField("title", i));

            doc.add(new StringField("tag", tags[i], Field.Store.YES));
            //字段是String时，是SortedDocValuesField
            doc.add(new SortedDocValuesField("tag", new BytesRef(tags[i])));

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

        TermQuery query = new TermQuery(new Term("content", "java"));

        //Sort sort = Sort.INDEXORDER:通过文档的id排序
        //Sort sort = Sort.RELEVANCE:通过文档的评分排序,默认按照这个排序

        //SortField sortField = SortField.FIELD_DOC; //order by document number
        //SortField sortField = SortField.FIELD_SCORE; //order by document score

        SortField sortField1 = new SortField("tag", SortField.Type.STRING, false);
        SortField sortField2 = new SortField("title", SortField.Type.LONG, false);
        //先按sortField1排序，然后再按sortField2排序
        Sort sort = new Sort(sortField1, sortField2);

        TopDocs topDocs = indexSearcher.search(query, 10, sort);
        System.out.println("总命中数:" + topDocs.totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        for (int i = 0; i < 10; i++) {
            int docId = scoreDocs[i].doc;
            //System.out.println(docId);
            Document document = indexSearcher.doc(docId);
            System.out.println(document.get("title"));
            System.out.println(document.get("tag"));
            System.out.println(document.get("content"));
            System.out.println("=======================================================");
        }
        indexReader.close();
    }

}
