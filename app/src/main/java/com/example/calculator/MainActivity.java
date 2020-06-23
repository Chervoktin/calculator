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

public class MainActivity extends AppCompatActivity {
    EditText editTextStringToParse;
    EditText editTextA;
    EditText editTextB;
    EditText editTextN;
    TextView textViewIsCheck;
    Handler handler;
    TextView textViewResult;
    ProgressBar progressBar;
    Thread thread = null;
    RunnableCalculate runnableCalculate;

    final int SHOW_RESULT = 1;
    final int SHOW_ERROR = 2;
    final int SHOW_PROGRESS = 3;

    class RunnableCalculate implements Runnable {

        private double a;
        private double b;
        private int n;
        private String function;
        private boolean stop = false;
        LeftTrapezoidApproximation leftTrapezoidApproximation;

        public void setFunction(String function) {
            this.function = function;
        }

        public void setA(double a) {
            this.a = a;
        }

        public void setB(double b) {
            this.b = b;
        }

        public void setN(int n) {
            this.n = n;
        }

        public void close() {
            stop = true;
            leftTrapezoidApproximation.stopCalculate();
        }

        @Override
        public void run() {
            leftTrapezoidApproximation = new LeftTrapezoidApproximation();
            leftTrapezoidApproximation.setHandler(handler);
            final double result = leftTrapezoidApproximation.calculate(new IFunction() {
                @Override
                public double calculate(double varible) {
                    double result = 0;
                    try {
                        result = Calculator.calculate(Parser.parse(function), varible);
                    } catch (FunctionNotFoundException e) {

                    } catch (InvalidTokenException e) {
                        close();
                        Message msg = handler.obtainMessage(SHOW_ERROR, e.getMessage());
                        handler.sendMessage(msg);
                    }
                    return result;
                }
            }, a, b, n);
            Message msg;
            if(stop){
                msg = handler.obtainMessage(SHOW_RESULT, "");
                handler.sendMessage(msg);
            }else{
                msg = handler.obtainMessage(SHOW_RESULT, Double.toString(result));
                handler.sendMessage(msg);
            }

        }
    }

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
                        Log.e("test", Integer.toString(msg.what));
                        activity.textViewResult.setText((String) msg.obj);
                        break;
                    case SHOW_ERROR:
                        activity.textViewIsCheck.setText((String) msg.obj);
                        break;
                    case SHOW_PROGRESS:
                        activity.textViewIsCheck.setText(Integer.toString((int) msg.obj));
                        activity.progressBar.setProgress((int) msg.obj);
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

        Button buttonStop = findViewById(R.id.buttonStop);
        buttonStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                runnableCalculate.close();
            }
        });

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);
                String str = editTextStringToParse.getText().toString();
                textViewIsCheck.setText("");

                final double a = Double.parseDouble(editTextA.getText().toString());
                final double b = Double.parseDouble(editTextB.getText().toString());
                final int n = Integer.parseInt(editTextN.getText().toString());

                runnableCalculate = new RunnableCalculate();
                runnableCalculate.setA(a);
                runnableCalculate.setB(b);
                runnableCalculate.setN(n);
                runnableCalculate.setFunction(str);
                thread = new Thread(runnableCalculate);
                thread.start();

            }
        });
    }
}
