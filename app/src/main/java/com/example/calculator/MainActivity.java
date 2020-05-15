package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText editTextStringToParse;
    EditText editTextA;
    EditText editTextB;
    EditText editTextN;
    TextView textViewIsCheck;
    Handler handler;
    TextView textViewResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextStringToParse = findViewById(R.id.editTextIntegral);
        editTextA = findViewById(R.id.editTextA);
        editTextB = findViewById(R.id.editTextB);
        editTextN = findViewById(R.id.editTextN);
        textViewResult = findViewById(R.id.textViewResult);
        final int SHOW_RESULT = 1;
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case SHOW_RESULT:
                        textViewResult.setText((String)msg.obj);
                }
            }
        };

        textViewIsCheck = findViewById(R.id.textView);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String str = editTextStringToParse.getText().toString();
                    final ArrayList<Token> tokens = Parser.parse(str);
                    textViewIsCheck.setText("");

                    final double a = Double.parseDouble(editTextA.getText().toString());
                    final double b = Double.parseDouble(editTextB.getText().toString());
                    final int n = Integer.parseInt(editTextN.getText().toString());
                    final int SHOW_RESULT = 1;

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            LeftTrapezoidApproximation leftTrapezoidApproximation = new LeftTrapezoidApproximation();
                            final double result = leftTrapezoidApproximation.calculate(new IFunction() {
                                @Override
                                public double calculate(double varible) {
                                    double result = 0;
                                    try{
                                        result = Calculator.calculate(tokens,varible);
                                    }catch (FunctionNotFoundException e){
                          //              textViewIsCheck.setText(e.getMessage());
                                    }
                                    return result;
                                }
                            },a,b,n);
                            Message msg = handler.obtainMessage(SHOW_RESULT,Double.toString(result));
                            handler.sendMessage(msg);
                            Log.e("test",Double.toString(result));
                        //    textViewResult.setText(Double.toString(result));
                        }
                    };
                    Thread th = new Thread(runnable);
                    th.start();

                } catch (InvalidTokenException e) {
                    textViewIsCheck.setText(e.getMessage());

            }}
        });
    }
}
