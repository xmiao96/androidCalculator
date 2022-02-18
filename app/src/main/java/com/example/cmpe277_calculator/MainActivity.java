package com.example.cmpe277_calculator;

import androidx.appcompat.app.AppCompatActivity;
import org.mariuszgromada.math.mxparser.*;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView result;
    private TextView typing;

    private Button[] digits = new Button[10];

    private Button dot;
    private Button equal;
    private Button times;
    private Button minus;
    private Button plus;
    private Button divide;
    private Button left_bracket;
    private Button right_bracket;
    private Button delete;
    private Button clear;

    private boolean isResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = (TextView) findViewById(R.id.resultText);
        typing = (TextView) findViewById(R.id.typingText);
        result.setText("0");
        typing.setText("0");

        digits[0] = (Button) findViewById(R.id.button_zero);
        digits[1] = (Button) findViewById(R.id.button_one);
        digits[2] = (Button) findViewById(R.id.button_two);
        digits[3] = (Button) findViewById(R.id.button_three);
        digits[4] = (Button) findViewById(R.id.button_four);
        digits[5] = (Button) findViewById(R.id.button_five);
        digits[6] = (Button) findViewById(R.id.button_six);
        digits[7] = (Button) findViewById(R.id.button_seven);
        digits[8] = (Button) findViewById(R.id.button_eight);
        digits[9] = (Button) findViewById(R.id.button_nine);

        for(int i = 0; i<10; i++){
            String number = Integer.toString(i);
            digits[i].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(!typing.getText().toString().equals("0")&&typing.getText().charAt(typing.getText().length()-1)!=')') {
                        typing.append(number);
                    }
                    if(typing.getText().toString().equals("0")){
                        typing.setText(number);
                    }
                }
            });
        }

        dot = (Button) findViewById(R.id.dot_button);
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isDot = false;
                int i = typing.getText().length()-1;
                while(i>1){
                    if(Character.isDigit(typing.getText().charAt(i))){
                        if(typing.getText().charAt(i-1) == '.'){
                            isDot = true;
                        }
                    }
                    else{
                        break;
                    }
                    i--;
                }

                if(Character.isDigit(typing.getText().charAt(typing.getText().length()-1)) && !isDot){
                    typing.append(".");
                }
            }
        });

        times = (Button) findViewById(R.id.multiply_button);
        divide = (Button) findViewById(R.id.divide_button);
        minus = (Button) findViewById(R.id.minus_button);
        plus = (Button) findViewById(R.id.plus_button);
        times.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isResult && (Character.isDigit(typing.getText().charAt(typing.getText().length()-1))||typing.getText().charAt(typing.getText().length()-1)==')')){
                    typing.append("*");
                }
                else if(isResult){
                    typing.setText(result.getText().toString() + "*");
                    isResult = false;
                }
            }
        });
        divide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isResult && (Character.isDigit(typing.getText().charAt(typing.getText().length()-1))||typing.getText().charAt(typing.getText().length()-1)==')')){
                    typing.append("/");
                }
                else if(isResult){
                    typing.setText(result.getText().toString() + "/");
                    isResult = false;
                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isResult && (Character.isDigit(typing.getText().charAt(typing.getText().length()-1))||typing.getText().charAt(typing.getText().length()-1)==')')){
                    typing.append("-");
                }
                else if(isResult){
                    typing.setText(result.getText().toString() + "-");
                    isResult = false;
                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isResult && (Character.isDigit(typing.getText().charAt(typing.getText().length()-1))||typing.getText().charAt(typing.getText().length()-1)==')')){
                    typing.append("+");
                }
                else if(isResult){
                    typing.setText(result.getText().toString() + "+");
                    isResult = false;
                }
            }
        });

        left_bracket = (Button) findViewById(R.id.left_bracket);
        right_bracket = (Button) findViewById(R.id.right_bracket);

        left_bracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                char x = typing.getText().charAt(typing.getText().length()-1);
                if(x=='+'||x=='-'||x=='*'||x=='/'||x=='(') {
                    typing.append("(");
                }
                if(typing.getText().toString().equals("0")){
                    typing.setText("(");
                }
            }
        });
        right_bracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int offset_brackets = 0;
                for(int i =0; i<typing.getText().length();i++){
                    if(typing.getText().charAt(i)=='('){
                        offset_brackets++;
                    }
                    if(typing.getText().charAt(i)==')'){
                        offset_brackets--;
                    }
                }
                if (offset_brackets>0){
                    typing.append(")");
                }
            }
        });
        equal = (Button) findViewById(R.id.equal_button);
        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tp = typing.getText().toString();
                Expression exp = new Expression(tp);
                String re = String.valueOf(exp.calculate());
                isResult = true;
                result.setText(re);
            }
        });

        clear = (Button) findViewById(R.id.clear_button);
        delete = (Button) findViewById(R.id.delete_button);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typing.setText("0");
                isResult = false;
                result.setText("0");
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!typing.getText().toString().equals("0") && typing.getText().length()>1) {
                    typing.setText(typing.getText().subSequence(0, typing.getText().length() - 1));
                }
                else if(typing.getText().toString().equals("0") && !result.getText().toString().equals("0")){
                    isResult = false;
                    result.setText("0");
                }
                else{
                    typing.setText("0");
                }
            }
        });



    }
}