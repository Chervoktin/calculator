package com.company;

import com.company.Token;
import com.company.TypesOfToken;

import java.util.ArrayList;

public class TokensCheck {

    public boolean checkTokens(ArrayList<Token> tokens) {
        StatesCheck state = StatesCheck.start;
        for (int i = 0; i < tokens.size(); i++) {
            switch (state) {
                case start:
                case reciveOpenBracket:
                    if (tokens.get(i).getType() == TypesOfToken.number)
                        state = StatesCheck.reciveNumber;
                    else if (tokens.get(i).getType() == TypesOfToken.operator)
                        state = StatesCheck.reciveOperators;
                    else if (tokens.get(i).getType() == TypesOfToken.function)
                        state = StatesCheck.reciveNameOfFunctions;
                    else if (tokens.get(i).getType() == TypesOfToken.varible)
                        state = StatesCheck.reciveVarible;
                    else if (tokens.get(i).getType() == TypesOfToken.openBracket)
                        state = StatesCheck.reciveOpenBracket;
                    else if (tokens.get(i).getType() == TypesOfToken.closeBracket)
                        return false;
                    break;

                case reciveNumber:
                case reciveCloseBracket:
                    if (tokens.get(i).getType() == TypesOfToken.number)
                        return false;
                    else if (tokens.get(i).getType() == TypesOfToken.operator)
                        state = StatesCheck.reciveOperators;
                    else if (tokens.get(i).getType() == TypesOfToken.function)
                        return false;
                    else if (tokens.get(i).getType() == TypesOfToken.varible)
                        state = StatesCheck.reciveVarible;
                    else if (tokens.get(i).getType() == TypesOfToken.openBracket)
                        return false;
                    else if (tokens.get(i).getType() == TypesOfToken.closeBracket)
                        state = StatesCheck.reciveCloseBracket;
                    break;

                case reciveOperators:
                    if (tokens.get(i).getType() == TypesOfToken.number)
                        state = StatesCheck.reciveNumber;
                    else if (tokens.get(i).getType() == TypesOfToken.operator)
                        return false;
                    else if (tokens.get(i).getType() == TypesOfToken.function)
                        state = StatesCheck.reciveNameOfFunctions;
                    else if (tokens.get(i).getType() == TypesOfToken.varible)
                        state = StatesCheck.reciveVarible;
                    else if (tokens.get(i).getType() == TypesOfToken.openBracket)
                        state = StatesCheck.reciveOpenBracket;
                    else if (tokens.get(i).getType() == TypesOfToken.closeBracket)
                        return false;
                    break;
                case reciveNameOfFunctions:
                    if (tokens.get(i).getType() == TypesOfToken.number)
                        return false;
                    else if (tokens.get(i).getType() == TypesOfToken.operator)
                        return false;
                    else if (tokens.get(i).getType() == TypesOfToken.function)
                        return false;
                    else if (tokens.get(i).getType() == TypesOfToken.varible)
                        return false;
                    else if (tokens.get(i).getType() == TypesOfToken.openBracket)
                        state = StatesCheck.reciveOpenBracket;
                    else if (tokens.get(i).getType() == TypesOfToken.closeBracket)
                        return false;
                    break;
                case reciveVarible:
                    if (tokens.get(i).getType() == TypesOfToken.number)
                        state = StatesCheck.reciveNumber;
                    else if (tokens.get(i).getType() == TypesOfToken.operator)
                        state = StatesCheck.reciveOperators;
                    else if (tokens.get(i).getType() == TypesOfToken.function)
                        return false;
                    else if (tokens.get(i).getType() == TypesOfToken.varible)
                        return false;
                    else if (tokens.get(i).getType() == TypesOfToken.openBracket)
                        return false;
                    else if (tokens.get(i).getType() == TypesOfToken.closeBracket)
                        state = StatesCheck.reciveCloseBracket;
                    break;
            }
        }
        return true;
    }

}