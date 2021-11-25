package dao;

import model.Question;

import javax.sql.DataSource;
import java.util.List;

public interface QuestionDao {

    public List<Question> getAllQuestions();
    public List<Question> getAllStarQuestions();
    public List<Question> getAllBehavioralQuestions();
    public void addStarQuestion(String question);
    public void addBehavioralQuestion(String question);
    public String deleteQuestion(String choice);
    public boolean isInTable(int questionId);
    public void updateQuestion(Question question, String questionText);
}
