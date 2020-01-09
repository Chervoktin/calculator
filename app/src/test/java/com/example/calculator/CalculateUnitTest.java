package com.example.calculator;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CalculateUnitTest {
    @Test
    public void leftTrapezoidApproximateIsCorrect() {
        try {
            double result = Calculator.leftTrapezoidApproximation("cos(x)/(x+2)", 0.4, 1.2, 100);
            assertEquals(result, 0.19898, 0.00001);

        } catch (InvalidTokenException e) {
            assertTrue(e.getMessage(), false);
        } catch (FunctionNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void calculateIsCorrect() {
        ArrayList<Token> tokens = new ArrayList<>();
        try {
            tokens = Parser.parse("2^3+2*(3+4/2-(1+2))*2+sin(1)+cos(2)+tan(0.5)");
        } catch (InvalidTokenException e) {
            assertTrue(e.getMessage(), false);
        }
        try {
            assertEquals(Calculator.calculate(tokens), Math.pow(2, 3) + 2 * (3 + 4 / 2 - (1 + 2)) * 2 + Math.sin(1) + Math.cos(2) + Math.tan(0.5), 0.0);
        } catch (FunctionNotFoundException e) {
            assertTrue(e.getMessage(), false);
        }
    }

    @Test
    public void calculateSinIsCorrect() {
        ArrayList<Token> tokens = new ArrayList<>();
        try {
            tokens = Parser.parse("sin(1)");
        } catch (InvalidTokenException e) {
            assertTrue(e.getMessage(), false);
        }
        try {
            assertEquals(Calculator.calculate(tokens), Math.sin(1), 0.0);
        } catch (FunctionNotFoundException e) {
            assertTrue(e.getMessage(), false);
        }
    }

    @Test
    public void calculateCosIsCorrect() {
        ArrayList<Token> tokens = new ArrayList<>();
        try {
            tokens = Parser.parse("cos(1)");
        } catch (InvalidTokenException e) {
            assertTrue(e.getMessage(), false);
        }
        try {
            assertEquals(Calculator.calculate(tokens), Math.cos(1), 0.0);
        } catch (FunctionNotFoundException e) {
            assertTrue(e.getMessage(), false);
        }
    }

    @Test
    public void FunctionNotFoundExceptionIsCorrect() {
        ArrayList<Token> tokens = new ArrayList<>();
        try {
            tokens = Parser.parse("qwerty(1)");
        } catch (InvalidTokenException e) {
            assertTrue(e.getMessage(), false);
        }
        try {
            assertEquals(Calculator.calculate(tokens), Math.cos(1), 0.0);
        } catch (FunctionNotFoundException e) {
            assertEquals(e.getMessage(), "function qwerty not found");
        }
    }

    @Test
    public void TokenInvaildExceptionIsCorrect() {
        ArrayList<Token> tokens = new ArrayList<>();
        try {
            tokens = Parser.parse("");
        } catch (InvalidTokenException e) {
            assertEquals(e.getMessage(), "string is empty");
        }
    }
}