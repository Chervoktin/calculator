package com.example.calculator;

import java.util.ArrayList;

public class LeftTrapezoidApproximation {
    public double calculate(IFunction function, double a, double b, int n) {
        double delta = (b - a) / n;
        double x = a + delta;
        double sum = 0;
        double f1;
        double fn;
        for (int i = 1; i < n; i++) {
            sum += function.calculate(x);
            x += delta;
        }
        f1 = function.calculate(a);
        fn = function.calculate(x);
        sum += (f1 + fn) / 2;
        sum *= delta;
        return sum;
    }
}
