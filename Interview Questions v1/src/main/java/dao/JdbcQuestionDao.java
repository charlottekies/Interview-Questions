package dao;

import model.Question;
import org.apache.commons.dbcp2.BasicDataSource;

import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;


public class JdbcQuestionDao implements QuestionDao {
    private JdbcTemplate jdbcTemplate;


    /******************** Constructor *******************/
    public JdbcQuestionDao(BasicDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /******************** Override Methods *************/

    @Override
    public List<Question> getAllQuestions() {
        Question question = null;
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM question ORDER BY question_id;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            question = mapRowToQuestion(results);
            questions.add(question);
        }
        return questions;
    }

    @Override
    public List<Question> getAllStarQuestions() {
        Question question = null;
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM question WHERE is_star = true;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            question = mapRowToQuestion(results);
            questions.add(question);
        }
        return questions;
    }

    @Override
    public List<Question> getAllBehavioralQuestions() {
        Question question;
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM question WHERE is_behavioral = true ORDER BY question_id;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            question = mapRowToQuestion(results);
            questions.add(question);
        }
        return questions;
    }

    @Override
    public void addStarQuestion(String question) {
        String sql = "INSERT INTO question (is_star,is_behavioral,question) " +
                     "VALUES (true,false,?) RETURNING question_id;";
        int id = jdbcTemplate.queryForObject(sql,int.class,question);

    }

    @Override
    public void addBehavioralQuestion(String question) {
        String sql = "INSERT INTO question (is_star,is_behavioral,question) " +
                "VALUES (false,true,?) RETURNING question_id;";
        try {
            int id = jdbcTemplate.queryForObject(sql, int.class, question);
        } catch (Exception e) {
            System.out.println(e.getCause() + "\n");
        }

    }

    @Override
    public void updateQuestion(Question question,String questionText) {
        String sql = "UPDATE question " +
                    " SET question = ? " +
                    " WHERE question_id = ?";
        int rowsAffected = jdbcTemplate.update(sql,questionText,question.getQuestionId());
    }

    @Override
    public String deleteQuestion(String choice) {
        if (isValidChoice(choice)) {
            String sql = "DELETE FROM question WHERE question_id = ?;";
            List<Question> questions = getAllQuestions();
            Question question = questions.get(Integer.parseInt(choice)-1);
            int id = question.getQuestionId();
            jdbcTemplate.update(sql,id);
            return "Your question was successfully deleted.\n";
        } else {
            return "Your question was not successfully deleted.\n";
        }
    }

    public boolean isValidChoice(String choice) {
        try {
            int id = Integer.parseInt(choice);
            if (isInTable(id)) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("\nYour input was not valid. Error code: " + e.getCause());
        }
        return false;
    }

    public boolean isInTable(int id) {
        List<Question> questions = getAllQuestions();
        Question question = questions.get(id-1);
        id = question.getQuestionId();
        String sql = "SELECT question_id FROM question WHERE question_id = ?;";
        if (jdbcTemplate.queryForObject(sql,int.class,id) > 0){
            return true;
        } else {
            return false;
        }
    }

    private Question mapRowToQuestion(SqlRowSet results) {
        Question question = new Question();
        question.setQuestionId(results.getInt("question_id"));
        question.setStar(results.getBoolean("is_star"));
        question.setBehavioral(results.getBoolean("is_behavioral"));
        question.setQuestion(results.getString("question"));
        return question;
    }



}
