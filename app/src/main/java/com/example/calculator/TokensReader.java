package com.example.calculator;
import java.util.ArrayList;

enum States {
    start,
    stop,
    reciveDigital,
    reciveOperators,
    reciveCharacters,
    reciveBrackets,
    reciveOpenBracket,
    reciveMinus,
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
    private ArrayList<Token> tokens = new ArrayList<>();
    private States state = States.start;
    private String string;

    private void addToken(StringBuilder stringBuilerOfToken, TypesOfToken type) {
        String stringOfToken = stringBuilerOfToken.toString();
        Token token = new Token();
        token.setToken(stringOfToken, type);
        tokens.add(token);
        stringBuilerOfToken.setLength(0);
    }

    private void reciveNumber(StringBuilder stringBuilerOfToken) throws InvaildTokenException {
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
        StringBuilder stringBuilerOfToken = new StringBuilder();
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
                    if (Character.isDigit(c)) {
                        state = States.reciveDigital;
                    } else if (minus) {
                        state = States.reciveMinus;
                    } else if (Character.isLetter(c)) {
                        state = States.reciveCharacters;
                    } else if (plus || minus || div || mul || pow) {
                        throw new InvaildTokenException("operators not expected");
                    } else if (openBracket) {
                        state = States.reciveOpenBracket;
                    } else if (closeBracket) {
                        throw new InvaildTokenException("closeBracket not expected");
                    } else if (point) {
                        throw new InvaildTokenException("point not expected");
                    } else {
                        throw new StateNotFoundException("state not fond");
                    }
                    break;
                case reciveDigital:
                    if (Character.isDigit(c)) {
                        reciveNumber(stringBuilerOfToken);
                        state = States.reciveDigital;
                        addToken(stringBuilerOfToken, TypesOfToken.number);
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
                    addToken(stringBuilerOfToken, TypesOfToken.function);
                    break;

                case reciveOperators:
                    if (plus || minus || div || mul || pow) {
                        stringBuilerOfToken.append(c);
                        position += 1;
                        state = States.reciveOperators;
                        addToken(stringBuilerOfToken, TypesOfToken.operator);
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
                case reciveMinus:
                    if (Character.isDigit(c)) {
                        state = States.reciveDigital;
                    } else if (minus) {
                        stringBuilerOfToken.append(c);
                        stringBuilerOfToken.append('u');
                        position += 1;
                        state = States.start;
                        addToken(stringBuilerOfToken, TypesOfToken.unaryMinus);
                    } else if (Character.isLetter(c)) {
                        state = States.reciveCharacters;
                    } else if (plus || minus || div || mul || pow) {
                        throw new InvaildTokenException("operators not expected");
                    } else if (openBracket) {
                        state = States.reciveBrackets;
                    } else if (closeBracket) {
                        throw new InvaildTokenException("closeBracket not expected");
                    } else if (point) {
                        throw new InvaildTokenException("point not expected");
                    } else {
                        throw new StateNotFoundException("state not fond");
                    }
                case reciveOpenBracket:
                    if (openBracket) {
                        stringBuilerOfToken.append(c);
                        position += 1;
                        state = States.reciveBrackets;
                        addToken(stringBuilerOfToken, TypesOfToken.openBracket);
                        break;
                    }
                    if (minus) {
                        state = States.reciveMinus;
                        break;
                    }
                    if (Character.isDigit(c)) {
                        state = States.reciveDigital;
                        break;
                    } else if (Character.isLetter(c)) {
                        state = States.reciveCharacters;
                        break;
                    } else if (plus || minus || div || mul || pow) {
                        throw new InvaildTokenException("operator not expected");
                    } else if (point) {
                        throw new InvaildTokenException("point not expected");
                    } else {
                        throw new StateNotFoundException("state not fond");
                    }
                case reciveBrackets:
                    if (openBracket || closeBracket) {
                        stringBuilerOfToken.append(c);
                        position += 1;
                        state = States.reciveBrackets;
                        addToken(stringBuilerOfToken, TypesOfToken.openBracket);
                        break;
                    } else if (minus) {
                        state = States.reciveMinus;
                    } else if (Character.isDigit(c)) {
                        state = States.reciveDigital;
                    } else if (Character.isLetter(c)) {
                        state = States.reciveCharacters;
                    } else if (plus || minus || div || mul || pow) {
                        state = States.reciveOperators;
                    } else if (point) {
                        throw new InvaildTokenException("point not expected");
                    } else {
                        throw new StateNotFoundException("state not fond");
                    }
            }
        }
        this.string = stringBuilerOfToken.toString();
        if (this.string != "") {
            throw new RuntimeException("Error");
        }
        return tokens;
    }
}