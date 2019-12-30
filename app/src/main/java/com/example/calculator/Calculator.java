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

class Calculator {
    private ArrayList<Token> tokens;
    Stack<Double> numbers;
    Stack<Token> operatorsAndFunctions;

    Calculator(ArrayList<Token> tokens) {
        this.tokens = tokens;
        numbers = new Stack<>();
        operatorsAndFunctions = new Stack<>();
    }

    private double calculateOnStack(Token token) {
        boolean plus = token.getString().equals("+");
        boolean min = token.getString().equals("-");
        boolean umin = token.getString().equals("-u");
        boolean mul = token.getString().equals("*");
        boolean div = token.getString().equals("/");
        boolean pow = token.getString().equals("^");
        boolean openBracket = token.getString().equals("(");
        boolean closeBracket = token.getString().equals(")");
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
        }
        return result;
    }

    public double calculate() {

        int position = 0;
        StateOfCalculate state = StateOfCalculate.start;
        while (position < tokens.size()) {
            switch (state) {
                case start:
                    if (tokens.get(position).getType() == TypesOfToken.number) {
                        state = StateOfCalculate.number;
                    } else if (tokens.get(position).getType() == TypesOfToken.operator) {
                        state = StateOfCalculate.operator;
                    } else if (tokens.get(position).getType() == TypesOfToken.unaryMinus) {
                        state = StateOfCalculate.operator;
                    } else if (tokens.get(position).getType() == TypesOfToken.function) {
                        state = StateOfCalculate.function;
                    } else if (tokens.get(position).getType() == TypesOfToken.closeBracket) {
                        state = StateOfCalculate.closeBracket;
                    } else if (tokens.get(position).getType() == TypesOfToken.openBracket) {
                        state = StateOfCalculate.openBracket;
                    }
                    break;
                case number:
                    if (tokens.get(position).getType() == TypesOfToken.number) {
                        state = StateOfCalculate.number;
                        numbers.push(Double.parseDouble(tokens.get(position).getString()));
                        position += 1;
                    } else if (tokens.get(position).getType() == TypesOfToken.operator) {
                        state = StateOfCalculate.operator;
                    } else if (tokens.get(position).getType() == TypesOfToken.closeBracket) {
                        state = StateOfCalculate.closeBracket;
                    }
                    break;
                case operator:
                    if (tokens.get(position).getType() == TypesOfToken.number) {
                        state = StateOfCalculate.number;
                    } else if (tokens.get(position).getType() == TypesOfToken.operator) {

                        if (operatorsAndFunctions.empty()) {
                            operatorsAndFunctions.push(tokens.get(position));
                            position += 1;
                        } else if (tokens.get(position).getPriority() > operatorsAndFunctions.lastElement().getPriority()) {
                            operatorsAndFunctions.push(tokens.get(position));
                            position += 1;
                        } else {
                            numbers.push(calculateOnStack(operatorsAndFunctions.pop()));

                        }
                    } else if (tokens.get(position).getType() == TypesOfToken.closeBracket) {
                        state = StateOfCalculate.closeBracket;
                    } else if (tokens.get(position).getType() == TypesOfToken.openBracket) {
                        state = StateOfCalculate.openBracket;
                    }
                    break;
                case openBracket:
                    if (tokens.get(position).getType() == TypesOfToken.number) {
                        state = StateOfCalculate.number;
                    } else if (tokens.get(position).getType() == TypesOfToken.operator) {
                        state = StateOfCalculate.operator;
                    } else if (tokens.get(position).getType() == TypesOfToken.function) {
                        state = StateOfCalculate.function;
                    } else if (tokens.get(position).getType() == TypesOfToken.openBracket) {
                        operatorsAndFunctions.push(tokens.get(position));
                        position += 1;
                    } else if (tokens.get(position).getType() == TypesOfToken.closeBracket) {
                        state = StateOfCalculate.closeBracket;
                    }
                    break;
                case closeBracket:
                    if (tokens.get(position).getType() == TypesOfToken.number) {
                        state = StateOfCalculate.number;
                    } else if (tokens.get(position).getType() == TypesOfToken.operator) {
                        state = StateOfCalculate.operator;
                    } else if (tokens.get(position).getType() == TypesOfToken.function) {
                        state = StateOfCalculate.function;
                    } else if (tokens.get(position).getType() == TypesOfToken.openBracket) {
                        state = StateOfCalculate.openBracket;
                    } else if (tokens.get(position).getType() == TypesOfToken.closeBracket) {
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