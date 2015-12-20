package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Das GameMenü ist die obere Menüzeile zur Steuerung des Spiels und erbt von JMenuBar
 * @author sephir0th
 *
 */

public class GameMenue extends JMenuBar {

	private GameController parentWindow = null;

	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor des Menüs initialisiert das Menü mit allen Untermenüs und MenüItems
	 * und referenziert das ParentObjekt ( GameController ) als Attribut, um auf die Funktionalität bei Menüklicks, auf Funktionen im Controller zu verweisen.
	 * @param parent GameController
	 */
	public GameMenue(GameController parent) {
		super();
		parentWindow = parent;

		// anlegen des Dateiauswahlmenï¿½s
		JMenu dateiMenue = parentWindow.guiBuilder.generateJMenu("fileMenu");

		// anlegen des Untermenï¿½items Level Neustarten
		JMenuItem jMenuItemRestartLevel = parentWindow.guiBuilder
				.generateJMenuItem("fMRestart");
		jMenuItemRestartLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuItemRestartLevelClicked();
			}
		});
		dateiMenue.add(jMenuItemRestartLevel);

		// anlegen des Untermenï¿½items Skin auswahl
		JMenu jMenuChooseSkin = parentWindow.guiBuilder
				.generateJMenu("fMChooseSkin");
		dateiMenue.add(jMenuChooseSkin);

		// anlegen des Untermenï¿½items DefaultSkin
		JMenuItem jMenuItemDefaultSkin = parentWindow.guiBuilder
				.generateJMenuItem("fMChooseSkinDefault");
		jMenuItemDefaultSkin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuItemDefaultSkinClicked();
			}
		});
		jMenuChooseSkin.add(jMenuItemDefaultSkin);

		// anlegen des Untermenï¿½items CircleSkin
		JMenuItem jMenuItemCircleSkin = parentWindow.guiBuilder
				.generateJMenuItem("fMChooseCircleSkin");
		jMenuItemCircleSkin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuItemCircleSkinClicked();
			}
		});
		jMenuChooseSkin.add(jMenuItemCircleSkin);
		
		
		// anlegen des Untermenï¿½items HexagonSkin
		JMenuItem jMenuItemHexagonSkin = parentWindow.guiBuilder
				.generateJMenuItem("fMChooseHexSkin");
		jMenuItemHexagonSkin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuItemHexagonSkinClicked();
			}
		});
		jMenuChooseSkin.add(jMenuItemHexagonSkin);
		

		// anlegen des Untermenï¿½items Sprach auswahlen
		JMenu jMenuChooseSpeech = parentWindow.guiBuilder
				.generateJMenu("fMChooseLanguage");
		dateiMenue.add(jMenuChooseSpeech);

		// anlegen des Untermenï¿½items Sprach auswahl
		JMenuItem jMenuItemDeutsch = parentWindow.guiBuilder
				.generateJMenuItem("fMGerman");
		// sprache wï¿½hlen item Action festlegen
		jMenuItemDeutsch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuItemDeutschClicked();
			}
		});
		jMenuChooseSpeech.add(jMenuItemDeutsch);

		// anlegen des Untermenï¿½items Sprach auswahl
		JMenuItem jMenuItemEnglish = parentWindow.guiBuilder
				.generateJMenuItem("fMEnglish");
		// sprache wï¿½hlen item Action festlegen
		jMenuItemEnglish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuItemEnglishClicked();
			}
		});
		jMenuChooseSpeech.add(jMenuItemEnglish);

		// anlegen des Untermenï¿½items Sprach auswahl
		JMenuItem jMenuItemFrench = parentWindow.guiBuilder
				.generateJMenuItem("fMFrench");
		// sprache wï¿½hlen item Action festlegen
		jMenuItemFrench.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuItemFrenchClicked();
			}
		});
		jMenuChooseSpeech.add(jMenuItemFrench);

		// anlegen des Untermenï¿½items close
		JMenuItem jMenuItemClose = parentWindow.guiBuilder
				.generateJMenuItem("fMQuit");
		// levelï¿½ffnen item Action festlegen
		jMenuItemClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuItemCloseClicked();
			}
		});
		dateiMenue.add(jMenuItemClose);

		// anlegen des BearbeitenAuswahlmenï¿½s
		JMenu editMenue = parentWindow.guiBuilder.generateJMenu("editMenu");

		// anlegen des Untermenï¿½items Undo
		JMenuItem jMenuItemUndo = parentWindow.guiBuilder
				.generateJMenuItem("eMUndo");
		jMenuItemUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuItemUndoClicked();
			}
		});
		editMenue.add(jMenuItemUndo);

		// anlegen des Untermenï¿½items Redo
		JMenuItem jMenuItemRedo = parentWindow.guiBuilder
				.generateJMenuItem("eMRedo");
		jMenuItemRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuItemRedoClicked();
			}
		});
		editMenue.add(jMenuItemRedo);

		// anlegen des Untermenï¿½item Hint
		JMenuItem jMenuItemHint = parentWindow.guiBuilder
				.generateJMenuItem("eMTip");
		jMenuItemHint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuItemHintClicked();
			}
		});
		editMenue.add(jMenuItemHint);

		// anlegen des Levelauswahlmenï¿½s
		JMenu levelMenue = parentWindow.guiBuilder.generateJMenu("optionsMenu");

		// anlegen der Untermenï¿½items level ï¿½ffnen, level generieren, level
		// speichern
		JMenuItem jMenuItemOpenLevel = parentWindow.guiBuilder
				.generateJMenuItem("oMopen");
		// levelï¿½ffnen item Action festlegen
		jMenuItemOpenLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuItemLevelOeffnenClicked();
			}
		});
		levelMenue.add(jMenuItemOpenLevel);

		JMenuItem jMenuItemSaveLevel = parentWindow.guiBuilder
				.generateJMenuItem("oMSave");
		// levelspeichern item Action festlegen
		jMenuItemSaveLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuItemSaveLevelClicked();
			}
		});
		levelMenue.add(jMenuItemSaveLevel);

		JMenuItem jMenuItemLoadLevel = parentWindow.guiBuilder
				.generateJMenuItem("oMLoad");
		// levelladen item Action festlegen
		jMenuItemLoadLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuItemLoadLevelClicked();
			}
		});
		levelMenue.add(jMenuItemLoadLevel);

		JMenuItem jMenuItemGenerateLevel = parentWindow.guiBuilder
				.generateJMenuItem("oMGenerate");
		// levelgenerieren item Action festlegen
		jMenuItemGenerateLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuItemGenerateLevelClicked();
			}
		});
		levelMenue.add(jMenuItemGenerateLevel);

		// anlegen des Hilfe Menues
		JMenu helpMenue = parentWindow.guiBuilder.generateJMenu("infoMenu");

		// anlegen der Untermenï¿½items Hilfe und ï¿½ber
		JMenuItem jMenuItemHelp = parentWindow.guiBuilder
				.generateJMenuItem("iMHelp");
		// Help item Action festlegen
		jMenuItemHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuItemHelpClicked();
			}
		});
		helpMenue.add(jMenuItemHelp);

		JMenuItem jMenuItemAbout = parentWindow.guiBuilder
				.generateJMenuItem("iMAbout");
		// ï¿½ber item Action festlegen
		jMenuItemAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuItemAboutClicked();
			}
		});
		helpMenue.add(jMenuItemAbout);

		JMenuItem jMenuItemHighscore = parentWindow.guiBuilder
				.generateJMenuItem("iMHighscore");
		jMenuItemHighscore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuItemHighscoreClicked();
			}
		});
		helpMenue.add(jMenuItemHighscore);

		// Untermenüs hinzufügen zur MenuBar Größe anpassen und Visible setzen
		add(dateiMenue);
		add(editMenue);
		add(levelMenue);
		add(helpMenue);
		setVisible(true);
		setSize(100, 50);
	}

	/**
	 * Aktion beim klicken auf Level Öffnen, dabei wird ein ÖffnenDialog angezeigt, das Ergebnis wird validiert und an den Controller weitergereicht
	 */
	private void menuItemLevelOeffnenClicked() {
		String filePath = parentWindow.oeffnenDialog();
		if (filePath != null && !(filePath.length() == 0) && !(filePath.contains(".sve")))
		parentWindow.loadAndStartLevel(filePath);
	}

	/**
	 * Aktion beim klicken auf Spiel Speichern
	 */
	private void menuItemSaveLevelClicked() {
		parentWindow.saveLevelClicked();
	}
	
	/**
	 * Aktion beim Klicken auf Hilfe
	 */
	private void menuItemHelpClicked() {
		parentWindow.helpClicked();
	}

	/**
	 * Aktion beim Klicken auf Level generieren
	 */
	private void menuItemGenerateLevelClicked() {
		parentWindow.generateLevelClicked();
	}

	/**
	 * Aktion beim klicken auf Über
	 */
	private void menuItemAboutClicked() {
		parentWindow.aboutClicked();
	}

	/**
	 * Aktion beim Klicken auf Neustarten
	 */
	private void menuItemRestartLevelClicked() {
		parentWindow.keyNewGamePressed();
	}
	
	/**
	 * Aktion beim klicken auf Undo
	 */
	private void menuItemUndoClicked() {
		parentWindow.keyUndoPressed();
	}
	/**
	 * Aktion beim klicken auf Redo
	 */
	private void menuItemRedoClicked() {
		parentWindow.keyRedoPressed();

	}

	/**
	 * Aktion beim Klicken auf Tipp
	 */
	private void menuItemHintClicked() {
		parentWindow.keyHintPressed();
	}

	/**
	 * Aktion beim klicken auf Deutsch
	 */
	private void menuItemDeutschClicked() {
		parentWindow.setLanguage(Locale.GERMANY);
	}

	/**
	 * Aktion beim klicken auf Englisch
	 */
	private void menuItemEnglishClicked() {
		parentWindow.setLanguage(Locale.US);
	}

	/**
	 * Aktion beim klicken auf Französisch
	 */
	private void menuItemFrenchClicked() {
		parentWindow.setLanguage(Locale.FRANCE);
	}

	/**
	 * Aktion beim klicken auf Schließen
	 */
	private void menuItemCloseClicked() {
		System.exit(0);

	}

	/**
	 * Aktion beim klicken auf DefaultSkin
	 */
	private void menuItemDefaultSkinClicked() {
		parentWindow
				.changeSkin("src/de/tu_darmstadt/gdi1/resources/images/defaultskin/");
	}

	/**
	 * Aktion beim klicken auf CircleSkin
	 */
	private void menuItemCircleSkinClicked() {
		parentWindow
				.changeSkin("src/de/tu_darmstadt/gdi1/resources/images/circleskin/");
	}
	
	/**
	 * Aktion beim klicken auf HexagonSkin
	 */
	private void menuItemHexagonSkinClicked() {
		parentWindow
				.changeSkin("src/de/tu_darmstadt/gdi1/resources/images/hexagonskin/");
	}

	/**
	 * Aktion welche ausgefï¿½hrt wird, wenn man im Menue auf Spielstand Laden
	 * klickt
	 */
	private void menuItemLoadLevelClicked() {
		parentWindow.loadLevelClicked();
	}

	/**
	 * Aktion beim klicken auf Highscore
	 */
	private void menuItemHighscoreClicked() {
		parentWindow.getHighscoreFrame().showHighscoreFrame();
	}

}
