package org.servlets.dao;

import org.servlets.model.Answer;
import org.servlets.model.Question;

import java.sql.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuestionsDAO extends BaseDAO {
    private static final Logger logger = LogManager.getLogger(QuestionsDAO.class);
    private static final String INSERT_ANSWER = "INSERT INTO answers (id, name, is_correct, question_id) VALUES (?, ?, ?, ?)";
    private static final String INSERT_QUESTION = "INSERT INTO questions (id, name) VALUES (?, ?)";
    private static final String DELETE_QUESTION = "DELETE FROM questions WHERE id = ?";
    private static final String EDIT_QUESTION = "UPDATE questions SET name = ? WHERE id = ?";
    private static final String EDIT_ANSWER = "UPDATE answers SET name = ?, is_correct = ? WHERE id = ?";
    private static final String GET_QUESTIONS_AND_ANSWERS = "SELECT \n" +
            "    questions.id AS question_id, \n" +
            "    questions.name AS question_name, \n" +
            "    answers.id AS answer_id,\n" +
            "    answers.name AS answer_name, \n" +
            "    answers.is_correct AS answer_is_correct\n" +
            "FROM \n" +
            "    questions\n" +
            "INNER JOIN \n" +
            "    answers \n" +
            "ON \n" +
            "    questions.id = answers.question_id \n" +
            "ORDER BY answers.id";
    private static final String GET_QUESTION_BY_ID = "SELECT \n" +
            "  questions.id AS question_id, \n" +
            "  questions.name AS question_name, \n" +
            "  answers.id AS answer_id, \n" +
            "  answers.name AS answer_name, \n" +
            "  answers.is_correct AS answer_is_correct \n" +
            "FROM \n" +
            "  questions \n" +
            "INNER JOIN answers \n" +
            "ON questions.id = answers.question_id\n" +
            "AND questions.id = ?\n" +
            "ORDER BY answers.id";


    public void saveQuestion(Question question) throws SQLException {
        logger.info("Start saving question: {}", question);

        // Try-with-resources for Connection ensures it is closed automatically.
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD)) {
            connection.setAutoCommit(false);

            // Save question to the database.
            try (PreparedStatement statementAddQuestion = connection.prepareStatement(INSERT_QUESTION)) {
                statementAddQuestion.setString(1, question.getId());
                statementAddQuestion.setString(2, question.getName());
                int affectedQuestionRows = statementAddQuestion.executeUpdate();
                logger.info("Affected question rows: {}", affectedQuestionRows);

                if (affectedQuestionRows != 1) {
                    logger.error("Affected question rows expected {} but received {}", 1, affectedQuestionRows);
                    throw new RuntimeException("Failed to save the question: " + question);
                }

                // Validate answers before proceeding.
                if (question.getAnswers() == null || question.getAnswers().isEmpty()) {
                    logger.error("No answers provided for question id {}", question.getId());
                    throw new IllegalArgumentException("No answers provided for question " + question.getId());
                }

                // Save each answer.
                try (PreparedStatement statementAddAnswers = connection.prepareStatement(INSERT_ANSWER)) {
                    for (Answer answer : question.getAnswers()) {
                        statementAddAnswers.setString(1, answer.getId());
                        statementAddAnswers.setString(2, answer.getName());
                        statementAddAnswers.setBoolean(3, answer.getIsCorrect());
                        statementAddAnswers.setString(4, question.getId());
                        int affectedAnswersRows = statementAddAnswers.executeUpdate();

                        if (affectedAnswersRows != 1) {
                            throw new RuntimeException("Failed to save an answer: " + answer);
                        }
                        logger.info("Successfully saved answer {} with question id {}", answer, question.getId());
                    }
                }

                // Commit transaction.
                connection.commit();
                logger.info("Transaction committed successfully.");
            } catch (SQLException e) {
                connection.rollback();
                logger.error("Transaction rolled back due to error", e);
                throw e; // Rethrow to indicate failure to the caller.
            }

        } catch (SQLException e) {
            logger.error("Error while saving question", e);
            throw e; // Propagate exception to the caller for handling.
        }

        logger.info("End saving question: {}", question);
    }


    public List<Question> getQuestions() throws SQLException {
        List<Question> result = new ArrayList<>();
        Map<String, Question> questionMap = new HashMap<>();

        // Try-with-resources for automatic closing of resources.
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
             PreparedStatement statementGetQuestions = connection.prepareStatement(GET_QUESTIONS_AND_ANSWERS);
             ResultSet resultSet = statementGetQuestions.executeQuery()) {

            while (resultSet.next()) {
                String questionId = resultSet.getString("question_id");
                String questionName = resultSet.getString("question_name");
                String answerId = resultSet.getString("answer_id");
                String answerName = resultSet.getString("answer_name");
                boolean answerIsCorrect = resultSet.getBoolean("answer_is_correct");

                // Use computeIfAbsent to get or create a new question.
                Question question = questionMap.computeIfAbsent(questionId, id -> {
                    Question q = new Question();
                    q.setId(id);
                    q.setName(questionName);
                    q.setAnswers(new ArrayList<>());
                    result.add(q);
                    return q;
                });

                // Only add an answer if it exists in the resultSet.
                if (answerId != null) {
                    Answer answer = new Answer();
                    answer.setId(answerId);
                    answer.setName(answerName);
                    answer.setIsCorrect(answerIsCorrect);
                    question.getAnswers().add(answer);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching questions", e);
            throw e; // Rethrow the exception to let the caller handle it.
        }

        return result;
    }

    public Question getQuestionById(String id) {
        Question question = null;

        // Try-with-resources for automatic closing of resources.
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
             PreparedStatement statementGetQuestionById = connection.prepareStatement(GET_QUESTION_BY_ID)) {

            statementGetQuestionById.setString(1, id);

            try (ResultSet resultSet = statementGetQuestionById.executeQuery()) {
                while (resultSet.next()) {
                    if (question == null) {
                        question = new Question();
                        question.setId(resultSet.getString("question_id"));
                        question.setName(resultSet.getString("question_name"));
                        question.setAnswers(new ArrayList<>());
                    }

                    // Create and add the answer to the question.
                    String answerId = resultSet.getString("answer_id");
                    if (answerId != null) { // Ensure that answerId exists before adding.
                        Answer answer = new Answer();
                        answer.setId(answerId);
                        answer.setName(resultSet.getString("answer_name"));
                        answer.setIsCorrect(resultSet.getBoolean("answer_is_correct"));
                        question.getAnswers().add(answer);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error fetching question by id", e);
        }

        return question;
    }

    public void deleteQuestion(String questionId) throws SQLException {
        // Try-with-resources for automatic closing of resources.
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD)) {
            connection.setAutoCommit(false);

            try (PreparedStatement ps = connection.prepareStatement(DELETE_QUESTION)) {
                ps.setString(1, questionId);
                int affectedRows = ps.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("No question found with ID: " + questionId);
                }

                logger.info("Deleted {} row(s) for question ID: {}", affectedRows, questionId);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error("Error deleting question with ID {}", questionId, e);
                throw e; // Rethrow the exception to notify the caller.
            }
        }
    }

    public void editQuestion(Question question) throws SQLException {
        // Try-with-resources for automatic closing of resources.
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD)) {
            connection.setAutoCommit(false);

            try (PreparedStatement statementQuestion = connection.prepareStatement(EDIT_QUESTION)) {
                // Update the question details.
                statementQuestion.setString(1, question.getName());
                statementQuestion.setString(2, question.getId());
                int affectedQuestionRows = statementQuestion.executeUpdate();

                if (affectedQuestionRows == 1) {
                    logger.info("Updated question {}", question.getId());

                    // Update the answers.
                    List<Answer> answers = question.getAnswers();
                    try (PreparedStatement statementAnswer = connection.prepareStatement(EDIT_ANSWER)) {
                        for (Answer answer : answers) {
                            statementAnswer.setString(1, answer.getName());
                            statementAnswer.setBoolean(2, answer.getIsCorrect());
                            statementAnswer.setString(3, answer.getId());
                            int affectedAnswersRows = statementAnswer.executeUpdate();

                            if (affectedAnswersRows == 1) {
                                logger.info("Successfully updated answer {}", answer.getId());
                            } else {
                                throw new SQLException("Failed to update answer " + answer.getId());
                            }
                        }
                    }
                } else {
                    throw new SQLException("Failed to update the question with ID: " + question.getId());
                }

                connection.commit();
                logger.info("Transaction committed for question {}", question.getId());
            } catch (SQLException e) {
                connection.rollback();
                logger.error("Transaction rolled back for question id {}", question.getId(), e);
                throw e; // Rethrow the exception to notify the caller.
            }
        }
    }
}
