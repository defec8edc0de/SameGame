package Controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Vector;
import java.util.Map.Entry;


public class LevelHandling {

	/**
	 * LevelHandling beinhaltet alle Funktionalitäten zum laden von Leveln und zur Validierung von Levelinformationen
	 */
	
	// Kopien der eigentlich Startdaten, für den Restart ( N Taste ) eines Levels
	private int minStonesInitialState = -1;
	private int timeInitialState = -1;
	private int[][] boardInitialState = null;
	private String levelStringInitialState = "";
	
	
	private int[][] board = null; // internes Spielfeldarray
	private boolean fileValidDecision = false; // Abfrage, ob das Level valid war
	private String levelPath = "src/de/tu_darmstadt/gdi1/resources/levels/defaultlevels/";
	private int minStones = -1; // Initialisierungswerte -1, falls keine Zusatzinformationen beim laden
	private int time = -1;		// vorhanden waren, wird -1 in der GameLogic als default interpretiert und die entsprechenden Defaultwerte werden berechnet
								// minStones sind dann 2 und die Zeit ist dann Anzahl der Spielfelder in Sekunden
	
	private Highscores highscores = null; // Highscoreverwaltungsklasse
	private boolean highscoresAvailable;
    private Vector<String> extraInfos = null; // Zur speicherung von ExtraInformationen wie Zeit, min Stones, Highscores

 
    
    //private GameController parentWindow = null;
	
    /**
     * Konstruktor gibt den JUnittestfall weiter an den Highscore und speichert das ParentObjekt ( GameController ) als Attribut
     * @param parent GameController
     * @param jTest boolean
     */
    public LevelHandling(GameController parent, boolean jTest){
    	//parentWindow = parent;
    	highscores = new Highscores(jTest);
    }
    
    
    /**
     * getter für den Initialwert der Min Stones
     * @return int
     */
    public int getMinStonesInitialState() {
		return minStonesInitialState;
	}


    /**
     * getter des Levelstrings zum InitialState mit allen eventuell vorhandenen Zusatzinfos
     * @return String
     */
	public String getLevelStringInitialState() {
		return levelStringInitialState;
	}


	/**
	 * Setter für die InitialMinStones
	 * @param minStonesInitialState int
	 */
	public void setMinStonesInitialState(int minStonesInitialState) {
		this.minStonesInitialState = minStonesInitialState;
	}


	/**
	 * Getter für den Zeit zum InitialState
	 * @return int
	 */
	public int getTimeInitialState() {
		return timeInitialState;
	}


	/**
	 * Setter für die Zeit zum InitialState
	 * @param timeInitialState int
	 */
	public void setTimeInitialState(int timeInitialState) {
		this.timeInitialState = timeInitialState;
	}


	/**
	 * getter für das Spielfeldarray zum InitialState
	 * @return int[][]
	 */
	public int[][] getBoardInitialState() {
		return boardInitialState;
	}


	/**
	 * Setter für das Board zum InitialState
	 * @param boardInitialState int[][]
	 */
	public void setBoardInitialState(int[][] boardInitialState) {
		this.boardInitialState = boardInitialState;
	}




	/**
	 * Getter für den ExtraInfoVector
	 * @return Vector<String>
	 */
	public Vector<String> getExtraInfos() {
		return extraInfos;
	}
	
	/**
	 * Setter für den ExtraInfoVector
	 * @param extraInfos Vector<String>
	 */
	public void setExtraInfos(Vector<String> extraInfos) {
		this.extraInfos = extraInfos;
	}
	
	/**
	 * Getter für das Spielfeldarray
	 * @return int[][]
	 */
	public int[][] getBoard() {
		return board;
	}
	
	/**
	 * Setter für das Spielfeldarray
	 * @param board int[][]
	 */
	public void setBoard(int[][] board) {
		this.board = board;
	}
	
	/**
	 * Getter für den Schalter, ob das Level valide ist
	 * @return boolean
	 */
	public boolean isFileValidDecision() {
		return fileValidDecision;
	}
	
	/**
	 * Setter für den chalter, ob das Level valide ist
	 * @param fileValidDecision boolean
	 */
	public void setFileValidDecision(boolean fileValidDecision) {
		this.fileValidDecision = fileValidDecision;
	}
	
	/**
	 * Getter für den aktuellen Pfad zur Leveldatei
	 * @return String
	 */
	public String getLevelPath() {
		return levelPath;
	}
	
	/**
	 * Setter für den aktuellen Pfad zur Leveldatei
	 * @param levelPath String
	 */
	public void setLevelPath(String levelPath) {
		this.levelPath = levelPath;
	}
	/**
	 * Getter für die in dem Level ermittelten Min Stones Information
	 * @return int
	 */
	public int getMinStones() {
		return minStones;
	}
	/**
	 * Setter für die Min Stone Informationen
	 * @param minStones int
	 */
	public void setMinStones(int minStones) {
		this.minStones = minStones;
	}
	
	/**
	 * Getter für die im Level ermittelte Zeit
	 * @return int
	 */
	public int getTime() {
		return time;
	}
	/**
	 * Setter für die Zeit
	 * @param time int
	 */
	public void setTime(int time) {
		this.time = time;
	}
	
	/**
	 * Getter für die Highscores
	 * @return Highscores
	 */
	public Highscores getHighscores() {
		return highscores;
	}
	
	/**
	 * Setter für die Highscores
	 * @param highscores Highscores
	 */
	public void setHighscores(Highscores highscores) {
		this.highscores = highscores;
	}
	
	/**
	 * Getter für den Schalter, ob Highscores available sind
	 * @return boolean
	 */
	public boolean isHighscoresAvailable() {
		return highscoresAvailable;
	}
	
	/**
	 * Setter ob Highscores available sind
	 * @param highscoresAvailable boolean
	 */
	public void setHighscoresAvailable(boolean highscoresAvailable) {
		this.highscoresAvailable = highscoresAvailable;
	}
	
	
	
	/**
	 * Bekommt den Level als String repräsentiert und extrahiert von diesem
	 * die Leveldaten in das Attribut int[][] Attribut board, sowie extrahiert
	 * die Zusatzinfos in ein Stringvectorattribut ExtraInfos.
	 * Und gibt true zurück, wenn das geklappt hat
	 * 
	 * @return boolean
	 * @param levelstring
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	public boolean loadLevelFromString(String levelstring){
		
		// wurde das Level schon geladen ? (für LevelRestart)
		if (levelstring.equals(levelStringInitialState)){
			
			time = timeInitialState;
			minStones = minStonesInitialState;
			board = boardInitialState;
			
			return fileValidDecision;
		}
		else {
			
		time = -1;
		minStones = -1;
		
		// highscoreinitialisieren
		clearHighscores();
		
		String[] rows = null;
		// vorher muss aber das Zeilenumbruchszeichen des Systems bestimmt werden und weil split ein reg exp
		// erwartet muss man noch gegebene \ escapen...
		String lineseparator = System.getProperty("line.separator");
	    if ((levelstring.contains("\n") && (!levelstring.contains("\r")))){
	    	// dieser Fall tritt ein, wenn man die Tests startet <.< oder Linux hat
	    	rows = levelstring.split("\\n");
	    	
	    	if ((!levelstring.contains("\n") && (levelstring.contains("\r")))){
		    	// dieser Fall tritt ein, wenn man das Level unter Mac läd
		    	rows = levelstring.split("\\r");
	    	}
	    }
	    else {
	    if (lineseparator.equals("\r\n"))  {
	    
		// Teilt den LevelString in seine Zeilen ein
		rows = levelstring.split("\\r\\n");
	    }
	    }

		if (rows.length == 0) fileValidDecision = false;
		else {
			fileValidDecision = true;
			// Initialisierungen
		int levelColNr = rows[0].length();
        Vector<int[]> tmpLevel = new Vector<int[]>(0,1); // Temporärer Vector zur späteren Ermittlung der Zeilenlänge
        extraInfos = new Vector<String>();
        int[] values = null;
        int value;
        boolean justNulls = true;
        
        
        // Geht die Zeilen des Levels durch
        for (int y = 0;y < rows.length;y++){ 
        	// Zusatzinfos speichern
        	if ((rows[y].contains("###")) && (rows[y].startsWith("###")))
        		extraInfos.add(rows[y]);
        	else {
        		
        	// Prueft ob die Zeilenanzahl gleich der ersten Zeile ist, wenn nich --> Level invalid
         	 if (!(rows[y].length() == levelColNr)) fileValidDecision = false;
         	  else{
         		 values = new int[levelColNr];
         		for (int x = 0; x < rows[y].length();x++){
         			// Prueft ob das aktuelle Zeichen in der Zeile ein Buchstabe ist
         			if (Character.isDigit(rows[y].charAt(x))){
       				 value =  (rows[y].charAt(x)  - '0');
       				// Prueft ob das Zeichen zwischen 0 und 5 liegt 
       				if ((value >= 0) && (value <= 5)){
   					 values[x] = value;
   					 if (value != 0) justNulls = false; 
       				}
   				    else fileValidDecision = false;
         		}
        		 else fileValidDecision = false;
        						 
        		  }
         		tmpLevel.add(values);
         	  }
        		   
        	
        }
        
       }
        
        // Falls die Leveldatei leer war oder nur Nullen drin standen ist das Level invalid
        if ((tmpLevel.size() == 0) || (justNulls)) fileValidDecision = false; 
        
       if (fileValidDecision){
           board = new int[tmpLevel.size()][levelColNr]; 
           for (int y = 0; y < board.length;y++)
        	   System.arraycopy(tmpLevel.elementAt(y),0,board[y],0,levelColNr);
           
           // Können die Zusatzdaten und Highscoredaten richtig extrahiert werden ?
           try {
			fileValidDecision = extractInfosAndValidate();
		} catch (IOException e) {
			System.out.println("Fehler beim verarbeiten der Zusatzinfos");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Fehler beim verarbeiten der Zusatzinfos");
			e.printStackTrace();
		}
			  
		
		// kopieren der initialwerte für einen eventuellen Restart
		timeInitialState = time;
		minStonesInitialState = minStones;
	    boardInitialState = board.clone();
	    StringBuffer sb = new StringBuffer(levelstring);
	    levelStringInitialState = sb.toString();

		
		
       }
		}
        return fileValidDecision;
		}
	}
	


	
	/**
	 * Wird aus loadLevelFromString aufgerufen, extrahiert eventuell vorhandene Zusatzinformationen, speichert diese
	 * in einen Vector, bzw, die Highscores in das Highscoreobjekt und wenn das alles klappt und die Zusatzdaten valide sind
	 * gibt die Funktion true zurück.
	 * @return boolean
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private boolean extractInfosAndValidate() throws IOException, ClassNotFoundException{
		boolean result = true;
		HashMap<String,String> tmpHighscoreEntries;	
		String[] infos;
		String[] keyValue;
        highscoresAvailable = true;
		
        // Zum kopieren des extraInfo Vectors
        // ObjectOutputStream erzeugen
        ByteArrayOutputStream bufOutStream = new ByteArrayOutputStream();
        ObjectOutputStream outStream = new ObjectOutputStream(bufOutStream);
        // Objekt im byte-Array speichern
        outStream.writeObject(extraInfos);
        outStream.close();

        // Pufferinhalt abrufen
        byte[] buffer = bufOutStream.toByteArray();
        // ObjectInputStream erzeugen
        ByteArrayInputStream bufInStream = new ByteArrayInputStream(buffer);
        ObjectInputStream inStream = new ObjectInputStream(bufInStream);
        // Objekt wieder auslesen
        Vector<String> tmpExtraInfos = new Vector<String>();
        tmpExtraInfos = (Vector<String>)inStream.readObject();


		
		// vorrangestellte ### entfernen
		for (int i = 0; i < tmpExtraInfos.size();i++){
			if (tmpExtraInfos.elementAt(i).contains("###"))
				tmpExtraInfos.set(i, tmpExtraInfos.elementAt(i).split("###")[1]);	
		}

		
		// Alle Zusatzinfos durchgehen, jedoch nur bis 11 Zusatzinfos ( 10 Highscores )
		for (int i = 0; ((i < tmpExtraInfos.size()) && (i <= 11));i++){
			
			
			// Ist die erste Zeile eine Zusatzinfo Zeile, dann extrahiere die Infos
			if ((i == 0) && tmpExtraInfos.elementAt(i).matches("(.+(:)[0-9]+(\\|).+(:)[0-9]+)|(.+(:)[0-9]+)")){
				
				//System.out.println("Zusatzinfos scheinen korrekt !");
				
				
				// Aufsplitten der Zusatzinfos in key:value Paare
				infos = tmpExtraInfos.elementAt(i).split("\\|");
				
				
				for (int j = 0;j < infos.length;j++){
					
					
					// jeweiliges Paar aufsplitten und überprüfen
					keyValue = infos[j].split(":");
					// die Paarverteilung kann variieren, jedoch darf jeder Key nur einmal vorkommen
					if ((keyValue[0].equals("target_time")) && (time == -1)){
						try { // 
							  int z = Integer.parseInt(keyValue[1]);
							  if (z < 0) result = false;
							  else time = z;
							} catch (NumberFormatException e) {
							   System.out.println("Zahlenformat von target_time ist falsch");
								result = false;
							}
					}
					else {
						if ((keyValue[0].equals("min_stones")) && (minStones == -1)){
							try {
								  int z = Integer.parseInt(keyValue[1]);
								  if (!((z >= 2) && (z <= 5))) result = false;
								  else minStones = z;
								} catch (NumberFormatException e) {
									System.out.println("Zahlenformat von min_stone ist falsch");
								   result = false;
								}
					}
						else result = false;
				}
				}
				
			}
			
			
			
			else {
				// Besteht die Zeile aus dem Format: Zeichen:Zeichen|Zeichen:Zeichen|Zeichen:Zeichen|Zeichen:Zeichen ? Dann ist es vielleicht ein Highscore
				if (tmpExtraInfos.elementAt(i).matches(".+(:).+(\\|).+(:).+(\\|).+(:).+(\\|).+(:).+")){
				// System.out.println("Highscoredaten scheinen korrekt !");
				
				// Highscoreentrys initialisieren, und am Ende prüfen, ob alle Daten vorhanden
				// indem man prüft, ob noch einer der Initialwerte drin steht	
				tmpHighscoreEntries = new HashMap<String, String>();
				tmpHighscoreEntries.put("name", "-1");
				tmpHighscoreEntries.put("points", "-1");
				tmpHighscoreEntries.put("rem_time", "-1");
				tmpHighscoreEntries.put("date", "-1");
				
				// Highscorezeile in Infoarray splitten
				infos = tmpExtraInfos.elementAt(i).split("\\|");
				
				
				for (int j = 0;j < infos.length;j++){

					// die Paarverteilung kann variieren, jedoch darf jeder Key nur einmal vorkommen
	
					
					// jeweiliges Paar aufsplitten und überprüfen
					keyValue = infos[j].split(":");
		
					// ist der aktuelle key überhaupt ein richtiger ?
					if (tmpHighscoreEntries.containsKey(keyValue[0])){
							// Ist der key points und noch nicht im Prüfvector, dann überprüf die Punktzahl und schreib se rein
							if ((keyValue[0].equals("points")) && (tmpHighscoreEntries.get(keyValue[0]).equals("-1"))){
								try { 
									  double z = Double.parseDouble(keyValue[1]);
									  if (z < 0) result = false;
									  else { 
										  tmpHighscoreEntries.remove(keyValue[0]);
										  tmpHighscoreEntries.put(keyValue[0], keyValue[1]);  
									  }
									} catch (NumberFormatException e) {
									   result = false;  
									}
							}
							
							
							if ((keyValue[0].equals("rem_time")) && (tmpHighscoreEntries.get(keyValue[0]).equals("-1"))){
								try { 
									  double z = Double.parseDouble(keyValue[1]);
									  if (z < 0) result = false;
									  else { 
										  tmpHighscoreEntries.remove(keyValue[0]);
										  tmpHighscoreEntries.put(keyValue[0], keyValue[1]);  
									  }
									} catch (NumberFormatException e) {
									   result = false;  
									}
							}
							
							
							if ((keyValue[0].equals("date")) && (tmpHighscoreEntries.get(keyValue[0]).equals("-1"))){

								// sieht verdaaaaaammt kompliziert aus ^^ is es auch !...Problem ist die Möglichkeit führender Nullen...also zB 01.11.2010...muss drin sein...das | ist ein ODER...und \\d allgemein Zahlen
								// im Bezug auf den semantischen Fehler 24:43:56...das wird bei mir nu au abgefragt    und zwaaaaaaaaaaaaaaaaaaaaaaaaar     da ( siehe zeile darunter )
								if (keyValue[1].matches("(0[1-9]|[1-9]|[12][0-9]|3[01])(\\.)(0[1-9]|1[012]|[1-9])(\\.)(19|20)\\d{2}( )([0-9]|0[0-9]|[1][0-9]|[2][0-4])(;)([0-9]|0[0-9]|[1][0-9]|[2][0-9]|[3][0-9]|[4][0-9]|[5][0-9])(;)([0-9]|0[0-9]|[1][0-9]|[2][0-9]|[3][0-9]|[4][0-9]|[5][0-9])")){

									tmpHighscoreEntries.remove(keyValue[0]);
									tmpHighscoreEntries.put(keyValue[0], keyValue[1]);
								}
	
							}
							
							
							if ((keyValue[0].equals("name")) && (tmpHighscoreEntries.get(keyValue[0]).equals("-1"))){
								tmpHighscoreEntries.remove(keyValue[0]);
								tmpHighscoreEntries.put(keyValue[0], keyValue[1]);   
							}
							
							
							
						
						}
						else result = false;

				}
					
				// Prüfen ob die Highscorezeile vollständig war:
				for (Entry<String, String> entry : tmpHighscoreEntries.entrySet())
				 if (entry.getValue().equals("-1")) highscoresAvailable = false;
				

				// Highscore eintrag eintragen	
				if (result && highscoresAvailable)
				 highscores.addHighscoreEntry(new HighscoreEntry(tmpHighscoreEntries.get("name"), 					  		// Name des Spielers im Highscoreeintrag
						 						   Double.valueOf(tmpHighscoreEntries.get("points")), 			// dessen Punkte
						 						   Double.valueOf(tmpHighscoreEntries.get("rem_time")), 		// dessen verbliebene Zeit
						 						   tmpHighscoreEntries.get("date").split(" ")[0].split("\\.")[0], // Datumsanteil: Tag
						 						   tmpHighscoreEntries.get("date").split(" ")[0].split("\\.")[1], // Datumsanteil: Monat
						 						   tmpHighscoreEntries.get("date").split(" ")[0].split("\\.")[2], // Datumsanteil: Jahr
						 						   tmpHighscoreEntries.get("date").split(" ")[1].split(";")[0], // Datumsanteil: Stunde
						 						   tmpHighscoreEntries.get("date").split(" ")[1].split(";")[1], // Datumsanteil: Minute
						 						   tmpHighscoreEntries.get("date").split(" ")[1].split(";")[2]));// Datumsanteil: sekunde

				
				}
				else highscoresAvailable = false;
				
				
				
			}
			
		}
			
		if (((minStones == -1) && (time == -1)) && (tmpExtraInfos.size() > 0))
			 result = false;
		
		
		
		return result;
	}
	
	
	/**
	 * Läd aus einer übergebenen Leveldatei ( Dateiname ) die Daten in einen
	 * String und gibt diesen zurück
	 * 
	 * @param fileName String
	 * @return String Level als String repräsentiert mit Zusatzinfos
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String readLevelFileInString(String fileName) throws FileNotFoundException, IOException{
		// sucht aus dem Systemumfeld das verwendete Zeilenumbruchszeichen
		String linebreak = System.getProperty("line.separator");
		String s = null; 
		StringBuffer level = new StringBuffer();
		if (fileName.contains(".lvl")){
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        while((s = in.readLine()) != null)
          level.append(s).append(linebreak);
        
        in.close();
		}
        return level.toString();
        
	}

	
	
	/**
	 * Setzt das Flag, dass kein Highscore verfügbar ist und löscht den Inhalt des Highscoreobjektes
	 */
	public void clearHighscores(){
		highscoresAvailable = false;
		highscores.clear();
	}
	
	
}
