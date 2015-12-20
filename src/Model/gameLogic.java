package Model;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import javax.swing.Timer;

public class gameLogic {
	//Konstanten
	private final int TARGET_TIME;
	private final int MIN_STONES;
	private final int NUMBER_OF_STONES;
	//aktuelle Spielfeld
	private brick[][] gameBoard;
	//Zustände des Spielfeldes
	private boolean finished;
	private boolean targetTimePassed;
	private int points;
	private int remainingStones;
	private double timeInSeconds;
	//Datenspeicher für Redo/Undo
	private Recovery recovery;
	//eventManager um die Listener über Änderungen am gameBoard zu informieren
	private ArrayEventGenerator eventManager;
	// Variablen benötigt für die Animation
	private int actualRow = 0;
	private int actualCol = 0;
	private Timer AnimationTimer = null;
	
	
	




	/**
	 * Konstruktor des gameLogic Objekts
	 * 
	 * @param newBoard
	 */
	public gameLogic(int[][] newBoard, int targetTime, int minStones,
			int points, ArrayEventGenerator manager) {
		gameBoard = new brick[newBoard.length][newBoard[0].length];
		finished = false;
		this.points = points;
		targetTimePassed = false;
		NUMBER_OF_STONES = countStones(newBoard);
		remainingStones = NUMBER_OF_STONES;

		// -1 heißt keine Angabe in der Level Datei vorhanden
		if (targetTime == -1) {
			TARGET_TIME = NUMBER_OF_STONES;
		} else {
			TARGET_TIME = targetTime;
		}
		// -1 heißt keine Angabe in der Level Datei vorhanden
		if (minStones == -1) {
			MIN_STONES = 2;
		} else {
			MIN_STONES = minStones;
		}
		for (int row = 0; row < newBoard.length; row++) {
			for (int col = 0; col < gameBoard[row].length; col++) {
				// Falls Nullen in der Datei stehen für freie Felder
				if (newBoard[row][col] == 0) {
					gameBoard[row][col] = null;
				} else {
					gameBoard[row][col] = new brick(newBoard[row][col]);
				}
			}
		}

		updateGameBoard(false);
		updateEqualNeighbors();
		isFinished();
		recovery = new Recovery(gameBoard, points);
		eventManager = manager;
		initializeAnimationTimer();

	}

	/**
	 * zählt die Anzahl von Spielsteinen im Spielfeld, welches dem Konstruktor
	 * übergeben wurde
	 * 
	 * @param board
	 * @return Int
	 */
	private int countStones(int[][] board) {
		int result = 0;
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				if (board[row][col] != 0) {
					result = result + 1;
				}
			}
		}
		return result;
	}

	// ==================================================================
	// Getter, toString Methode

	/**
	 * gibt das gameBoard zurück
	 * 
	 * @return brick[][]
	 */
	public brick[][] getGameBoard() {
		return gameBoard;
	}

	/**
	 * gibt die Min_Stones zurück die zum entfernen eines Steines benötigt
	 * werden
	 * 
	 * @return
	 */
	public int getMIN_STONES() {
		return MIN_STONES;
	}

	/**
	 * gibt den Wert des finsihed Attributs zurück
	 * 
	 * @return boolean
	 */
	public boolean getFinished() {
		return finished;
	}

	/**
	 * Getter des Points Attribut
	 * 
	 * @return
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * gibt die Zeit zurück in der das Spiel beendet werden sollte
	 * 
	 * @return
	 */
	public int getTARGET_TIME() {
		return TARGET_TIME;
	}

	/**
	 * erzeugt die String ausgabe eine gameLogic Objekts ohne Extrainfos
	 * 
	 * @return String
	 */
	public String toString() {
		StringBuffer result = new StringBuffer();
		for (int row = 0; row < gameBoard.length; row++) {
			for (int col = 0; col < gameBoard[row].length; col++) {
				if (gameBoard[row][col] == null) {
					result.append("0");
				} else {
					result.append(gameBoard[row][col].getValue());
				}
				if ((col == gameBoard[row].length - 1)
						&& (row != gameBoard.length - 1)) {
					result.append("\n");
				}
			}
		}
		return result.toString();
	}

	// ===================================================================
	// Methoden zum bearbeiten des Arrays von außerhalb der GameLogic
	// (löschen,redo/undo)

	/**
	 * entfernt den Spielstein an der durch die gegebenen Stelle aus dem
	 * Spielfeld und alle gleichfarbigen Nachbarn die direkt mit ihm verbunden
	 * sind oder über einen seiner Nachbarn, so lange die Anzahl der
	 * angrenzenden Steine >= minStones
	 * 
	 * @param row
	 *            Int
	 * @param col
	 *            Int
	 * @return Boolean (true falls der Stein entfernt werden konnte false falls
	 *         nicht genug Steine angrenzten)
	 */
	public boolean deleteBrick(int row, int col, double time,
			boolean withAnimation) {
		boolean result = false;

		timeInSeconds = time;
		if (row >= 0 && row < gameBoard.length && col >= 0
				&& col < gameBoard[row].length && gameBoard[row][col] != null
				&& gameBoard[row][col].getEqualNeighbors().size() >= MIN_STONES
				&& (!finished)) {
			result = true;

			// Löschen der Steine
			int removedStones = (gameBoard[row][col].getEqualNeighbors())
					.size();
			Iterator<Point> equalBricks = gameBoard[row][col]
					.getEqualNeighbors().iterator();
			// löscht gleichfarbige Elemente
			while (equalBricks.hasNext()) {
				Point position = equalBricks.next();
				gameBoard[position.y][position.x] = null;
			}
			points = points
					+ calculatePointsForDeletedBricks(removedStones,
							targetTimePassed);
			remainingStones = remainingStones - removedStones;
			updateGameBoard(withAnimation);
			if (!withAnimation) {
				updateEqualNeighbors();
				isFinished();
				if (finished) {

					points = points
							+ calculatePointsGameFinished(remainingStones,
									TARGET_TIME - timeInSeconds,
									NUMBER_OF_STONES);
				}

				// Speichern für undo
				recovery.saveCurrent(gameBoard, points);
				// Befehl zum Zeichnen/updaten das gameBoards

				eventManager.arrayGotChanged();
			}

		}
		return result;
	}

	/**
	 * Wiederherstellen der Spielsituation vor dem letzten Zug
	 * @return true, wenn undo möglich
	 */
	public boolean undo() {
		if (recovery.previous() && (!finished)) {
			gameBoard = recovery.getGameBoard();
			points = recovery.getPoints();
			eventManager.arrayGotChanged();
			return true;
		} else{
			return false;
		}
	}

	/**
	 * Wiedherstellen der Spielsituation nach einem Undo
	 * @return true, wenn redo möglich
	 */
	public boolean redo() {
		if (recovery.next() && (!finished)) {
			gameBoard = recovery.getGameBoard();
			points = recovery.getPoints();
			eventManager.arrayGotChanged();
			return true;
		} else{
			return false;
		}
	}

	// ==============================================================
	// Methoden zum updaten des Arrays nach dem Löschen eines Steines und dem
	// Initialisieren

	/**
	 * Verändert das Spielfeld so, dass alle Steine unter denen null als Eintrag
	 * vorkommt herunterfallen und leere Spalten geschlossen werden
	 * 
	 * @Param Boolean with ANimation (entscheidet ob das updaten des Feldes
	 *        animiert wird)
	 */
	private void updateGameBoard(boolean withAnimation) {
		if (withAnimation) {
			actualRow = 0;
			actualCol = gameBoard[0].length - 1;
			AnimationTimer.start();
		} else {
			falldown(withAnimation);
			moveUp(withAnimation);
		}

	}

	private void initializeAnimationTimer() {

		AnimationTimer = new Timer(1, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AnimationTimer.stop();
				if (actualRow < gameBoard.length) {
					falldown(true);
					AnimationTimer.start();

				} else {
					if (actualCol >= 0) {
						moveUp(true);
						AnimationTimer.start();
					} else {
						updateEqualNeighbors();
						isFinished();
						if (finished) {

							points = points
									+ calculatePointsGameFinished(
											remainingStones, TARGET_TIME
													- timeInSeconds,
											NUMBER_OF_STONES);
						}

						// Speichern für undo
						recovery.saveCurrent(gameBoard, points);
						// Befehl zum Zeichnen/updaten das gameBoards

						eventManager.arrayGotChanged();
					}

				}
			}
		});
		AnimationTimer.setRepeats(true);

	}

	/**
	 * berechnet das herunterfallen des Arrays
	 */
	private void falldown(boolean withAnimation) {
		if (withAnimation) {
			boolean redraw = false;
			for (int col = 0; col < gameBoard[0].length; col++) {
				if (gameBoard[actualRow][col] == null) {
					moveColOneRowDown(actualRow, col);
					redraw = true;
				}

			}
			if (redraw) {
				eventManager.arrayGotChangedAnimation();
			}
			actualRow++;

		}
		if (!withAnimation) {
			for (int row = 0; row < gameBoard.length; row++) {
				for (int col = 0; col < gameBoard[0].length; col++) {
					if (gameBoard[row][col] == null) {
						moveColOneRowDown(row, col);

					}
				}
			}

		}
	}

	/**
	 * verschiebt den Teil des Arrays oberhalb der EIngabezeile in der gegebenen
	 * Spalte um eine Zeile nach unten
	 * 
	 * @param row
	 *            Int
	 * @param col
	 *            Int
	 */
	private void moveColOneRowDown(int row, int col) {
		for (int actRow = row - 1; actRow >= 0; actRow--) {

			gameBoard[row][col] = gameBoard[actRow][col];
			row--;
		}
		gameBoard[0][col] = null;
	}

	private void moveUp(boolean withAnimation) {
		int lastRow = gameBoard.length - 1;
		if (!withAnimation) {
			for (int col = gameBoard[lastRow].length - 1; col >= 0; col--) {
				if (gameBoard[lastRow][col] == null) {
					moveArrayOneColLeft(lastRow, col);
				}
			}
		}
		if (withAnimation) {
			if (gameBoard[lastRow][actualCol] == null) {
				moveArrayOneColLeft(lastRow, actualCol);
				eventManager.arrayGotChangedAnimation();
			}
			actualCol--;
		}

	}

	/**
	 * verschiebt das Array um eine Spalte nach links beginnend an der Stelle
	 * col(Eingabe)+1
	 * 
	 * @param lastRow
	 *            Int
	 * @param col
	 *            Int
	 */
	private void moveArrayOneColLeft(int lastRow, int col) {
		for (int row = 0; row < gameBoard.length; row++) {
			for (int actCol = col + 1; actCol < gameBoard[row].length; actCol++) {
				gameBoard[row][actCol - 1] = gameBoard[row][actCol];
			}
		}
		int lastCol = gameBoard[lastRow].length - 1;
		for (int row = 0; row < gameBoard.length; row++) {
			gameBoard[row][lastCol] = null;
		}
	}

	/**
	 * Überprüft für jede Position des Spielfelds, welche Steine in der
	 * jeweiligen Nachbarschaft die gleiche Farbe haben und speichert diese
	 * Information im gameBoard
	 * 
	 */
	private void updateEqualNeighbors() {
		// enthält die schon bearbeiteten Punkte
		Vector<Point> visited = new Vector<Point>();
		for (int row = 0; row < gameBoard.length; row++) {
			for (int col = 0; col < gameBoard[row].length; col++) {

				if (gameBoard[row][col] != null
						&& !(visited.contains(new Point(col, row)))) {
					// Erstellt Vektor mit gleichfarbigen benachberten Steinen
					// inklusive sich selbst
					Vector<Point> equals = getEquals(row, col,
							gameBoard[row][col].getValue(), new Vector<Point>());
					Iterator<Point> bricks = equals.iterator();
					// fügt diesen Vektor als Attribut bei jedem Brick aus
					// equals hinzu und fügt gleichzeitig jeden Stein zu
					// "visited" hinzu
					while (bricks.hasNext()) {
						Point position = bricks.next();
						gameBoard[position.y][position.x]
								.setEqualNeighbors(equals);
						visited.add(position);

					}
				}
			}
		}
	}

	/**
	 * Erstellt einen Vektor derjenigen Felder, die die gleiche Färbung wie das
	 * Ausgangsfeld haben
	 * 
	 * @param row
	 *            Int
	 * @param col
	 *            Int
	 * @param brickType
	 *            Int
	 * @param equals
	 *            Vector <Point>
	 * @return Vector der farbgleichen Felder Vector <Point>
	 */
	private Vector<Point> getEquals(int row, int col, int brickType,
			Vector<Point> equals) {
		// prüft ob der Punkt innerhalb des Arrays liegt und ob er nicht schon
		// bearbeitet wurde und ob er die passende Farbe besitzt
		if (row >= 0 && row < gameBoard.length && col >= 0
				&& col < gameBoard[0].length && gameBoard[row][col] != null
				&& gameBoard[row][col].getValue() == brickType
				&& (!equals.contains(new Point(col, row)))) {
			equals.add(new Point(col, row));
			// Rekursionsaufrufe für alle Nachbarn
			equals = getEquals(row - 1, col, brickType, equals);
			equals = getEquals(row + 1, col, brickType, equals);
			equals = getEquals(row, col - 1, brickType, equals);
			equals = getEquals(row, col + 1, brickType, equals);
		}
		return equals;
	}

	/**
	 * Prüft, ob weitere Spielzüge möglich sind oder nicht
	 * 
	 * 
	 */
	private void isFinished() {
		boolean temp = true;
		if (!finished) {
			for (int row = 0; row < gameBoard.length; row++) {
				for (int col = 0; col < gameBoard[row].length; col++) {
					if (gameBoard[row][col] != null
							&& gameBoard[row][col].getEqualNeighbors().size() >= MIN_STONES) {
						temp = false;
						break;
					}
				}
			}
		}
		finished = temp;

	}

	// ===================================================================
	// Methoden zur Punkteberechnung

	/**
	 * Updated die Punkte gemäß den Regeln beim entfernen von Steinen
	 * 
	 * @param NrOfDeletedBricks
	 *            Int
	 */
	public int calculatePointsForDeletedBricks(int NrOfDeletedBricks,
			boolean timepassed) {
		double square = NrOfDeletedBricks * NrOfDeletedBricks;
		int result = 0;
		if (timepassed) {
			double temp = (square / 2);
			result = (int) (Math.floor(temp));
		} else {
			result = (int) square;
		}
		return result;
	}

	/**
	 * Berechnet die Punkte nach dem Ende des Spiels gemäß den Regeln
	 */
	public int calculatePointsGameFinished(int stonesLeft, double timeLeft,
			int NrOfStones) {
		int result = 0;
		result = (int) timeLeft;

		if (stonesLeft == 0) {
			result = result + NrOfStones;
		} else {
			result = result - stonesLeft;
		}
		return result;
	}

	// =========================================================================
	// Methoden zum erstellen eines Tipps (Suche nach einem klickbaren Stein)

	/**
	 * sucht einen entfernbaren Stein zufällig aus dem Spielfeld heraus
	 * 
	 * @return gibt einen Punkt mit den Koordinaten für einen entfernbaren Stein
	 *         zurück
	 */
	public Point getAHint() {
		Point result = null;
		if (!finished) {
			Vector<Point> removeableBricks = searchForRemoveableBricks();
			Random help = new Random();
			int choice = help.nextInt(removeableBricks.size());
			result = new Point(removeableBricks.get(choice).x, removeableBricks
					.get(choice).y);
		}
		return result;
	}

	/**
	 * erstellt einen Vector<Point> mit allen entfernabren Steinen des
	 * gameBoards zu diesem Zeitpunkt
	 * 
	 * @return Vector <Point> mit allen entfernbaren Steinen des gameBoards zu
	 *         diesem zeitpunkt
	 */
	private Vector<Point> searchForRemoveableBricks() {
		Vector<Point> result = new Vector<Point>(0, 1);

		for (int row = 0; row < gameBoard.length; row++) {
			for (int col = 0; col < gameBoard[row].length; col++) {
				if (gameBoard[row][col] != null) {
					if (gameBoard[row][col].getEqualNeighbors().size() >= MIN_STONES) {
						result.add(new Point(col, row));
					}
				}
			}
		}

		return result;
	}

}