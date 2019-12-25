package com.example.calculator;

import java.util.ArrayList;

enum States {
    start,
    stop,
    reciveDigital,
    reciveOperators,
    reciveCharacters,
    reciveBrackets

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

    public ArrayList<Token> parseString(String string) {
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
            switch (state) {
                case start:
                    stringBuilerOfToken = new StringBuilder();
                    if (Character.isDigit(c)) {
                        state = States.reciveDigital;
                    }
                    if (Character.isLetter(c)) {
                        state = States.reciveCharacters;
                    }

                    if (plus || minus || div || mul || pow) {
                        state = States.reciveOperators;
                    }

                    if (openBracket || closeBracket) {
                        state = States.reciveBrackets;
                    }
                    break;

                case reciveDigital:
                    if (Character.isDigit(c)) {
                        stringBuilerOfToken.append(c);
                        position += 1;
                        state = States.reciveDigital;
                        break;
                    }
                    if (Character.isLetter(c)) {
                        state = States.reciveCharacters;
                    }
                    if (plus || minus || div || mul || pow) {
                        state = States.reciveOperators;
                    }
                    if (openBracket || closeBracket) {
                        state = States.reciveBrackets;
                    }
                    addToken();
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
                    }

                    if (plus || minus || div || mul || pow) {
                        state = States.reciveOperators;
                    }

                    if (openBracket || closeBracket) {
                        state = States.reciveBrackets;
                    }
                    addToken();
                    break;

                case reciveOperators:
                    if (plus || minus || div || mul || pow) {
                        stringBuilerOfToken.append(c);
                        position += 1;
                        state = States.reciveOperators;
                        break;
                    }
                    if (Character.isDigit(c)) {
                        state = States.reciveDigital;
                    }
                    if (Character.isLetter(c)) {
                        state = States.reciveCharacters;
                    }
                    if (openBracket || closeBracket) {
                        state = States.reciveBrackets;
                    }
                    addToken();
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
                    }
            }
        }
        return tokens;
    }
}