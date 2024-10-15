package org.servlets.dao;

import org.servlets.model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDisplayDAO extends BaseDAO {

    public Question getRecords(int page, List<Question> questions) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
        connection.setAutoCommit(false);
        try {
            return questions.get(page-1);
        } catch (Exception e) {
            connection.rollback();
        } finally {
            connection.commit();
            connection.close();
        }
        return null;
    }
}
