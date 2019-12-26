package com.example.calculator;

import java.util.ArrayList;

enum States {
    start,
    stop,
    reciveDigital,
    reciveOperators,
    reciveCharacters,
    reciveBrackets,
    recivePoint

}

class InvaildTokenException extends Exception {
    public InvaildTokenException(String message) {
        super(message);
    }
}

class StateNotFoundException extends RuntimeException {
    public StateNotFoundException(String message) {
        super(message);
    }
}

class TokensReader {
    private int position = 0;
    private StringBuilder stringBuilerOfToken = null;
    private ArrayList<Token> tokens = new ArrayList<>();
    private States state = States.start;
    private String string;

    private void addToken() {
        String stringOfToken = null;
        Token token = null;
        stringOfToken = stringBuilerOfToken.toString();
        token = new Token();
        token.setToken(stringOfToken);
        tokens.add(token);
        stringBuilerOfToken = new StringBuilder();
    }

    private void reciveNumber() throws InvaildTokenException {
        boolean flag = false;
        char c = string.charAt(position);
        boolean isDigit = Character.isDigit(c);
        while ((isDigit || (c == '.')) && (position != string.length())) {
            stringBuilerOfToken.append(c);
            position += 1;
            if (flag && (c == '.')) {
                throw new InvaildTokenException("two points in a row");
            } else if (string.length() != position) {
                if (c == '.') flag = true;
                c = string.charAt(position);
                isDigit = Character.isDigit(c);
            } else if ((string.charAt(position - 1) == '.') && (position == string.length())) {
                throw new InvaildTokenException("two points in a row");
            } else {
                break;
            }
        }
    }

    public ArrayList<Token> parseString(String string) throws InvaildTokenException {
        this.string = string;
        char c;
        while ((position < string.length()) && state != States.stop) {
            c = string.charAt(position);
            boolean plus = (c == '+');
            boolean minus = (c == '-');
            boolean div = (c == '/');
            boolean mul = (c == '*');
            boolean pow = (c == '^');
            boolean openBracket = (c == '(');
            boolean closeBracket = (c == ')');
            boolean point = (c == '.');
            switch (state) {
                case start:
                    stringBuilerOfToken = new StringBuilder();
                    if (Character.isDigit(c)) {
                        state = States.reciveDigital;
                    } else if (Character.isLetter(c)) {
                        state = States.reciveCharacters;
                    } else if (plus || minus || div || mul || pow) {
                        state = States.reciveOperators;
                    } else if (openBracket || closeBracket) {
                        state = States.reciveBrackets;
                    } else if (point) {
                        throw new InvaildTokenException("point not expected");
                    } else {
                        throw new StateNotFoundException("state not fond");
                    }
                    break;
                case reciveDigital:
                    if (Character.isDigit(c)) {
                        reciveNumber();
                        state = States.reciveDigital;
                        addToken();
                        break;
                    } else if (Character.isLetter(c)) {
                        state = States.reciveCharacters;
                    } else if (plus || minus || div || mul || pow) {
                        state = States.reciveOperators;
                    } else if (openBracket || closeBracket) {
                        state = States.reciveBrackets;
                    } else if (point) {
                        throw new InvaildTokenException("point after digit");
                    } else {
                        throw new StateNotFoundException("state not fond");
                    }
                    break;

                case reciveCharacters:
                    if (Character.isLetter(c)) {
                        stringBuilerOfToken.append(c);
                        position += 1;
                        state = States.reciveCharacters;
                        break;
                    }
                    if (Character.isDigit(c)) {
                        state = States.reciveDigital;
                    } else if (plus || minus || div || mul || pow) {
                        state = States.reciveOperators;
                    } else if (openBracket || closeBracket) {
                        state = States.reciveBrackets;
                    } else if (point) {
                        throw new InvaildTokenException("point not expected");
                    } else {
                        throw new StateNotFoundException("state not fond");
                    }
                    addToken();
                    break;

                case reciveOperators:
                    if (plus || minus || div || mul || pow) {
                        stringBuilerOfToken.append(c);
                        position += 1;
                        state = States.reciveOperators;
                        addToken();
                        break;
                    } else if (Character.isDigit(c)) {
                        state = States.reciveDigital;
                    } else if (Character.isLetter(c)) {
                        state = States.reciveCharacters;
                    } else if (openBracket || closeBracket) {
                        state = States.reciveBrackets;
                    } else if (point) {
                        throw new InvaildTokenException("point not expected");
                    } else {
                        throw new StateNotFoundException("state not fond");
                    }
                    break;

                case reciveBrackets:
                    if (openBracket || closeBracket) {
                        stringBuilerOfToken.append(c);
                        position += 1;
                        state = States.reciveBrackets;
                        addToken();
                        break;
                    }
                    if (Character.isDigit(c)) {
                        state = States.reciveDigital;
                        break;
                    } else if (Character.isLetter(c)) {
                        state = States.reciveCharacters;
                        break;
                    } else if (plus || minus || div || mul || pow) {
                        state = States.reciveOperators;
                        break;
                    } else if (point) {
                        throw new InvaildTokenException("point not expected");
                    } else {
                        throw new StateNotFoundException("state not fond");
                    }
            }
        }
        this.string = this.stringBuilerOfToken.toString();
        if (this.string != "") {
            addToken();
        }
        return tokens;
    }
}