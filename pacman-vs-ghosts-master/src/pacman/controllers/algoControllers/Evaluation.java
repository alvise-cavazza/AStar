/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.controllers.algoControllers;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;


// Consists of Two Methods 
// 1) EvaluateGameState() : 
// 2) GetBestMove() :

public class Evaluation {
    
    // if true Pac-Man goes for level completion than high score
	//private static final boolean COMPLETE_LEVEL = true; 
	private static final int MIN_GHOST_DISTANCE = 20;
	//private static final int MIN_EDIBLE_GHOST_DISTANCE = 100;
        
        //Defines the depth of the tree created. Should be balanced since deeper trees increases the execution time and 
        // shallow trees reduces the best probability of the best move really being the best move
	public static final int DEPTH = 8;  
  
	
	
	public static int evaluateGameState(Game gameState) {
		int pacmanNode = gameState.getPacmanCurrentNodeIndex();
		
		int distanceFromGhost = 0;
		
		int shortestEdibleGhostDistance = Integer.MAX_VALUE, shortestGhostDistance = Integer.MAX_VALUE,secondShortestGhostDistance = Integer.MAX_VALUE ;
		
	/*	for (GHOST ghost : GHOST.values()) {
			
			//prendo la distanza tra me e ogni ghost
			int distance = gameState.getShortestPathDistance(pacmanNode,
					gameState.getGhostCurrentNodeIndex(ghost));
			
			//se posso mangiare un ghost allora la piú piccola distanza equivale a quella dal fantasma
			if (gameState.isGhostEdible(ghost)) {
				if (distance < shortestEdibleGhostDistance) {
					shortestEdibleGhostDistance = distance;
				}
			} else {
				if (distance < shortestGhostDistance) {
					secondShortestGhostDistance = shortestGhostDistance;
					shortestGhostDistance = distance;
				}
			}
		}*/
		
		if (shortestGhostDistance != Integer.MAX_VALUE && shortestGhostDistance != -1
				&& shortestGhostDistance < MIN_GHOST_DISTANCE) {
			if (secondShortestGhostDistance != Integer.MAX_VALUE && secondShortestGhostDistance != -1
					&& secondShortestGhostDistance < MIN_GHOST_DISTANCE) {


				int avgGhostDistance = (shortestGhostDistance + secondShortestGhostDistance) / 2;
				distanceFromGhost += avgGhostDistance * 10000;
			} else {
				// increase heuristic the farther pacman is from the nearest ghost
				distanceFromGhost += shortestGhostDistance * 10000;
			}
		} else {

                    // this prevents pacman from staying near MIN_GHOST_DISTANCE
			distanceFromGhost += (MIN_GHOST_DISTANCE + 10) * 10000;
		}
                
                //Goes towards edible ghost for points if COMPLETE_LEVEL is set False else it goes away
		/*if (!COMPLETE_LEVEL) {
			if (shortestEdibleGhostDistance != Integer.MAX_VALUE && shortestEdibleGhostDistance != -1
					&& shortestEdibleGhostDistance < MIN_EDIBLE_GHOST_DISTANCE) {
				// multiplier needs to be high
				distanceFromGhost += (MIN_EDIBLE_GHOST_DISTANCE - shortestEdibleGhostDistance) * 130;
			}
		}*/
		
                //aggiorna sempre gli indici delle pillole
		int[] activePillIndices = gameState.getActivePillsIndices();
		int[] activePowerPillIndices = gameState.getActivePowerPillsIndices();
		int[] pillIndices = new int[activePillIndices.length + activePowerPillIndices.length];
		System.arraycopy(activePillIndices, 0, pillIndices, 0, activePillIndices.length);
		System.arraycopy(activePowerPillIndices, 0, pillIndices, activePillIndices.length, activePowerPillIndices.length);
		
		int shortestPillDistance =  gameState.getShortestPathDistance(pacmanNode,
				gameState.getClosestNodeIndexFromNodeIndex(pacmanNode, pillIndices, DM.PATH));
		
		return distanceFromGhost + gameState.getScore() * 100 + gameState.getPacmanNumberOfLivesRemaining() * 10000000 + (200 - shortestPillDistance);
		//return distanceFromGhost + gameState.getScore() * 5 + (4-gameState.getNumberOfActivePowerPills())*800 + gameState.getPacmanNumberOfLivesRemaining() * 10000 + (200 - shortestPillDistance);
		//return distanceFromGhost + (4-gameState.getNumberOfActivePowerPills())*800 + gameState.getPacmanNumberOfLivesRemaining() * 10000;
		
	
	/*	 		int shortestPillDistance =  gameState.getShortestPathDistance(pacmanNode,
				gameState.getClosestNodeIndexFromNodeIndex(pacmanNode, activePillIndices, DM.PATH));
		
		int shortestPillPowerDistance =  gameState.getShortestPathDistance(pacmanNode,
				gameState.getClosestNodeIndexFromNodeIndex(pacmanNode, activePowerPillIndices, DM.PATH));
		
		return distanceFromGhost + (4-gameState.getNumberOfActivePowerPills())*80000 + (200 - shortestPillDistance ) + gameState.getScore() * 100 + gameState.getPacmanNumberOfLivesRemaining() * 1000000000;
		
		*/
	
	}
	
        
	/*public static MOVE getBestMove(int leftValue, int rightValue, int upValue, int downValue) {
		
		MOVE bestMove = MOVE.NEUTRAL;
		int bestValue = Integer.MIN_VALUE;
		if (leftValue != Integer.MIN_VALUE && leftValue > bestValue) {
			bestMove = MOVE.LEFT;
			bestValue = leftValue;
		}
		if (rightValue != Integer.MIN_VALUE && rightValue > bestValue) {
			bestMove = MOVE.RIGHT;
			bestValue = rightValue;
		}
		if (upValue != Integer.MIN_VALUE && upValue > bestValue) {
			bestMove = MOVE.UP;
			bestValue = upValue;
		}
		if (downValue != Integer.MIN_VALUE && downValue > bestValue) {
			bestMove = MOVE.DOWN;
			bestValue = downValue;
		}
		
		return bestMove;
	}*/
}