package Model;
import java.util.EventObject;


public class ArrayChangeEvent extends EventObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Konstruktor des Events
	 * @param source
	 */
	public ArrayChangeEvent(Object source) {
		super(source);
		
	}

}
