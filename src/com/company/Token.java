package com.company;

public class Token {
    private TypesOfToken type;
    private String string;

    public TypesOfToken getType() {
        return type;
    }

    public String getString() {
        return this.string;
    }

    private boolean isDigit(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isFunction(String string) {

        boolean isNameOfFunction = true;
        for (int i = 0; i < string.length(); i++) {
            isNameOfFunction = Character.isAlphabetic(string.charAt(i));
            if (!isNameOfFunction)
                break;
        }
        return isNameOfFunction;
    }

    public void setToken(String string) {
        this.string = string;
        boolean plus = string.equals("+");
        boolean minus = string.equals("-");
        boolean div = string.equals("/");
        boolean mul = string.equals("*");
        boolean pow = string.equals("^");
        boolean isOperator = plus || minus || div || mul || pow;
        boolean openBracket = string.equals("(");
        boolean closeBracket = string.equals(")");
        boolean isVariable = string.length() == 1 && Character.isAlphabetic(string.charAt(0));

        if (isDigit(string)) {
            this.type = TypesOfToken.number;
        } else if (isOperator) {
            this.type = TypesOfToken.operator;
        } else if (openBracket) {
            this.type = TypesOfToken.openBracket;
        } else if (closeBracket) {
            this.type = TypesOfToken.closeBracket;
        } else if (isVariable) {
            this.type = TypesOfToken.varible;
        } else if (isFunction(string)) {
            this.type = TypesOfToken.function;
        }
    }
}