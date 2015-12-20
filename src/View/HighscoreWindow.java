package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import translator.TranslatableGUIElement;
import translator.Translator;
import Controller.GameController;

public class HighscoreWindow extends JFrame implements WindowListener {

	/**
	 * HighscoreWindow  ist ein JFrame, welches Highscoreinformationen zu einem Level in einer Tabelle
	 * anzeigt, weiterhin befindet sich dort ein Button, mit welchem man seinen eigenen Highscore nach dem durchspielen speichern kann
	 */
	private static final long serialVersionUID = 1L;
	private JPanel highscoreBar = null;
	private JLabel highscoreTitle = null;
	private JTable jTableHighscores = null;
	private JLabel minStones = null;
	private JLabel timeToSolve = null;
	private JScrollPane tablePane = null;
	private AbstractButton saveHighscoreButton = null;
	private GameController controller;
	private TranslatableGUIElement guiBuilder;
	private Translator translator;

	public HighscoreWindow(Translator translator,
			 GameController controller,Object[][] highscoreValues, boolean saveEnabled) {
		super("Highscores");
		this.translator = translator;
		guiBuilder = translator.getGenerator();
		this.controller = controller;
		setResizable(false);
		setSize(325, 500);
		createElements(highscoreValues, saveEnabled);
		addElements();
	}
	
	
	/**
	 * erstellt alle benötigten Elelemente des Highscorefensters, wobei die Tabelle bei einem neuen Spiel als leer geladen wird und sonst mit Daten aus der Leveldatei gefüllt wird
	 * @param newGame
	 * @see initializeHighscoreTable
	 */
	private void createElements(Object[][] highscoreValues, boolean saveEnabled) {
		timeToSolve = new JLabel();
		minStones = new JLabel();
		highscoreBar = new JPanel();
		highscoreBar.setPreferredSize(new Dimension(0, 0));
		saveHighscoreButton = guiBuilder.generateJButton("highscoreSaveButton");
		saveHighscoreButton.setEnabled(saveEnabled);
		saveHighscoreButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.saveHighscoreClicked();

			}
		});
		highscoreTitle = new JLabel("Highscores");
		highscoreTitle.setForeground(Color.black);
		initiliazeHighscoreTable(highscoreValues);
	}
	
	/**
	 * erstellt die Highscore Tabelle eine leere wenn es ein neues Spiel ist und eine mit Daten gefüllte wenn schon ein Highscore existiert
	 * @param newGame
	 */
	private void initiliazeHighscoreTable(Object[][] highscoreValues) {
			//leere Tabelle bei Spielbeginn
		if (highscoreValues != null){
		jTableHighscores = new JTable(highscoreValues,
				new String[] {translator.translateMessage("highscoreName"), 
				translator.translateMessage("highscorePoints"), 
				translator.translateMessage("highscoreTime"), 
				translator.translateMessage("highscoreDate")});
		}else{
			jTableHighscores = new JTable(new Object[][] {
				{ null, null, null, null }, { null, null, null, null },
				{ null, null, null, null }, { null, null, null, null },
				{ null, null, null, null }, { null, null, null, null },
				{ null, null, null, null }, { null, null, null, null },
				{ null, null, null, null }, { null, null, null, null } },
				new String[] {translator.translateMessage("highscoreName"), 
					translator.translateMessage("highscorePoints"), 
					translator.translateMessage("highscoreTime"), 
					translator.translateMessage("highscoreDate")});
		}
			
		


		jTableHighscores.getTableHeader().setReorderingAllowed(false);
		jTableHighscores.setEnabled(false);
		jTableHighscores.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		jTableHighscores.setPreferredScrollableViewportSize(new Dimension(550,
				500));
		addTablePane();

	}
	
	/**
	 * erstellt den Scroll Balken des Highscore Fensters
	 */
	private void addTablePane() {
		tablePane = new JScrollPane(jTableHighscores);
		tablePane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		tablePane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		jTableHighscores.setPreferredScrollableViewportSize(new Dimension(550,
				500));

		int vScrollBarWidth = tablePane.getVerticalScrollBar()
				.getPreferredSize().width;
		Dimension dimTable = jTableHighscores.getPreferredSize();
		dimTable.setSize(dimTable.getWidth() + vScrollBarWidth, dimTable
				.getHeight());

		tablePane.setPreferredSize(new Dimension(300, 300));
		tablePane.setMaximumSize(new Dimension(dimTable.width, 250));
		tablePane.setMinimumSize(new Dimension(dimTable.width, 50));

		tablePane.setViewportView(jTableHighscores);

		tablePane.revalidate();

	}
	
	/**
	 * fügt alle erstellten Elemente dem Highscore Fesnter hinzu
	 */
	private void addElements() {
		highscoreBar.add(highscoreTitle);
		highscoreBar.add(tablePane);
		highscoreBar.add(minStones);
		highscoreBar.add(timeToSolve);
		highscoreBar.add(saveHighscoreButton);
		highscoreBar.setVisible(true);
		highscoreBar.setBorder(BorderFactory.createLineBorder(Color.black));
		add(highscoreBar);
	}

	/**
	 * Enabled bzw. Disabled den saveHighscoreButton
	 * @param b
	 */
	public void saveHighscoreButtonSetEnabled(boolean b) {
		saveHighscoreButton.setEnabled(b);

	}
	
	public boolean isSaveHighscoreButtonEnabled(){
		return saveHighscoreButton.isEnabled();
	}
	/**
	 * macht das Highscore Fesnter an der berechneten Position sichtbar
	 */
	public void showHighscoreFrame() {
		setLocationRelativeTo(controller);
		setLocation(getLocation().x + (controller.getWidth() - (controller.getWidth() / 4)),
				getLocation().y);
		setVisible(true);
	}
	
	/**
	 * gibt die Highscore Tabelle zurück
	 * @return
	 */
	public JTable getjTableHighscores() {
		return jTableHighscores;
	}
	
	public void clearjTableHighscores(){
		for (int row=0; row<jTableHighscores.getRowCount(); row++){
			for (int col=0; col<jTableHighscores.getColumnCount(); col++){
				jTableHighscores.setValueAt(null, row, col);
			}
		}
	}
	
	/**
	 * setzt das JLabel minSTones auf den gegebenen int-Value
	 * @param stones
	 */
	public void setMinStones(int stones) {
		minStones.setText(String.valueOf(stones));

	}
	
	/**
	 * setzt das JLabel TimeToSolve auf die gegebene Zeit
	 * @param time
	 */
	public void setTimeToSolve(double time) {
		timeToSolve.setText(translator.translateMessage("highscoreTargetTime")
				+ String.valueOf((int) time));

	}
	
	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		setVisible(false);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}
}
