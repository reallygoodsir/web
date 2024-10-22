package org.servlets.model;

public class Answer {
    private String id;
    private String name;
    private boolean isCorrect;
    private String questionId;

    public Answer() {
    }

    public Answer(String name, boolean isCorrect) {
        this.name = name;
        this.isCorrect = isCorrect;
    }

    public Answer(String id, String name, boolean isCorrect) {
        this.id = id;
        this.name = name;
        this.isCorrect = isCorrect;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
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
