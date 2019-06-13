package com.example.lucene_example.util;

import com.example.lucene_example.ikAdapter.IKAnalyzer6x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class LuceneUtil {

    //索引存放目录
    private static final String PATH = "D:\\examples\\lucene";

    private static IndexWriter indexWriter;
    private static IndexReader indexReader;
    private static IndexSearcher indexSearcher;

    private LuceneUtil() {
    }

    public static IndexWriter getIndexWriter() {
        if (indexWriter == null) {
            synchronized (LuceneUtil.class) {
                if (indexWriter == null) {
                    try {
                        //此处可以获取配置文件中的PATH,并设置PATH
                        Directory directory = FSDirectory.open(Paths.get(PATH));
                        Analyzer analyzer = new IKAnalyzer6x(true);
                        IndexWriterConfig conf = new IndexWriterConfig(analyzer);
                        indexWriter = new IndexWriter(directory, conf);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return indexWriter;
    }

    public static IndexSearcher getIndexSearcher() {
        try {
            if (indexReader == null) {
                //此处可以获取配置文件中的PATH,并设置PATH
                Directory directory = FSDirectory.open(Paths.get(PATH));
                indexReader = DirectoryReader.open(directory);
            } else {
                DirectoryReader newIndexReader = DirectoryReader.openIfChanged((DirectoryReader) indexReader);
                if (newIndexReader != null) {
                    indexReader.close();
                    indexReader = newIndexReader;
                }
            }
            indexSearcher = new IndexSearcher(indexReader);
            return indexSearcher;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
