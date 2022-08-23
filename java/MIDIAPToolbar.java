import java.awt.Button;
import java.awt.Choice;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MIDIAPToolbar extends Panel implements ActionListener, ItemListener {
	protected MIDIApplet applet;
	Button btBack;
	Button btSend;
	Button btPlay;
	Choice cMenu;
	public MIDIAPToolbar(MIDIApplet parent) {
		applet = parent;
		btBack = new Button(applet.getString("Back"));
		btBack.addActionListener(this);
		btPlay = new Button(applet.getString("Play"));
		btPlay.addActionListener(this);
		btSend = new Button(applet.getString("Send"));
		btSend.addActionListener(this);
		cMenu = new Choice();
		cMenu.add(applet.getString("Track 1"));
		cMenu.add(applet.getString("Track 2"));
		cMenu.add(applet.getString("Track 3"));
		cMenu.add(applet.getString("Track 4"));
		cMenu.add(applet.getString("Track 5"));
		cMenu.add(applet.getString("Track 6"));
		cMenu.add(applet.getString("Track 7"));
		cMenu.add(applet.getString("Track 8"));
		cMenu.add(applet.getString("Track 9"));
		cMenu.add(applet.getString("Track 10"));
		cMenu.add(applet.getString("Track 11"));
		cMenu.add(applet.getString("Track 12"));
		cMenu.add(applet.getString("Track 13"));
		cMenu.add(applet.getString("Track 14"));
		cMenu.add(applet.getString("Track 15"));
		cMenu.add(applet.getString("Track 16"));
		cMenu.add(applet.getString("General Setting"));
		cMenu.add(applet.getString("Backup"));
		cMenu.add(applet.getString("About MIFUMIDIA"));
		cMenu.addItemListener(this);
		cMenu.select(0);
		add(cMenu);
		add(btBack);
		add(btPlay);
		add(btSend);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btSend) {
		    btSend.setEnabled(false);
			applet.doc.Send();
		    btSend.setEnabled(true);
		}else if (e.getSource() == btPlay) {
			if (btPlay.getLabel().equals(applet.getString("Play"))) {
				applet.midiPlay(applet.currenttime);
				btPlay.setLabel(applet.getString("Stop"));
			}else {
				applet.midiStop();
				btPlay.setLabel(applet.getString("Play"));
			}
		}else if (e.getSource() == btBack) {
			applet.currenttime = 0;
			applet.view.refresh();
		}
	}
	public void itemStateChanged(ItemEvent e) {
		String s = e.getItem().toString();
		if (s.equals(applet.getString("Track 1"))) applet.changeView(new MIDIViewTrack(applet,0));
		if (s.equals(applet.getString("Track 2"))) applet.changeView(new MIDIViewTrack(applet,1));
		if (s.equals(applet.getString("Track 3"))) applet.changeView(new MIDIViewTrack(applet,2));
		if (s.equals(applet.getString("Track 4"))) applet.changeView(new MIDIViewTrack(applet,3));
		if (s.equals(applet.getString("Track 5"))) applet.changeView(new MIDIViewTrack(applet,4));
		if (s.equals(applet.getString("Track 6"))) applet.changeView(new MIDIViewTrack(applet,5));
		if (s.equals(applet.getString("Track 7"))) applet.changeView(new MIDIViewTrack(applet,6));
		if (s.equals(applet.getString("Track 8"))) applet.changeView(new MIDIViewTrack(applet,7));
		if (s.equals(applet.getString("Track 9"))) applet.changeView(new MIDIViewTrack(applet,8));
		if (s.equals(applet.getString("Track 10"))) applet.changeView(new MIDIViewTrack(applet,9));
		if (s.equals(applet.getString("Track 11"))) applet.changeView(new MIDIViewTrack(applet,10));
		if (s.equals(applet.getString("Track 12"))) applet.changeView(new MIDIViewTrack(applet,11));
		if (s.equals(applet.getString("Track 13"))) applet.changeView(new MIDIViewTrack(applet,12));
		if (s.equals(applet.getString("Track 14"))) applet.changeView(new MIDIViewTrack(applet,13));
		if (s.equals(applet.getString("Track 15"))) applet.changeView(new MIDIViewTrack(applet,14));
		if (s.equals(applet.getString("Track 16"))) applet.changeView(new MIDIViewTrack(applet,15));
		if (s.equals(applet.getString("General Setting"))) applet.changeView(new MIDIViewGeneralSetting(applet));
		if (s.equals(applet.getString("Backup"))) applet.changeView(new MIDIViewBackup(applet));
		if (s.equals(applet.getString("About MIFUMIDIA"))) applet.changeView(new ViewMessage(applet, "Copyright Mifumi"));
	}
}
