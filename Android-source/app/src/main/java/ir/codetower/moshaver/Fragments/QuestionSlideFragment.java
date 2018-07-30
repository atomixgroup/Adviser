package ir.codetower.moshaver.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import ir.codetower.moshaver.Models.Question;
import ir.codetower.moshaver.R;


public class QuestionSlideFragment extends Fragment {

    public RadioButton option1,option2,option3,option4;
    public AppCompatTextView questionText;
    private Question question;
    public static QuestionSlideFragment newInstance() {

        return new QuestionSlideFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View layout=inflater.inflate(R.layout.question_frame, container, false);
       option1=layout.findViewById(R.id.option1);
       option2=layout.findViewById(R.id.option2);
       option3=layout.findViewById(R.id.option3);
       option4=layout.findViewById(R.id.option4);
       option1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if(b){
                   question.setTrueAnswer(1);
               }
           }
       });
       option4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if(b){
                   question.setTrueAnswer(4);
               }
           }
       });
        option2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    question.setTrueAnswer(2);
                }
            }
        });
        option3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    question.setTrueAnswer(3);
                }
            }
        });
       questionText=layout.findViewById(R.id.question);
        option1.setText(question.getAnswer1());
        option2.setText(question.getAnswer2());
        option3.setText(question.getAnswer3());
        option4.setText(question.getAnswer4());
        questionText.setText(question.getQuestion());
       return layout;
    }
    public void setQuestion(Question question){

        this.question=question;
    }
    public Question getQuestion(){
        return question;
    }


}
