package org.query.servlets;

import org.query.converter.ResponseConverter;
import org.query.dao.FileDataDAO;
import org.query.filter.WordsFilter;
import org.query.model.FileMetaData;
import org.query.model.QueryFilteringResponse;
import org.query.validator.QueryFilteringRequestValidator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class FilteringServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String queryTextParameter = req.getParameter("queryText");
        String lengthParameter = req.getParameter("length");
        String includeMetaDataParameter = req.getParameter("includeMetaData");

        QueryFilteringRequestValidator queryFilteringRequestValidator =
                new QueryFilteringRequestValidator(queryTextParameter, lengthParameter, includeMetaDataParameter);

        boolean isQueryTextValid = queryFilteringRequestValidator.validateQueryText();

        if (!isQueryTextValid) {
            PrintWriter writer = resp.getWriter();
            writer.println("Query text must not be empty!");
            resp.setStatus(400);
            return;
        }

        boolean isLengthValid = queryFilteringRequestValidator.validateLength();
        if (!isLengthValid) {
            PrintWriter writer = resp.getWriter();
            writer.println("Please check if length has correct value. Length must be a positive integer!");
            resp.setStatus(400);
            return;
        }

        boolean isIncludeMetaDataValid = queryFilteringRequestValidator.validateIncludeMetaData();
        if (!isIncludeMetaDataValid) {
            PrintWriter writer = resp.getWriter();
            writer.println("Please check if includeMetaData has correct value. IncludeMetaData can be empty or have true or false value!");
            resp.setStatus(400);
            return;
        }

        int length = Integer.parseInt(lengthParameter);

        QueryFilteringResponse queryFilteringResponse = new QueryFilteringResponse();

        FileDataDAO fileDataDAO = new FileDataDAO();
        List<String> allWords = fileDataDAO.readWords("data.txt");

        WordsFilter wordsFilter = new WordsFilter();
        List<String> filteredWords = wordsFilter.filter(allWords, queryTextParameter, length);

        queryFilteringResponse.setText(filteredWords);

        if ("true".equalsIgnoreCase(includeMetaDataParameter)) {
            FileMetaData fileMetaData = fileDataDAO.readMetaData("data.txt");
            queryFilteringResponse.setMetaData(fileMetaData);
        }

        ResponseConverter responseConverter = new ResponseConverter();
        String json = responseConverter.convert(queryFilteringResponse);

        resp.setContentType("application/json");
        resp.getOutputStream().print(json);
    }
}
