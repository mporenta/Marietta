package com.omadi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OmadiReport implements Serializable {
    @JsonProperty("total_count")
    private int totalCount;

    @JsonProperty("total_pages")
    private int totalPages;

    @JsonProperty("first_record")
    private int firstRecord;

    @JsonProperty("last_record")
    private int lastRecord;

    @JsonProperty("current_page")
    private int currentPage;

    @JsonProperty("next_page_query")
    private String nextPageQuery;

    @JsonProperty("data")
    private List<ReportData> reportDataList = new ArrayList<>();

    public OmadiReport() {
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getFirstRecord() {
        return firstRecord;
    }

    public void setFirstRecord(int firstRecord) {
        this.firstRecord = firstRecord;
    }

    public int getLastRecord() {
        return lastRecord;
    }

    public void setLastRecord(int lastRecord) {
        this.lastRecord = lastRecord;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getNextPageQuery() {
        return nextPageQuery;
    }

    public void setNextPageQuery(String nextPageQuery) {
        this.nextPageQuery = nextPageQuery;
    }

    public List<ReportData> getReportDataList() {
        return reportDataList;
    }

    public void setReportDataList(List<ReportData> reportDataList) {
        this.reportDataList = reportDataList;
    }
}
