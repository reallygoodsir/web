package org.servlets.dao;

import org.servlets.model.Answer;
import org.servlets.model.Question;

import java.sql.*;
import java.util.*;

public class QuestionsDAO extends BaseDAO {
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
            "    questions.id = answers.question_id;";
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
            "AND questions.id = ?";


    public void saveQuestion(Question question) throws SQLException {
        System.out.println("Start save question: " + question);
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
        connection.setAutoCommit(false);
        try {
            PreparedStatement statementAddQuestion = connection.prepareStatement(INSERT_QUESTION);
            statementAddQuestion.setString(1, question.getId());
            statementAddQuestion.setString(2, question.getName());
            System.out.println("Saving question to db ");
            int affectedQuestionRows = statementAddQuestion.executeUpdate();
            System.out.println("affectedRows " + affectedQuestionRows);
            if (affectedQuestionRows == 1) {
                if (question.getAnswers() == null || question.getAnswers().isEmpty()) {
                    System.out.println("Failed to save answers because we dont have them for question " + question.getId());
                    throw new RuntimeException("No nswers for question " + question.getId());
                }
                for (Answer answer : question.getAnswers()) {
                    System.out.println("Saving answer " + answer.toString() + " with question id " + question.getId());
                    PreparedStatement statementAddAnswers = connection.prepareStatement(INSERT_ANSWER);
                    statementAddAnswers.setString(1, answer.getId());
                    statementAddAnswers.setString(2, answer.getName());
                    statementAddAnswers.setBoolean(3, answer.getIsCorrect());
                    statementAddAnswers.setString(4, question.getId());
                    int affectedAnswersRows = statementAddAnswers.executeUpdate();
                    if (affectedAnswersRows == 1) {
                        System.out.println("Successfully saved an answer " + answer + " with question id " + question.getId());
                    } else {
                        System.out.println("Failed to save an answer " + answer + " with question id " + question.getId());
                        throw new RuntimeException("Failed to save an answer " + answer);
                    }
                }
            } else {
                System.out.println("Failed to save an question " + question);
                throw new RuntimeException("Failed to save the question " + question);
            }
            System.out.println("Before commit transaction");
            connection.commit();
            System.out.println("After commit transaction");
        } catch (Exception exception) {
            System.out.println("ERROR: " + exception.getMessage());
            System.out.println("Rollback transaction");
            connection.rollback();
        } finally {
            System.out.println("Close connection");
            connection.close();
        }
        System.out.println("End save question: " + question);
    }

    public List<Question> getQuestions() throws SQLException {
        List<Question> result = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
             PreparedStatement statementGetQuestions = connection.prepareStatement(GET_QUESTIONS_AND_ANSWERS);
             ResultSet resultSet = statementGetQuestions.executeQuery()) {

            Map<String, Question> questionMap = new HashMap<>();

            while (resultSet.next()) {
                String questionId = resultSet.getString("question_id");
                String questionName = resultSet.getString("question_name");
                String answerId = resultSet.getString("answer_id");
                String answerName = resultSet.getString("answer_name");
                boolean answerIsCorrect = resultSet.getBoolean("answer_is_correct");

                Question question = questionMap.get(questionId);
                if (question == null) {
                    question = new Question();
                    question.setId(questionId);
                    question.setName(questionName);
                    question.setAnswers(new ArrayList<Answer>());
                    questionMap.put(questionId, question);
                    result.add(question);
                }

                // Create a new answer and add it to the question's list of answers
                Answer answer = new Answer();
                answer.setId(answerId);
                answer.setName(answerName);
                answer.setIsCorrect(answerIsCorrect);
                List<Answer> answers = question.getAnswers();
                answers.add(answer);
            }
        } catch (Exception e) {
            System.out.println("Error fetching questions: " + e.getMessage());
        }
        return result;
    }

    public Question getQuestionById(String id) {
        Question question = null;
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
            PreparedStatement statementGetQuestionById = connection.prepareStatement(GET_QUESTION_BY_ID);
            statementGetQuestionById.setString(1, id);
            ResultSet resultSet = statementGetQuestionById.executeQuery();

            while (resultSet.next()) {
                String questionId = resultSet.getString("question_id");
                String questionName = resultSet.getString("question_name");
                String answerId = resultSet.getString("answer_id");
                String answerName = resultSet.getString("answer_name");
                boolean answerIsCorrect = resultSet.getBoolean("answer_is_correct");

                if (question == null) {
                    question = new Question();
                    question.setId(questionId);
                    question.setName(questionName);
                    question.setAnswers(new ArrayList<Answer>());
                }

                Answer answer = new Answer();
                answer.setId(answerId);
                answer.setName(answerName);
                answer.setIsCorrect(answerIsCorrect);
                List<Answer> answers = question.getAnswers();
                answers.add(answer);
            }
        } catch (Exception e) {
            System.out.println("Error fetching questions: " + e.getMessage());
        }
        return question;
    }

    public void deleteQuestion(String questionId) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
        connection.setAutoCommit(false);
        try {
            PreparedStatement ps = connection.prepareStatement(DELETE_QUESTION);
            ps.setString(1, questionId);
            int affectedRows = ps.executeUpdate();
            System.out.println("affectedRows " + affectedRows);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
        } finally {
            connection.close();
        }
    }

    public void editQuestion(Question question) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
        connection.setAutoCommit(false);
        try {
            PreparedStatement statementQuestion = connection.prepareStatement(EDIT_QUESTION);
            statementQuestion.setString(1, question.getName());
            statementQuestion.setString(2, question.getId());
            int affectedQuestionRows = statementQuestion.executeUpdate();
            System.out.println("affectedQuestionRows " + affectedQuestionRows);
            if (affectedQuestionRows == 1) {
                List<Answer> answers = question.getAnswers();
                for (Answer answer : answers) {
                    PreparedStatement statementAnswer = connection.prepareStatement(EDIT_ANSWER);
                    statementAnswer.setString(1, answer.getName());
                    statementAnswer.setBoolean(2, answer.getIsCorrect());
                    statementAnswer.setString(3, answer.getId());
                    int affectedAnswersRows = statementAnswer.executeUpdate();
                    if (affectedAnswersRows == 1) {
                        System.out.println("Successfully updated answer " + answer.getId());
                    } else {
                        System.out.println("Failed to update answer " + answer.getId());
                        throw new RuntimeException("Failed to update answer " + answer.getId());
                    }
                }
            } else {
                System.out.println("Failed to update the question " + question);
                throw new RuntimeException("Failed to update the question " + question);
            }
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
        } finally {
            connection.close();
        }
    }
}
