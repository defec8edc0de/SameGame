package View;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import translator.TranslatableGUIElement;

public class StatusBar extends JPanel {

	/**
	 * StatusBar ist ein Panel welches Informationen zum aktuellen Spielstand anzeigt in Labeln
	 * z.B.: aktuelle Zeit, Punkte, min Stones
	 */
	private static final long serialVersionUID = 1L;
	private JLabel JPointLabel = null;
	private JLabel JPoints = null;
	private JLabel JTimeLabel = null;
	private JLabel JTime = null;
	private JLabel JStoneLabel = null;
	private JLabel minStones = null;
	private TranslatableGUIElement guiBuilder;


	/**
	 * Konstruktor der StatusBar erzeugt eine Statusbar in der passenden Sprache
	 * durch den gegebenen Gui-Builder
	 * 
	 * @param guiBuilder
	 * @param controller
	 */
	public StatusBar(TranslatableGUIElement guiBuilder
			) {
		this.guiBuilder = guiBuilder;

		// erzeugen der Elemente
		createElements();
		// hinzufügen der Elemente
		addElements();
		setVisible(true);
	}
	
	/**
	 * erzeugt die benötigten Objekte 
	 */
	private void createElements() {
		JPointLabel = guiBuilder.generateJLabel("statusPoints");
		JPoints = new JLabel();
		JPoints.setForeground(Color.black);
		JTime = new JLabel();
		JTime.setForeground(Color.black);
		JTimeLabel = guiBuilder.generateJLabel("statusTime");
		JStoneLabel = guiBuilder.generateJLabel("statusStones");
		minStones = new JLabel();
		setBorder(BorderFactory.createLineBorder(Color.black));

	}
	
	/**
	 * fügt die erzeugten Objekte der Statusbar hinzu
	 */
	private void addElements() {
		add(JPointLabel);
		add(JPoints);
		add(JTimeLabel);
		add(JTime);
		add(JStoneLabel);
		add(minStones);
	}
	
	/**
	 * aendert den Wert des JTime Labels entsprechend zur Eingabe Zeit
	 * @param time
	 */
	public void setJTime(double time) {
		if (time < 0) {
			JTime.setForeground(Color.red);
			JTime.setText(String.valueOf((int) time));
		} else {
			JTime.setForeground(Color.black);
			JTime.setText(String.valueOf((int) time));
		}
	}
	
	/**
	 * setter für JPoints
	 * @param points
	 */
	public void setJPoints(double points) {
		JPoints.setText(String.valueOf((int) points));
	}
	
	/**
	 * Setter für minStones;
	 * @param stones
	 */
	public void setMinStones(int stones) {
		minStones.setText(String.valueOf(stones));

	}

}
