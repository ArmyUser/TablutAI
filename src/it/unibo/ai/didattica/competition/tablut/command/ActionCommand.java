package it.unibo.ai.didattica.competition.tablut.command;
import it.unibo.ai.didattica.competition.tablut.game.BoardManager;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

public class ActionCommand implements Command
{
    private MyVector from, to;
    private char toMove;

    public ActionCommand(MyVector from, MyVector to, char toMove){
        this.from = from;
        this.to = to;
        this.toMove = toMove;
    }

    public void doIt(){
        BoardManager.getInstance().setPawn(from,to,toMove);
    }//doIt

    public void undoIt(){
        BoardManager.getInstance().setPawn(to, from,toMove);
    }//undoIt
}//ActionCommand
