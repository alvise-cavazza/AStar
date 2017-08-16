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

// creates a tree for the Astar search
public class Tree {

	private Node headNode;
	// not so safe but for the moment this works
	private int id = 0;
	private int prof;
	private MOVE move;

	public Tree(int depth, Game game, EnumMap<GHOST, MOVE> ghostMoves) {
		headNode = new Node();
		prof = depth;

		ArrayList<Node> currentDepthNodes = new ArrayList<Node>();
		ArrayList<Node> nextDepthNodes = new ArrayList<Node>();
		currentDepthNodes.add(headNode);

		// here a create 4 different copies of the actual game
		Game copy, copy1, copy2, copy3 = game.copy();

		Node node = headNode;

		copy = game.copy();
		copy1 = game.copy();
		copy2 = game.copy();
		copy3 = game.copy();

		// and for every direction i try to search which is the best direction to take
		Node left = new Node(MOVE.LEFT, node);
		copy.advanceGame(MOVE.LEFT, ghostMoves);
		left.setCost(Evaluation.evaluateGameState(copy));
		left.setGameState(copy);
		left.setDepth(1);
		// i need a id for recognize ever node
		left.setId(id++);
		// i put the move inside the node because at the end i need to know what move i
		// have to do
		left.setMove(MOVE.LEFT);

		Node right = new Node(MOVE.RIGHT, node);
		copy1.advanceGame(MOVE.RIGHT, ghostMoves);
		right.setCost(Evaluation.evaluateGameState(copy1));
		right.setGameState(copy1);
		right.setDepth(1);
		right.setId(id++);
		right.setMove(MOVE.RIGHT);

		Node up = new Node(MOVE.UP, node);
		copy2.advanceGame(MOVE.UP, ghostMoves);
		up.setCost(Evaluation.evaluateGameState(copy2));
		up.setGameState(copy2);
		up.setDepth(1);
		up.setId(id++);
		up.setMove(MOVE.UP);

		Node down = new Node(MOVE.DOWN, node);
		copy3.advanceGame(MOVE.DOWN, ghostMoves);
		down.setCost(Evaluation.evaluateGameState(copy3));
		down.setGameState(copy3);
		down.setDepth(1);
		down.setId(id++);
		down.setMove(MOVE.DOWN);

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

		// here i call a recursive function that helps me to spread until i reach the
		// max depth of the tree
		move = fun(neighbors, ghostMoves);

	}

	private MOVE fun(ArrayList<Node> argomento, EnumMap<GHOST, MOVE> ghostMoves) {
		int max = 0;
		int foundid = 0;
		Node node = new Node();

		// i find the node to expand that has not been expanded yet and that has the
		// maximum value of heuristics
		for (Node node2 : argomento) {
			if (node2.getCost() > max) {
				max = node2.getCost();
				foundid = node2.getId();
				node = node2;
			}
		}

		// if i reach the max depth, i simply return the original move of the branch in
		// which i'm inside
		if (node.getDepth() == prof)
			return node.getMove();

		Game copy = node.getGameState().copy();
		Game copy1 = node.getGameState().copy();
		Game copy2 = node.getGameState().copy();
		Game copy3 = node.getGameState().copy();

		Node left = new Node(MOVE.LEFT, node);
		copy.advanceGame(MOVE.LEFT, ghostMoves);
		left.setCost(Evaluation.evaluateGameState(copy) - 1000);
		left.setGameState(copy);
		left.setDepth(node.getDepth() + 1);
		left.setId(id++);
		// i pass the type of move from the ancestor to the child, so i know in every
		// moment in which branch i am
		left.setMove(node.getMove());

		Node right = new Node(MOVE.RIGHT, node);
		copy1.advanceGame(MOVE.RIGHT, ghostMoves);
		right.setCost(Evaluation.evaluateGameState(copy1) - 1000);
		right.setGameState(copy1);
		right.setDepth(node.getDepth() + 1);
		right.setId(id++);
		right.setMove(node.getMove());

		Node up = new Node(MOVE.UP, node);
		copy2.advanceGame(MOVE.UP, ghostMoves);
		up.setCost(Evaluation.evaluateGameState(copy2) - 1000);
		up.setGameState(copy2);
		up.setDepth(node.getDepth() + 1);
		up.setId(id++);
		up.setMove(node.getMove());

		Node down = new Node(MOVE.DOWN, node);
		copy3.advanceGame(MOVE.DOWN, ghostMoves);
		down.setCost(Evaluation.evaluateGameState(copy3) - 1000);
		down.setGameState(copy3);
		down.setDepth(node.getDepth() + 1);
		down.setId(id++);
		down.setMove(node.getMove());

		ArrayList<Node> neighbors = new ArrayList<Node>(4);
		neighbors.add(left);
		neighbors.add(right);
		neighbors.add(up);
		neighbors.add(down);
		node.setNeighbors(neighbors);

		// i set a new list of nodes that could be expanded at the next iteration and i
		// avoid the last node expanded
		for (Node node2 : argomento) {
			if (node2.getId() != foundid) {
				neighbors.add(node2);
			}
		}

		// i call the recursive function
		return fun(neighbors, ghostMoves);

	}

	public Node getHeadNode() {
		return headNode;
	}

	public MOVE getMove() {
		return move;
	}

	public void setMove(MOVE move) {
		this.move = move;
	}
}