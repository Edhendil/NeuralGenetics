package pl.krug.game.tron;

import java.awt.Point;
import java.util.Arrays;

import pl.krug.game.NeuralPlayer;

/**
 * Basic game, 2 players, even distance from starting positions. Result based on
 * who is the winner Both players can lose this game if they crash head on or
 * touch the wall at the same time
 * 
 * @author edhendil
 * 
 */
public class TronGame {

	// world state constants
	private static final int EMPTY = 1; // albo czy to rozroznienie jest
										// potrzebne?
	private static final int WALL = 2;

	// world parameters constants
	private static final int ROWSTOEDGE = 4;
	private static final int COLSTOEDGE = 2;
	// has to be an even integer
	private static final int INITDISTANCE = 6;

	private NeuralPlayer _leftPlayer = null;
	private NeuralPlayer _rightPlayer = null;

	// helper variables
	private boolean _gameEnded = false;
	private NeuralPlayer _winner = null;

	// state of all points in the world
	// 0 - empty - can be problematic as no energy on input is provided
	// 1 - left player
	// 2 - right player
	// 3 - outer wall
	// first index is for y coordinate, second for x
	private int[][] _worldState;

	// helper dimensions
	private int _worldWidth;
	private int _worldHeight;

	// players states
	private Point _leftPlayerPosition = null;
	private Point _rightPlayerPosition = null;

	// directions players are facing
	private Point _leftPlayerDirection = null;
	private Point _rightPlayerDirection = null;

	public TronGame(NeuralPlayer leftPlayer, NeuralPlayer rightPlayer) {
		_leftPlayer = leftPlayer;
		_rightPlayer = rightPlayer;
		initializeWorld();
	}

	private void initializeWorld() {
		// prepare the world
		_worldHeight = ROWSTOEDGE * 2 + 1 + 2;
		_worldWidth = COLSTOEDGE * 2 + INITDISTANCE + 2 + 2;
		_worldState = new int[_worldHeight][_worldWidth];

		// set outer wall
		for (int i = 0; i < _worldHeight; i++) {
			for (int j = 0; j < _worldWidth; j++) {
				if (i == 0 || j == 0 || i == _worldHeight - 1
						|| j == _worldWidth - 1)
					_worldState[i][j] = WALL;
				else
					_worldState[i][j] = EMPTY;
			}
		}

		// prepare players states
		_leftPlayerPosition = new Point(COLSTOEDGE + 1, ROWSTOEDGE + 1);
		_rightPlayerPosition = new Point(COLSTOEDGE + INITDISTANCE + 2,
				ROWSTOEDGE + 1);

		_leftPlayerDirection = new Point(1, 0);
		_rightPlayerDirection = new Point(-1, 0);
	}

	/**
	 * Single round Choose one of 3 options Turn left, turn right, go straight
	 */
	public void runIteration() {
		// input as if player was always on the left side
		double[] normalInput = new double[_worldHeight * _worldWidth * 2];
		double[] reverseInput = new double[_worldHeight * _worldWidth * 2];

		int inputRowLength = _worldWidth * 2;

		for (int i = 0; i < _worldHeight; i++) {
			for (int j = 0; j < _worldWidth; j++) {
				normalInput[i * inputRowLength + j * 2] = reverseInput[(_worldHeight - 1 - i)
						* inputRowLength + (_worldWidth - 1 - j) * 2] = (_worldState[i][j] == EMPTY ? 1
						: 0);
				normalInput[i * inputRowLength + j * 2 + 1] = reverseInput[(_worldHeight - 1 - i)
						* inputRowLength + (_worldWidth - 1 - j) * 2 + 1] = 1 - normalInput[i
						* inputRowLength + j * 2];
			}
		}

		// run networks
		_leftPlayer.runNetwork(normalInput, 25);
		_rightPlayer.runNetwork(reverseInput, 25);

		// get responses
		double[] leftResp = _leftPlayer.getResponse();
		double[] rightResp = _rightPlayer.getResponse();

		// update states
		// replace current players positions with wall
		_worldState[(int) _leftPlayerPosition.getY()][(int) _leftPlayerPosition
				.getX()] = WALL;
		_worldState[(int) _rightPlayerPosition.getY()][(int) _rightPlayerPosition
		                              				.getX()] = WALL;
		// change players directions according to the response
		switch (getMaxPosition(leftResp)) {
		// go forward
		case 0:
			break;
		// go left
		case 1:
			_leftPlayerDirection.setLocation(
					(int) _leftPlayerDirection.getX() != 0 ? 0
							: -(int) _leftPlayerDirection.getY(),
					(int) _leftPlayerDirection.getY() != 0 ? 0
							: (int) _leftPlayerDirection.getX());
			break;
		// go right
		case 2:
			_leftPlayerDirection.setLocation(
					(int) _leftPlayerDirection.getX() != 0 ? 0
							: (int) _leftPlayerDirection.getY(),
					(int) _leftPlayerDirection.getY() != 0 ? 0
							: -(int) _leftPlayerDirection.getX());
			break;
		}
		// right player
		switch (getMaxPosition(rightResp)) {
		// go forward
		case 0:
			break;
		// go left
		case 1:
			_rightPlayerDirection.setLocation((int) _rightPlayerDirection
					.getX() != 0 ? 0 : -(int) _rightPlayerDirection.getY(),
					(int) _rightPlayerDirection.getY() != 0 ? 0
							: (int) _rightPlayerDirection.getX());
			break;
		// go right
		case 2:
			_rightPlayerDirection.setLocation((int) _rightPlayerDirection
					.getX() != 0 ? 0 : (int) _rightPlayerDirection.getY(),
					(int) _rightPlayerDirection.getY() != 0 ? 0
							: -(int) _rightPlayerDirection.getX());
			break;
		}
		// move players by a square
		_leftPlayerPosition.setLocation(_leftPlayerPosition.getX()
				+ _leftPlayerDirection.getX(), _leftPlayerPosition.getY()
				+ _leftPlayerDirection.getY());
		_rightPlayerPosition.setLocation(_rightPlayerPosition.getX()
				+ _rightPlayerDirection.getX(), _rightPlayerPosition.getY()
				+ _rightPlayerDirection.getY());

		// debug
		printWorldState();
		
		// check if game has ended
		checkGameEnd();
	}

	private int getMaxPosition(double[] array) {
		if (array.length == 0)
			return -1;
		double maxValue = array[0];
		int index = 0;
		for (int i = 1; i < array.length; i++) {
			if (array[i] > maxValue) {
				maxValue = array[i];
				index = i;
			}
		}
		return index;
	}
	
	private void printWorldState() {
		for (int i = 0 ; i < _worldHeight; i++) {
			for (int j = 0 ; j < _worldWidth; j++) {
				System.out.print(_worldState[i][j] == WALL ? "x" : "o");
			}
			System.out.println();
		}
		System.out.println();
	}

	private void checkGameEnd() {
		// players positions are the same? draw
		if ((int) _leftPlayerPosition.getY() == (int) _rightPlayerPosition
				.getY()
				&& (int) _leftPlayerPosition.getX() == (int) _rightPlayerPosition
						.getX()) {
			_gameEnded = true;
			// no winner
		}
		// are they on the wall?
		boolean leftLose = _worldState[(int) _leftPlayerPosition.getY()][(int) _leftPlayerPosition
				.getX()] == WALL ? true : false;
		boolean rightLose = _worldState[(int) _rightPlayerPosition.getY()][(int) _rightPlayerPosition
				.getX()] == WALL ? true : false;

		if (leftLose && rightLose) {
			// draw
			_gameEnded = true;
		} else if (leftLose) {
			// right won
			_gameEnded = true;
			_winner = _rightPlayer;
		} else if (rightLose) {
			// left won
			_gameEnded = true;
			_winner = _leftPlayer;
		}
	}

	/**
	 * Till one of players win Returns the winner, but can return null (in case
	 * of a draw)
	 */
	public NeuralPlayer runGame() {
		while (!isGameEnded()) {
			runIteration();
		}
		return _winner;
	}

	private boolean isGameEnded() {
		return _gameEnded;
	}

}
