package org.query.model;

import java.util.List;

public class QueryFilteringResponse {

    private List<String> text;

    private FileMetaData metaData;

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    public FileMetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(FileMetaData fileMetaData) {
        this.metaData = fileMetaData;
    }
}
