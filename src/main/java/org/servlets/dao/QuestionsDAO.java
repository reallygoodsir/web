package org.servlets.dao;

import org.servlets.model.Answer;
import org.servlets.model.Question;

import java.sql.*;

public class QuestionsDAO extends BaseDAO {
    private static final String INSERT_ANSWER = "INSERT INTO answers (name, is_correct, question_id) VALUES (?, ?, ?)";
    private static final String INSERT_QUESTION = "INSERT INTO questions (name) VALUES (?)";

    public void saveQuestion(Question question) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
        connection.setAutoCommit(false);
        try {
            System.out.println("1");
            PreparedStatement statementAddQuestion = connection.prepareStatement(INSERT_QUESTION, Statement.RETURN_GENERATED_KEYS);
            statementAddQuestion.setString(1, question.getName());
            int affectedRows = statementAddQuestion.executeUpdate();
            System.out.println("affectedRows " + affectedRows);
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statementAddQuestion.getGeneratedKeys()) {
                    System.out.println("2");
                    if (generatedKeys.next()) {
                        System.out.println("3");
                        long generatedQuestionId = generatedKeys.getLong(1);
                        System.out.println("generatedQuestionId " + generatedQuestionId);
                        for (Answer answer : question.getAnswers()) {
                            System.out.println("Before saving answer " + answer.toString() + " with question id " + generatedQuestionId);
                            PreparedStatement statementAddAnswers = connection.prepareStatement(INSERT_ANSWER);
                            statementAddAnswers.setString(1, answer.getName());
                            statementAddAnswers.setString(2, answer.getIsCorrect());
                            statementAddAnswers.setInt(3, (int) generatedQuestionId);
                            statementAddAnswers.executeUpdate();
                            System.out.println("After saving answer " + answer + " with question id " + generatedQuestionId);
                        }
                    }
                }
            }
        } catch (Exception exception) {
            System.out.println("ERROR: " + exception.getMessage());
            connection.rollback();
        } finally {
            System.out.println("COMMIT");
            connection.commit();
        }
    }
}
