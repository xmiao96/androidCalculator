package com.example.cmpe277_calculator;

import androidx.appcompat.app.AppCompatActivity;
import org.mariuszgromada.math.mxparser.*;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView result; // upper text box showing the result
    private TextView typing; // lower text box showing the calculation process

    private Button[] digits = new Button[10]; // 10 digital numbers buttons

    //Operation buttons:
    private Button times;
    private Button minus;
    private Button plus;
    private Button divide;

    //Other buttons like equal, brackets, delete, clear and floating point.
    private Button dot;
    private Button equal;
    private Button left_bracket;
    private Button right_bracket;
    private Button delete;
    private Button clear;

    // check if the result has shown which is not 0
    private boolean hasResult = false;

    // Operations symbol logic function:
    public void operations(String operation){
        boolean isLastCharDigit = Character.isDigit(typing.getText().charAt(typing.getText().length()-1));
        boolean isLastCharRightBracket = typing.getText().charAt(typing.getText().length()-1)==')';

        if(!hasResult && (isLastCharDigit||isLastCharRightBracket) ){
            typing.append(operation);
        }
        else if(hasResult){
            typing.setText(result.getText().toString() + operation);
            hasResult = false;
        }
    }

    // main function once activity is created:
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing the text boxes:
        result = (TextView) findViewById(R.id.resultText);
        typing = (TextView) findViewById(R.id.typingText);
        result.setText("0");
        typing.setText("0");

        // creating the Button objects and their functions for 10 digital buttons:
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

        // running a loop to go through 10 digital number buttons
        for(int i = 0; i<10; i++){
            String number = Integer.toString(i);
            digits[i].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    int lenOfTyping = typing.getText().length();
                    char lastLetterOfTyping = typing.getText().charAt(lenOfTyping-1);
                    boolean isTypingZero = typing.getText().toString().equals("0");

                    if(!isTypingZero&&lastLetterOfTyping!=')') {
                        typing.append(number);
                    }
                    if(isTypingZero){
                        typing.setText(number);
                    }
                }
            });
        }

        //Floating point logic function:
        dot = (Button) findViewById(R.id.dot_button);
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasDot = false;
                int i = typing.getText().length()-1;
                // to find if there is a dot already before, and that are all digits before this dot is going to place.
                while(i>1){
                    if(Character.isDigit(typing.getText().charAt(i))){
                        if(typing.getText().charAt(i-1) == '.'){
                            hasDot = true;
                        }
                    }
                    else{
                        break;
                    }
                    i--;
                }
                // After last loop, hasDot will identify if there is a dot before.
                // Also, the dot can only be placed after digital numbers.
                if(Character.isDigit(typing.getText().charAt(typing.getText().length()-1)) && !hasDot){
                    typing.append(".");
                }
            }
        });
        //operations buttons:
        times = (Button) findViewById(R.id.multiply_button);
        divide = (Button) findViewById(R.id.divide_button);
        minus = (Button) findViewById(R.id.minus_button);
        plus = (Button) findViewById(R.id.plus_button);

        //operations listeners:
        times.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operations("*");
            }
        });
        divide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operations("/");
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operations("-");
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operations("+");
            }
        });

        // brackets buttons:
        left_bracket = (Button) findViewById(R.id.left_bracket);
        right_bracket = (Button) findViewById(R.id.right_bracket);
        // brackets listeners:
        left_bracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                char prevChar = typing.getText().charAt(typing.getText().length()-1);
                if(prevChar=='+'||prevChar=='-'||prevChar=='*'||prevChar=='/'||prevChar=='(') {
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
        //equal button and its listener:
        // It has used third party functions from mariuszgromada, the mxparser can transform the expression to an answer.
        equal = (Button) findViewById(R.id.equal_button);
        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tp = typing.getText().toString();
                Expression exp = new Expression(tp);
                String re = String.valueOf(exp.calculate());
                hasResult = true;
                result.setText(re);
            }
        });

        // clear and delete buttons and their listeners:
        clear = (Button) findViewById(R.id.clear_button);
        delete = (Button) findViewById(R.id.delete_button);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typing.setText("0");
                hasResult = false;
                result.setText("0");
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lengthOfTyping = typing.getText().length();
                boolean isTypingZero = typing.getText().toString().equals("0");
                boolean isResultZero = result.getText().toString().equals("0");

                if(!isTypingZero && lengthOfTyping>1) {
                    typing.setText(typing.getText().subSequence(0, lengthOfTyping - 1));
                }
                else if(isTypingZero && !isResultZero){
                    hasResult = false;
                    result.setText("0");
                }
                else{
                    typing.setText("0");
                }
            }
        });
    }
}