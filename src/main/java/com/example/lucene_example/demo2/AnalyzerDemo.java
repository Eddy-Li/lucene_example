package com.example.lucene_example.demo2;

import com.example.lucene_example.ikAdapter.IKAnalyzer6x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class AnalyzerDemo {


    @Test
    public void test1() throws IOException {
        String str = "This is my house,I'm coming from JiangSu";
        StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
        displayToken(str,standardAnalyzer);
        SimpleAnalyzer simpleAnalyzer = new SimpleAnalyzer();
        displayToken(str,simpleAnalyzer);
        IKAnalyzer6x analyzer6x = new IKAnalyzer6x(true);
        displayToken("从中亚之行的三句古语看习近平的外交理念",analyzer6x);
    }

    private void displayToken(String content, Analyzer analyzer) throws IOException {
        TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(content));
        tokenStream.reset();
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        while (tokenStream.incrementToken()) {
            System.out.print("[" + charTermAttribute + "]");
        }
        System.out.println("\n");
        tokenStream.close();
    }


}

