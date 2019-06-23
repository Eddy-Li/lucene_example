package com.example.lucene_example.demo3_custScore;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.CustomScoreProvider;

import java.io.IOException;

public class MyCustomScoreProvider extends CustomScoreProvider {

    public MyCustomScoreProvider(LeafReaderContext context) {
        super(context);
    }

    @Override
    public float customScore(int doc, float subQueryScore, float[] valSrcScores) throws IOException {
        return super.customScore(doc, subQueryScore, valSrcScores);
    }

    @Override
    public float customScore(int doc, float subQueryScore, float valSrcScore) throws IOException {
        return super.customScore(doc, subQueryScore, valSrcScore);
    }
}
