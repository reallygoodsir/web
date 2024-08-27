package org.query.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.query.model.QueryFilteringResponse;

import java.io.IOException;

public class ResponseConverter {

    public String convert(QueryFilteringResponse response) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
