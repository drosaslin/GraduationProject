package com.example.android.locations_info;

import com.example.android.map.Results;

import java.util.ArrayList;

/**
 * Created by David Rosas on 9/5/2018.
 */

public class LocationsResponse {
    private String next_page_token;

    private ArrayList<Results> results;

    private Result result;

    private String[] html_attributions;

    private String status;

    public LocationsResponse() {
        results = new ArrayList<>();
    }

    public String getNext_page_token ()
    {
        return next_page_token;
    }

    public void setNext_page_token (String next_page_token)
    {
        this.next_page_token = next_page_token;
    }

    public ArrayList<Results> getResults ()
    {
        return results;
    }

    public void setResults (ArrayList<Results> results)
    {
        this.results = results;
    }

    public String[] getHtml_attributions ()
    {
        return html_attributions;
    }

    public void setHtml_attributions (String[] html_attributions)
    {
        this.html_attributions = html_attributions;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [next_page_token = "+next_page_token+", results = "+results+", html_attributions = "+html_attributions+", status = "+status+"]";
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
