package Controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

/**
 * Repräsentiert eine Highscoreliste, dazu enthält es eine List<HighscoreEntrys>
 * und relevante Getter und Setter zum hinzufügen von Einträgen bzw löschen
 * @author sephir0th
 *
 */


public class Highscores {

	private List<HighscoreEntry> highscoreEntrys; // Liste welche die Einträge verwaltet ( bis zu 10 Stück )
	private boolean jUnitTest;	// JUnitTestFlag, ob ein Test stattfindet
	
	/**
	 * Konstruktor initialisiert die HighscoreListe und das JUnitTestflag
	 * @param jTest
	 */
	public Highscores(boolean jTest){
		this.highscoreEntrys = new Vector<HighscoreEntry>(0,1);
		jUnitTest = jTest;
		
	}
	/**
	 * Zum hinzufügen eines Highscoreeintrages, natürlich nur, wenn der selbe Eintrag nicht schon drin steht oder der Eintrag null ist
	 * Im Anschluss werden die Highscores sortiert, damit der Highscore richtig eingeordnet wird.
	 * Und falls danach die Größe der Liste größer als 10 ist, wird der letzte Eintrag entfernt
	 * @param entry HighscoreEntry
	 */
	public void addHighscoreEntry(HighscoreEntry entry){
		if ((entry != null) && (!highscoreEntrys.contains(entry))){
		 
		 highscoreEntrys.add(entry);
		 sortHighscore();
		 
		 if (highscoreEntrys.size() == 11)
			 highscoreEntrys.remove(highscoreEntrys.size()-1);
		}
	}
	
	/**
	 * sortiert die Highscoreliste nach Punkten, dann nach verbliebener Zeit, dann nach Datum und dann nach Name
	 * ich konnte diese Funktion leider noch nicht testen...die Idee mit den Comparatoren hab ich von google.de
	 * 
	 * also man sortiert mit der Methode sort ( mergesort ) der Collectionsklasse, übergibt der eine Liste
	 * und einen Komparator, in unserem Fall einen verschachtelten Comparator...der zuerst nach Punkten sortiert
	 * dann nach Zeit, dann nach Datum und dann nach Namen
	 * 
	 */
	public void sortHighscore(){
		Collections.sort(highscoreEntrys, new ByPoints(new ByRTime(new ByDate(new ByName(null)))));

	}
	/**
	 * Gibt die aktuelle Größe der HighscoreListe zurück
	 * @return int
	 */
	public int size(){
		return highscoreEntrys.size();
	}
	
	
	/**
	 * Versucht einen HighscoreEntry zurückzugeben der an übergebener Position in der Liste steht,
	 * wenn es solch einen Eintrag nicht gibt, wird null zurück gegeben
	 * @param index int
	 * @return HighscoreEntry
	 */
	public HighscoreEntry getEntry(int index){
		HighscoreEntry result;
		try{
		 result = highscoreEntrys.get(index);
		}
		catch (IndexOutOfBoundsException e){
		 result = null;
		}
		return result;
	}
	
	/**
	 * Gibt den Highscore als String Zeile für Zeile zurück
	 * 
	 * @return String
	 */
	public String toString(){
		StringBuffer str = new StringBuffer();
		for (int i = 0;i < highscoreEntrys.size();i++){
			str.append(highscoreEntrys.get(i).getName()).append(" ");
			str.append(highscoreEntrys.get(i).getPoints()).append(" ");
			str.append(highscoreEntrys.get(i).getRemainingTime()).append(" ");
			str.append(highscoreEntrys.get(i).getDate()).append(System.getProperty("line.separator"));
		}
		
		return str.toString();
	}
	
	/**
	 * Gibt die HighscoreListe als String zurück. Wird aufgerufen beim Speichern der Highscores in der Leveldatei
	 * Die Highscorezeilen haben dabei genau das Format, wie es in der Aufgabenstellung beschrieben ist.
	 * z.B.: ###name:Horst|points:0|date:15.08.2010 18;03;34|rem_time:25
	 * @return String
	 */
	public String toStringForLevelFile(){
		StringBuffer str = new StringBuffer();
		for (int i = 0;i < highscoreEntrys.size();i++){
			str.append("###name:").append(highscoreEntrys.get(i).getName()).append("|");
			str.append("points:").append((int)highscoreEntrys.get(i).getPoints()).append("|");
	
			Calendar cal = Calendar.getInstance();
			
			cal.setTime(highscoreEntrys.get(i).getDate());
			
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH;mm;ss");
			str.append("date:").append(format.format(highscoreEntrys.get(i).getDate())).append("|");
			
			str.append("rem_time:").append((int)highscoreEntrys.get(i).getRemainingTime());

			if (i != highscoreEntrys.size()-1){
				if (jUnitTest){
					str.append("\n");
				}
				else str.append(System.getProperty("line.separator"));
			}
			 
			
		}
		
		return str.toString();
	}
	
		/**
		 * Löscht die HighscoreListe
		 */
	   public void clear(){
	     highscoreEntrys.clear();	
	    }
	
}



/*
 * 
 * Alles was hier unten folgt, sind Komparatorimplementierungen zum sortieren des Highscore
 * 
 */


class ByPoints implements Comparator<HighscoreEntry> {
    Comparator<HighscoreEntry> tieBreaker;
 
    ByPoints(Comparator<HighscoreEntry> tieBreaker) {
        this.tieBreaker = tieBreaker;
    }
 
    public int compare (HighscoreEntry one, HighscoreEntry two){
        int result  = (int) (two.getPoints() - one.getPoints());
        if (result == 0 && tieBreaker != null) {
            result = tieBreaker.compare(one, two);
        }
        return result;
    }
}
 
class ByRTime implements Comparator<HighscoreEntry> {
    Comparator<HighscoreEntry> tieBreaker;
 
    ByRTime(Comparator<HighscoreEntry> tieBreaker) {
        this.tieBreaker = tieBreaker;
    }
 
    public int compare (HighscoreEntry one, HighscoreEntry two){
        int result = (int) (two.getRemainingTime() - one.getRemainingTime());
        if (result == 0 && tieBreaker != null) {
            result = tieBreaker.compare(one, two);
        }
        return result;
    }
}
 
class ByDate implements Comparator<HighscoreEntry> {
    Comparator<HighscoreEntry> tieBreaker;
 
    ByDate(Comparator<HighscoreEntry> tieBreaker) {
        this.tieBreaker = tieBreaker;
    }
 
    public int compare (HighscoreEntry one, HighscoreEntry two){
        int result = one.getDate().compareTo(two.getDate());
        if (result == 0 && tieBreaker != null) {
            result = tieBreaker.compare(one, two);
        }
        return result;
    }
}

class ByName implements Comparator<HighscoreEntry> {
    Comparator<HighscoreEntry> tieBreaker;
 
    ByName(Comparator<HighscoreEntry> tieBreaker) {
        this.tieBreaker = tieBreaker;
    }
 
    public int compare (HighscoreEntry one, HighscoreEntry two){
        int result = one.getName().compareTo(two.getName());
        if (result == 0 && tieBreaker != null) {
            result = tieBreaker.compare(one, two);
        }
        return result;
    }
    
 
}