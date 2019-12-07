package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        String string = in.nextLine();
        TokensReader tokensReader = new TokensReader();
        ArrayList<String> tokens = tokensReader.parseString(string);
        for (int i = 0; i < tokens.size(); i++) {
            Token t = new Token();
            t.setToken(tokens.get(i));
            System.out.println(t.getType());
        }
    }
}