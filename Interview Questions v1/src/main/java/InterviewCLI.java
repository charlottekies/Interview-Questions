import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import dao.*;
import model.*;
import org.apache.commons.dbcp2.BasicDataSource;


public class InterviewCLI {
    Scanner scanner = new Scanner(System.in);
    private final JdbcQuestionDao jdbcQuestionDao;
    Random random;



/*********************** Main Method *********************/
    public static void main(String[] args) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/InterviewQuestions");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");

        InterviewCLI interviewCLI = new InterviewCLI(dataSource);
        interviewCLI.run();
//        SpringApplication.run(InterviewCLI.class, args);
    }

/******************** Class Constructor ***************/

    public InterviewCLI(BasicDataSource dataSource) {
        jdbcQuestionDao = new JdbcQuestionDao(dataSource);
        random = new Random();

    }

/******************** Run Method ***********************/
    private void run() {
        //print welcome message
        //print menu of options
        //collect user choice
        //perform requested choice
        printWelcome();
        while (true) {
            printMenu();
            int choice = collectChoice("Please select an option: >>> ");
            switch (choice) {
                case 1: printAllQuestions();
                    break;
                case 2: getRandomQuestion();
                    break;
                case 3: getRandomStarQuestion();
                    break;
                case 4: getRandomBehavioralQuestion();
                    break;
                case 5: addQuestion();
                    break;
                case 6: updateQuestion();
                    break;
                case 7: deleteQuestion();
                    break;
                case 8: exit();
                     break;
                default: displayError("Invalid option selected. \n");
            }
        }
    }


/************** All Other Methods Called in the Run() Method******************/


    private void printWelcome() {
        System.out.println("-----------------------------------------");
        System.out.println("|     Interview Question Generator      |");
        System.out.println("-----------------------------------------");
    }

    private void printMenu() {
        System.out.println("-----------------------------------------");
        System.out.println("|               MAIN MENU               |");
        System.out.println("-----------------------------------------");
        System.out.println("1. Display all questions");
        System.out.println("2. Display a random question");
        System.out.println("3. Display a random STAR question");
        System.out.println("4. Display a random behavioral question");
        System.out.println("5. Add new question");
        System.out.println("6. Update an existing question");
        System.out.println("7. Delete an existing question");
        System.out.println("8. Exit");
    }

    private int collectChoice(String consoleMessage) {
        System.out.print("\n"+consoleMessage);
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            System.out.println("");
            return choice;
        } catch (Exception e) {
            System.out.println("That was not a valid number. \n");
            return 0;
        }
    }

    private void printAllQuestions() {
        int count = 1;
        for (Question question : jdbcQuestionDao.getAllQuestions()) {
            System.out.println(count + ". " + question.getQuestion());
            count++;
        };
        System.out.println("");
        System.out.println("Press any key to return to the main menu\n");
        scanner.nextLine();
    }

    private void getRandomQuestion() {
        List<Question> questions = jdbcQuestionDao.getAllQuestions();
        if (questions.size() > 0) {
            int randomIndex = random.nextInt(questions.size());
            System.out.println(questions.get(randomIndex).getQuestion() + "\n");
            System.out.print("Press 1 to get another random question. Press any other key to return to the main menu: >>> ");
            String input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        System.out.println("");
                        getRandomQuestion();
                        break;
                    default:
                        System.out.println("");
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.getCause());
            }
        } else {
                System.out.println("There are no STAR questions yet.");
                System.out.println("Press any key to return to the main menu.");
                scanner.nextLine();
            }
    }

    private void getRandomStarQuestion() {
        List<Question> questions = jdbcQuestionDao.getAllStarQuestions();
        int randomIndex = random.nextInt(questions.size());
        System.out.println(questions.get(randomIndex).getQuestion()+"\n");
        System.out.print("Press 1 to get another STAR question. Press any other key to return to the main menu: >>> ");
        String input = scanner.nextLine();
        try {
            int choice = Integer.parseInt(input);
            switch (choice) {
                case 1:
                    System.out.println("");
                    getRandomStarQuestion();
                    break;
                default: break;
            }
        } catch (Exception e) {
            System.out.println(e.getCause());
        }
    }

    private void getRandomBehavioralQuestion() {
        List<Question> questions = jdbcQuestionDao.getAllBehavioralQuestions();
        if (questions.size() >0) {
            int randomIndex = random.nextInt(questions.size());
            System.out.println(questions.get(randomIndex).getQuestion() + "\n");
            System.out.print("Press 1 to get another behavioral question. Press any other key to return to the main menu: >>> ");
            String input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        System.out.println("");
                        getRandomQuestion();
                        break;
                    default: printMenu();
                }
            } catch (Exception e) {
                System.out.println(e.getCause());
            }
        } else {
            System.out.println("There are no behavioral questions yet.");
            System.out.println("Press any key to return to the main menu.");
            scanner.nextLine();
        }
    }

    private void addQuestion() {
        boolean isStar = getQuestionType();
        int choice;
        System.out.println("Type your question: \n");
        String question = scanner.nextLine();
        if (isStar) {
            jdbcQuestionDao.addStarQuestion(question);
        } else {
            jdbcQuestionDao.addBehavioralQuestion(question);
        }
        System.out.print("Press 1 to add another question. Press any other key to return to the main menu: >>> ");
        String input = scanner.nextLine();

        try {
            choice = Integer.parseInt(input);
            switch (choice) {
                case 1: addQuestion();
                    break;
                default: break;
            }
        } catch (Exception e) {
            System.out.println(e.getCause());
        }
    }

    private boolean getQuestionType() {
        System.out.println("1. STAR");
        System.out.println("2. Behavioral");

        System.out.print("\nWhat kind of question would you like to add? >>> ");

        int choice = 0;
        boolean isStar = false;
        String questionType = scanner.nextLine();
        try {
            switch(Integer.parseInt(questionType)) {
                case 1: isStar = true;
                break;
                case 2: isStar = false;
                break;
                default: System.out.println("That was not a valid option.");
                addQuestion();
                break;
            }
        } catch (Exception e) {
            System.out.println("Please enter a valid number.");
            addQuestion();
        }
        return isStar;
    }


    private void updateQuestion() {
        List<Question> questions = new ArrayList(jdbcQuestionDao.getAllQuestions());
        for (int i = 0; i< questions.size(); i++) {
            System.out.println(i+1 + ". " + questions.get(i).getQuestion());
        }
        System.out.print("\nWhat question would you like to update?: >>> ");
        String input = scanner.nextLine();
        try {
            int choice = Integer.parseInt(input);
            Question question = questions.get(choice-1);
            System.out.println("Type the updated question: \n");
            String questionText = scanner.nextLine();
            jdbcQuestionDao.updateQuestion(question,questionText);
            System.out.print("Press 1 to update another question. Press any other key to return to the main menu: >>> ");
            input = scanner.nextLine();
        } catch (Exception e) {
            System.out.println("That was not a valid option. " + e.getMessage());
        }
        try {
            int choice = Integer.parseInt(input);
            switch (choice) {
                case 1: updateQuestion();
                    break;
                default: break;
            }
        } catch (Exception e) {
            System.out.println(e.getCause());
        }
    }

    private void deleteQuestion() {
        List<Question> questions = new ArrayList(jdbcQuestionDao.getAllQuestions());
        for (int i = 0; i< questions.size(); i++) {
            System.out.println(i+1 + ". " + questions.get(i).getQuestion());
        }
        System.out.print("\nWhich question would you like to delete?: >>> ");
        String choice = scanner.nextLine();
        System.out.println(jdbcQuestionDao.deleteQuestion(choice));
        System.out.print("Press 1 to delete another question. Press any other key to return to the main menu: >>> ");
        String input = scanner.nextLine();

        try {
            int selection = Integer.parseInt(input);
            switch (selection) {
                case 1: deleteQuestion();
                    break;
                default: break;
            }
        } catch (Exception e) {
            System.out.println(e.getCause());
        }
    }

    private void exit() {
        System.exit(0);
    }

    private void displayError(String consoleMessage) {
        System.out.println(consoleMessage);
    }


}
