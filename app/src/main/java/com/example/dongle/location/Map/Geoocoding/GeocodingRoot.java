package com.example.dongle.location.Map.Geoocoding;

import java.util.List;

/**
 * Created by DongLe on 01-Dec-17.
 */

public class GeocodingRoot {
    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public GeocodingRoot setResults(List<Result> results) {
        this.results = results;
        return this;
    }
}
