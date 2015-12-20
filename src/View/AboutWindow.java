package View;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import translator.TranslatableGUIElement;
import translator.Translator;

public class AboutWindow extends JFrame implements WindowListener {
	/**
	 * Das AboutWindow ist ein JFrame welches Informationen zu den Entwicklern und dem Progrmm in Labeln anzeigt
	 */
	private static final long serialVersionUID = 1L;
	private TranslatableGUIElement guiBuilder;
	
	
	/**
	 * erzeugt ein AboutWindow in der passenden Sprache durch den gegebenen Translator
	 * @param translator
	 */
	public AboutWindow(Translator translator) {
		guiBuilder = translator.getGenerator();
		this.setTitle(translator.translateMessage("aboutTitle"));
		getContentPane().setLayout(new BorderLayout());
		addGeneralInfos();
		addDeveloperInfos();
		addExitButton();
		//Layout anpassen und unsichtbar machen
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(false);
	}
	
	/**
	 * fügt die allgemeinen Infos dem AboutWindow hinzu
	 */
	private void addGeneralInfos() {
		JPanel generalInfo = guiBuilder
				.generateBorderedJPanel("aboutGeneralInfo");
		getContentPane().add(generalInfo, BorderLayout.NORTH);
		JLabel genInf = guiBuilder.generateJLabel("aboutGenInfLabel");
		generalInfo.add(genInf);
	}
	
	/**
	 * fügt die Developer Infos dem AboutWindow hinzu
	 */
	private void addDeveloperInfos() {
		JPanel infoPanel = guiBuilder.generateBorderedJPanel("aboutInfoPanel");
		getContentPane().add(infoPanel, BorderLayout.CENTER);
		JLabel label = guiBuilder.generateJLabel("aboutInfoLabel");
		infoPanel.add(label);
	}
	
	/**
	 * fügt den Ok/Exit Button dem AboutWindow hinzu
	 */
	private void addExitButton() {
		JPanel exitPanel = guiBuilder.generateBorderedJPanel("aboutExitPanel");
		getContentPane().add(exitPanel, BorderLayout.SOUTH);
		AbstractButton exit = guiBuilder.generateJButton("aboutExitButton");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		exitPanel.add(exit);
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
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
