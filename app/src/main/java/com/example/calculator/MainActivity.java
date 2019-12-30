package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
        Button button = findViewById(R.id.button);
        final TextView textViewIsCheck = findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TokensReader tokensReader = new TokensReader();
                String str = editTextStringToParse.getText().toString();
                ArrayList<Token> tokens = null;
                try {
                    tokens = tokensReader.parseString(str);
                } catch (InvaildTokenException e) {
                    textViewIsCheck.setText(e.getMessage());
                    tokens = null;
                }
                if (tokens != null) {
                    textViewIsCheck.setText("");
                    Calculator c = new Calculator(tokens);
                    textViewResult.setText(Double.toString(c.calculate()));
                    for (Token token : tokens) {
                        Log.e("Test", token.getString()
                                + " type:" + token.getType()
                                + "Priority:" + token.getPriority());
                    }
                }
            }
        });
    }
}
