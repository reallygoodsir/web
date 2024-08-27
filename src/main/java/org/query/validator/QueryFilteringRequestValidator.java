package org.query.validator;

public class QueryFilteringRequestValidator {

    private String queryText;
    private String length;
    private String includeMetaData;

    public QueryFilteringRequestValidator(String queryText, String length, String includeMetaData) {
        this.queryText = queryText;
        this.length = length;
        this.includeMetaData = includeMetaData;
    }


    public boolean validateQueryText() {
        return queryText != null && !queryText.isEmpty();
    }

    public boolean validateLength() {
        if (length == null || length.isEmpty()) {
            return false;
        }

        try {
            int value = Integer.parseInt(length);
            if (value <= 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }


    public boolean validateIncludeMetaData() {
        if (includeMetaData == null) {
            return true;
        } else {
            return "true".equals(includeMetaData) || "false".equals(includeMetaData);
        }
    }
}
