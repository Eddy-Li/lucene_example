package com.example.lucene_example.demo1;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;

public class FieldDemo {
    public static void main(String[] args) {
        Document doc1 = new Document();

        String name = "name";
        String value = "value";

        FieldType fieldType = new FieldType();
        fieldType.setStored(true);//字段是否存储
        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);//该字段是否创建索引
        fieldType.setTokenized(true);//该字段是否要分词

        Field field = new Field(name, value, fieldType);//字段默认字符串类型
        //设置字段类型与值
        //field.setLongValue(10);
        //field.setStringValue("");
        //field.setDoubleValue(1.1);

    }
}
