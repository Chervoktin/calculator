package com.example.calculator;

import java.util.ArrayList;
import java.util.Stack;

enum StateOfCalculate {
    start,
    number,
    varible,
    operator,
    function,
    openBracket,
    closeBracket,
}

class FunctionNotFoundException extends Exception {
    public FunctionNotFoundException(String message) {
        super(message);
    }
}

class Calculator {

    public static double leftTrapezoidApproximation(String integral, double a, double b, int n) throws InvalidTokenException, FunctionNotFoundException {
        double delta = (b - a) / n;
        double x = a + delta;
        double sum = 0;
        double f1;
        double fn;
        ArrayList<Token> tokens = Parser.parse(integral);
        for (int i = 1; i < n; i++) {
            sum += Calculator.calculate(tokens, x);
            x += delta;
        }
        f1 = Calculator.calculate(tokens, a);
        fn = Calculator.calculate(tokens, x);
        sum += (f1 + fn) / 2;
        sum *= delta;
        return sum;
    }

    private static double calculateOnStack(Token token, Stack<Double> numbers) throws FunctionNotFoundException {
        boolean plus = token.getString().equals("+");
        boolean min = token.getString().equals("-");
        boolean umin = token.getString().equals("-u");
        boolean mul = token.getString().equals("*");
        boolean div = token.getString().equals("/");
        boolean pow = token.getString().equals("^");
        boolean sin = token.getString().equals("sin");
        boolean cos = token.getString().equals("cos");
        boolean tan = token.getString().equals("tan");
        double firstOperand;
        double secondOperand;
        double result = 0;
        if (plus) {
            secondOperand = numbers.pop();
            firstOperand = numbers.pop();
            result = firstOperand + secondOperand;
        } else if (min) {
            secondOperand = numbers.pop();
            firstOperand = numbers.pop();
            result = firstOperand - secondOperand;
        } else if (div) {
            secondOperand = numbers.pop();
            firstOperand = numbers.pop();
            result = firstOperand / secondOperand;
        } else if (mul) {
            secondOperand = numbers.pop();
            firstOperand = numbers.pop();
            result = firstOperand * secondOperand;
        } else if (pow) {
            secondOperand = numbers.pop();
            firstOperand = numbers.pop();
            return Math.pow(firstOperand, secondOperand);
        } else if (umin) {
            firstOperand = numbers.pop();
            result = -firstOperand;
        } else if (sin) {
            firstOperand = numbers.pop();
            result = Math.sin(firstOperand);
        } else if (cos) {
            firstOperand = numbers.pop();
            result = Math.cos(firstOperand);
        } else if (tan) {
            firstOperand = numbers.pop();
            result = Math.tan(firstOperand);
        } else {
            throw new FunctionNotFoundException("function " + token.getString() + " not found");
        }
        return result;
    }

    public static double calculate(ArrayList<Token> tokens, double varibale) throws FunctionNotFoundException {
        for (Token token : tokens) {
            if (token.getType() == TypesOfToken.varible) {
                token.setToken(varibale, TypesOfToken.varible);
            }
        }
        return calculate(tokens);
    }

    public static double calculate(ArrayList<Token> tokens) throws FunctionNotFoundException {
        Stack<Double> numbers = new Stack<>();
        Stack<Token> operatorsAndFunctions = new Stack<>();
        int position = 0;
        StateOfCalculate state = StateOfCalculate.start;
        boolean number;
        boolean operator;
        boolean unaryMinus;
        boolean function;
        boolean closeBracket;
        boolean openBracket;
        boolean varible;
        while (position < tokens.size()) {
            number = tokens.get(position).getType() == TypesOfToken.number;
            operator = tokens.get(position).getType() == TypesOfToken.operator;
            unaryMinus = tokens.get(position).getType() == TypesOfToken.unaryMinus;
            function = tokens.get(position).getType() == TypesOfToken.function;
            closeBracket = tokens.get(position).getType() == TypesOfToken.closeBracket;
            openBracket = tokens.get(position).getType() == TypesOfToken.openBracket;
            varible = tokens.get(position).getType() == TypesOfToken.varible;
            switch (state) {
                case start:
                    if (number || varible) {
                        state = StateOfCalculate.number;
                    } else if (operator) {
                        state = StateOfCalculate.operator;
                    } else if (unaryMinus) {
                        state = StateOfCalculate.operator;
                    } else if (function) {
                        state = StateOfCalculate.function;
                    } else if (closeBracket) {
                        state = StateOfCalculate.closeBracket;
                    } else if (openBracket) {
                        state = StateOfCalculate.openBracket;
                    }
                    break;
                case number:
                case varible:
                    if (number || varible) {
                        state = StateOfCalculate.number;
                        numbers.push(tokens.get(position).getNumber());
                        position += 1;
                    } else if (operator) {
                        state = StateOfCalculate.operator;
                    } else if (closeBracket) {
                        state = StateOfCalculate.closeBracket;
                    }
                    break;
                case function:
                case operator:
                    if (number || varible) {
                        state = StateOfCalculate.number;
                    } else if (operator || function) {

                        if (operatorsAndFunctions.empty()) {
                            operatorsAndFunctions.push(tokens.get(position));
                            position += 1;
                        } else if (tokens.get(position).getPriority() > operatorsAndFunctions.lastElement().getPriority()) {
                            operatorsAndFunctions.push(tokens.get(position));
                            position += 1;
                        } else {
                            numbers.push(calculateOnStack(operatorsAndFunctions.pop(), numbers));

                        }
                    } else if (closeBracket) {
                        state = StateOfCalculate.closeBracket;
                    } else if (openBracket) {
                        state = StateOfCalculate.openBracket;
                    } else if (function) {
                        state = StateOfCalculate.function;
                    }
                    break;
                case openBracket:
                    if (number || varible) {
                        state = StateOfCalculate.number;
                    } else if (operator) {
                        state = StateOfCalculate.operator;
                    } else if (function) {
                        state = StateOfCalculate.function;
                    } else if (openBracket) {
                        operatorsAndFunctions.push(tokens.get(position));
                        position += 1;
                    } else if (closeBracket) {
                        state = StateOfCalculate.closeBracket;
                    }
                    break;
                case closeBracket:
                    if (number || varible) {
                        state = StateOfCalculate.number;
                    } else if (operator) {
                        state = StateOfCalculate.operator;
                    } else if (function) {
                        state = StateOfCalculate.function;
                    } else if (openBracket) {
                        state = StateOfCalculate.openBracket;
                    } else if (closeBracket) {
                        if (operatorsAndFunctions.lastElement().getType() != TypesOfToken.openBracket) {
                            numbers.push(calculateOnStack(operatorsAndFunctions.pop(), numbers));
                        } else {
                            operatorsAndFunctions.pop();
                            position += 1;
                        }
                    }
            }
        }
        while (operatorsAndFunctions.size() > 0) {
            numbers.push(calculateOnStack(operatorsAndFunctions.pop(), numbers));
        }
        return numbers.pop();
    }

}