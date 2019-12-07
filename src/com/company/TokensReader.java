package com.company;

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

    public ArrayList<Token> parseString(String string) {
        this.string = string;
        char c;
        String stringOfToken = null;
        Token token = null;
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
                    if (Character.isAlphabetic(c)) {
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
                    if (Character.isAlphabetic(c)) {
                        state = States.reciveCharacters;
                    }
                    if (plus || minus || div || mul || pow) {
                        state = States.reciveOperators;
                    }
                    if (openBracket || closeBracket) {
                        state = States.reciveBrackets;
                    }
                    stringOfToken = stringBuilerOfToken.toString();
                    token = new Token();
                    token.setToken(stringOfToken);
                    tokens.add(token);
                    stringBuilerOfToken = new StringBuilder();
                    break;

                case reciveCharacters:
                    if (Character.isAlphabetic(c)) {
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
                    stringOfToken = stringBuilerOfToken.toString();
                    token = new Token();
                    token.setToken(stringOfToken);
                    tokens.add(token);
                    stringBuilerOfToken = new StringBuilder();
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
                    if (Character.isAlphabetic(c)) {
                        state = States.reciveCharacters;
                    }
                    if (openBracket || closeBracket) {
                        state = States.reciveBrackets;
                    }
                    stringOfToken = stringBuilerOfToken.toString();
                    token = new Token();
                    token.setToken(stringOfToken);
                    tokens.add(token);
                    stringBuilerOfToken = new StringBuilder();
                    break;

                case reciveBrackets:
                    if (openBracket || closeBracket) {
                        stringBuilerOfToken.append(c);
                        position += 1;
                        state = States.reciveBrackets;
                        break;
                    }
                    if (Character.isDigit(c)) {
                        state = States.reciveDigital;
                    }
                    if (Character.isAlphabetic(c)) {
                        state = States.reciveCharacters;
                    }
                    if (plus || minus || div || mul || pow) {
                        state = States.reciveOperators;
                    }
                    stringOfToken = stringBuilerOfToken.toString();
                    token = new Token();
                    token.setToken(stringOfToken);
                    tokens.add(token);
                    stringBuilerOfToken = new StringBuilder();
                    break;
            }
            if (position == string.length()) {
                stringOfToken = stringBuilerOfToken.toString();
                token = new Token();
                token.setToken(stringOfToken);
                tokens.add(token);
            }
        }
        return tokens;
    }
}