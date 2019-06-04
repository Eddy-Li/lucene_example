package com.example.lucene_example.demo1;

import org.junit.Test;

import java.io.IOException;

public class DocumentDemo {

    //增加文档
    @Test
    public void test1() throws IOException {
        //Document doc1 = new Document();
        //indexWriter.addDocument(doc1);
    }

    //查询文档
    @Test
    public void test2() throws IOException {
        //TopDocs topDocs = indexSearcher.search(query, 10);
    }

    //获取文档
    @Test
    public void test3() {
        //Document document = indexSearcher.doc(docId);
    }

    //删除文档
    @Test
    public void test4() {
        //删除所有文档
        //indexWriter.deleteAll();

        //删除字段name有关键词term的文档
        //indexWriter.deleteDocuments(new Term("name","term"));

        //删除查询条件Query1 Query2查询到的文档
        //indexWriter.deleteDocuments(Query1,Query2);

    }
}
