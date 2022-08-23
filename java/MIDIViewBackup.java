import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MIDIViewBackup extends MIDIView implements ActionListener {
	TextArea textArea;
	Button btRestore;
	public MIDIViewBackup(MIDIApplet parent) {
		applet = parent;
		textArea = new TextArea(applet.doc.getBackupString(),0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
		setLayout(new BorderLayout());
		removeAll();
		add("Center", textArea);
		Panel p = new Panel();
		btRestore = new Button(applet.getString("Restore"));
		btRestore.addActionListener(this);
		p.add(btRestore);
		add("South",p);
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==btRestore) {
			applet.doc.restoreFromBackup(textArea.getText());
		}
	}
}
