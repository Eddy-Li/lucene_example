package com.example.lucene_example.ikAdapter;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

//重写IKAnalyzer,解决lucene与IKAnalyzer的版本问题
//
// 来自姚潘《从lucene到ElasticSearch全文检索实战》
//
//如果在使用中出现错误
//java.lang.AssertionError: TokenStream implementation classes or at least their incrementToken() implementation must be final
//那么为IKTokenizer6x class 加入final关键字
public class IKAnalyzer6x extends Analyzer {
    private boolean useSmart;

    public boolean useSmart() {
        return useSmart;
    }

    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    // IK分词器Lucene Analyzer接口实现类;默认细粒度切分算法
    public IKAnalyzer6x() {
        this(false);
    }

    // IK分词器Lucene Analyzer接口实现类;当为true时，分词器进行智能切分
    //true:粗粒度分词, false:细粒度分词
    public IKAnalyzer6x(boolean useSmart) {
        super();
        this.useSmart = useSmart;
    }

    // 重写最新版本的createComponents;重载Analyzer接口，构造分词组件
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer _IKTokenizer = new IKTokenizer6x(this.useSmart());
        return new TokenStreamComponents(_IKTokenizer);
    }
}