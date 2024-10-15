package org.servlets.dao;

import org.servlets.model.Answer;
import org.servlets.model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO extends BaseDAO {
    private static final String GET_QUESTION = "select * from questions";
    private static final String GET_ANSWERS = "select * from answers where question_id=?";

    public List<Question> getQuestions() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
        connection.setAutoCommit(false);
        try {

            PreparedStatement statementGetQuestions = connection.prepareStatement(GET_QUESTION);
            ResultSet resultSetQuestion = statementGetQuestions.executeQuery();
            List<Question> questions = new ArrayList<>(); // !!!!
            while (resultSetQuestion.next()) {
                int id = resultSetQuestion.getInt("id");
                String name = resultSetQuestion.getString("name");
                Question question = new Question(name);
                question.setId(id);
                List<Answer> answers = new ArrayList<>(); // !!!!
                PreparedStatement statementGetAnswers = connection.prepareStatement(GET_ANSWERS);
                statementGetAnswers.setInt(1, id);
                ResultSet resultSetAnswers = statementGetAnswers.executeQuery();
                while (resultSetAnswers.next()) {
                    int answerId = resultSetAnswers.getInt("id");
                    String answerName = resultSetAnswers.getString("name");
                    String isCorrect = resultSetAnswers.getString("is_correct");
                    Answer answer = new Answer(answerName, isCorrect);
                    answer.setQuestionId(id);
                    answer.setId(answerId);
                    answers.add(answer);
                }
                question.setAnswers(answers);
                questions.add(question);
//                System.out.println("-----\nQuestion number " + id + ", question info: " + question.getName() + " " + question.getId());
//                System.out.println("Question answers:");
//                for (Answer answer : answers) {
//                    System.out.println("Answer number " + answer.getId() + " info: " + answer.getName() + " " + answer.getIsCorrect() + " " + answer.getQuestionId());
//                }
//                System.out.println("-----\n-----\n");
            }
            return questions;
        } catch (Exception e) {
            connection.rollback();
        } finally {
            connection.commit();
            connection.close();
        }
        return null;
    }
}
