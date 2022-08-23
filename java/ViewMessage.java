import java.awt.Label;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ViewMessage extends MIDIView implements MouseListener {
	String message;
	MIDIView returnto;
	public ViewMessage(MIDIApplet parent, String Message) {
		applet = parent;
		message = applet.getString(Message);
		returnto = applet.view;
		Label l = new Label(message);
		l.addMouseListener(this);
		add(l);
		addMouseListener(this);
	}
    public void mouseClicked(MouseEvent e) {
        applet.changeView(returnto);
    }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
}
