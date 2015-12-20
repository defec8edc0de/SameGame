package Model;

import java.util.Random;

public class levelGenerator {
	int height;
	int width;
	int colors;
	int[][] level;
	int minStones;

	/**
	 * Konstruktur des Level Generators erzeugt ein neues zufälliges Level gemäß
	 * den eingebeben Parametern
	 * 
	 * @param width
	 * @param height
	 * @param colors
	 * @param minStones
	 */
	public levelGenerator() {
		width = 0;
		height = 0;
		colors = 0;
		minStones = 0;

	}

	public void generateNewLevel(int width, int height, int colors,
			int minStones) throws WrongParamException {
		if (checkParam(width, height, colors, minStones)) {
			this.width = width;
			this.height = height;
			this.colors = colors;
			this.minStones = minStones;
			createLevel();
			fillLevel();
		} else {
			throw new WrongParamException();
		}
	}

	/**
	 * prüft ob die gegebenen Parametern die Bedingungen für ein zufällig
	 * erzeugtes level erfüllen
	 * 
	 * @param width
	 * @param height
	 * @param colors
	 * @param minStones
	 * @return
	 */
	private boolean checkParam(int width, int height, int colors, int minStones) {
		boolean result = false;
		if (width >= 6 && width <= 30 && height >= 5 && height <= 20
				&& colors >= 2 && colors <= 5 && minStones >= 2
				&& minStones <= 5) {
			result = true;
		}
		return result;
	}

	/**
	 * Initialisiert des Level mit 0 als Value für jeden Spielstein
	 */
	private void createLevel() {
		level = new int[height][width];
		for (int row = 0; row < level.length; row++) {
			for (int col = 0; col < level[row].length; col++) {
				level[row][col] = 0;
			}
		}
	}

	/**
	 * füllt das Level mit passenden Values
	 * 
	 * @see fillLevelWithRemoveAbleBlock
	 * @see fillLevelWithinStonesPerColor
	 * @see fillRestLevel
	 */
	private void fillLevel() {
		Random colorChoice = new Random();
		int chosenColor = colorChoice.nextInt(colors) + 1;
		fillLevelWithRemoveAbleBlock(chosenColor);
		fillLevelWithMinStonesPerColor(chosenColor);
		fillRestLevel();
	}

	/**
	 * fügt einen entfernbaren Block in der letzten vorkommenden Farbe des
	 * Spielfeldes ins Spielfeld ein
	 */
	private void fillLevelWithRemoveAbleBlock(int chosenColor) {
		int destroycounter = 0;
		Random help = new Random();
		int direction = 0;
		int x = help.nextInt(width);
		int y = help.nextInt(height);
		level[y][x] = chosenColor;
		int counter = 1;
		while (counter < minStones && destroycounter < 10000) {
			destroycounter++;
			int tempX = 0;
			int tempY = 0;
			direction = help.nextInt(4);
			if (direction == 0) {
				tempX = x;
				tempY = y - 1;
			}
			if (direction == 1) {
				tempX = x + 1;
				tempY = y;
			}
			if (direction == 2) {
				tempX = x;
				tempY = y + 1;
			}
			if (direction == 3) {
				tempX = x - 1;
				tempY = y;
			}
			if (tempX < width && tempX > 0 && tempY < height && tempY > 0) {
				x = tempX;
				y = tempY;
				if (level[y][x] == 0) {
					level[y][x] = chosenColor;
					counter = counter + 1;
				}
			}
		}
		if (destroycounter == 10000) {
			createLevel();
			fillLevel();
		}
	}

	/**
	 * füllt das Spielfeld mit der MIndestanzahl von Spielsteinen von jeder
	 * Farbe ausgenommen der Farbe die auf jeden Fall einen entfernbaren Block
	 * erhalten hat (chosenColor)
	 * 
	 * @param int chosenColor (Farbe die zuvor einen entfernbaren Block erstellt
	 *        bekommen hat)
	 */
	private void fillLevelWithMinStonesPerColor(int chosenColor) {
		int destroycounter = 0;
		for (int i = 1; i <= colors; i++) {
			if (destroycounter == 100000) {
				break;
			} else

			{
				if (i != chosenColor) {
					int counter = 0;
					int x = 0;
					int y = 0;
					Random help = new Random();
					while (counter < minStones) {
						destroycounter++;
						if (destroycounter == 100000) {
							break;
						} else {
							x = help.nextInt(width);
							y = help.nextInt(height);
							if (level[y][x] == 0) {
								counter = counter + 1;
								level[y][x] = i;
							}
						}
					}
				}
			}
		}
		if (destroycounter == 100000) {
			createLevel();
			fillLevel();
		}
	}

	/**
	 * füllt das restliche level mit Steinen
	 */
	private void fillRestLevel() {
		Random value = new Random();
		for (int row = 0; row < level.length; row++) {
			for (int col = 0; col < level[row].length; col++) {
				if (level[row][col] == 0) {
					level[row][col] = (value.nextInt(colors) + 1);
				}
			}
		}
	}

	/**
	 * gibt das erzeugte Level als int[][] zurück
	 * 
	 * @return
	 */
	public int[][] getLevel() {
		return level;
	}

	/**
	 * gibt MIN_STONES des erzeugten Levels zurück
	 * 
	 * @return
	 */
	public int getMinStones() {
		return minStones;
	}

	/**
	 * erzeugt eine String Ausgabe des levels ohne Zusatzinfos
	 */
	public String toString() {
		StringBuffer result = new StringBuffer();
		for (int row = 0; row < level.length; row++) {
			for (int col = 0; col < level[row].length; col++) {
				result.append(level[row][col]);
				if (col == level[row].length - 1) {
					result.append("\n");
				}
			}
		}

		return result.toString();
	}
}