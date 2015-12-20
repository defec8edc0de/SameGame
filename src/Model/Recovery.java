package Model;

import java.util.Vector;

public class Recovery {
	//undo/redo functionality
	
	private Vector<brick[][]> gameBoardHistory;
	private Vector<Integer> pointHistory;
	private int currentMove;
	
	/**
	 * Initialisierung von Recovery + Speichern der 
	 * anf�nglichen Spielsituation
	 * @param board anf�ngliches Spielfeld
	 * @param points anf�nglicher Punktestand
	 */
	public Recovery(brick [][] board, int points){
		gameBoardHistory = new Vector<brick [][]>();
		pointHistory = new Vector<Integer>();
		currentMove = 0;
		
		gameBoardHistory.add(getCopy(board));
		pointHistory.add(points);	

	}
	
	/**
	 * Speichern der aktuellen Spielsituation, 
	 * Eliminierung der M�glichkeit von "redo"
	 * @param board aktuelles Spielfeld
	 * @param points aktueller Punktestand
	 */
	public void saveCurrent(brick [][] board, int points){
		if (pointHistory.size() > currentMove +1) {
			for (int index = currentMove +1 ; index < pointHistory.size(); index++) {
				pointHistory.removeElementAt(index);
				gameBoardHistory.removeElementAt(index);
			}
		}
		gameBoardHistory.add(getCopy(board));
		pointHistory.add(points);
		currentMove++;
		
	}
	
	/**
	 * Auswahl der nachfolgenden Spielsituation, wenn m�glich ("redo")
	 * @return true, wenn redo m�glich
	 */
	public boolean next(){
		if (pointHistory.size() > currentMove +1){
			currentMove++;
			return true;
		}
		else{
			return false;
		}
		}
	
	/**
	 * Auswahl der vorherigen Spielsituation, wenn m�glich ("undo")
	 * @return true, wenn undo m�glich
	 */
	public boolean previous(){
		if (currentMove>0){
			currentMove--;
			return true;
		}
		else{
			return false;
		}
		}
	
	/**
	 * gibt gameBoard f�r ausgew�hlten Spielzug zur�ck
	 * @return 
	 */
	public brick[][] getGameBoard(){
		return getCopy(gameBoardHistory.elementAt(currentMove));
	}
	
	/**
	 * gibt Punktestand f�r ausgew�hlten Spielzug zur�ck
	 * @return
	 */
	public int getPoints(){
		return pointHistory.elementAt(currentMove);
	}
	
	/**
	 * gibt eine echte Kopie eines brick-Array zur�ck
	 * @param board zu kopierendes Array
	 * @return Kopie des gegebenen Arrays
	 */
	private brick[][] getCopy(brick[][] board){
		brick[][] boardCopy = new brick[board.length][board[0].length];
		for (int row = 0; row<board.length; row++){
			for (int col = 0; col<board[row].length; col++){
				boardCopy[row][col] = board[row][col];
			}
		}
		return boardCopy;
	}
}
