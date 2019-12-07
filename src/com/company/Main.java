package com.company;

import java.util.ArrayList;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        String string = in.nextLine();
        TokensReader tokensReader = new TokensReader();
        ArrayList<Token> tokens = tokensReader.parseString(string);
        TokensCheck tc = new TokensCheck();
        System.out.println(tc.checkTokens(tokens));
        for (int i = 0; i < tokens.size(); i++) {
            System.out.println(tokens.get(i).getType());
        }
    }
}