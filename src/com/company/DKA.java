package com.company;

import java.util.LinkedList;
import java.util.List;

public class DKA {
    final int q0 = 0, q1 = 1, q2 = 2, q3 = 3, q4 = 4, q5 = 5, q6 = 6, q7 = 7, q8 = 8, q9=9;
    final String[][] alphabet = new String[][]{
            {"boolean", "byte", "char", "double",  "int", "float", "long", "short",}
            , { "Boolean", "Byte", "Character", "Double", "Float", "Integer", "Long", "Short"}
            , {" ","\r", "\n","\t"}
            , {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B"
            , "C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"}, {"_"}
            , {"1","2","3","4","5","6","7","8","9"}, {","}, {"["}, {"]"}, {";"}, {"\0"}};

    final int ERROR = -1;
    final int HALT=99;
    final int Q0=q0;
    final int [][] delta={
            {q1, q1 ,q0, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,HALT}
            ,{ERROR,ERROR, q2, ERROR,ERROR,ERROR,ERROR, q3, ERROR,ERROR,ERROR}
            ,{q9,q9, q2,q4,q4,ERROR,ERROR,q3,ERROR,ERROR,ERROR}
            ,{ERROR,ERROR,q3,ERROR,ERROR,ERROR,ERROR,ERROR,q2,ERROR,ERROR}
            ,{q4,q4,q7,q4,q4,q4,q8,q5,ERROR,q0,ERROR}
            ,{ERROR,ERROR,q5,ERROR,ERROR,ERROR,ERROR,ERROR,q6,q0,ERROR}
            ,{ERROR,ERROR,q6,ERROR,ERROR,ERROR,q8,q5,ERROR,q0,ERROR}
            ,{ERROR,ERROR,q7,ERROR,ERROR,ERROR,q8, ERROR,ERROR,q0,ERROR}
            ,{ERROR,ERROR,q8, q4,q4, ERROR,ERROR,ERROR,ERROR,ERROR,ERROR}
            ,{q4,q4,ERROR, q4,q4, q4,ERROR,ERROR,ERROR,ERROR,ERROR}
    };
    final int [][] actions={
            {0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0}
            ,{1,1,0,1,1,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0}
            ,{2,2,3,2,2,2,3,3,0,3,0}
            ,{0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,1,1,0,0,0,0,0,0}
            ,{2,2,0,2,2,2,0,0,0,0,0}
    };
    private List<String> stack=new LinkedList<>();
    private StringBuilder value;

    public DKA(String fromFile) {
        String str=fromFile+"\0";
        try {
            compiler(str);
            System.out.println("Ошибок не найдено!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void compiler(String str) throws Exception {
        int k=0;
        int row=1;
        int colAtRow=0;
        int state=Q0;
        int[] indexSymbol;
        String symbol;
        while (state!=HALT){
            indexSymbol=findIndex(str, k);
            if (indexSymbol[0]==-1)
                throw new Exception("Неизвестный символ! Строка: "+row+" Столбец: "+(colAtRow-1));
            //symbol=str.substring(k,k+indexSymbol[1]);
            if(delta[state][indexSymbol[0]]==ERROR)
                throw new Exception("Неожидаемый символ! Строка: "+row+" Столбец: "+(colAtRow-1));
            if(delta[state][indexSymbol[0]]==HALT)
                break;
            if(actions[state][indexSymbol[0]]!=0){
                doAction(state,indexSymbol[0],indexSymbol[1]);
            }
            if (str.charAt(k)=='\r'){
                row++;
                colAtRow=0;
            }
            state=delta[state][indexSymbol[0]];
            k+=alphabet[indexSymbol[0]][indexSymbol[1]].length();
            colAtRow+=alphabet[indexSymbol[0]][indexSymbol[1]].length();
        }
    }
    private int[] findIndex(String str, int k){
        for (int i = 0; i < alphabet.length; i++) {
            for (int j = 0; j < alphabet[i].length; j++) {
                if (str.startsWith(alphabet[i][j], k))
                    return new int[] {i,j};
            }
        }
        return new int[]{-1,-1,-1};
    }


    private void doAction(int state, int alphCol, int alphSym) throws Exception {
        switch (actions[state][alphCol]) {
            case 1:
                one(alphCol, alphSym);
                break;
            case 2:
                two(alphCol,alphSym);
                break;
            case 3:
                if(three())
                    throw new Exception("Имя переменной уже используется!"); //Строка: "+row+" Столбец: "+(colAtRow-1));
                break;
        }
    }

    private void one(int i, int j){
        value=new StringBuilder(alphabet[i][j]);
    }

    private void two(int i, int j){
        value.append(alphabet[i][j]);
    }

    private boolean three(){
        boolean isUsed=false;
        for (String el:stack) {
            if (el.equals(value.toString())){
                isUsed=true;
                return isUsed;
            }
        }
        stack.add(value.toString());
        return isUsed;
    }
}
