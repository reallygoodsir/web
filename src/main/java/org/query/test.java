package org.query;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class test {
    public static void main(String[] args) throws IOException {
        String fileName = "D:\\data\\Java\\projects\\query-filtering-web-app\\src\\main\\resources\\data.txt";

        File file = new File(fileName);

        System.out.println(file.exists());


        System.out.println(file.getName());
        System.out.println((file.length() / 1024));
        FileTime creationTime = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
        LocalDateTime localDateTime = creationTime
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        System.out.println(localDateTime);
    }
}
