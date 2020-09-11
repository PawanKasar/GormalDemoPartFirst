package com.example.gormaldemofirstpart.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductModel {
    @SerializedName("results")
    @Expose
    private Result results;

    public Result getResults() {
        return results;
    }

    public void setResults(Result results) {
        this.results = results;
    }
}
