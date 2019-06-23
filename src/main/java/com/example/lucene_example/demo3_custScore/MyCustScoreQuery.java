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

    public MyCustScoreQuery(Query subQuery, FunctionQuery scoringQuery) {
        super(subQuery, scoringQuery);
    }

    public MyCustScoreQuery(Query subQuery, FunctionQuery... scoringQueries) {
        super(subQuery, scoringQueries);
    }


    @Override
    protected CustomScoreProvider getCustomScoreProvider(LeafReaderContext context) throws IOException {
        return super.getCustomScoreProvider(context);
    }
}
