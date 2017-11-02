package trainedge.qaapp.Model;

/**
 * Created by Nikita on 28-Oct-17.
 */

public class QuesModel {
    private String question,option1,option2,option3,option4;
    private String answer;
    private String category;

    public QuesModel(){}

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String ques) {
        this.question = ques;
    }

    public String getoption1() {
        return option1;
    }

    public void setoption1(String option1) {
        this.option1 = option1;
    }

    public String getoption2() {
        return option2;
    }

    public void setoption2(String option2) {
        this.option2 = option2;
    }

    public String getoption3() {
        return option3;
    }

    public void setoption3(String option3) {
        this.option3 = option3;
    }

    public String getoption4() {
        return option4;
    }

    public void setoption4(String option4) {
        this.option4 = option4;
    }

    public QuesModel(String ques, String option1, String option2, String option3, String option4,String answer,String category) {
        this.question = ques;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
