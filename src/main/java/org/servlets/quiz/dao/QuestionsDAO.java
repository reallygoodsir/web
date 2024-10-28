package org.servlets.quiz.dao;

import org.servlets.quiz.model.Answer;
import org.servlets.quiz.model.Question;

import java.sql.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuestionsDAO extends BaseDAO {
    private static final Logger LOGGER = LogManager.getLogger(QuestionsDAO.class);
    private static final String INSERT_ANSWER = "INSERT INTO answers (id, name, is_correct, question_id) VALUES (?, ?, ?, ?)";
    private static final String INSERT_QUESTION = "INSERT INTO questions (id, name) VALUES (?, ?)";
    private static final String DELETE_QUESTION = "DELETE FROM questions WHERE id = ?";
    private static final String EDIT_QUESTION = "UPDATE questions SET name = ? WHERE id = ?";
    private static final String EDIT_ANSWER = "UPDATE answers SET name = ?, is_correct = ? WHERE id = ?";
    private static final String GET_QUESTIONS_AND_ANSWERS = "SELECT " +
            "    questions.id AS question_id, " +
            "    questions.name AS question_name, " +
            "    answers.id AS answer_id, " +
            "    answers.name AS answer_name, " +
            "    answers.is_correct AS answer_is_correct " +
            "FROM " +
            "    questions " +
            "INNER JOIN " +
            "    answers " +
            "ON " +
            "    questions.id = answers.question_id " +
            "ORDER BY answers.id";
    private static final String GET_QUESTION_BY_ID = "SELECT " +
            "  questions.id AS question_id, " +
            "  questions.name AS question_name, " +
            "  answers.id AS answer_id, " +
            "  answers.name AS answer_name, " +
            "  answers.is_correct AS answer_is_correct " +
            "FROM " +
            "  questions " +
            "INNER JOIN answers " +
            "ON questions.id = answers.question_id " +
            "AND questions.id = ? " +
            "ORDER BY answers.id";

    public void saveQuestion(Question question) throws SQLException {
        LOGGER.info("Start saving question: {}", question);

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD)) {
            connection.setAutoCommit(false);

            try (PreparedStatement statementAddQuestion = connection.prepareStatement(INSERT_QUESTION)) {
                statementAddQuestion.setString(1, question.getId());
                statementAddQuestion.setString(2, question.getName());
                int affectedQuestionRows = statementAddQuestion.executeUpdate();
                LOGGER.info("Affected question rows: {}", affectedQuestionRows);

                if (affectedQuestionRows != 1) {
                    LOGGER.error("Affected question rows expected {} but received {}", 1, affectedQuestionRows);
                    throw new RuntimeException("Failed to save the question: " + question);
                }

                if (question.getAnswers() == null || question.getAnswers().isEmpty()) {
                    LOGGER.error("No answers provided for question id {}", question.getId());
                    throw new IllegalArgumentException("No answers provided for question " + question.getId());
                }

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
                        LOGGER.info("Successfully saved answer {} with question id {}", answer, question.getId());
                    }
                }

                connection.commit();
                LOGGER.info("Transaction committed successfully.");
            } catch (SQLException sqlException) {
                connection.rollback();
                LOGGER.error("Transaction rolled back due to error", sqlException);
                throw sqlException;
            }

        } catch (SQLException sqlException) {
            LOGGER.error("Error while saving question", sqlException);
            throw sqlException;
        }

        LOGGER.info("End saving question: {}", question);
    }


    public List<Question> getQuestions() throws SQLException {
        List<Question> result = new ArrayList<>();
        Map<String, Question> questionMap = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
             PreparedStatement statementGetQuestions = connection.prepareStatement(GET_QUESTIONS_AND_ANSWERS);
             ResultSet resultSet = statementGetQuestions.executeQuery()) {

            while (resultSet.next()) {
                String questionId = resultSet.getString("question_id");
                String questionName = resultSet.getString("question_name");
                String answerId = resultSet.getString("answer_id");
                String answerName = resultSet.getString("answer_name");
                boolean answerIsCorrect = resultSet.getBoolean("answer_is_correct");

                Question question = questionMap.computeIfAbsent(questionId, id -> {
                    Question q = new Question();
                    q.setId(id);
                    q.setName(questionName);
                    q.setAnswers(new ArrayList<>());
                    result.add(q);
                    return q;
                });

                if (answerId != null) {
                    Answer answer = new Answer();
                    answer.setId(answerId);
                    answer.setName(answerName);
                    answer.setIsCorrect(answerIsCorrect);
                    question.getAnswers().add(answer);
                }
            }
        } catch (SQLException sqlException) {
            LOGGER.error("Error fetching questions", sqlException);
            throw sqlException;
        }

        return result;
    }

    public Question getQuestionById(String id) throws SQLException {
        Question question = null;

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

                    String answerId = resultSet.getString("answer_id");
                    if (answerId != null) {
                        Answer answer = new Answer();
                        answer.setId(answerId);
                        answer.setName(resultSet.getString("answer_name"));
                        answer.setIsCorrect(resultSet.getBoolean("answer_is_correct"));
                        question.getAnswers().add(answer);
                    }
                }
            }
        } catch (SQLException sqlException) {
            LOGGER.error("Error fetching question by id", sqlException);
            throw sqlException;
        }

        return question;
    }

    public void deleteQuestion(String questionId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD)) {
            connection.setAutoCommit(false);

            try (PreparedStatement deleteQuestionStatement = connection.prepareStatement(DELETE_QUESTION)) {
                deleteQuestionStatement.setString(1, questionId);
                int affectedRows = deleteQuestionStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("No question found with ID: " + questionId);
                }

                LOGGER.info("Deleted {} row(s) for question ID: {}", affectedRows, questionId);
                connection.commit();
            } catch (SQLException sqlException) {
                connection.rollback();
                LOGGER.error("Error deleting question with ID {}", questionId, sqlException);
                throw sqlException;
            }
        }
    }

    public void editQuestion(Question question) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD)) {
            connection.setAutoCommit(false);

            try (PreparedStatement statementQuestion = connection.prepareStatement(EDIT_QUESTION)) {
                statementQuestion.setString(1, question.getName());
                statementQuestion.setString(2, question.getId());
                int affectedQuestionRows = statementQuestion.executeUpdate();

                if (affectedQuestionRows == 1) {
                    LOGGER.info("Updated question {}", question.getId());

                    List<Answer> answers = question.getAnswers();
                    try (PreparedStatement statementAnswer = connection.prepareStatement(EDIT_ANSWER)) {
                        for (Answer answer : answers) {
                            statementAnswer.setString(1, answer.getName());
                            statementAnswer.setBoolean(2, answer.getIsCorrect());
                            statementAnswer.setString(3, answer.getId());
                            int affectedAnswersRows = statementAnswer.executeUpdate();

                            if (affectedAnswersRows == 1) {
                                LOGGER.info("Successfully updated answer {}", answer.getId());
                            } else {
                                throw new SQLException("Failed to update answer " + answer.getId());
                            }
                        }
                    }
                } else {
                    throw new SQLException("Failed to update the question with ID: " + question.getId());
                }

                connection.commit();
                LOGGER.info("Transaction committed for question {}", question.getId());
            } catch (SQLException sqlException) {
                connection.rollback();
                LOGGER.error("Transaction rolled back for question id {}", question.getId(), sqlException);
                throw sqlException;
            }
        }
    }
}
