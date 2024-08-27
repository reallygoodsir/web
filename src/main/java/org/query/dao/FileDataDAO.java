package org.query.dao;

import org.query.model.FileMetaData;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FileDataDAO {

    public List<String> readWords(String fileName) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream(fileName);
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        String stringResult = s.hasNext() ? s.next() : "";
        String[] tempArray = stringResult.split("\\s+");
        return new ArrayList<>(Arrays.asList(tempArray));
    }

    public FileMetaData readMetaData(String fileName) {
        try {
            URI uri = getClass().getClassLoader().getResource(fileName).toURI();
            File file = new File(uri);
            String fileSize = String.valueOf((file.length() / 1024));
            FileTime creationTime = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
            String fileCreationDate = String.valueOf(creationTime);

            FileMetaData fileMetaData = new FileMetaData();
            fileMetaData.setFileName(fileName);
            fileMetaData.setFileSize(fileSize);
            fileMetaData.setFileCreationDate(fileCreationDate);
            return fileMetaData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
