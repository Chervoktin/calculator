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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText editTextStringToParse;
    EditText editTextA;
    EditText editTextB;
    EditText editTextN;
    TextView textViewIsCheck;
    Handler handler;
    TextView textViewResult;
    ProgressBar progressBar;

    final int SHOW_RESULT = 1;
    final int SHOW_ERROR = 2;
    final int SHOW_PROGRESS = 3;

    private class HandlerIntegral extends Handler {

        private final WeakReference<MainActivity> weakReferenceMainActivity;

        public HandlerIntegral(MainActivity mainActivity) {
            this.weakReferenceMainActivity = new WeakReference<MainActivity>(mainActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            MainActivity activity = this.weakReferenceMainActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case SHOW_RESULT:
                        Log.e("test",Integer.toString(msg.what));
                        activity.textViewResult.setText((String) msg.obj);
                        break;
                    case SHOW_ERROR:
                        activity.textViewIsCheck.setText((String) msg.obj);
                        break;
                    case SHOW_PROGRESS:
                        activity.textViewIsCheck.setText(Integer.toString((int)msg.obj));
                        activity.progressBar.setProgress((int)msg.obj);
                        break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);

        editTextStringToParse = findViewById(R.id.editTextIntegral);
        editTextA = findViewById(R.id.editTextA);
        editTextB = findViewById(R.id.editTextB);
        editTextN = findViewById(R.id.editTextN);

        textViewResult = findViewById(R.id.textViewResult);
        textViewIsCheck = findViewById(R.id.textViewIsCheck);

        handler = new HandlerIntegral(this);



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
                            leftTrapezoidApproximation.setHandler(handler);
                            final double result = leftTrapezoidApproximation.calculate(new IFunction() {
                                @Override
                                public double calculate(double varible) {
                                    double result = 0;
                                    try {
                                        result = Calculator.calculate(tokens, varible);
                                    } catch (FunctionNotFoundException e) {

                                    }
                                    return result;
                                }
                            }, a, b, n);
                            Message msg = handler.obtainMessage(SHOW_RESULT, Double.toString(result));
                            handler.sendMessage(msg);
                        }
                    };
                    Thread th = new Thread(runnable);
                    th.start();

                } catch (InvalidTokenException e) {
                    textViewIsCheck.setText(e.getMessage());
                }
            }
        });
    }
}
