package com.huawei.qugramming.model;

public class UserAnswer {
    private Question question;
    private AnswerOption answer;

    public UserAnswer(Question question, AnswerOption answer) {
        this.question = question;
        this.answer = answer;
    }

    public Question getQuestion() {
        return question;
    }

    public AnswerOption getAnswer() {
        return answer;
    }

    public boolean isAnswerCorrect(){
        String isCorrect = "false";
        switch(answer){
            case answer_a:
                isCorrect = question.getCorrect_answers().getAnswer_a_correct();
                break;
            case answer_b:
                isCorrect = question.getCorrect_answers().getAnswer_b_correct();
                break;
            case answer_c:
                isCorrect = question.getCorrect_answers().getAnswer_c_correct();
                break;
            case answer_d:
                isCorrect = question.getCorrect_answers().getAnswer_d_correct();
                break;
            case answer_e:
                isCorrect = question.getCorrect_answers().getAnswer_e_correct();
                break;
            case answer_f:
                isCorrect = question.getCorrect_answers().getAnswer_f_correct();
                break;
        }

        return isCorrect.equals("true") ? true : false;
    }
}
