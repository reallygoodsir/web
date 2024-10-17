package org.servlets.model;

public class Answer {
    private Integer id;
    private String name;
    private String isCorrect;
    private Integer questionId;

    public Answer() {
    }

    public Answer(String name, String isCorrect) {
        this.name = name;
        this.isCorrect = isCorrect;
    }

    public Answer(Integer id, String name, String isCorrect) {
        this.id = id;
        this.name = name;
        this.isCorrect = isCorrect;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(String isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isCorrect='" + isCorrect + '\'' +
                ", questionId=" + questionId +
                '}';
    }
}
