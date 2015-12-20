package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;

import Controller.GameController;
import Model.brick;
import de.tu_darmstadt.gdi1.samegame.exceptions.InternalFailureException;
import de.tu_darmstadt.gdi1.samegame.exceptions.InvalidOperationException;
import de.tu_darmstadt.gdi1.samegame.exceptions.ParameterOutOfRangeException;
import de.tu_darmstadt.gdi1.samegame.ui.GamePanel;
import de.tu_darmstadt.gdi1.samegame.ui.GameWindow;

public class GameGUI extends GamePanel {

	/**
	 * GameGUI ist das Spielbrett, welches im Center des Hauptfensters ( Controller ) eingebettet liegt und jegliche
	 * Funktionalitäten der Darstellung und der Mausverarbeitung enthält. Dabei erbt diese Klasse von dem Template
	 * GamePanel, welche wiederrum von einem Panel erbt.
	 * z.B.: 	verarbeiten von Bildern (skins)
	 * 			highlighten von Maus/Tastaturpositionen
	 *			Verarbeitung von Animationen/Veränderung des Spielfeldaussehens
	 *			platzieren der Spielsteine
	 * @see GamePanel
	 */
	
	
	private ImageIcon backgroundIcon; // Backgroundimage

	private brick[][] board = null;	// repräsentiert das interne Spielfeld
	private GameController parent = null; 
	private String skinPath = "src/de/tu_darmstadt/gdi1/resources/images/defaultskin/";
	
	// Funktionalität zum Highlighten der Maus/Tastaturpositionen und der gleichen Nachbarn dieser Positionen
	private Vector<JButton> currentNeighborsMouse = null;
	private Vector<JButton> currentNeighborsKeyboard = null;
	private Vector<JButton> currentNeighborsHint = null;
	private Border whiteBorder = null;
	private Border BorderKeyBoardPosition = null;
	private Border BorderKeyBoardHighlight = null;
	private Border defaultBorder = null;

	private JButton buttonWithCurrentKeyPosition = null;
	private int currentX = 0;
	private int currentY = 0;
	
	private static final long serialVersionUID = 1L;


	/**
	 * Konstruktor des GameGUI, initialisiert alle Attribute und die Bilder
	 * @param theParentWindow GameController
	 */
	public GameGUI(GameWindow theParentWindow) {
		super(theParentWindow);

		parent = (GameController) theParentWindow;
		currentNeighborsHint = new Vector<JButton>(0, 1);
		currentNeighborsMouse = new Vector<JButton>(0, 1);
		currentNeighborsKeyboard = new Vector<JButton>(0, 1);
		whiteBorder = BorderFactory.createMatteBorder(3, 3, 3, 3, Color.white);
		BorderKeyBoardPosition = BorderFactory.createMatteBorder(5, 5, 5, 5,
				Color.DARK_GRAY);
		BorderKeyBoardHighlight = BorderFactory.createMatteBorder(3, 3, 3, 3,
				Color.cyan);
		defaultBorder = null;
		;

		initializeImages();

	}
	
	
	/**
	 * Initialisiert die Bilder
	 */
	public void initializeImages() {
		// Bilder einladen

		setBackGroundImage(new ImageIcon(skinPath + "back.jpg"));

		try {
			registerImage("1", skinPath + "colorone.png");
			registerImage("2", skinPath + "colortwo.png");
			registerImage("3", skinPath + "colorthree.png");
			registerImage("4", skinPath + "colorfour.png");
			registerImage("5", skinPath + "colorfive.png");
			registerImage("empty", skinPath + "empty.png");

		} catch (ParameterOutOfRangeException e) {
			System.out.println("Fehler beim laden der Buttonbilder");
			e.printStackTrace();
		} catch (InvalidOperationException e) {
			System.out.println("Fehler beim laden der Buttonbilder");
			e.printStackTrace();
		} catch (InternalFailureException e) {
			System.out.println("Fehler beim laden der Buttonbilder");
			e.printStackTrace();
		}
	}

	

	/**
	 * Wird ausgeführt beim Mausklick auf ein Button und bekommt die Position des Buttons im internen Spielfeldarray
	 * um die Aktion weiterzuleiten an den Controller und dieser an die Spiellogik
	 */
	@Override
	protected void entityClicked(int positionX, int positionY) {
		removeHighlighting(currentNeighborsHint);
		parent.BrickClicked(positionY, positionX);

	}

		/**
		 * Zeigt bei einem Tastdruck auf H einen Hinweis an, welche Steine wegklickbar sind ( indem es dieseh highlighted )
		 * @param hint Point
		 */
		public void showHint(Point hint) {
			removeHighlighting(currentNeighborsHint);
			Border border = BorderFactory.createMatteBorder(3, 3, 3, 3,
					Color.magenta);
			highlightNeighbors(board[hint.y][hint.x], border, currentNeighborsHint);

		}
	
	
	
	/**
	 * Wird ausgeführt beim Spacetastendruck auf ein Button und bekommt die Position des Buttons im internen Spielfeldarray
	 * um die Aktion weiterzuleiten an den Controller und dieser an die Spiellogik
	 */
	public void entitySpacePressed() {
		removeHighlighting(currentNeighborsHint);
		parent.BrickClicked(currentY, currentX);
	}
	
	/**
	 * wird bei einem Tastendruck auf down ausgeführt und aktualisiert entsprechend die interne KeyCursorPosition sowie die Highlightings
	 */
	public void keyDownPressed() {
		if (currentY != (board.length - 1)) {
			currentY = currentY + 1;
			removeHighlighting(currentNeighborsKeyboard);
			highlightNeighbors(board[currentY][currentX],
					BorderKeyBoardHighlight, currentNeighborsKeyboard);
			buttonHighlighting();

		}
	}
	/**
	 * wird bei einem Tastendruck auf up ausgeführt und aktualisiert entsprechend die interne KeyCursorPosition sowie die Highlightings
	 */
	public void keyUpPressed() {
		if (currentY != 0) {
			currentY = currentY - 1;
			removeHighlighting(currentNeighborsKeyboard);
			highlightNeighbors(board[currentY][currentX],
					BorderKeyBoardHighlight, currentNeighborsKeyboard);
			buttonHighlighting();
		}
	}
	/**
	 * wird bei einem Tastendruck auf right ausgeführt und aktualisiert entsprechend die interne KeyCursorPosition sowie die Highlightings
	 */
	public void keyRightPressed() {
		if (currentX != (board[0].length - 1)) {
			currentX = currentX + 1;
			removeHighlighting(currentNeighborsKeyboard);
			highlightNeighbors(board[currentY][currentX],
					BorderKeyBoardHighlight, currentNeighborsKeyboard);
			buttonHighlighting();
		}
	}
	/**
	 * wird bei einem Tastendruck auf left ausgeführt und aktualisiert entsprechend die interne KeyCursorPosition sowie die Highlightings
	 */
	public void keyLeftPressed() {
		if (currentX != 0) {
			currentX = currentX - 1;
			removeHighlighting(currentNeighborsKeyboard);
			highlightNeighbors(board[currentY][currentX],
					BorderKeyBoardHighlight, currentNeighborsKeyboard);
			buttonHighlighting();
		}
	}
	
	
	

	/**
	 * Highlighted gleichfarbige Buttons um die KeyCursorPosition
	 */
	public void buttonHighlighting() {
		int VectorPosition = currentY * board[currentY].length + currentX;
		JButton current = getEntities().get(VectorPosition);
		buttonWithCurrentKeyPosition.setBorder(defaultBorder);
		buttonWithCurrentKeyPosition = current;

		highlightNeighbors(board[currentY][currentX], BorderKeyBoardHighlight,
				currentNeighborsKeyboard);
		buttonWithCurrentKeyPosition.setBorder(BorderKeyBoardPosition);
	}
	
	/**
	 * getter für ds Hintergrundbild ( relevant zur Größenanpassung des Controllers )
	 * @return ImageIcon
	 */
	public ImageIcon getBackgroundIcon() {
		return backgroundIcon;
	}
	
	/**
	 * setter für das Hintergrundbild ( relevant für den Skinwechsel )
	 * @param image ImageIcon
	 */
	public void setBackGroundImage(ImageIcon image) {
		backgroundIcon = image;
	}

		
	/**
	 * getter für den Skin Pfad
	 * @return String
	 */
	public String getSkinPath() {
		return skinPath;
	}

	/**
	 * setter für den Skin Pfad ( relevant bei der Auswahl eines anderen Skins )
	 * @param skinPath String
	 */
	public void setSkinPath(String skinPath) {
		this.skinPath = skinPath;
	}

	/**
	 * Abstrakte Methode aus GamePanel ( Implementierungspflicht ), nicht von uns verwendet.
	 */
	@Override
	protected void panelResized() {
	}


	/**
	 * entfernt alle Highlights der im Vector befindlichen Buttons und
	 * highlightet die Tastaturposition erneut
	 * 
	 * @param currentNeighbors Vector<JButton>
	 */
	private void removeHighlighting(Vector<JButton> currentNeighbors) {
		for (int i = 0; i < currentNeighbors.size(); i++) {
			currentNeighbors.get(i).setBorder(defaultBorder);
		}
		buttonHighlighting();

	}

	/**
	 * Löscht alle Bilder aus der geerbten BilderHashMap ( relevant beim Skinwechsel )
	 */
	public void clearImages() {
		getImages().clear();
	}


	/**
	 * Tritt ein wenn die Maus einen Button verlässt und ist für das Dehighlightling der vorherigen Buttons
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
		arg0.consume();
		removeHighlighting(currentNeighborsMouse);
	}

	
	/* ======================================================================== */
	/**
	 * Wird ausgeführt wenn der Mauscursor einen Button betritt, dann wird ermittelt welcher Button das ist und dessen Position im Spielfeldarray
	 * um anschließend ihn und seine gleichfarbigen Nachbarn zu highlighten
	 */
	@Override
	public void mouseEntered(MouseEvent evt) {
		if (!hasEntities())
			return;

		for (int i = 0; i < getEntities().size(); i++) {
			JButton btn = getEntities().get(i);
			if (evt.getSource() == btn) {

				evt.consume();
				// Ermittlung der Nachbarbuttons zum highlighten...
				// aus Koordinaten auf den Button kommen..schwierig...
				// ButtonID müsste in jeden Brick...

				brick tmp = board[i / board[0].length][i % board[0].length];
				highlightNeighbors(tmp, whiteBorder, currentNeighborsMouse);

			}
		}
	}

	
	
	
	
		/**
		 * This method handles the "mouse clicked" mouse event by converting the
		 * event into a call to <em>entityClicked(int, int)</em>.
		 * 
		 * Wir haben diese Methode aus dem Template mit unserem eigenen Algorithmus überschrieben
		 * 
		 * @param evt
		 *            the mouse event caused by clicking "somewhere" on the screen
		 * @see #entityClicked(int, int)
		 */
	    @Override
		public void mouseClicked(MouseEvent evt) {
			if (!hasEntities())
				return;

			// iterate buttons until right one was found
			for (int i = 0; i < getEntities().size(); i++) {
				JButton btn = getEntities().get(i);
				if (evt.getSource() == btn) {
					entityClicked(i % board[0].length, i / board[0].length);

					// done!
					evt.consume();
					break;
				}
			}
		}


	
	/**
	 * Highlighted die Buttons, die gleichfarbige Nachbarn vom EIngabebrick sind
	 * mit dem Eingaberand
	 * 
	 * @param position brick
	 * @param border Border
	 * @param currentNeighbors Vector<JButton>
	 */
	private void highlightNeighbors(brick position, Border border,
			Vector<JButton> currentNeighbors) {
		if (position != null) {
			for (int j = 0; j < position.getEqualNeighbors().size(); j++) {
				// berechnet die Positionen im vector der Buttons nach den
				// Koordinaten der EqualNeighbors
				int xCo = position.getEqualNeighbors().get(j).x;
				int yCo = position.getEqualNeighbors().get(j).y;
				int VectorPosition = yCo * board[yCo].length + xCo;
				JButton current = getEntities().get(VectorPosition);
				current.setBorder(border);
				currentNeighbors.add(current);
			}

		}

	}

	/**
	 * Wird wärend der Animation bzw. bei der Veränderung des Spielfeldes aufgrund eines Klicks/SpacePressed aufgerufen
	 * Dabei holt es sich das veränderte Spielfeldarray aus dem Parent also dem Controller, welcher eine Referenz auf das aktuelle in der GameLogic hat
	 * Und setzt dem neuen Spielfeldarray entsprechen die neuen Buttonwerte
	 * 
	 * Da diese Methode auch beim Start ausgeführt wird, wird darin auch die KeyCursorPosition initialisiert auf x=0 y=0 ( linke obere Ecke )
	 */
	@Override
	protected void setGamePanelContents() {
		board = parent.getGameBoard();
		for (int y = 0; y < board.length; y++)
			for (int x = 0; x < board[0].length; x++)
				try {
					if (board[y][x] == null)
						placeEntity("empty");
					else
						placeEntity(String.valueOf(board[y][x].getValue()));
				} catch (ParameterOutOfRangeException e) {
					System.out
							.println("Neuzeichnen der Button: Die GameBoard Parameter sind falsch ( length )");
					e.printStackTrace();
				}

		// Für die Tastensteuerung wird der Startpunkt auf die linke obere Ecke
		// gesetzt
		if (!parent.isLevelStarted()) {
			currentX = 0;
			currentY = 0;
			buttonWithCurrentKeyPosition = getEntities().get(0);
			buttonWithCurrentKeyPosition.setBorder(BorderKeyBoardPosition);
			highlightNeighbors(board[0][0], BorderKeyBoardHighlight,
					currentNeighborsKeyboard);
		}
	}
	
	/**
	 * Methode zum Zeichnen des Hintergrundbildes auf den Hintergrund des Panels
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.backgroundIcon != null) {
			g.drawImage(backgroundIcon.getImage(), 0, 0, this);
		}
	}
	
	/**
	 * Gibt die bevorzugte Dimension anhand der Hintergrundbildgröße zurück
	 * @return Dimension 
	 */
	public Dimension getPreferredSize() {
		if (backgroundIcon != null) {
			return new Dimension(backgroundIcon.getIconWidth(), backgroundIcon
					.getIconHeight());
		} else {
			return super.getPreferredSize();
		}
	}


}
