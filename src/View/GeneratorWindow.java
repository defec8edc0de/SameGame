package View;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

import translator.TranslatableGUIElement;
import translator.Translator;
import Controller.GameController;

public class GeneratorWindow extends JFrame implements WindowListener {
	/**
	 * GeneratorWindow ist ein JFrame, auf welchem man alle Einstellungen mit Slidern machen kann, um ein
	 * Level zu generieren. ( zB. Spielfelddimension, minStones, Anzahl Farben )
	 */
	private static final long serialVersionUID = 1L;
	private TranslatableGUIElement guiBuilder;
	private GameController controller = null;
	private JSlider width = null;
	private JSlider height = null;
	private JSlider colors = null;
	private JSlider stones = null;
	private JPanel buttons = null;
	private AbstractButton ok = null;
	private AbstractButton cancel = null;

	/**
	 * Konstruktor eines GeneratorWindows erstellt einen Konstruktor in der
	 * Sprache,die durch den Translator bestimmt wird
	 * 
	 * @param guiBuilder
	 * @param controller
	 */
	public GeneratorWindow(Translator translator,
			GameController controller) {
		guiBuilder = translator.getGenerator();
		this.setTitle(translator.translateMessage("generatorTitle"));
		this.controller = controller;
		getContentPane().setLayout(new GridLayout(5, 0));
		addWidthSettingsPanel();
		addHeightSettingsPanel();
		addColorSettingsPanel();
		addStoneSettingsPanel();
		addButtons();
		// Layout anapssen und unsichtbar amchen
		setResizable(false);
		setVisible(false);
		pack();
		setLocationRelativeTo(null);
	}

	/**
	 * fügt den Slider für die Breiteneisntellung des zu erstellenden Levels
	 * hinzu
	 */
	private void addWidthSettingsPanel() {
		JPanel widthPanel = guiBuilder
				.generateBorderedJPanel("generatorWidthPanel");
		add(widthPanel);
		width = new JSlider(6, 30, 6);
		width.setMajorTickSpacing(4);
		width.setMinorTickSpacing(1);
		width.setPaintLabels(true);
		width.setPaintTicks(true);
		width.setSnapToTicks(true);
		widthPanel.add(width);

	}

	/**
	 * fügt den Slider für die Höheneinstellung des zu erstellenden levels hinzu
	 */
	private void addHeightSettingsPanel() {
		JPanel heightPanel = guiBuilder
				.generateBorderedJPanel("generatorHeightPanel");
		add(heightPanel);
		height = new JSlider(5, 20, 5);
		height.setMajorTickSpacing(5);
		height.setMinorTickSpacing(1);
		height.setPaintLabels(true);
		height.setPaintTicks(true);
		height.setSnapToTicks(true);
		heightPanel.add(height);

	}

	/**
	 * fügt den Slider für die Auswahl der Anzahl von verschiedenen Steinen
	 * hinzu
	 */
	private void addColorSettingsPanel() {
		JPanel colorPanel = guiBuilder
				.generateBorderedJPanel("generatorColorPanel");
		add(colorPanel);
		colors = new JSlider(2, 5, 2);
		colors.setMajorTickSpacing(1);
		colors.setPaintLabels(true);
		colors.setPaintTicks(true);
		colors.setSnapToTicks(true);
		colorPanel.add(colors);
	}

	/**
	 * fügt den SLider zur EInstellung der zum entfernen benötigten
	 * gleichfarbigen Steine hinzu
	 */
	private void addStoneSettingsPanel() {
		JPanel stonePanel = guiBuilder
				.generateBorderedJPanel("generatorStonePanel");
		add(stonePanel);
		stones = new JSlider(2, 5, 2);
		stones.setMajorTickSpacing(1);
		stones.setPaintLabels(true);
		stones.setPaintTicks(true);
		stones.setSnapToTicks(true);
		stonePanel.add(stones);
	}

	/**
	 * fügt den OK und Cancel Button hinzu
	 * 
	 * @see addOKButton
	 * @see addCancelButton
	 */
	private void addButtons() {
		buttons = guiBuilder.generateBorderedJPanel("generatorButtonPanel");
		buttons.setLayout(new GridLayout(0, 2));
		add(buttons);
		addOkButton();
		addCancelButton();
	}

	/**
	 * fügt den Ok Button hinzu
	 */
	private void addOkButton() {
		ok = guiBuilder.generateJButton("generatorOK");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.generateLevel(width.getValue(), height.getValue(),
						colors.getValue(), stones.getValue());
				setVisible(false);

			}
		});
		buttons.add(ok);
	}

	/**
	 * fügt den Cancel Button hinzu
	 */
	private void addCancelButton() {
		cancel = guiBuilder.generateJButton("generatorCancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		buttons.add(cancel);
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
