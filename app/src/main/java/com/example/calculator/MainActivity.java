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

        final EditText editTextStringToParse = findViewById(R.id.editTextIntegral);
        final EditText editTextA = findViewById(R.id.editTextA);
        final EditText editTextB = findViewById(R.id.editTextB);
        final EditText editTextN = findViewById(R.id.editTextN);

        final TextView textViewResult = findViewById(R.id.textViewResult);
        final TextView textViewIsCheck = findViewById(R.id.textView);
        final Button button = findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String str = editTextStringToParse.getText().toString();
                    ArrayList<Token> tokens = Parser.parse(str);
                    textViewIsCheck.setText("");

                    double a = Double.parseDouble(editTextA.getText().toString());
                    double b = Double.parseDouble(editTextB.getText().toString());
                    int n = Integer.parseInt(editTextN.getText().toString());
                    double result = Calculator.leftTrapezoidApproximation(str, a, b, n);

                    textViewResult.setText(Double.toString(result));
                } catch (InvalidTokenException e) {
                    textViewIsCheck.setText(e.getMessage());
                } catch (FunctionNotFoundException e) {
                    textViewIsCheck.setText(e.getMessage());
                }
            }
        });
    }
}
