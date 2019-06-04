package com.example.lucene_example.demo1;

import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.PointValues;
import org.apache.lucene.search.Query;

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


        //一般使用Field的子类
        //字符串：创建索引、分词、存储
        TextField textField1 = new TextField("name", "value", Field.Store.YES);
        //字符串：创建索引、分词、不存储
        TextField textField2 = new TextField("name", "value", Field.Store.NO);
        //字符串：创建索引、不分词、存储
        StringField stringField1 = new StringField("name", "value", Field.Store.YES);
        //字符串：创建索引、不分词、不存储
        StringField stringField2 = new StringField("name", "value", Field.Store.NO);

        //重载方法：不索引、不分词、只存储
        StoredField storedField1 = new StoredField("name", 1);
        StoredField storedField2 = new StoredField("name", 1.1);
        StoredField storedField3 = new StoredField("name", 1L);
        StoredField storedField4 = new StoredField("name", "1");
        StoredField storedField5 = new StoredField("name", "1".getBytes());

        //只索引、不分词、不存储
        //如果要存储，需要额外创建 StoredField storedField1 = new StoredField("name", xxx);
        //IntPoint DoublePoint FloatPoint BigIntegerPoint同LongPoint
        LongPoint longPoint1 = new LongPoint("name", 100);//1维数据
        //还可以用于2维数据、3维数据情况
        LongPoint longPoint2 = new LongPoint("name", 100, 200);//2维数据
        LongPoint longPoint3 = new LongPoint("name", 100, 200, 300);//3维数据
        //LongPoint的查询方法
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
}
