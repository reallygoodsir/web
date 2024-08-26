package org.query.servlets;

import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;
import org.query.dao.JacksonCreate;
import org.query.model.JavaWord;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilteringServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String metadataParameter = req.getParameter("includeMetaData");
        String queryParameter = req.getParameter("queryText");
        int lengthParameter = Integer.parseInt(req.getParameter("length"));
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream("data.txt");
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        String stringResult = s.hasNext() ? s.next() : "";
        String[] tempArray = stringResult.split("\\s+");
        List<String> list = new ArrayList<>(Arrays.asList(tempArray));
        System.out.println(list);

        List<JavaWord> wordList = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            String line = list.get(i);
            if(line.length() <= lengthParameter && line.contains(queryParameter)){
                JavaWord javaWord = new JavaWord();
                javaWord.setWord(line);
                wordList.add(javaWord);
            }
        }
        PrintWriter result = resp.getWriter();
        JacksonCreate JsonResult = new JacksonCreate();
        JsonResult.createStringVersion(wordList);
        String printThis = JsonResult.createStringVersion(wordList);
        result.println(printThis);

        if(metadataParameter.equalsIgnoreCase("true")){
            String filePath = "D:\\data\\Java\\projects\\query-filtering-web-app\\src\\main\\resources\\data.txt";
            File file = new File(filePath);

            JacksonCreate metadata = new JacksonCreate();
            String fileName = file.getName();
            String fileSize = String.valueOf((file.length() / 1024));
            FileTime creationTime = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
            String fileCreationDate = String.valueOf(creationTime);
            String metadataString = metadata.createMetadata(fileName, fileSize, fileCreationDate);

            result.println(metadataString);
        }
    }
}
