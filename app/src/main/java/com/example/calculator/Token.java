package com.example.calculator;

class InvaildTypeOfTokenException extends RuntimeException {
    public InvaildTypeOfTokenException(String message) {
        super(message);
    }
}

public class Token {
    private TypesOfToken type;
    private String string;

    public TypesOfToken getType() {
        return type;
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
        this.string = string;
        this.type = type;
    }
}