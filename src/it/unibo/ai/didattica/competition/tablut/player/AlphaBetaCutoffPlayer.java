package it.unibo.ai.didattica.competition.tablut.player;


import it.unibo.ai.didattica.competition.tablut.algorithms.AlphaBetaCutoffAlgorithm;
import it.unibo.ai.didattica.competition.tablut.game.Game;

public class AlphaBetaCutoffPlayer extends AIPlayer{

    public AlphaBetaCutoffPlayer(Game game, int maxDepth, String player, int timeout ) {
        super( new AlphaBetaCutoffAlgorithm(game,maxDepth,player, timeout) );
    }

    /*
    public AlphaBetaCutoffPlayer(Game game, HistoryCommandHandler handler, int maxDepth, float whiteWeights,
                                 float h1Wheight, float h2Wheight, float h3Wheight, float h4Wheight,
                                 float escapeCoefficient, float bridgeCoefficient, float safetyCoefficient, char player){
        super( new AlphaBetaCutoffAlgorithm(game, handler, maxDepth, whiteWeights, h1Wheight, h2Wheight,
                h3Wheight, h4Wheight, escapeCoefficient, bridgeCoefficient, safetyCoefficient, player) );
    }
     */
}//AIPlayer
