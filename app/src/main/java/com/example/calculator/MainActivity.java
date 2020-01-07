package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editTextStringToParse = findViewById(R.id.editText);
        final TextView textViewResult = findViewById(R.id.textViewResult);
        final Button button = findViewById(R.id.button);
        final TextView textViewIsCheck = findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String str = editTextStringToParse.getText().toString();
                    ArrayList<Token> tokens = Parser.parse(str);
                    textViewIsCheck.setText("");
                    textViewResult.setText(Double.toString(Calculator.calculate(tokens)));
                } catch (InvalidTokenException e) {
                    textViewIsCheck.setText(e.getMessage());
                } catch (FunctionNotFoundException e) {
                    textViewIsCheck.setText(e.getMessage());
                }
            }
        });
    }
}
