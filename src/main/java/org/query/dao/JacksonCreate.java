package org.query.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.query.model.JavaWord;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JacksonCreate {
    public String createStringVersion(List<JavaWord> customers) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            String string = mapper.writeValueAsString(customers);
            return string;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String createMetadata(String fileName, String fileSize, String fileCreationDate){
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String,String> metadata = new HashMap<>();
            metadata.put("fileName",fileName);
            metadata.put("fileSize",fileSize);
            metadata.put("fileCreationDate", fileCreationDate);

            String json = new ObjectMapper().writeValueAsString(metadata);
            return json;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
