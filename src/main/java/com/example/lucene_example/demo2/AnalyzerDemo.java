package com.example.lucene_example.demo2;

import com.example.lucene_example.ikAdapter.IKAnalyzer6x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

//Analyzer:包含Tokenizer和TokenFilter
//Tokenizer用来根据某种规则分词
//TokenFilter用来根据某种规则做大小写转换、复数转单数、去掉停止词等
public class AnalyzerDemo {


    @Test
    public void test1() throws IOException {
        String str = "This is my house,I'm coming from JiangSu";
        StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
        displayToken(str, standardAnalyzer);
        SimpleAnalyzer simpleAnalyzer = new SimpleAnalyzer();
        displayToken(str, simpleAnalyzer);
        IKAnalyzer6x analyzer6x = new IKAnalyzer6x(true);
        displayToken("从中亚之行的三句古语看习近平的外交理念", analyzer6x);
    }

    private void displayToken(String content, Analyzer analyzer) throws IOException {
        TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(content));
        tokenStream.reset();
        //CharTermAttribute:存放term
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        //OffsetAttribute:存放term的起始偏移量、结束偏移量
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        //PositionIncrementAttribute:存放term的位置增长量
        PositionIncrementAttribute positionIncrementAttribute = tokenStream.addAttribute(PositionIncrementAttribute.class);
        while (tokenStream.incrementToken()) {
            System.out.print("[term = " + charTermAttribute +
                    ", startOffset = " + offsetAttribute.startOffset() +
                    ", endOffset = " + offsetAttribute.endOffset() +
                    ", positionIncrement = " + positionIncrementAttribute.getPositionIncrement() + "]");
        }
        System.out.print("\n");
        tokenStream.close();
    }


}

