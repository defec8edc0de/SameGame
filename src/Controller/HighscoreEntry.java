package Controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Eine Highscoreelementklasse Sie enthält folgende Daten:
 * Name des Spielers
 * Erreichte Punktzahl
 * Verbliebene Zeit
 * Datum und Zeit des Spiels
 * @author sephir0th
 *
 */

public class HighscoreEntry{

	private String name;
	private double points;
	private double remainingTime;
	private HashMap<String,String> dateMap; // zur besseren Unterscheidung der einzelnen Datumsanteile
	private Date date;
	
	
	
	/**
	 * Getter für das Datum
	 * @return Date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Setter für die DatumsMap
	 * @param dateMap HashMap<String, String>
	 */
	public void setDateMap(HashMap<String, String> dateMap) {
		this.dateMap = dateMap;
	}
 
	/**
	 * Setter für die Points double
	 * @param points double
	 */
	public void setPoints(double points) {
		this.points = points;
	}
	
	/**
	 * Setter für die verbliebene Zeit double
	 * @param remainingTime double
	 */
	public void setRemainingTime(double remainingTime) {
		this.remainingTime = remainingTime;
	}
	
	
	/**
	 * Setter für das Datum
	 * @param date Date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Getter für den Spielernamen
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter für den Spielernamen
	 * @param name String
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * getter für die Punkte double
	 * @return double
	 */
	public double getPoints() {
		return points;
	}

	/**
	 * Setter für die Punkte integer
	 * @param points int
	 */
	public void setPoints(int points) {
		this.points = points;
	}

	/**
	 * Getter für die verbliebene Zeit double
	 * @return double
	 */
	public double getRemainingTime() {
		return remainingTime;
	}
	/**
	 * Setter für die verbliebene Zeit integer
	 * @param remainingTime int
	 */
	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}

	/**
	 * Getter für die DatumsMap
	 * @return HashMap<String,String>
	 */
	public HashMap<String,String> getDateAsMap() {
		return dateMap;
	}

	/**
	 * Setter für die DatumsMap
	 * @param date HashMap<String,String>
	 */
	public void setDateAsMap(HashMap<String,String> date) {
		this.dateMap = date;
	}

	/**
	 * Setter für die DatumsMap mit einzelnen Datumsanteilen
	 * @param day String
	 * @param month String
	 * @param year String
	 * @param hour String
	 * @param minute String
	 * @param second String
	 */
	public void setDateAsMap(String day,String month,String year,String hour,String minute, String second){
		dateMap.clear();
		dateMap.put("day", day);
		dateMap.put("month", month);
		dateMap.put("year", year);
		dateMap.put("hour", hour);
		dateMap.put("minute", minute);
		dateMap.put("second", second);
	}
	
	
	/**
	 * Getter für das Datum in bestimmten Format
	 * @return Date
	 */
	public Date getDate_extended(){
		// SimpleDateFormat ist recht praktisch, weil es jeder erdenkliche Art von Datumsangabe
		// z.B. 1.1.2010 oder 10-10-2010  annimmt und in ein Date Object parst
	    SimpleDateFormat result = new SimpleDateFormat();
		try {
			return result.parse(dateMap.get("day")+"."+dateMap.get("month")+"."+dateMap.get("year")+" "+dateMap.get("hour")+":"+dateMap.get("minute")+":"+dateMap.get("second"));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	
	/**
	 * Getter für die DatumsMap
	 * @return HashMap<String,String>
	 */
	public HashMap<String, String> getDateMap() {
		return dateMap;
	}

	/**
	 * Konstructor eines HighscoreEntrys mit umfangreicher Datumsanteilangabe
	 * @param name String
	 * @param points double
	 * @param remTime double
	 * @param day String
	 * @param month String
	 * @param year String
	 * @param hour String
	 * @param minute String
	 * @param second String
	 */
	public HighscoreEntry(String name, Double points, Double remTime,String day,String month,String year,String hour,String minute, String second){
		this.name = name;
		this.points = points;
		this.remainingTime = remTime;
		dateMap = new HashMap<String,String>();
		dateMap.put("day", day);
		dateMap.put("month", month);
		dateMap.put("year", year);
		dateMap.put("hour", hour);
		dateMap.put("minute", minute);
		dateMap.put("second", second);
		SimpleDateFormat tmp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		try {
			this.date = tmp.parse(day+"."+month+"."+year+" "+hour+":"+minute+":"+second);
		} catch (ParseException e) {
			System.out.println("Fehler beim Parsen des Datums");
			e.printStackTrace();
		}
		
	}
	/**
	 * Einfacherer Konstruktor eines HighscoreEintrages, nur für Angabe eines DatumObjectes
	 * @param name String
	 * @param points double
	 * @param rem_time double
	 * @param date Date
	 */
	public HighscoreEntry(String name, Double points, Double rem_time,Date date){
		this.name = name;
		this.points = points;
		this.remainingTime = rem_time;
		this.date = date;
	}

	
}
