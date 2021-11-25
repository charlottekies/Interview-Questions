package model;

public class Question {
    private int questionId;
    private boolean isStar;
    private boolean isBehavioral;
    private String question;


    public int getQuestionId()  {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public boolean isStar() {
        return isStar;
    }

    public void setStar(boolean star) {
        isStar = star;
    }

    public boolean isBehavioral() {
        return isBehavioral;
    }

    public void setBehavioral(boolean behavioral) {
        isBehavioral = behavioral;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }



}
