package com.example.calculator;

import java.util.ArrayList;
import java.util.Stack;

enum StateOfCalculate {
    start,
    number,
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
    private ArrayList<Token> tokens;
    Stack<Double> numbers;
    Stack<Token> operatorsAndFunctions;

    Calculator(ArrayList<Token> tokens) {
        this.tokens = tokens;
        numbers = new Stack<>();
        operatorsAndFunctions = new Stack<>();
    }

    private double calculateOnStack(Token token) throws FunctionNotFoundException {
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

    public double calculate() throws FunctionNotFoundException {

        int position = 0;
        StateOfCalculate state = StateOfCalculate.start;
        boolean number;
        boolean operator;
        boolean unaryMinus;
        boolean function;
        boolean closeBracket;
        boolean openBracket;
        while (position < tokens.size()) {
            number = tokens.get(position).getType() == TypesOfToken.number;
            operator = tokens.get(position).getType() == TypesOfToken.operator;
            unaryMinus = tokens.get(position).getType() == TypesOfToken.unaryMinus;
            function = tokens.get(position).getType() == TypesOfToken.function;
            closeBracket = tokens.get(position).getType() == TypesOfToken.closeBracket;
            openBracket = tokens.get(position).getType() == TypesOfToken.openBracket;
            switch (state) {
                case start:
                    if (number) {
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
                    if (number) {
                        state = StateOfCalculate.number;
                        numbers.push(Double.parseDouble(tokens.get(position).getString()));
                        position += 1;
                    } else if (operator) {
                        state = StateOfCalculate.operator;
                    } else if (closeBracket) {
                        state = StateOfCalculate.closeBracket;
                    }
                    break;
                case function:
                case operator:
                    if (number) {
                        state = StateOfCalculate.number;
                    } else if (operator || function) {

                        if (operatorsAndFunctions.empty()) {
                            operatorsAndFunctions.push(tokens.get(position));
                            position += 1;
                        } else if (tokens.get(position).getPriority() > operatorsAndFunctions.lastElement().getPriority()) {
                            operatorsAndFunctions.push(tokens.get(position));
                            position += 1;
                        } else {
                            numbers.push(calculateOnStack(operatorsAndFunctions.pop()));

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
                    if (number) {
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
                    if (number) {
                        state = StateOfCalculate.number;
                    } else if (operator) {
                        state = StateOfCalculate.operator;
                    } else if (function) {
                        state = StateOfCalculate.function;
                    } else if (openBracket) {
                        state = StateOfCalculate.openBracket;
                    } else if (closeBracket) {
                        if (operatorsAndFunctions.lastElement().getType() != TypesOfToken.openBracket) {
                            numbers.push(calculateOnStack(operatorsAndFunctions.pop()));
                        } else {
                            operatorsAndFunctions.pop();
                            position += 1;
                        }
                    }
            }
        }
        while (operatorsAndFunctions.size() > 0) {
            numbers.push(calculateOnStack(operatorsAndFunctions.pop()));
        }
        return numbers.pop();
    }

}