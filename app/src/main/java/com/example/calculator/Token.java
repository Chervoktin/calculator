package com.example.calculator;

class InvaildTypeOfTokenException extends RuntimeException {
    public InvaildTypeOfTokenException(String message) {
        super(message);
    }
}

public class Token {
    private TypesOfToken type;
    private String string;
    private int priority = -1;

    public TypesOfToken getType() {
        return type;
    }

    public int getPriority() {
        return this.priority;
    }

    public String getString() {
        return this.string;
    }

    private boolean isNumber(String string) {
        try {
            Double.parseDouble(string);
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

    public void setToken(String string, TypesOfToken type) {
        boolean plus = string.equals("+");
        boolean minus = string.equals("-");
        boolean div = string.equals("/");
        boolean mul = string.equals("*");
        boolean pow = string.equals("^");
        boolean uminus = string.equals("-u");
        if (plus || minus) this.priority = 1;
        if (div || mul) this.priority = 2;
        if (pow || uminus) this.priority = 3;
        if (type == TypesOfToken.function) this.priority = 4;
        this.string = string;
        this.type = type;
    }
}