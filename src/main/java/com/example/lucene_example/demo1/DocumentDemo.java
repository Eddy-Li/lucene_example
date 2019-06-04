package com.example.lucene_example.demo1;

import org.junit.Test;

import java.io.IOException;

public class DocumentDemo {

    //增加文档(可以选择字段是否创建索引)
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

    //删除文档（同时会删除索引）
    @Test
    public void test4() {
        //删除所有文档
        //indexWriter.deleteAll();

        //删除字段name有关键词term的文档
        //indexWriter.deleteDocuments(new Term("name","term"));

        //删除查询条件Query1 Query2查询到的文档
        //indexWriter.deleteDocuments(Query1,Query2);
    }

    //更新文档（索引）：先删除，后创建
    @Test
    public void test5() {
        //先删除查询条件为Term的文档，然后创建新的文档(删除一些文档，创建一个文档)
        //如果是指定id的文档，可以将Term的字段name设置为文档id名,值为文档id值（这时是删除一个文档，创建一个文档）
        //indexWriter.updateDocument(new Term("name", "term"), new Document());


        //删除一些文档，创建一些文档,用于批量更新
        //indexWriter.updateDocuments(new Term("name", "term"), Arrays.asList(new Document()));

    }
}
