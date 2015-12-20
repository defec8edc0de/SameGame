package Model;
import java.util.EventListener;

/**
 * Interface eines ArrayEventListeners
 * @author Markus Tasch
 *
 */
public interface ArrayEventListener extends EventListener {

	public void arrayChanged (ArrayChangeEvent e);
	public void arrayChangeAnimation (ArrayChangeAnimationEvent e);
	
}
