import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;

public class MIDIViewTrack extends MIDIView implements AdjustmentListener {
	int track;
	int editUnit = 24;
	boolean viewAllTracks = false;
	class PanelPianoRoll extends Panel implements MouseListener {
		int note = 0;
		int scrolly = 72;
		int scrollx = 0;
		// 画面バッファ
		Image bgImage = null;
		int bgWidth = 0;
		int bgHeight = 0;
		boolean bgDirty = true;
		// 階名
		String noteName[] = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B",};
		// 1だと黒鍵、ということにしておこう
		int noteColorIndex[] = {0,1,0,1,0,0,1,0,1,0,1,0,};
		// 左側の鍵盤
		Color noteColorL[] = {new Color(0xffffff),new Color(0x000000),};
		// 右側のピアノロール
		Color noteColorR[] = {new Color(0xffffff),new Color(0xc0c0c0),};
		// 文字色
		Color noteColorS[] = {new Color(0x000000),new Color(0xffffff),};
		// ノートの色
		Color noteColorN[] = {
				new Color(0xff0000),new Color(0xff6000),new Color(0xffb700),new Color(0xe0ff00),
				new Color(0x85ff00),new Color(0x1eff00),new Color(0x00ff3b),new Color(0x00ff9a),
				new Color(0x00fff0),new Color(0x00a3ff),new Color(0x0043ff),new Color(0x1d00ff),
				new Color(0x7c00ff),new Color(0xdb00ff),new Color(0xff00c3),new Color(0xff0064),};

		public PanelPianoRoll() {
			addMouseListener(this);
		}
		public void paint(Graphics g){
			// コンテナをペイント
			if (bgWidth!=getWidth()||bgHeight!=getHeight()) {
				// 大きさが変わっているらしいので裏画面を作り直す
				bgWidth = getWidth();
				bgHeight = getHeight();
				bgImage = new BufferedImage(bgWidth, bgHeight, BufferedImage.TYPE_INT_RGB);
				bgDirty = true;
				// スクロールバーを更新(本来ここで更新するのはおかしいのだが)
				hscroll.setVisibleAmount(bgWidth-50);
				vscroll.setVisibleAmount(bgHeight/10);
			}
			if (bgDirty) {
				Graphics g2 = bgImage.getGraphics();
				for (int i=0;i<applet.getSize().height/10;i++) {
					drawKeyboard(g2,i*10,scrolly-i);
				}
				if (viewAllTracks) {
					Color c;
					for (int i=0; i<16; i++) {
						c = noteColorN[i];
						if (i!=track) drawAllNotes(g2, i,
								new Color(c.getRed()/2+96,c.getGreen()/2+96,c.getBlue()/2+96));
					}
				}
				drawAllNotes(g2, track, noteColorN[track]);
				hscroll.setMaximum(applet.doc.getLength()+bgWidth-50);
				bgDirty = false;
			}
			g.drawImage(bgImage,0,0,null);
			if (scrollx<=applet.currenttime&&applet.currenttime<scrollx+bgWidth) {
				int x = applet.currenttime-scrollx+50;
				g.setColor(new Color(0,0,0));
				g.drawLine(x,0,x,bgHeight);
			}
		}

		// マウスの処理
		public void mouseEntered(MouseEvent e){
		}
		public void mousePressed(MouseEvent e){
			note = scrolly-e.getY()/10;
			// 0以上128未満じゃなくて0から127の間ね。そういう定義だった気がする
			if (0<=note && note<=127) {
				if (e.getX()-50>=0) {
					int pos = e.getX()-50+scrollx;
					if ((e.getClickCount()&1)==0) {
						if (e.getButton()==MouseEvent.BUTTON1){
							if (selected==null) {
							    applet.doc.tracks[track].addNote(new MIDIAPNote((pos/editUnit)*editUnit,editUnit,note,100));
							}else{
							    selected.duration += editUnit;
							}
						}else if (e.getButton()==MouseEvent.BUTTON3) {
							applet.doc.tracks[track].removeNote(selected);
						}else {
							if (selected!=null) {
							    selected.duration -= editUnit;
							    if (selected.duration <= 0) applet.doc.tracks[track].removeNote(selected);
							}
						}
					}
					select(find(pos,note));
					applet.currenttime = (pos/editUnit)*editUnit;
					dirty();
				}
				if ((e.getClickCount()&1)==1) {
					applet.noteOn(track,note, selected!=null ? selected.velocity : 100);
				}
			}
		}
		public void mouseExited(MouseEvent e){
		}
		public void mouseReleased(MouseEvent e){
			if (e.getButton()==MouseEvent.BUTTON1){
				applet.noteOff(track,note);
				note = 0;
			}else {
				applet.noteOff(track,note);
				note = 0;
			}
		}
		public void mouseClicked(MouseEvent e){
		}

		public MIDIAPNote find(int time, int note) {
			Iterator it=applet.doc.tracks[track].notes.iterator();
			MIDIAPNote n;
			while (it.hasNext()) {
				n = (MIDIAPNote)it.next();
				if (n.note==note && n.start<=time && time<n.start+n.duration) return n;
			}
			return null;
		}

		public void drawKeyboard(Graphics g, int y, int note) {
			if (0<=note && note<=127) {
				g.setColor(noteColorL[noteColorIndex[note%12]]);
				g.fill3DRect(0,y,50,10,true);
				g.setColor(noteColorR[noteColorIndex[note%12]]);
				g.fill3DRect(50,y,bgWidth-50,10,true);
				g.setColor(noteColorS[noteColorIndex[note%12]]);
				g.drawString((note/12)+" "+applet.getString(noteName[note%12]),0,y+9);
			}else {
				g.setColor(new Color(0x808080));
				g.fill3DRect(0,y,bgWidth,10,true);
			}
		}
		public void drawAllNotes(Graphics g, int track, Color color) {
			Iterator it=applet.doc.tracks[track].notes.iterator();
			MIDIAPNote note;
			int l,r;
			g.setColor(color);
			while (it.hasNext()) {
				note = (MIDIAPNote)it.next();
				l = 50+note.getStart()-scrollx;
				r = l+note.getDuration();
				if (l<50) l=50;
				if (r>bgWidth) r=bgWidth;
				if (l<r) {
					g.fill3DRect(l,(scrolly-note.getNote())*10,r-l,10,true);
				}
			}
		}

		public void dirty() {
			bgDirty = true;
			repaint();
		}
		public void update(Graphics g) {
			paint(g);
		}
	}
	class PanelTrackParameter extends Panel implements ItemListener {
		Checkbox checkViewAllTracks = null;
		Choice choiceProgram = null;
		Choice choiceEditUnit = null;
		int editUnits[] = {192,96,48,24,12,6,3,64,32,16,8,4,2,1};
		public PanelTrackParameter() {
			Panel p;
			int i;
			// 全トラック表示
			checkViewAllTracks = new Checkbox(applet.getString("View All Tracks"), viewAllTracks);
			checkViewAllTracks.addItemListener(this);
			add(checkViewAllTracks);
			// 音色選択
			p = new Panel();
			p.add(new Label(applet.getString("Program")));
			choiceProgram = new Choice();
			if (track!=9) {
				for (i=0; i<=127; i++) {
					choiceProgram.add(applet.getString("Program "+(i+1)));
				}
			}else {
				choiceProgram.add(applet.getString("Drum Kit"));
			}
			choiceProgram.select(applet.doc.tracks[track].program);
			choiceProgram.addItemListener(this);
			p.add(choiceProgram);
			add(p);
			// 編集単位
			p = new Panel();
			p.add(new Label(applet.getString("Edit Unit")));
			choiceEditUnit = new Choice();
			for (i=0; i<editUnits.length; i++) {
				choiceEditUnit.add(applet.getString("Edit Unit "+editUnits[i]));
				if (editUnits[i]==editUnit) choiceEditUnit.select(i);
			}
			choiceEditUnit.addItemListener(this);
			p.add(choiceEditUnit);
			add(p);
			// 終了
			validate();
		}
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource()==checkViewAllTracks) {
				viewAllTracks = checkViewAllTracks.getState();
				piano.dirty();
			}else if (e.getSource()==choiceProgram) {
				applet.doc.tracks[track].program = choiceProgram.getSelectedIndex();
				applet.noteOn(track, 60, 100);
			}else if (e.getSource()==choiceEditUnit) {
				editUnit = editUnits[choiceEditUnit.getSelectedIndex()];
			}
		}
	}
	class PanelNoteEdit extends Panel implements ItemListener, ActionListener {
		TextField textDuration = null;
		TextField textVelocity = null;
		public PanelNoteEdit() {
			Panel p;
			// 音の長さ
			textDuration = new TextField("",5);
			textDuration.addActionListener(this);
			p = new Panel(new GridLayout(1,0));
			p.add(new Label(applet.getString("Duration")));
			p.add(textDuration);
			add(p);
			// 音の強さ
			textVelocity = new TextField("",3);
			textVelocity.addActionListener(this);
			p = new Panel(new GridLayout(1,0));
			p.add(new Label(applet.getString("Velocity")));
			p.add(textVelocity);
			add(p);
			// 終了
			validate();
		}
		public void itemStateChanged(ItemEvent e) {
		}
		public void actionPerformed(ActionEvent e) {
			if (e.getSource()==textDuration) {
				if (selected!=null) {
					try {
						int d = Integer.parseInt(textDuration.getText());
						if (0<d && d<48*4*256) {
							selected.duration = d;
							piano.dirty();
						}
					}catch (NumberFormatException ex) {
					}
				}
			}else if (e.getSource()==textVelocity) {
				if (selected!=null) {
					try {
						int d = Integer.parseInt(textVelocity.getText());
						if (0<=d && d<=127) {
							selected.velocity = d;
							piano.dirty();
						}
					}catch (NumberFormatException ex) {
					}
				}
			}
		}
		public void refresh() {
			textDuration.setText(Integer.toString(selected.duration));
			textVelocity.setText(Integer.toString(selected.velocity));
		}
	}
	PanelPianoRoll piano = null;
	Scrollbar vscroll = null;
	Scrollbar hscroll = null;
	MIDIAPNote selected = null;
	Panel topPanel = null;
	PanelTrackParameter parameter = null;
	PanelNoteEdit noteEdit = null;
	CardLayout topLayout = null;
	public MIDIViewTrack(MIDIApplet parent, int track) {
		applet = parent;
		this.track=track;
		piano = new PanelPianoRoll();
		vscroll = new Scrollbar(Scrollbar.VERTICAL,127-piano.scrolly,1,0,128);
		hscroll = new Scrollbar(Scrollbar.HORIZONTAL,0,1,0,1920);
		topLayout = new CardLayout();
		topPanel = new Panel(topLayout);
		parameter = new PanelTrackParameter();
		noteEdit = new PanelNoteEdit();
		topPanel.add("param",parameter);
		topPanel.add("edit",noteEdit);
		topLayout.show(topPanel,"param");
		// 配置
		setLayout(new BorderLayout());
		removeAll();
		add("Center",piano);
		add("East",vscroll);
		add("South",hscroll);
		add("North", topPanel);
		validate();
		vscroll.addAdjustmentListener(this);
		hscroll.addAdjustmentListener(this);
	}

	public void adjustmentValueChanged(AdjustmentEvent e) {
		if (e.getSource()==vscroll) {
			piano.scrolly = 127-e.getValue();
			piano.dirty();
		}else if(e.getSource()==hscroll) {
			piano.scrollx = e.getValue();
			piano.dirty();
		}
	}

	public void select(MIDIAPNote note) {
		if (selected!=note) {
			selected = note;
			if (note==null) {
				topLayout.show(topPanel,"param");
			}else {
				noteEdit.refresh();
				topLayout.show(topPanel,"edit");
			}
			topPanel.validate();
			validate();
		}
	}

	public void refresh() {
		piano.repaint();
	}
}
