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

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        String string = in.nextLine();
        States state = States.start;
        char c;
        int position = 0;
        StringBuilder token = null;
        ArrayList<String> tokens = new ArrayList<>();
        while ((position < string.length()) && state != States.stop) {
            c = string.charAt(position);

            boolean plus = (c == '+');
            boolean minus = (c == '-');
            boolean div = (c == '/');
            boolean mul = (c == '*');
            boolean pow = (c == '^');
            boolean openBacket = (c == '(');
            boolean closeBacket = (c == ')');
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

                    if (openBacket || closeBacket) {
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

                    if (openBacket || closeBacket) {
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

                    if (openBacket || closeBacket) {
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


                    if (openBacket || closeBacket) {
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


                    if (openBacket || closeBacket) {
                        token.append(c);
                        position += 1;
                        state = States.reciveBrackets;
                    }
                    break;
            }
            if (position == string.length())
                tokens.add(token.toString());
        }
        for (int i = 0; i < tokens.size(); i++)
            System.out.println(tokens.get(i));
    }

}


