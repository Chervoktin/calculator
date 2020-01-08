package com.example.calculator;

import java.util.ArrayList;

enum StatesOfParse {
    start,
    stop,
    reciveDigital,
    reciveOperators,
    reciveCharacters,
    reciveCloseBracket,
    reciveOpenBracket,
    reciveMinus,
}

class InvalidTokenException extends Exception {
    public InvalidTokenException(String message) {
        super(message);
    }
}

class Parser {

    private static boolean checkBrackets(ArrayList<Token> tokens) {
        int countOfOpened = 0;
        int countOfClosed = 0;
        for (Token token : tokens) {
            if (token.getType() == TypesOfToken.openBracket)
                countOfOpened += 1;
            if (token.getType() == TypesOfToken.closeBracket)
                countOfClosed += 1;
        }
        return countOfOpened == countOfClosed;
    }

    private static void addToken(StringBuilder stringBuilerOfToken, TypesOfToken type, ArrayList<Token> tokens) {
        String stringOfToken = stringBuilerOfToken.toString();
        Token token = new Token();
        try {
            double number = Double.parseDouble(stringOfToken);
            token.setToken(number);
            tokens.add(token);
            stringBuilerOfToken.setLength(0);
        } catch (Exception e) {
            token.setToken(stringOfToken, type);
            tokens.add(token);
            stringBuilerOfToken.setLength(0);
        }

    }

    private static int reciveNumber(StringBuilder stringBuilerOfToken, String string, Integer position, ArrayList<Token> tokens) throws InvalidTokenException {
        boolean flag = false;
        char c = string.charAt(position);
        boolean isDigit = Character.isDigit(c);
        while ((isDigit || (c == '.')) && (position != string.length())) {
            stringBuilerOfToken.append(c);
            position += 1;
            if (flag && (c == '.')) {
                throw new InvalidTokenException("two points in a row");
            } else if (string.length() != position) {
                if (c == '.') flag = true;
                c = string.charAt(position);
                isDigit = Character.isDigit(c);
            } else if ((string.charAt(position - 1) == '.') && (position == string.length())) {
                throw new InvalidTokenException("two points in a row");
            } else {
                break;
            }
        }
        return position;
    }


    public static ArrayList<Token> parse(String string) throws InvalidTokenException {
        ArrayList<Token> tokens = new ArrayList<>();
        if (string.length() == 0) {
            throw new InvalidTokenException("string is empty");
        }
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
                        throw new InvalidTokenException("operators not excepted");
                    } else if (openBracket) {
                        state = StatesOfParse.reciveOpenBracket;
                    } else if (closeBracket) {
                        throw new InvalidTokenException("closeBracket not excepted");
                    } else if (point) {
                        throw new InvalidTokenException("point not excepted");
                    } else {
                        throw new InvalidTokenException(string.charAt(position) + " not excepted");
                    }
                    break;
                case reciveDigital:
                    if (Character.isDigit(c)) {
                        position = reciveNumber(stringBuilerOfToken, string, position, tokens);
                        state = StatesOfParse.reciveDigital;
                        addToken(stringBuilerOfToken, TypesOfToken.number, tokens);
                        break;
                    } else if (Character.isLetter(c)) {
                        throw new InvalidTokenException("character is not excepted");
                    } else if (plus || minus || div || mul || pow) {
                        state = StatesOfParse.reciveOperators;
                    } else if (openBracket) {
                        throw new InvalidTokenException("open bracket not excepted");
                    } else if (closeBracket) {
                        state = StatesOfParse.reciveCloseBracket;
                    } else if (point) {
                        throw new InvalidTokenException("point after digit");
                    } else {
                        throw new InvalidTokenException(string.charAt(position) + " not excepted");
                    }
                    break;

                case reciveCharacters:
                    if (Character.isLetter(c)) {
                        while (Character.isLetter(c)) {
                            stringBuilerOfToken.append(c);
                            position += 1;
                            if (position < string.length()) {
                                c = string.charAt(position);
                            } else {
                                break;
                            }
                            state = StatesOfParse.reciveCharacters;
                        }
                        if (stringBuilerOfToken.toString().length() == 1) {
                            addToken(stringBuilerOfToken, TypesOfToken.varible, tokens);
                        } else {
                            addToken(stringBuilerOfToken, TypesOfToken.function, tokens);
                        }
                    } else if (Character.isDigit(c)) {
                        state = StatesOfParse.reciveDigital;
                    } else if (plus || minus || div || mul || pow) {
                        state = StatesOfParse.reciveOperators;
                    } else if (openBracket) {
                        state = StatesOfParse.reciveOpenBracket;
                    } else if (closeBracket) {
                        if (tokens.get(tokens.size() - 1).getType() == TypesOfToken.varible) {
                            state = StatesOfParse.reciveCloseBracket;
                        } else {
                            throw new InvalidTokenException("close bracket not excepted");
                        }
                    } else if (point) {
                        throw new InvalidTokenException("point not excepted");
                    } else {
                        throw new InvalidTokenException(string.charAt(position) + " not excepted");
                    }
                    break;

                case reciveOperators:
                    if (plus || minus || div || mul || pow) {
                        if (tokens.get(tokens.size() - 1).getType() == TypesOfToken.operator) {
                            throw new InvalidTokenException(string.charAt(position) + " not excepted");
                        }
                        stringBuilerOfToken.append(c);
                        position += 1;
                        state = StatesOfParse.reciveOperators;
                        addToken(stringBuilerOfToken, TypesOfToken.operator, tokens);
                        break;
                    } else if (Character.isDigit(c)) {
                        state = StatesOfParse.reciveDigital;
                    } else if (Character.isLetter(c)) {
                        state = StatesOfParse.reciveCharacters;
                    } else if (openBracket) {
                        state = StatesOfParse.reciveOpenBracket;
                    } else if (closeBracket) {
                        throw new InvalidTokenException("close bracket not excepted");
                    } else if (point) {
                        throw new InvalidTokenException("point not excepted");
                    } else {
                        throw new InvalidTokenException(string.charAt(position) + " not excepted");
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
                        addToken(stringBuilerOfToken, TypesOfToken.operator, tokens);
                    } else if (Character.isLetter(c)) {
                        state = StatesOfParse.reciveCharacters;
                    } else if (plus || minus || div || mul || pow) {
                        throw new InvalidTokenException("operators not excepted");
                    } else if (openBracket) {
                        state = StatesOfParse.reciveOpenBracket;
                    } else if (closeBracket) {
                        throw new InvalidTokenException("closeBracket not excepted");
                    } else if (point) {
                        throw new InvalidTokenException("point not excepted");
                    } else {
                        throw new InvalidTokenException(string.charAt(position) + " not excepted");
                    }
                    break;
                case reciveOpenBracket:
                    if (openBracket) {
                        stringBuilerOfToken.append(c);
                        position += 1;
                        state = StatesOfParse.reciveOpenBracket;
                        addToken(stringBuilerOfToken, TypesOfToken.openBracket, tokens);
                    } else if (closeBracket) {
                        throw new InvalidTokenException("close bracket not excepted");
                    } else if (minus) {
                        state = StatesOfParse.reciveMinus;
                    } else if (Character.isDigit(c)) {
                        state = StatesOfParse.reciveDigital;
                    } else if (Character.isLetter(c)) {
                        state = StatesOfParse.reciveCharacters;
                    } else if (plus || minus || div || mul || pow) {
                        throw new InvalidTokenException("operator not excepted");
                    } else if (point) {
                        throw new InvalidTokenException("point not excepted");
                    } else {
                        throw new InvalidTokenException(string.charAt(position) + " not excepted");
                    }
                    break;
                case reciveCloseBracket:
                    if (closeBracket) {
                        stringBuilerOfToken.append(c);
                        position += 1;
                        state = StatesOfParse.reciveCloseBracket;
                        addToken(stringBuilerOfToken, TypesOfToken.closeBracket, tokens);
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
                        throw new InvalidTokenException("point not excepted");
                    } else {
                        throw new InvalidTokenException(string.charAt(position) + " not excepted");
                    }
                    break;
            }
        }
        if (!checkBrackets(tokens)) {
            throw new InvalidTokenException(") excepted");
        }
        string = stringBuilerOfToken.toString();
        if (!string.equals("")) {
            throw new RuntimeException("Error");
        }
        return tokens;
    }
}