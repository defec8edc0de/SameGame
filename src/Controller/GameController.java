package Controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.border.Border;

import translator.TranslatableGUIElement;
import translator.Translator;
import Model.ArrayChangeAnimationEvent;
import Model.ArrayChangeEvent;
import Model.ArrayEventGenerator;
import Model.ArrayEventListener;
import Model.WrongParamException;
import Model.brick;
import Model.gameLogic;
import Model.levelGenerator;
import View.AboutWindow;
import View.GameGUI;
import View.GeneratorWindow;
import View.HelpWindow;
import View.HighscoreWindow;
import View.StatusBar;
import de.tu_darmstadt.gdi1.samegame.exceptions.InternalFailureException;
import de.tu_darmstadt.gdi1.samegame.exceptions.ParameterOutOfRangeException;
import de.tu_darmstadt.gdi1.samegame.ui.GameWindow;

/**
 * GameController ( erbt von Template GameWindow ) ist die Controller- und
 * Überklasse des SameGame diese Klasse initialisiert die View und Model
 * Klassen, sowie alle anderen Relevanten Klassen und steuert deren
 * Kommunikation/Aufgabendelegation
 * 
 * @since 16.08.2010
 * @author sephir0th ( funky324@yahoo.de )
 * @version 0.9
 */

public class GameController extends GameWindow implements ArrayEventListener {

	// #########################################################
	// Attribute
	// #########################################################

	private final int GAME_WIDTH = 640;
	private final int GAME_HEIGHT = 480;

	// Switch für Tests
	private boolean JUnittest;
	// Ein Toolkit, um zB an die Bildschirmauflösung zu kommen
	private Toolkit toolkit = null;

	// Internationalisierungselemente
	protected Translator translator;
	protected TranslatableGUIElement guiBuilder;

	// Menue
	protected GameMenue menue = null;

	// Levelverarbeitung
	protected LevelHandling levelHandling = null;
	private String currentLevelPath = "src/de/tu_darmstadt/gdi1/resources/levels/defaultlevels/";
	private boolean levelStarted = false;

	// Highscore Frame zeigt den aktuellen Highscore an
	private HighscoreWindow highscoreFrame = null;

	// About-Fenster
	private AboutWindow aboutFrame = null;

	// Help-Fenster
	private HelpWindow helpFrame = null;

	// Level-Generator-Fenster
	private GeneratorWindow generatorFrame = null;

	// Level-Generator
	private levelGenerator levelGen;
	private boolean levelGenerated = false;

	// eventManagr zum verwalten der Events die von der gameLogic geworfen
	// werden
	private ArrayEventGenerator eventManager;
	// GameLogik von Model
	protected gameLogic gamelogic = null;

	// Spielfeldarray ( brick ist eine Klasse mit Value und gleichfarbigen
	// Nachbarn als PunkteVector
	private brick[][] gameBoard = null;

	// Timer für den Ablauf der Zeit
	private Timer timer = null;

	private double time;
	private double points;

	// Südliches Statusfeld zeigt verbleibende Zeit, Punktzahl und MinStones an
	private StatusBar statusBar = null;

	private static final long serialVersionUID = 1L;

	// #########################################################

	// Konstructor
	/**
	 * Konstruktor des Controllers Initialisiert alle Attribute Setzt ein
	 * Programmicon ( oben links ) Setzt die Größe und die Position des
	 * Controllers
	 * 
	 * @param test
	 *            boolean
	 * @see GameWindow
	 */
	public GameController(boolean test) {

		// im GameWindow wird das GamePanel initialisiert, bzw es wird
		// CreateGamePanel gestartet
		super("SameGame");
		JUnittest = test;
		eventManager = new ArrayEventGenerator();
		initTimer();

		// schwarze Border bei allen Elementen
		Border blackline = BorderFactory.createLineBorder(Color.black);

		// Initialisierung von Internationalisierung
		translator = new Translator("Controller/language", Locale.GERMANY);
		guiBuilder = translator.getGenerator();

		// Menü initialisieren
		menue = new GameMenue(this);
		setJMenuBar(menue);
		menue.setBorder(blackline);

		// Initialisierung der Levelverarbeitung
		levelHandling = new LevelHandling(this, JUnittest);
		eventManager.addArrayListener(this);

		// Icon in der linken oberen Ecke setzen
		ImageIcon imageIcon = new ImageIcon("ico.gif");
		setIconImage(imageIcon.getImage());

		// Position und Größe festlegen
		toolkit = Toolkit.getDefaultToolkit();
		Dimension aufloesung = toolkit.getScreenSize();

		// Highscore Fesnter
		highscoreFrame = new HighscoreWindow(translator, this, null, false);
		// AboutFenster
		aboutFrame = new AboutWindow(translator);
		// HilfeFenster
		helpFrame = new HelpWindow(translator);
		// GeneratorFenster
		generatorFrame = new GeneratorWindow(translator, this);

		levelGen = new levelGenerator();

		// suedliche Statusanzeige ( Punkte, Zeit )
		statusBar = new StatusBar(guiBuilder);
		add(statusBar, BorderLayout.SOUTH);

		// GameLogic initilisieren
		gamelogic = null;

		setSize(GAME_WIDTH, GAME_HEIGHT);

		setLocation(aufloesung.width / 2 - (GAME_WIDTH / 2), aufloesung.height
				/ 2 - (GAME_HEIGHT / 2));

	}

	// #########################################################
	// Getter und Setter
	// #########################################################

	/**
	 * Gibt den Pfad zum aktuellen Level zurück
	 * 
	 * @return String
	 */
	public String getCurrentLevelPath() {
		return currentLevelPath;
	}

	/**
	 * Setzt den aktuellen levelpfad
	 * 
	 * @param currentLevelPath
	 *            String
	 */
	public void setCurrentLevelPath(String currentLevelPath) {
		this.currentLevelPath = currentLevelPath;
	}

	/**
	 * Getter für den aktuellen StandardLevelPfad
	 * 
	 * @return String
	 */
	public String getDefaultLevelPath() {
		return levelHandling.getLevelPath();
	}

	/**
	 * Getter für den Schalter, ob ein Level gestartet wurde
	 * 
	 * @return boolean
	 */
	public boolean isLevelStarted() {
		return levelStarted;
	}

	/**
	 * Setter für den Schalter, ob ein Level gestartet wurde
	 * 
	 * @param levelStarted
	 *            boolean
	 */
	public void setLevelStarted(boolean levelStarted) {
		this.levelStarted = levelStarted;
	}

	/**
	 * Getter für das HighscoreFrame
	 * 
	 * @return HighscoreFrame
	 */
	public HighscoreWindow getHighscoreFrame() {
		return highscoreFrame;
	}

	/**
	 * Getter für den Highscore aus dem LevelHandling
	 * 
	 * @return Highscores
	 */
	public Highscores getHighscores() {
		return levelHandling.getHighscores();
	}

	/**
	 * Setzt den aktuellen Highscore in der Tabelle im Highscore Fenster und
	 * löscht vorher den Tabelleninhalt
	 */
	public void setJHighscores() {

		Highscores scores = getHighscores();
		scores.sortHighscore();

		// Vorher Tabelle clearen

		for (int nRow = 0; nRow < highscoreFrame.getjTableHighscores()
				.getRowCount(); nRow++) {
			for (int nColumn = 0; nColumn < highscoreFrame
					.getjTableHighscores().getColumnCount(); nColumn++) {
				highscoreFrame.getjTableHighscores().setValueAt("", nRow,
						nColumn);
			}
		}

		for (int i = 0; i < scores.size(); i++) {
			highscoreFrame.getjTableHighscores().setValueAt(
					scores.getEntry(i).getName(), i, 0);
			highscoreFrame.getjTableHighscores().setValueAt(
					scores.getEntry(i).getPoints(), i, 1);
			highscoreFrame.getjTableHighscores().setValueAt(
					scores.getEntry(i).getRemainingTime(), i, 2);

			SimpleDateFormat dateformated = new SimpleDateFormat("dd.MM.yyyy");

			highscoreFrame.getjTableHighscores().setValueAt(
					dateformated.format(scores.getEntry(i).getDate()), i, 3);

		}
	}

	/**
	 * Getter für die GameLogic
	 * 
	 * @return gameLogic
	 */
	public gameLogic getgameLogic() {
		return gamelogic;
	}

	/**
	 * Getter für das LevelHandling
	 * 
	 * @return LevelHandling
	 */
	public LevelHandling getLevelHandling() {
		return levelHandling;
	}

	/**
	 * Setter für die Zeit
	 * 
	 * @param time
	 *            double
	 */
	public void setTime(double time) {
		this.time = time;
	}

	/**
	 * Setter für die Punkte
	 * 
	 * @param points
	 *            double
	 */
	public void setPoints(double points) {
		this.points = points;
	}

	/**
	 * Getter für die Zeit
	 * 
	 * @return double
	 */
	public double getTime() {
		return time;
	}

	/**
	 * Getter für die Punkte
	 * 
	 * @return points
	 */
	public double getPoints() {
		return points;
	}

	/**
	 * Getter für das Spielfeldarray
	 * 
	 * @return brick[][]
	 */
	public brick[][] getGameBoard() {
		return gameBoard;
	}

	/**
	 * Setter für das Spielfeldarray
	 * 
	 * @param gameBoard
	 *            brick[][]
	 */
	public void setGameBoard(brick[][] gameBoard) {
		this.gameBoard = gameBoard;
	}

	/**
	 * Getter für den Timer
	 * 
	 * @return
	 */
	public Timer getTimer() {
		return timer;
	}

	/**
	 * Setzt die Sprache
	 * 
	 * @param targetLocale
	 *            Locale
	 */
	public void setLanguage(Locale targetLocale) {
		translator.setTranslatorLocale(targetLocale);
		Locale.setDefault(targetLocale);
		JComponent.setDefaultLocale(targetLocale);
		// Zwischenspeichern der Sichtbarkeit der Fenster
		boolean aboutVisible = aboutFrame.isVisible();
		boolean generatorVisible = generatorFrame.isVisible();
		boolean helpVisible = helpFrame.isVisible();
		boolean highscoreVisible = highscoreFrame.isVisible();
		// Zwischenspeichern der Position der Fenster
		Point aboutLocation = aboutFrame.getLocation();
		Point generatorLocation = generatorFrame.getLocation();
		Point helpLocation = helpFrame.getLocation();
		Point highscoreLocation = highscoreFrame.getLocation();
		// Zwischenspeichern der Daten der Highscoretabelle
		JTable highscore = highscoreFrame.getjTableHighscores();
		Object[][] data = new Object[highscore.getRowCount()][highscore
				.getColumnCount()];
		for (int row = 0; row < highscore.getRowCount(); row++)
			for (int col = 0; col < highscore.getColumnCount(); col++)
				data[row][col] = highscore.getValueAt(row, col);
		boolean saveButtonEnabled = highscoreFrame
				.isSaveHighscoreButtonEnabled();
		// Fenster leeren
		aboutFrame.dispose();
		generatorFrame.dispose();
		helpFrame.dispose();
		highscoreFrame.dispose();
		// Fenster neu erstellen
		aboutFrame = new AboutWindow(translator);
		generatorFrame = new GeneratorWindow(translator, this);
		helpFrame = new HelpWindow(translator);
		highscoreFrame = new HighscoreWindow(translator, this, data,
				saveButtonEnabled);
		// Positition der neuen Fenster festlegen
		aboutFrame.setLocation(aboutLocation);
		generatorFrame.setLocation(generatorLocation);
		helpFrame.setLocation(helpLocation);
		highscoreFrame.setLocation(highscoreLocation);
		// Sichtbarkeit der Fenster festlegen
		aboutFrame.setVisible(aboutVisible);
		generatorFrame.setVisible(generatorVisible);
		helpFrame.setVisible(helpVisible);
		highscoreFrame.setVisible(highscoreVisible);
	}

	// #########################################################

	// #########################################################
	// Timer Methoden
	// #########################################################

	/**
	 * Initialisiert den Timer, mit der Aufgabe, die Zeit sekündlich zu
	 * dekrementieren und entsprechend die Zeit im Label auf dem südlichen
	 * Statusfeld zu aktualisieren
	 */
	protected void initTimer() {
		timer = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setTime(time - 1);
				statusBar.setJTime(time);

			}
		});
	}

	/**
	 * Startet den Timer
	 */
	protected void startTimer() {
		timer.start();
	}

	/**
	 * Startet den Timer neu
	 */
	protected void restartTimer() {
		timer.restart();
	}

	/**
	 * Startet bzw Restartet den Timer je nach Zustands des Timers
	 */
	protected void rebootTimer() {
		if (getTimer().isRunning())
			restartTimer();
		else
			startTimer();
	}

	// #########################################################

	// #########################################################
	// Tastendruck-Methoden
	// #########################################################

	/**
	 * Wird aufgerufen wenn die Taste Links gedrückt wurde, daraufin wird im
	 * GameGUI die nötige Methode ausgeführt
	 */
	@Override
	public void keyLeftPressed() {
		if (getGamePanel() != null) {
			if (!levelStarted) {
				setLevelStarted(true);
				rebootTimer();
			}
			getGamePanel().keyLeftPressed();
		}
	}

	/* ======================================================================== */

	/**
	 * Wird aufgerufen wenn die Taste Rechts gedrückt wurde, daraufin wird im
	 * GameGUI die nötige Methode ausgeführt
	 */
	@Override
	public void keyRightPressed() {
		if (getGamePanel() != null) {
			if (!levelStarted) {
				setLevelStarted(true);
				rebootTimer();
			}
			getGamePanel().keyRightPressed();
		}
	}

	/* ======================================================================== */

	/**
	 * Wird aufgerufen wenn die Taste Hoch gedrückt wurde, daraufin wird im
	 * GameGUI die nötige Methode ausgeführt
	 */
	@Override
	public void keyUpPressed() {
		if (getGamePanel() != null) {
			if (!levelStarted) {
				setLevelStarted(true);
				rebootTimer();
			}
			getGamePanel().keyUpPressed();
		}
	}

	/* ======================================================================== */

	/**
	 * Wird aufgerufen wenn die Taste Runter gedrückt wurde, daraufin wird im
	 * GameGUI die nötige Methode ausgeführt
	 */
	@Override
	public void keyDownPressed() {
		if (getGamePanel() != null) {
			if (!levelStarted) {
				setLevelStarted(true);
				rebootTimer();
			}
			getGamePanel().keyDownPressed();
		}
	}

	/**
	 * Wird ausgeführt, wenn man auf die Space Taste drückt und ist äquivalent
	 * zum Klicken mit der Maus auf einen Button
	 */
	@Override
	public void keySpacePressed() {
		if (getGamePanel() != null) {
			getGamePanel().entitySpacePressed();
		}
	}

	/* ======================================================================== */

	/**
	 * Wird aufgerufen wenn die Taste Q gedrückt wurde, daraufin wird das
	 * Programm beendet
	 */
	@Override
	protected void keyQuitPressed() {
		System.exit(0);
	}

	/* ======================================================================== */

	/**
	 * Wird aufgerufen wenn die N Taste gedrückt wurde oder im Menü auf Neustart
	 * geklickt wurde Startet das Level neu mit den zwischengespeicherten
	 * Initialisierungsdaten aus LevelHandling
	 */
	@Override
	public void keyNewGamePressed() {
		timer.stop();
		try {
			int minSt = gamelogic.getMIN_STONES();

			// Level mit allen darin enthaltenen Daten laden
			if (levelHandling.loadLevelFromString(levelHandling
					.getLevelStringInitialState())) {

				// GameLogic Initialisieren
				if (!levelGenerated)
					initializeGameLogic(levelHandling.getBoard(), levelHandling
							.getTime(), levelHandling.getMinStones(), 0);
				else
					initializeGameLogic(levelHandling.getBoard(), levelHandling
							.getTime(), minSt, 0);

				updateStatusBar();
				if (getLevelHandling().isHighscoresAvailable())
					setJHighscores();

				levelStarted = false;
				highscoreFrame.saveHighscoreButtonSetEnabled(false);

				updateWindowSize();
				notifyLevelLoaded(levelHandling.getBoard()[0].length,
						levelHandling.getBoard().length);
				checkFinished();

			}
		} catch (ParameterOutOfRangeException e) {
			e.printStackTrace();
		} catch (InternalFailureException e) {
			e.printStackTrace();
		}
	}

	/* ======================================================================== */
	/**
	 * Wird beim drücken auf Enter oder beim klicken auf Undo im Menü aufgerufen
	 * und stellt einen Zug wiederher
	 */
	@Override
	public void keyUndoPressed() {
		if (gamelogic != null && gamelogic.undo()) {
			removeKeyListener(this);
			updateGameAfterRedoUndo();
		}

	}

	/* ======================================================================== */

	/**
	 * Wird beim drücken auf Backspace oder beim klicken auf Redo im Menü
	 * aufgerufen und macht einen Zug rückgängig
	 */
	@Override
	public void keyRedoPressed() {
		if (gamelogic != null && gamelogic.redo()) {
			removeKeyListener(this);
			updateGameAfterRedoUndo();
		}
	}

	/* ======================================================================== */

	/**
	 * Wird aufgerufen wenn man auf H drückt oder im Menü auf Tipp, und
	 * highlighted eine wegklickbare Kombination
	 */
	@Override
	protected void keyHintPressed() {
		if (gamelogic != null) {
			Point hint = gamelogic.getAHint();
			if (hint != null) {
				getGamePanel().showHint(hint);
			}
		}

	}

	// #########################################################

	// #########################################################
	// Menü-Methoden
	// #########################################################

	/**
	 * Wird beim MenüKlick Level speichern aufgerufen und speichert den
	 * aktuellen Stand des Levels in eine *.sve Datei der Dateiname wird über
	 * ein Dialog ermittelt. In der Speicherstanddatei steht an oberster Stelle
	 * der Pfad zur Leveldatei, zu welcher der Speicherstand gehört. Danach
	 * kommen die Levelinformationen, also das Board, die Zeit und die Punkte
	 * und danach kommt der String "Ende" was signalisiert, dass die
	 * Speicherdatei dort endet.
	 */
	public void saveLevelClicked() {
		// wenn das Level generiert wurde, muss erst das Level gespeichert
		// werden
		timer.stop();
		if (levelGenerated) {

			File file;
			FileWriter fw;
			BufferedWriter bw;
			String outPutFileName = "";
			StringBuffer inhalt = new StringBuffer();

			try {

				while (outPutFileName != null && outPutFileName.length() == 0) {
					outPutFileName = JOptionPane.showInputDialog(this,
							translator.translateMessage("messageSaveLevel"),
							translator
									.translateMessage("messageSaveLevelTitle"),
							JOptionPane.PLAIN_MESSAGE);

				}
				// Wenn nicht abbrechen geklickt wirde...speichere den
				// Spielstand
				if (outPutFileName != null) {
					String fileName = getDefaultLevelPath() + outPutFileName
							+ ".lvl";

					file = new File(fileName);
					if (file.exists()) {
						int value = JOptionPane
								.showConfirmDialog(
										this,
										translator
												.translateMessage("messageSaveGame"),
										translator
												.translateMessage("messageSaveGameTitle"),
										JOptionPane.YES_NO_OPTION);

						if (value == JOptionPane.YES_OPTION) {

							fw = new FileWriter(file);
							bw = new BufferedWriter(fw);

							inhalt.append(levelHandling
									.getLevelStringInitialState());
							inhalt.append("###min_stones:").append(
									gamelogic.getMIN_STONES()).append(
									"|target_time:").append(
									gamelogic.getTARGET_TIME());

							bw.write(inhalt.toString());
							bw.close();

						}
					} else {

						fw = new FileWriter(file);
						bw = new BufferedWriter(fw);

						inhalt.append(levelHandling
								.getLevelStringInitialState());
						inhalt.append("###min_stones:").append(
								gamelogic.getMIN_STONES()).append(
								"|target_time:").append(
								gamelogic.getTARGET_TIME());

						bw.write(inhalt.toString());
						bw.close();
					}

					// aktualisiert den Levelpfad zu dem neu erstellten Level
					setCurrentLevelPath(fileName);
					// setzt das generiert-flag auf false, damit beim nächsten
					// Klick
					// auf speichern, gespeichert wird.
					levelGenerated = false;

				}

			} catch (ArrayIndexOutOfBoundsException aioobe) {
				System.out.println("ArrayOutOfBounds beim saven");
			} catch (IOException ioe) {
				System.out.println("Habe gefangen: " + ioe);
			}

		} else {

			// Momentanen Zustand speichern

			File file;
			FileWriter fw;
			BufferedWriter bw;
			String outPutFileName = "";
			StringBuffer inhalt = new StringBuffer();

			try {

				while (outPutFileName != null && outPutFileName.length() == 0) {
					outPutFileName = JOptionPane
							.showInputDialog(
									this,
									translator
											.translateMessage("messageSaveGame"),
									translator
											.translateMessage("messageSaveGameTitle"),
									JOptionPane.PLAIN_MESSAGE);

				}
				if (outPutFileName != null) {
					String fileName = getDefaultLevelPath() + outPutFileName
							+ ".sve";

					file = new File(fileName);
					if (file.exists()) {
						int value = JOptionPane
								.showConfirmDialog(
										this,
										translator
												.translateMessage("messageOverwrite"),
										translator
												.translateMessage("messageOverwriteTitle"),
										JOptionPane.YES_NO_OPTION);

						if (value == JOptionPane.YES_OPTION) {

							fw = new FileWriter(file);
							bw = new BufferedWriter(fw);
							// Dateiname des aktuellen Level reinschreiben
							inhalt.append(getCurrentLevelPath()).append("\n");
							// Zeit reinschreiben
							inhalt.append(getTime()).append("\n");
							// Punktzahl reinschreiben
							inhalt.append(getPoints()).append("\n");
							// aktuelles Board reinschreiben
							inhalt.append(getLevelAsStringWithoutExtraInfo())
									.append("\n");
							// Ende signalisiert Ende der Leveldatei
							inhalt.append("Ende");

							bw.write(inhalt.toString());
							bw.close();

						}
					} else {

						fw = new FileWriter(file);
						bw = new BufferedWriter(fw);
						// Dateiname des aktuellen Level reinschreiben
						inhalt.append(getCurrentLevelPath()).append("\n");
						// Zeit reinschreiben
						inhalt.append(getTime()).append("\n");
						// Punktzahl reinschreiben
						inhalt.append(getPoints()).append("\n");
						// aktuelles Board reinschreiben
						inhalt.append(getLevelAsStringWithoutExtraInfo())
								.append("\n");
						// Ende signalisiert Ende der Leveldatei
						inhalt.append("Ende");

						bw.write(inhalt.toString());
						bw.close();

					}
				}
			} catch (ArrayIndexOutOfBoundsException aioobe) {
				System.out.println("ArrayOutOfBounds beim saven");
			} catch (IOException ioe) {
				System.out.println("Habe gefangen: " + ioe);
			}
		}
		timer.start();
	}

	/**
	 * Wird aufgerufen wenn im Menü auf Level Laden geklickt wird. Man kann
	 * dabei nur SVE Dateien Laden, also Speicherstände zu bestimmten Leveln
	 * Nach dem parsen der SVE Datei werden die Informationen eingelesen und die
	 * GameLogic wird neu initialisiert, sowie Timer neugestartet, diverse Flags
	 * auf False gesetzt und das GameGui wird über NotifyLevelLoaded angeregt
	 * zum neu erstellen des Spielfeldes
	 */
	public void loadLevelClicked() {
		timer.stop();
		String fileName = null;
		fileName = oeffnenDialog();
		if (fileName.contains(".sve") && (fileName != null) && !(fileName.length() == 0)) {
			// sucht aus dem Systemumfeld das verwendete Zeilenumbruchszeichen
			String linebreak = System.getProperty("line.separator");
			String s = null;
			StringBuffer level = new StringBuffer();
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(fileName));
				while ((s = in.readLine()) != null)
					level.append(s).append(linebreak);
				in.close();

				String[] data = level.toString().split(linebreak);
				// Wichtig die Reihenfolge der Daten in der Speicherstanddatei
				// Index0: Levelpfad
				// Index1: bisherige Zeit
				// Index2: bisherige Punkte
				// der Rest sind Leveldaten

				// prüft, ob der Speicherstand auf eine Leveldatei verweist
				if (data[0].contains(".lvl")) {

					if (levelHandling.loadLevelFromString(levelHandling
							.readLevelFileInString(data[0]))) {

						int[][] gameBoard = new int[levelHandling.getBoard().length][levelHandling
								.getBoard()[0].length];

						char[] zeile;

						for (int y = 0; y < gameBoard.length; y++) {
							zeile = data[y + 3].toCharArray();
							for (int x = 0; x < gameBoard[0].length; x++) {
								gameBoard[y][x] = (zeile[x] - '0');
							}

						}

						// GameLogic Initialisieren
						initializeGameLogic(gameBoard, (int) Double
								.parseDouble(data[1]), levelHandling
								.getMinStones(), (int) Double
								.parseDouble(data[2]));

						updateStatusBar();
						highscoreFrame.saveHighscoreButtonSetEnabled(false);
						levelStarted = false;
						levelGenerated = false;
						updateWindowSize();
						notifyLevelLoaded(levelHandling.getBoard()[0].length,
								levelHandling.getBoard().length);
						checkFinished();

					}

				}

				else
					JOptionPane
							.showMessageDialog(
									this,
									translator
											.translateMessage("messageErrorLoadLevel"),
									translator
											.translateMessage("messageErrorLoadLevelTitle"),
									JOptionPane.ERROR_MESSAGE);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParameterOutOfRangeException e) {
				e.printStackTrace();
			} catch (InternalFailureException e) {
				e.printStackTrace();
			}

		}
		else
			if ((gamelogic != null) && (fileName.length() == 0))
				restartTimer();

	}

	/**
	 * Wird ausgeführt beim Klicken auf Hilfe im Menü und macht das Hilfefenster
	 * sichtbar
	 */
	public void helpClicked() {
		helpFrame.setVisible(true);
	}

	/**
	 * Wird ausgeführt beim Klicken auf Über im Menü und macht das Überfenster
	 * sichtbar
	 */
	public void aboutClicked() {
		aboutFrame.setVisible(true);
	}

	/**
	 * Wird ausgeführt beim Klicken auf Level Generieren im Menü und macht das
	 * GenerierungsFenster sichtbar
	 */
	public void generateLevelClicked() {
		generatorFrame.setVisible(true);

	}

	/**
	 * Funktion welche einen SkinPath erhält und entsprechend das Skin ändert
	 * beim klicken auf das jeweilige Skin im Menü dabei wird zuerst im GameGUI
	 * die BilderMap-Inhalte gelöscht, dann wird der Pfad zum Skin im GameGUI
	 * aktualisiert und danach werden die Bilder neu eingelesen und die
	 * Fenstergröße des Controllers angepasst Falls der Fall Eintritt, dass vor
	 * Spielbeginn das Skin geändert wird, wird die Größe an das neue
	 * Hintergrundbild angepasst
	 * 
	 * @param skinPath
	 */
	public void changeSkin(String skinPath) {
		if (gameBoard != null) {
			getGamePanel().clearImages();
			getGamePanel().setSkinPath(skinPath);
			getGamePanel().initializeImages();
			try {
				updateWindowSize();
				getGamePanel().redraw();
			} catch (InternalFailureException e) {
				e.printStackTrace();
			}
		} else {

			getGamePanel().clearImages();
			getGamePanel().setSkinPath(skinPath);
			getGamePanel().initializeImages();
			setSize(getGamePanel().getBackgroundIcon().getIconWidth(),
					getGamePanel().getBackgroundIcon().getIconHeight());

		}
	}

	/**
	 * Die Standardmethode zum laden eines Levels aus einer Datei, dabei wird
	 * der Pfad zur LevelDatei übergeben und im Anschluss wird das Level über
	 * das LevelHandling eingelesen, validiert,der aktuelle LevelPfad wird
	 * gesetzt, die GameLogic wird initialisiert, die Größe des Controllers wird
	 * ans Level angepasst, Flags werden gesetzt, Timer gestartet und das
	 * GameGUI wird zum zeichnen angeregt.
	 * 
	 * @param level
	 */
	public void loadAndStartLevel(String level) {
		timer.stop();
		setCurrentLevelPath(level);

		try {
			// Level mit allen darin enthaltenen Daten laden und auf validität
			// prüfen
			if (levelHandling.loadLevelFromString(levelHandling
					.readLevelFileInString(level))) {

				// GameLogic Initialisieren
				initializeGameLogic(levelHandling.getBoard(), levelHandling
						.getTime(), levelHandling.getMinStones(), 0);

				updateStatusBar();
				if (getLevelHandling().isHighscoresAvailable())
					setJHighscores();

				updateWindowSize();
				levelStarted = false;
				highscoreFrame.saveHighscoreButtonSetEnabled(false);
				levelGenerated = false;
				notifyLevelLoaded(levelHandling.getBoard()[0].length,
						levelHandling.getBoard().length);

				checkFinished();

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParameterOutOfRangeException e) {
			e.printStackTrace();
		} catch (InternalFailureException e) {
			e.printStackTrace();
		}

	}

	// #########################################################

	// #########################################################
	// Hilfsmethoden
	// #########################################################

	/**
	 * Konstruiert das GameGUI, also das Spielfeld mit den Buttons und setzt es
	 * in den Centerbereich des Controllers
	 */
	@Override
	protected GameGUI createGamePanel() {
		GameGUI panel = new GameGUI(this);

		add(panel, BorderLayout.CENTER);

		return panel;
	}

	/**
	 * Initialisiert die GameLogic und setzt die Startinformationen in den
	 * jeweiligen Labels Wird überall aufgerufen an den Stellen, wo auch ein
	 * Level geladen wurde, also beim: Level öffnen Level Generieren Level
	 * Neustarten Level Laden
	 * 
	 * @param board
	 *            int[][]
	 * @param time
	 *            int
	 * @param minStones
	 *            int
	 * @param points
	 *            int
	 */
	public void initializeGameLogic(int[][] board, int time, int minStones,
			int points) {
		// GameLogic initialiseren
		gamelogic = new gameLogic(board, time, minStones, points, eventManager);
		setGameBoard(gamelogic.getGameBoard());
		setTime(gamelogic.getTARGET_TIME());
		setPoints(gamelogic.getPoints());
		statusBar.setMinStones(gamelogic.getMIN_STONES());

	}

	/**
	 * Gibt zurück, ob das geladene Level valide war ( wird nur bei Tests
	 * verwendet )
	 * 
	 * @return boolean
	 */
	public boolean isCorrectLevel() {
		return levelHandling.isFileValidDecision();
	}

	/**
	 * Holt den LevelString aus der GameLogic und gibt ihn ohne ExtraInfos
	 * zurück ( für Tests )
	 * 
	 * @return String
	 */
	public String getLevelAsStringWithoutExtraInfo() {
		if (gamelogic == null)
			return null;
		else
			return gamelogic.toString();
	}

	/**
	 * Gibt den LevelString mit allen Informationen zurück, inklusive sortierten
	 * Algorithmus ( für Tests )
	 * 
	 * @return
	 */
	public String getLevelAsStringWithExtraInfo() {

		StringBuffer result = new StringBuffer();
		result.append(gamelogic.toString()).append("\n");
		if (levelHandling.getExtraInfos().size() > 0) {
			if (levelHandling.getExtraInfos().firstElement().contains(
					"target_time:")
					|| levelHandling.getExtraInfos().firstElement().contains(
							"min_stones:"))
				result.append(levelHandling.getExtraInfos().firstElement());

		} else
			result.append("###target_time:").append(gamelogic.getTARGET_TIME())
					.append("|").append("min_stones:").append(
							gamelogic.getMIN_STONES());

		if (levelHandling.getHighscores().size() != 0) {
			result.append("\n");
			result.append(levelHandling.getHighscores().toStringForLevelFile());
		}

		return result.toString();
	}

	/**
	 * Wird aus dem GameGui aufgerufen beim Klick auf ein Button und ruft die
	 * entsprechende Methode in der GameLogic auf, Dabei wird je nach
	 * Rüchgabewert der GameLogic Aktion ( es wurde was gelöscht oder nicht )
	 * ein anderer Sound abgespielt
	 * 
	 * @param y
	 *            int
	 * @param x
	 *            int
	 */
	public void BrickClicked(int y, int x) {
		// Durch das Klicken wird in der GameLogik das Board verändert oder
		// nicht
		getGamePanel().removeMouseListener(getGamePanel());
		removeKeyListener(this);
		if (!levelStarted) {
			setLevelStarted(true);
			rebootTimer();
		}
		boolean result = gamelogic.deleteBrick(y, x, time, !JUnittest);
		// Punkte aktualisieren
		if (result) {
			setPoints(gamelogic.getPoints());
			statusBar.setJPoints(points);
			if (!JUnittest) {
				playSound("clicked.wav");

			}
		}
		// Soundabspielen beim klicken
		if (!result) {
			getGamePanel().addMouseListener(getGamePanel());
			addKeyListener(this);
			if (!JUnittest) {
				playSound("invalidclick.wav");

			}
		}

	}

	/**
	 * Abspielen der durch file spezifizierten Sounddatei
	 * 
	 * @param file
	 *            String
	 */
	private void playSound(String file) {
		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(new File(file));
			AudioFormat af = audioInputStream.getFormat();
			int size = (int) (af.getFrameSize() * audioInputStream
					.getFrameLength());
			byte[] audio = new byte[size];
			DataLine.Info info = new DataLine.Info(Clip.class, af, size);
			audioInputStream.read(audio, 0, size);

			Clip clip = (Clip) AudioSystem.getLine(info);
			clip.open(af, audio, 0, size);
			clip.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Wird ausgeführt beim Klicken auf Ok im LevelGenerierungsFenster, dabei
	 * wird ein neues Level generiert die GameLogic wird initialisiert, das
	 * Level wird eingeladen, das GenerierungsFenster unsichtbar gemacht der
	 * Timer wird neu gestartet, das GameGUI wird aufgefordert sich neu zu
	 * zeichnen und der LevelGeneratedflag wird auf true gesetzt
	 */
	public void generateLevel(int width, int height, int colors, int minStones) {
		timer.stop();
		levelGenerated = false;
		try {
			levelGen.generateNewLevel(width, height, colors, minStones);
			// GameLogic Initialisieren
			initializeGameLogic(levelGen.getLevel(), -1, minStones, 0);
			levelHandling.loadLevelFromString(levelGen.toString());
			updateStatusBar();
			highscoreFrame.saveHighscoreButtonSetEnabled(false);
			highscoreFrame.clearjTableHighscores();
			levelStarted = false;
			updateWindowSize();
			notifyLevelLoaded(levelGen.getLevel()[0].length, levelGen
					.getLevel().length);
			levelGenerated = true;
			checkFinished();
		} catch (WrongParamException e) {
			e.printStackTrace();
		} catch (ParameterOutOfRangeException e) {
			e.printStackTrace();
		} catch (InternalFailureException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Öffnet einen Standard-OpenDialog zur Auswahl der Leveldatei
	 * 
	 * @return String Pfad
	 */
	public String oeffnenDialog() {

		String inputVerzStr = "";
		final JFileChooser chooser = new JFileChooser("Verzeichnis wählen");
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		final File file = new File(getDefaultLevelPath());
		chooser.setCurrentDirectory(file);

		chooser.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				if (e.getPropertyName().equals(
						JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)
						|| e.getPropertyName().equals(
								JFileChooser.DIRECTORY_CHANGED_PROPERTY)) {
					// final File f = (File) e.getNewValue();
				}
			}
		});

		chooser.setVisible(true);
		final int result = chooser.showOpenDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			File inputVerzFile = chooser.getSelectedFile();
			inputVerzStr = inputVerzFile.getPath();

		}
		chooser.setVisible(false);

		return inputVerzStr;
	}

	/**
	 * Event, das zum rezeichnen des GamePanels führt
	 */
	@Override
	public void arrayChangeAnimation(ArrayChangeAnimationEvent e) {
		try {
			getGamePanel().redraw();

		} catch (InternalFailureException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * Event, das zum rezeichnen des Panels fürht und prüft ob das SPiel beendet
	 * ist
	 */
	@Override
	public void arrayChanged(ArrayChangeEvent e) {
		getGamePanel().addMouseListener(getGamePanel());
		addKeyListener(this);
		setGameBoard(gamelogic.getGameBoard());
		try {
			getGamePanel().redraw();
		} catch (InternalFailureException e1) {
			e1.printStackTrace();
		}

		// prüfen auf Spielende
		checkFinished();
		getGamePanel().buttonHighlighting();
	}

	/**
	 * Passt die Größe des Controllerfensters an die Größe des Levels an im
	 * Maximalfall wird das Fenster so groß wie die Hintergrundbildgröße und im
	 * Minimalfall sogroß, dass die Label in der Statuszeile ( im unteren
	 * Bereich ) noch lesbar sind
	 */
	public void updateWindowSize() {

		int width = gameBoard[0].length * 40;
		int height = gameBoard.length * 40 + 100;
		int iconWidth = getGamePanel().getBackgroundIcon().getIconWidth();
		int iconHeight = getGamePanel().getBackgroundIcon().getIconHeight();

		// das JFrame darf nicht größere sein als das Hintergrundbild
		if (width > iconWidth) {
			if (height > iconHeight)
				setSize(iconWidth, iconHeight);
			else
				setSize(iconWidth, height);
		} else {
			// zu klein und man kann das Menü kaum bedienen und einige Elemente
			// nicht sehen
			if (width < 350)
				setSize(350, height);
			else {
				if (height < 350)
					setSize(350, 350);
				else
					setSize(width, height);
			}

		}

	}

	/**
	 * prüft ob das Spiel beenendet ist und verlangt je nachdem eine Eingabe des
	 * highscores, oder zuerst, dass ein generiertes Level gespeichert wird.
	 * Danach öffnet es die Highscore Liste und man kann über einen Button
	 * seinen Highscore speichern.
	 */
	private void checkFinished() {
		if (gamelogic.getFinished()) {
			// noch ein mal kurz zeichnen
			try {
				gamePanel.redraw();
			} catch (InternalFailureException e2) {
				e2.printStackTrace();
			}

			timer.stop();
			// Die Dialoge zum Speichern des Highscores etc, sollen nur kommen,
			// wenn es sich nicht um einen
			// JUnitTest handelt
			if (!JUnittest) {
				playSound("gamewon.wav");
				JOptionPane.showMessageDialog(this, translator
						.translateMessage("messageGameOver")
						+ getPoints(), translator
						.translateMessage("messageGameOverTitle"), 1);

				if (!levelGenerated) {
					String name = "";
					while (name != null && name.length() == 0)
						name = JOptionPane.showInputDialog(this, translator
								.translateMessage("messageHighscore"),
								"Highscore", JOptionPane.INFORMATION_MESSAGE);
					if (name != null) {
						// Highscore eintragen

						levelHandling.getHighscores().addHighscoreEntry(
								new HighscoreEntry(name, (double) gamelogic
										.getPoints(), time, new Date()));

						setJHighscores();

						highscoreFrame.saveHighscoreButtonSetEnabled(true);

						highscoreFrame.showHighscoreFrame();
					}
				} else {
					// Erst das generierte Level speichern, bevor man einen
					// Highscore speichern kann
					JOptionPane
							.showMessageDialog(
									this,
									translator
											.translateMessage("messageSaveGenerated"),
									translator
											.translateMessage("messageSaveGeneratedTitle"),
									JOptionPane.INFORMATION_MESSAGE);
					saveLevelClicked();

					if (!levelGenerated) {

						String hName = "";
						while (hName != null && hName.length() == 0)
							hName = JOptionPane
									.showInputDialog(
											this,
											translator
													.translateMessage("messageHighscore"),
											"Highscore",
											JOptionPane.INFORMATION_MESSAGE);
						if (hName != null) {
							// Highscore eintragen

							levelHandling.getHighscores().addHighscoreEntry(
									new HighscoreEntry(hName,
											(double) gamelogic.getPoints(),
											time, new Date()));

							setJHighscores();

							highscoreFrame.saveHighscoreButtonSetEnabled(true);

							highscoreFrame.showHighscoreFrame();
						}
					}
				}
			}

		}
	}

	/**
	 * updated die Statusbar auf den Stand beim initialisieren
	 */
	private void updateStatusBar() {
		statusBar.setJPoints(points);
		statusBar.setJTime(time);
		highscoreFrame.setMinStones(gamelogic.getMIN_STONES());
		highscoreFrame.setTimeToSolve(time);
	}

	/**
	 * updated das Spielfeld und die Statusbar nach einem Redo/Undo
	 */
	private void updateGameAfterRedoUndo() {
		setPoints(gamelogic.getPoints());
		statusBar.setJPoints(points);
		setGameBoard(gamelogic.getGameBoard());
		try {
			gamePanel.redraw();
		} catch (InternalFailureException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Wird ausgeführt, nachdem man das Spiel durchgespielt hat und im
	 * HighscoreFenster auf den HighscoreSpeichern Button geklickt hat, dabei
	 * wird das Level komplett überschrieben mit den Initialdaten aus dem
	 * LevelHandling und angehangen den neuen sortierten Highscores ersetzt
	 */
	public void saveHighscoreClicked() {

		File file;
		FileWriter fw;
		BufferedWriter bw;
		String fileName = getCurrentLevelPath();
		StringBuffer inhalt = new StringBuffer();

		try {
			file = new File(fileName);
			if (file.exists()) {

				fw = new FileWriter(file);
				bw = new BufferedWriter(fw);

				// Leveldaten
				for (int y = 0; y < levelHandling.getBoardInitialState().length; y++) {
					for (int x = 0; x < levelHandling.getBoardInitialState()[0].length; x++)
						inhalt
								.append(levelHandling.getBoardInitialState()[y][x]);

					inhalt.append(System.getProperty("line.separator"));
				}
				// Wenn keine ExtraInfos vorhanden, hole diese aus der GameLogic
				// ( zB bei generierten Leveln )
				if (levelHandling.getExtraInfos().size() == 0) {
					inhalt.append("###min_stones:").append(
							gamelogic.getMIN_STONES()).append("|target_time:")
							.append(gamelogic.getTARGET_TIME());
					inhalt.append(System.getProperty("line.separator"));

				}
				// Sind welche Enthalten in der ersten Zeile des
				// ExtraInfoVectors, nimm diese, ansonsten hole sie wieder aus
				// der GameLogic
				else {
					if (levelHandling.getExtraInfos().firstElement().contains(
							"min_stones:")
							|| levelHandling.getExtraInfos().firstElement()
									.contains("target_time:")) {
						inhalt.append(
								levelHandling.getExtraInfos().firstElement())
								.append(System.getProperty("line.separator"));
					} else {
						inhalt.append("###min_stones:").append(
								gamelogic.getMIN_STONES()).append(
								"|target_time:").append(
								gamelogic.getTARGET_TIME());
						inhalt.append(System.getProperty("line.separator"));

					}

				}

				// Highscores anfügen
				inhalt.append(levelHandling.getHighscores()
						.toStringForLevelFile());

				bw.write(inhalt.toString());
				bw.close();
				// Nach dem speichern, soll man nicht noch einmal speichern
				// können, über den Klick im Menü auf Highscore
				highscoreFrame.saveHighscoreButtonSetEnabled(false);

			}

		}

		catch (ArrayIndexOutOfBoundsException aioobe) {
			System.out.println("ArrayOutOfBounds beim saven des Highscores");
		} catch (IOException ioe) {
			System.out.println("Habe gefangen: " + ioe);
		}

	}

	// #########################################################

	/**
	 * Main Prozedur, die den Controller startet...
	 * 
	 * @param args
	 *            String[]
	 */

	public static void main(String[] args) {
		GameController gameController = new GameController(false);
		gameController.setVisible(true);

	}
}
