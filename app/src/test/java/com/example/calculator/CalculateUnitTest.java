package com.example.calculator;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CalculateUnitTest {
    @Test
    public void calculateIsCorrect() {
        TokensReader tokensReader = new TokensReader();
        ArrayList<Token> tokens = new ArrayList<>();
        try {
            tokens = tokensReader.parseString("2^3+2*(3+4/2-(1+2))*2+sin(1)+cos(2)+tan(0.5)");
        } catch (InvaildTokenException e) {
            assertTrue(e.getMessage(), false);
        }
        Calculator calculator = new Calculator(tokens);
        try {
            assertEquals(calculator.calculate(), Math.pow(2, 3) + 2 * (3 + 4 / 2 - (1 + 2)) * 2 + Math.sin(1) + Math.cos(2) + Math.tan(0.5), 0.0);
        } catch (FunctionNotFoundException e) {
            assertTrue(e.getMessage(), false);
        }
    }

    @Test
    public void calculateSinIsCorrect() {
        TokensReader tokensReader = new TokensReader();
        ArrayList<Token> tokens = new ArrayList<>();
        try {
            tokens = tokensReader.parseString("sin(1)");
        } catch (InvaildTokenException e) {
            assertTrue(e.getMessage(), false);
        }
        Calculator calculator = new Calculator(tokens);
        try {
            assertEquals(calculator.calculate(), Math.sin(1), 0.0);
        } catch (FunctionNotFoundException e) {
            assertTrue(e.getMessage(), false);
        }
    }

    @Test
    public void calculateCosIsCorrect() {
        TokensReader tokensReader = new TokensReader();
        ArrayList<Token> tokens = new ArrayList<>();
        try {
            tokens = tokensReader.parseString("cos(1)");
        } catch (InvaildTokenException e) {
            assertTrue(e.getMessage(), false);
        }
        Calculator calculator = new Calculator(tokens);
        try {
            assertEquals(calculator.calculate(), Math.cos(1), 0.0);
        } catch (FunctionNotFoundException e) {
            assertTrue(e.getMessage(), false);
        }
    }

    @Test
    public void FunctionNotFoundExceptionIsCorrect() {
        TokensReader tokensReader = new TokensReader();
        ArrayList<Token> tokens = new ArrayList<>();
        try {
            tokens = tokensReader.parseString("qwerty(1)");
        } catch (InvaildTokenException e) {
            assertTrue(e.getMessage(), false);
        }
        Calculator calculator = new Calculator(tokens);
        try {
            assertEquals(calculator.calculate(), Math.cos(1), 0.0);
        } catch (FunctionNotFoundException e) {
            assertEquals(e.getMessage(), "function qwerty not found");
        }
    }

    @Test
    public void TokenInvaildExceptionIsCorrect() {
        TokensReader tokensReader = new TokensReader();
        ArrayList<Token> tokens = new ArrayList<>();
        try {
            tokens = tokensReader.parseString("");
        } catch (InvaildTokenException e) {
            assertEquals(e.getMessage(), "string is empty");
        }
    }
}