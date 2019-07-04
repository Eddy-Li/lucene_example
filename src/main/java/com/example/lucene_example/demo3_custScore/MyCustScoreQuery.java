package com.example.lucene_example.demo3_custScore;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.queries.function.FunctionQuery;
import org.apache.lucene.search.Query;

import java.io.IOException;

public class MyCustScoreQuery extends CustomScoreQuery {

    private String field;

    public MyCustScoreQuery(Query subQuery, String field) {
        this(subQuery);
        this.field = field;
    }

    private MyCustScoreQuery(Query subQuery) {
        super(subQuery);
    }

    private MyCustScoreQuery(Query subQuery, FunctionQuery scoringQuery) {
        super(subQuery, scoringQuery);
    }

    private MyCustScoreQuery(Query subQuery, FunctionQuery... scoringQueries) {
        super(subQuery, scoringQueries);
    }


    @Override
    protected CustomScoreProvider getCustomScoreProvider(LeafReaderContext context) throws IOException {
        return new MyCustomScoreProvider(context, field);
    }
}
