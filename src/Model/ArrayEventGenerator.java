package Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayEventGenerator {
	// Liste mit allen Listenern
	private List<ArrayEventListener> listeners = new ArrayList<ArrayEventListener>();

	/**
	 * Methode zum werfen einens ArrayChangeEvents
	 */
	public synchronized void arrayGotChanged() {
		performArrayChangeEvent();
	}

	/**
	 * Informiert alle Listener über das geworfene ArrayChangeEvent
	 */
	private synchronized void performArrayChangeEvent() {
		ArrayChangeEvent change = new ArrayChangeEvent(this);
		Iterator<ArrayEventListener> listenersIt = listeners.iterator();
		while (listenersIt.hasNext()) {
			listenersIt.next().arrayChanged(change);
		}
	}

	/**
	 * Methode zum werfen eines ArrayChangeAnimationEvents
	 */
	public synchronized void arrayGotChangedAnimation() {
		performArrayChangeAnimationEvent();
	}

	/**
	 * Informiert alle Listener über das geworfene ArrayChangeAnimationEvent
	 */
	private void performArrayChangeAnimationEvent() {
		ArrayChangeAnimationEvent change = new ArrayChangeAnimationEvent(this);
		Iterator<ArrayEventListener> listenersIt = listeners.iterator();
		while (listenersIt.hasNext()) {
			listenersIt.next().arrayChangeAnimation(change);
		}
	}

	/**
	 * fügt den gegebenen ArrayListener der Listenerliste hinzu
	 * 
	 * @param source
	 */
	public void addArrayListener(ArrayEventListener source) {
		listeners.add(source);
	}

	/**
	 * entfernt den gegebenen ArrayListener aus der Listener Liste
	 * 
	 * @param source
	 */
	public void removeArrayListener(ArrayEventListener source) {
		listeners.add(source);
	}

}
