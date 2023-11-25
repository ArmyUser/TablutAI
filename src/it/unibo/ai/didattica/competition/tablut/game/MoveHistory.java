package it.unibo.ai.didattica.competition.tablut.game;

import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.LinkedList;

public class MoveHistory {
    private static MoveHistory Instance;
    private LinkedList<MyVector[]> history;

    private MoveHistory(){
        history = new LinkedList<>();
    }

    public static MoveHistory getInstance(){
        if( Instance == null ) Instance = new MoveHistory();
        return Instance;
    }//getInstance

    public boolean loopMove(MyVector from, MyVector to){
        if( history.size() < 3 ) return false;
        return history.getFirst()[0].equals(history.get(2)[1]) && history.getFirst()[1].equals(history.get(2)[0]) &&
                history.get(1)[0].equals(to) && history.get(1)[1].equals(from);
    }//loopMove

    public void insertMove(MyVector from, MyVector to){
        if( history.size() == 3 ){
            history.removeFirst();
            history.addLast(new MyVector[]{from,to});
        }
    }//insertMove
}//MoveHistory
