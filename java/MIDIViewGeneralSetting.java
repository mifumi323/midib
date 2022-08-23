import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MIDIViewGeneralSetting extends MIDIView implements ActionListener {
	TextField textTempo = null;
	TextField textAuthor = null;
	TextField textTitle = null;
    public MIDIViewGeneralSetting(MIDIApplet parent) {
		applet = parent;
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c1 = new GridBagConstraints(), c2 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridwidth = GridBagConstraints.REMAINDER;
		c2.weightx = 1.0;
		setLayout(gridbag);
		Label l;
		l = new Label(applet.getString("Tempo"));
		gridbag.setConstraints(l,c1);
		add(l);
		textTempo = new TextField(Integer.toString(applet.doc.tempo));
		textTempo.addActionListener(this);
		gridbag.setConstraints(textTempo,c2);
		add(textTempo);
		l = new Label(applet.getString("Author"));
		gridbag.setConstraints(l,c1);
		add(l);
		textAuthor = new TextField(applet.doc.author);
		textAuthor.addActionListener(this);
		gridbag.setConstraints(textAuthor,c2);
		add(textAuthor);
		l = new Label(applet.getString("Title"));
		gridbag.setConstraints(l,c1);
		add(l);
		textTitle = new TextField(applet.doc.title);
		textTitle.addActionListener(this);
		gridbag.setConstraints(textTitle,c2);
		add(textTitle);
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==textTempo) {
            try {
                int tempo = Integer.parseInt(textTempo.getText());
            	if (tempo>=4) applet.doc.tempo = tempo;
            }catch(NumberFormatException ex) {
            }
        }else if (e.getSource()==textAuthor) {
            applet.doc.author = textAuthor.getText();
        }else if (e.getSource()==textTitle) {
            applet.doc.title = textTitle.getText();
        }
    }
}
