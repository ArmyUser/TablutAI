package it.unibo.ai.didattica.competition.tablut.command;
import it.unibo.ai.didattica.competition.tablut.game.BoardManager;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.LinkedList;

public class ActionCommand implements Command
{
    private MyVector from, to;
    private LinkedList<MyVector> capturedPosition;
    private byte toMove;

    public ActionCommand(MyVector from, MyVector to, byte toMove){
        this.from = from;
        this.to = to;
        this.toMove = toMove;
    }

    public void doIt(){
        capturedPosition = BoardManager.getInstance().setPawn(from,to,toMove);
    }//doIt

    public void undoIt(){
        byte opposite;
        if( BoardManager.getInstance().kingWasCaptured() ) opposite = BoardManager.K;
        else opposite = toMove == BoardManager.W ? BoardManager.B : BoardManager.W;

        for( MyVector pos : capturedPosition )
            BoardManager.getInstance().respawnPawn(pos,opposite);

        BoardManager.getInstance().resetPawn(to, from,toMove);
    }//undoIt
}//ActionCommand
