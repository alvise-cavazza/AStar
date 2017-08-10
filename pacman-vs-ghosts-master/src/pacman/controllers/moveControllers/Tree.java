/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.controllers.moveControllers;
import java.util.ArrayList;
import java.util.EnumMap;

import pacman.controllers.algoControllers.Evaluation;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
/**
 *
 * @author Marmik
 */

// creates a tree for easy BFS/DFS/Iterative Deepening traversals
public class Tree {
	

	private Node headNode;
	
	public Tree(int depth, Game game, EnumMap<GHOST, MOVE> ghostMoves) {
		headNode = new Node();
		
		ArrayList<Node> currentDepthNodes = new ArrayList<Node>();
		ArrayList<Node> nextDepthNodes = new ArrayList<Node>();
		currentDepthNodes.add(headNode);
		
		Game copy, copy1, copy2, copy3 = game.copy();
		int val, val1,val2,val3=0;
		
		
		for (int i = 0; i < depth; i++) {
			for (Node node : currentDepthNodes) {
				copy = game.copy();
				copy1 = game.copy();
				copy2 = game.copy();
				copy3 = game.copy();
				
				Node left = new Node(MOVE.LEFT, node);
				copy.advanceGame(MOVE.LEFT, ghostMoves);
				val=Evaluation.evaluateGameState(copy);
				
				Node right = new Node(MOVE.RIGHT, node);
				copy1.advanceGame(MOVE.RIGHT, ghostMoves);
				val1=Evaluation.evaluateGameState(copy1);
				
				Node up = new Node(MOVE.UP, node);
				copy2.advanceGame(MOVE.UP, ghostMoves);
				val2=Evaluation.evaluateGameState(copy2);
				
				Node down = new Node(MOVE.DOWN, node);
				copy3.advanceGame(MOVE.DOWN, ghostMoves);
				val3=Evaluation.evaluateGameState(copy3);
				//node.setCost(Evaluation.evaluateGameState(node.getGameState()));
				
				nextDepthNodes.add(left);
				nextDepthNodes.add(right);
				nextDepthNodes.add(up);
				nextDepthNodes.add(down);
				
				ArrayList<Node> neighbors = new ArrayList<Node>(4);
				neighbors.add(left);
				neighbors.add(right);
				neighbors.add(up);
				neighbors.add(down);
				node.setNeighbors(neighbors);
			}
			
			currentDepthNodes = nextDepthNodes;
			nextDepthNodes = new ArrayList<Node>();
		}
	}
	
	
	public Node getHeadNode() {
		return headNode;
	}
}