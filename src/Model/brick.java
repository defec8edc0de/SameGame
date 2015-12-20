package Model;

import java.awt.Point;
import java.util.Vector;

public class brick {
	private int value;

	private Vector<Point> equalNeighbors;
	
	/**
	 * Konstruktor eines brick Objekts erstellt einen Brick mit dem gegebenen Value und einem leerem equalNeighbor Vector
	 * @param value
	 */
	public brick(int value) {
		this.value = value;
		equalNeighbors = new Vector<Point>(0, 1);
	}


	/**
	 * gibt den Value des Brick-Objekts zurück
	 * 
	 * @return Int
	 */
	public int getValue() {
		return value;
	}

	/**
	 * setzt den Value des Brick-Objekts auf den gegeben Int-Wert
	 * 
	 * @param value
	 *            Int
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * gibt dne Vector equalNeighbors zurück
	 * @return
	 */
	public Vector<Point> getEqualNeighbors() {
		return equalNeighbors;
	}
	
	/**
	 * sett equalNeighbors auf den gegebenen Vector
	 * @param equalNeighbors
	 */
	public void setEqualNeighbors(Vector<Point> equalNeighbors) {
		this.equalNeighbors = equalNeighbors;
	}
}
