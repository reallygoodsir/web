package org.servlets.dao;

import org.servlets.model.Answer;
import org.servlets.model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionsDAO extends BaseDAO {
    private static final String INSERT_ANSWER = "INSERT INTO answers (name, is_correct, question_id) VALUES (?, ?, ?)";
    private static final String INSERT_QUESTION = "INSERT INTO questions (name) VALUES (?)";
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

    public List<Question> getQuestions() throws SQLException {
        List<Question> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD)) {
            PreparedStatement statementGetQuestions = connection.prepareStatement(GET_QUESTIONS_AND_ANSWERS);
            ResultSet resultSet = statementGetQuestions.executeQuery();

            Question previousQuestion = null;
            while (resultSet.next()) {
                int questionId = resultSet.getInt("question_id");
                String questionName = resultSet.getString("question_name");
                int answerId = resultSet.getInt("answer_id");
                String answerName = resultSet.getString("answer_name");
                String answerIsCorrect = resultSet.getString("answer_is_correct");

                System.out.println("questionId " + questionId);
                System.out.println("questionName " + questionName);
                System.out.println("answerId " + answerId);
                System.out.println("answerName " + answerName);
                System.out.println("answerIsCorrect " + answerIsCorrect);

                if (previousQuestion == null || !previousQuestion.getId().equals(questionId)) {
                    Question question = new Question();
                    question.setId(questionId);
                    question.setName(questionName);

                    Answer answer = new Answer();
                    answer.setId(answerId);
                    answer.setName(answerName);
                    answer.setIsCorrect(answerIsCorrect);

                    List<Answer> answers = new ArrayList<>();
                    answers.add(answer);

                    question.setAnswers(answers);

                    result.add(question);
                    previousQuestion = question;
                } else {
                    List<Answer> answers = previousQuestion.getAnswers();
                    Answer answer = new Answer();
                    answer.setId(answerId);
                    answer.setName(answerName);
                    answer.setIsCorrect(answerIsCorrect);
                    answers.add(answer);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
}
