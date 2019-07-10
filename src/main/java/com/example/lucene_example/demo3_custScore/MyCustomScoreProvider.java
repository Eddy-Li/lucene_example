package com.example.lucene_example.demo3_custScore;

import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.queries.CustomScoreProvider;

import java.io.IOException;

public class MyCustomScoreProvider extends CustomScoreProvider {

    public MyCustomScoreProvider(LeafReaderContext context) {
        super(context);
    }

    @Override
    public float customScore(int doc, float subQueryScore, float valSrcScore) throws IOException {
        LeafReader reader = this.context.reader();
        NumericDocValues numericDocValues = reader.getNumericDocValues("_click_num");
        long clickNum = numericDocValues.get(doc);
//        double factor = Math.log()
        return super.customScore(doc, subQueryScore, valSrcScore) * (clickNum + 1);
    }
}
