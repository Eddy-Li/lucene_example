package com.example.lucene_example.demo3_custScore;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.queries.function.FunctionQuery;
import org.apache.lucene.search.Query;

import java.io.IOException;

public class MyCustScoreQuery extends CustomScoreQuery {

    public MyCustScoreQuery(Query subQuery) {
        super(subQuery);
    }

    @Override
    protected CustomScoreProvider getCustomScoreProvider(LeafReaderContext context) throws IOException {
        return new MyCustomScoreProvider(context);
    }
}
