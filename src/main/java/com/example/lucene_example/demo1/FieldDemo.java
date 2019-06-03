package com.example.lucene_example.demo1;

import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.PointValues;

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


        /**
         * An indexed {@code long} field for fast range filters.  If you also
         * need to store the value, you should add a separate {@link StoredField} instance.
         * <p>
         * Finding all documents within an N-dimensional shape or range at search time is
         * efficient.  Multiple values for the same field in one document
         * is allowed.
         * <p>
         * This field defines static factory methods for creating common queries:
         * <ul>
         *   <li>{@link #newExactQuery(String, long)} for matching an exact 1D point.
         *   <li>{@link #newSetQuery(String, long...)} for matching a set of 1D values.
         *   <li>{@link #newRangeQuery(String, long, long)} for matching a 1D range.
         *   <li>{@link #newRangeQuery(String, long[], long[])} for matching points/ranges in n-dimensional space.
         * </ul>
         * @see PointValues
         */
        LongPoint longPoint = new LongPoint();
        IntPoint intPoint = new IntPoint();
        DoublePoint doublePoint = new DoublePoint();
        FloatPoint floatPoint = new FloatPoint();
        BigIntegerPoint bigIntegerPoint = new BigIntegerPoint();
    }
}
