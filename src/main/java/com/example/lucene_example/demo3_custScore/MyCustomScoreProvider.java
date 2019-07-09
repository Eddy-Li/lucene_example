package com.example.lucene_example.demo3_custScore;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.search.IndexSearcher;

import java.io.IOException;

public class MyCustomScoreProvider extends CustomScoreProvider {

    private String field;

    public MyCustomScoreProvider(LeafReaderContext context, String field) {
        super(context);
        this.field = field;
    }

//    @Override
//    public float customScore(int doc, float subQueryScore, float[] valSrcScores) throws IOException {
//        return super.customScore(doc, subQueryScore, valSrcScores);
//    }


    @Override
    public float customScore(int doc, float subQueryScore, float valSrcScore) throws IOException {
        LeafReader reader = this.context.reader();
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        Document document = indexSearcher.doc(doc);
        String value = document.get(field);
        switch (value) {
            case "a":
                return 1.0f;
            case "b":
                return 2.0f;
            default:
                return 0.5f;
        }

    }
}
