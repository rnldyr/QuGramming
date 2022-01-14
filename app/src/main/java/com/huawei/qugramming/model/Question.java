package com.huawei.qugramming.model;

import java.util.ArrayList;

public class Question{
    public class Answers{
        private String answer_a;
        private String answer_b;
        private String answer_c;
        private String answer_d;
        private String answer_e;
        private String answer_f;

        public Answers(String answer_a, String answer_b, String answer_c, String answer_d, String answer_e, String answer_f) {
            this.answer_a = answer_a;
            this.answer_b = answer_b;
            this.answer_c = answer_c;
            this.answer_d = answer_d;
            this.answer_e = answer_e;
            this.answer_f = answer_f;
        }

        public String getAnswer_a() {
            return answer_a;
        }

        public String getAnswer_b() {
            return answer_b;
        }

        public String getAnswer_c() {
            return answer_c;
        }

        public String getAnswer_d() {
            return answer_d;
        }

        public String getAnswer_e() {
            return answer_e;
        }

        public String getAnswer_f() {
            return answer_f;
        }
    }

    public class CorrectAnswers{
        private String answer_a_correct;
        private String answer_b_correct;
        private String answer_c_correct;
        private String answer_d_correct;
        private String answer_e_correct;
        private String answer_f_correct;

        public CorrectAnswers(String answer_a_correct, String answer_b_correct, String answer_c_correct, String answer_d_correct, String answer_e_correct, String answer_f_correct) {
            this.answer_a_correct = answer_a_correct;
            this.answer_b_correct = answer_b_correct;
            this.answer_c_correct = answer_c_correct;
            this.answer_d_correct = answer_d_correct;
            this.answer_e_correct = answer_e_correct;
            this.answer_f_correct = answer_f_correct;
        }

        public String getAnswer_a_correct() {
            return answer_a_correct;
        }

        public String getAnswer_b_correct() {
            return answer_b_correct;
        }

        public String getAnswer_c_correct() {
            return answer_c_correct;
        }

        public String getAnswer_d_correct() {
            return answer_d_correct;
        }

        public String getAnswer_e_correct() {
            return answer_e_correct;
        }

        public String getAnswer_f_correct() {
            return answer_f_correct;
        }
    }

    public class Tag{
        public String name;

        public Tag(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private int id;
    private String question;
    private Object description;
    private Answers answers;
    private String multiple_correct_answers;
    private CorrectAnswers correct_answers;
    private String correct_answer;
    private Object explanation;
    private Object tip;
    private ArrayList<Tag> tags;
    private String category;
    private String difficulty;

    public Question(int id, String question, Object description, Answers answers, String multiple_correct_answers, CorrectAnswers correct_answers, String correct_answer, Object explanation, Object tip, ArrayList<Tag> tags, String category, String difficulty) {
        this.id = id;
        this.question = question;
        this.description = description;
        this.answers = answers;
        this.multiple_correct_answers = multiple_correct_answers;
        this.correct_answers = correct_answers;
        this.correct_answer = correct_answer;
        this.explanation = explanation;
        this.tip = tip;
        this.tags = tags;
        this.category = category;
        this.difficulty = difficulty;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public Object getDescription() {
        return description;
    }

    public Answers getAnswers() {
        return answers;
    }

    public String getMultiple_correct_answers() {
        return multiple_correct_answers;
    }

    public CorrectAnswers getCorrect_answers() {
        return correct_answers;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public Object getExplanation() {
        return explanation;
    }

    public Object getTip() {
        return tip;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public String getCategory() {
        return category;
    }

    public String getDifficulty() {
        return difficulty;
    }
}