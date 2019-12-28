package com.example.calculator;

import java.util.ArrayList;

enum StatesOfParse {
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
    ArrayList<Token> tokens;

    TokensReader() {
        tokens = new ArrayList<>();
    }

    private void addToken(StringBuilder stringBuilerOfToken, TypesOfToken type) {
        String stringOfToken = stringBuilerOfToken.toString();
        Token token = new Token();
        token.setToken(stringOfToken, type);
        tokens.add(token);
        stringBuilerOfToken.setLength(0);
    }

    private int reciveNumber(StringBuilder stringBuilerOfToken, String string, Integer position) throws InvaildTokenException {
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
        return position;
    }

    public ArrayList<Token> parseString(String string) throws InvaildTokenException {
        Integer position = 0;
        char c;
        StringBuilder stringBuilerOfToken = new StringBuilder();
        StatesOfParse state = StatesOfParse.start;
        while ((position < string.length()) && state != StatesOfParse.stop) {
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
                        state = StatesOfParse.reciveDigital;
                    } else if (minus) {
                        state = StatesOfParse.reciveMinus;
                    } else if (Character.isLetter(c)) {
                        state = StatesOfParse.reciveCharacters;
                    } else if (plus || minus || div || mul || pow) {
                        throw new InvaildTokenException("operators not expected");
                    } else if (openBracket) {
                        state = StatesOfParse.reciveOpenBracket;
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
                        position = reciveNumber(stringBuilerOfToken, string, position);
                        state = StatesOfParse.reciveDigital;
                        addToken(stringBuilerOfToken, TypesOfToken.number);
                        break;
                    } else if (Character.isLetter(c)) {
                        state = StatesOfParse.reciveCharacters;
                    } else if (plus || minus || div || mul || pow) {
                        state = StatesOfParse.reciveOperators;
                    } else if (openBracket || closeBracket) {
                        state = StatesOfParse.reciveBrackets;
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
                        state = StatesOfParse.reciveCharacters;
                        break;
                    }
                    if (Character.isDigit(c)) {
                        state = StatesOfParse.reciveDigital;
                    } else if (plus || minus || div || mul || pow) {
                        state = StatesOfParse.reciveOperators;
                    } else if (openBracket || closeBracket) {
                        state = StatesOfParse.reciveBrackets;
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
                        state = StatesOfParse.reciveOperators;
                        addToken(stringBuilerOfToken, TypesOfToken.operator);
                        break;
                    } else if (Character.isDigit(c)) {
                        state = StatesOfParse.reciveDigital;
                    } else if (Character.isLetter(c)) {
                        state = StatesOfParse.reciveCharacters;
                    } else if (openBracket || closeBracket) {
                        state = StatesOfParse.reciveBrackets;
                    } else if (point) {
                        throw new InvaildTokenException("point not expected");
                    } else {
                        throw new StateNotFoundException("state not fond");
                    }
                    break;
                case reciveMinus:
                    if (Character.isDigit(c)) {
                        state = StatesOfParse.reciveDigital;
                    } else if (minus) {
                        stringBuilerOfToken.append(c);
                        stringBuilerOfToken.append('u');
                        position += 1;
                        state = StatesOfParse.start;
                        addToken(stringBuilerOfToken, TypesOfToken.operator);
                    } else if (Character.isLetter(c)) {
                        state = StatesOfParse.reciveCharacters;
                    } else if (plus || minus || div || mul || pow) {
                        throw new InvaildTokenException("operators not expected");
                    } else if (openBracket) {
                        state = StatesOfParse.reciveBrackets;
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
                        state = StatesOfParse.reciveBrackets;
                        addToken(stringBuilerOfToken, TypesOfToken.openBracket);
                        break;
                    }
                    if (minus) {
                        state = StatesOfParse.reciveMinus;
                        break;
                    }
                    if (Character.isDigit(c)) {
                        state = StatesOfParse.reciveDigital;
                        break;
                    } else if (Character.isLetter(c)) {
                        state = StatesOfParse.reciveCharacters;
                        break;
                    } else if (plus || minus || div || mul || pow) {
                        throw new InvaildTokenException("operator not expected");
                    } else if (point) {
                        throw new InvaildTokenException("point not expected");
                    } else {
                        throw new StateNotFoundException("state not fond");
                    }
                case reciveBrackets:
                    if (closeBracket) {
                        stringBuilerOfToken.append(c);
                        position += 1;
                        state = StatesOfParse.reciveBrackets;
                        addToken(stringBuilerOfToken, TypesOfToken.closeBracket);
                        break;
                    } else if (openBracket) {
                        state = StatesOfParse.reciveOpenBracket;
                    } else if (minus) {
                        state = StatesOfParse.reciveOperators;
                    } else if (Character.isDigit(c)) {
                        state = StatesOfParse.reciveDigital;
                    } else if (Character.isLetter(c)) {
                        state = StatesOfParse.reciveCharacters;
                    } else if (plus || minus || div || mul || pow) {
                        state = StatesOfParse.reciveOperators;
                    } else if (point) {
                        throw new InvaildTokenException("point not expected");
                    } else {
                        throw new StateNotFoundException("state not fond");
                    }
            }
        }
        string = stringBuilerOfToken.toString();
        if (string != "") {
            throw new RuntimeException("Error");
        }
        return tokens;
    }
}