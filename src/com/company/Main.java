package com.company;

import java.util.ArrayList;
import java.util.Scanner;

enum States {
    start,
    stop,
    reciveDigital,
    reciveOperators,
    reciveCharacters,
    reciveBrackets

}

class FinalStateMachine {
    private int position = 0;
    private StringBuilder token = null;
    private ArrayList<String> tokens = new ArrayList<>();
    private States state = States.start;
    private String string;

    public ArrayList<String> getTokens(String string) {
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
                    token = new StringBuilder();
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
                        token.append(c);
                        position += 1;
                        state = States.reciveDigital;
                    }
                    if (Character.isAlphabetic(c)) {
                        tokens.add(token.toString());
                        token = new StringBuilder();
                        state = States.reciveCharacters;
                    }

                    if (plus || minus || div || mul || pow) {
                        tokens.add(token.toString());
                        token = new StringBuilder();
                        state = States.reciveOperators;
                    }

                    if (openBracket || closeBracket) {
                        tokens.add(token.toString());
                        token = new StringBuilder();
                        state = States.reciveBrackets;
                    }
                    break;
                case reciveCharacters:
                    if (Character.isDigit(c)) {
                        tokens.add(token.toString());
                        token = new StringBuilder();
                        state = States.reciveDigital;
                    }
                    if (Character.isAlphabetic(c)) {
                        token.append(c);
                        position += 1;
                        state = States.reciveCharacters;
                    }

                    if (plus || minus || div || mul || pow) {
                        tokens.add(token.toString());
                        token = new StringBuilder();
                        state = States.reciveOperators;
                    }

                    if (openBracket || closeBracket) {
                        tokens.add(token.toString());
                        token = new StringBuilder();
                        state = States.reciveBrackets;
                    }
                    break;
                case reciveOperators:
                    if (Character.isDigit(c)) {
                        tokens.add(token.toString());
                        token = new StringBuilder();
                        state = States.reciveDigital;
                    }
                    if (Character.isAlphabetic(c)) {
                        tokens.add(token.toString());
                        token = new StringBuilder();
                        state = States.reciveCharacters;
                    }

                    if (plus || minus || div || mul || pow) {
                        token.append(c);
                        position += 1;
                        state = States.reciveOperators;
                    }


                    if (openBracket || closeBracket) {
                        tokens.add(token.toString());
                        token = new StringBuilder();
                        state = States.reciveBrackets;
                    }
                    break;
                case reciveBrackets:
                    if (Character.isDigit(c)) {
                        tokens.add(token.toString());
                        token = new StringBuilder();
                        state = States.reciveDigital;
                    }
                    if (Character.isAlphabetic(c)) {
                        tokens.add(token.toString());
                        token = new StringBuilder();
                        state = States.reciveCharacters;
                    }

                    if (plus || minus || div || mul || pow) {
                        tokens.add(token.toString());
                        token = new StringBuilder();
                        state = States.reciveOperators;
                    }


                    if (openBracket || closeBracket) {
                        token.append(c);
                        position += 1;
                        state = States.reciveBrackets;
                    }
                    break;
            }
            if (position == string.length())
                tokens.add(token.toString());
        }
        return tokens;
    }
}


public class Main {

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        String string = in.nextLine();
        FinalStateMachine fsm = new FinalStateMachine();
        ArrayList<String> tokens = fsm.getTokens(string);
        for (int i = 0; i < tokens.size(); i++)
            System.out.println(tokens.get(i));
    }

}


