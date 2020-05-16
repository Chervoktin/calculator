package com.example.calculator;

import android.os.Message;
import android.os.Handler;

public class LeftTrapezoidApproximation {
    private Handler handler = null;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public double calculate(IFunction function, double a, double b, int n) {
        int SHOW_PROGRESS = 3;
        double delta = (b - a) / n;
        double x = a + delta;
        double sum = 0;
        double f1;
        double fn;
        int per = 0;
        int prev = 0;
        for (int i = 1; i < n; i++) {
            prev = per;
            per = i / (n / 100);
            if (handler != null) {

                if (per != prev) {
                    Message msg = handler.obtainMessage(SHOW_PROGRESS, per );
                    handler.sendMessage(msg);
                }
            }
            sum += function.calculate(x);
            x += delta;
        }
        f1 = function.calculate(a);
        fn = function.calculate(x);
        sum += (f1 + fn) / 2;
        sum *= delta;
        if(handler != null){
            Message msg = handler.obtainMessage(SHOW_PROGRESS, 100 );
            handler.sendMessage(msg);
        }
        return sum;
    }
}
