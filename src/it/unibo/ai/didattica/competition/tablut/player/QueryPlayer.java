package it.unibo.ai.didattica.competition.tablut.player;

import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.Scanner;

public class QueryPlayer extends HumanPlayer{
    private Scanner sc;
    public QueryPlayer(){
        sc = new Scanner(System.in);
    }
    @Override
    public MyVector[] getNextAction(GameState state) {
        System.out.print("From Row>");
        int fRow = sc.nextInt();
        System.out.print("From Col>");
        int fCol = sc.nextInt();
        System.out.print("To Row>");
        int tRow = sc.nextInt();
        System.out.print("To Col>");
        int tCol = sc.nextInt();
        return new MyVector[]{new MyVector(fRow,fCol), new MyVector(tRow,tCol)};
    }
}//QueryPlayer
