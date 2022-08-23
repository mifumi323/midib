import java.applet.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.sound.midi.*;

public class MIDIApplet extends Applet {
	MIDIDocument	doc = null;
	MIDIView		view = null;
	MIDIAPToolbar	toolbar = null;
	URI				uri_save = null;
	URI				uri_exit = null;
	URI				uri_language = null;

	Sequencer sequencer = null;
	int currenttime = 0;
//	String backup = "";
	boolean doBackup;

	HashMap stringTable = new HashMap();
	
	// アプレットの基本機能
	public void init()
	{
		// アプレットがシステムにロードされた
//	    System.out.println("init:"+backup);
	}
	public void start()
	{
		// アプレットの実行を開始
		// パラメータ取得
		try {
			uri_save = getURI(getParameter("url_save"));
		}catch (Exception e) {
		}
		try {
			uri_exit = getURI(getParameter("url_exit"));
		}catch (Exception e) {
		}
		try {
			uri_language = getURI(getParameter("url_language"));
		}catch (Exception e) {
		}
		// 文字列テーブルを読み込む
		setStringTable();
		// 画面の構築
		removeAll();
		doc = new MIDIDocument(this);
		if (getParameter("data")!=null) {
		    doc.restoreFromBackup(getParameter("data"));
		}
		toolbar = new MIDIAPToolbar(this);
		setLayout(new BorderLayout());
		add("North",toolbar);
		changeView(new MIDIViewTrack(this,0));
		// 自動バックアップ
/*		doBackup = "true".equals(getParameter("autobackup"));
		if (doBackup) {
		    System.out.println("start:"+backup);
			if (backup.length()>0) {
			    MIDIViewBackup v = new MIDIViewBackup(this);
			    v.textArea.setText(backup);
			    v.add("North", new Label(getString("you can restore midi from backup")));
			    changeView(v);
			}
		}*/
		// MIDIの初期化
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
		} catch (MidiUnavailableException e) {
			System.out.println("fail to initialize sequencer");
			sequencer = null;
			return;
		}
	}
	public void destroy()
	{
		// 割り当てられたすべてのリソースを破棄する必要がある
//	    System.out.println("destroy:"+backup);
	}
	public void stop()
	{
		// アプレットの実行を停止
		midiStop();	// こうしないとブラウザで戻ったときなどに困る
		if (sequencer!=null) {
			sequencer.close();
		}
/*		if (doBackup) {
		    if (doc.getLength()>0) {
		        backup = doc.getBackupString();
		    }
		    System.out.println("stop:"+backup);
		}*/
	}

	// 表示一般
	public void changeView(MIDIView v)
	{
		if (view!=null) remove(view);
		view = v;
		add("Center",view);
		// これを忘れちゃいかん！！必須！！
		validate();
	}
	public void messageOut(String message)
	{
		changeView(new ViewMessage(this, message));
		//Frame f = new Frame(message);
		//f.setVisible(true);
	}
	public void showError(String message)
	{ messageOut("Error: "+message); }
	public void showFatalError(String message)
	{
		removeAll();
		view = null;
		add(new ViewMessage(this, message));
		validate();
	}

	// データ周り
	public void setStringTable() {
		stringTable.clear();
		if (uri_language==null) return;
		try {
			Socket socket = new Socket(uri_language.getHost(),80);
			InputStreamReader isr = new InputStreamReader(socket.getInputStream(),"SJIS");
			BufferedReader br = new BufferedReader(isr);
			PrintWriter print = new PrintWriter(socket.getOutputStream());
			print.println("GET "+uri_language.getPath()+" HTTP/1.1");
			print.println("Host: "+uri_language.getHost()+":80");
			print.println("Connection: close");
			print.println();
			print.flush();
			socket.getOutputStream().flush();
			String line, key, val;
			if (br.readLine().indexOf(" 200 ")==-1) return;
			while (!(line = br.readLine()).equals("")) {}
			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, "\t\r\n");
				if (st.hasMoreTokens()) key=st.nextToken(); else continue;
				if (st.hasMoreTokens()) val=st.nextToken(); else continue;
				stringTable.put(key, val);
			}
			br.close();
			isr.close();
			socket.close();
		}catch (Exception ex) {
			// ここではエラーが起こっても気にしない
			System.out.println(ex.getClass().toString()+" / "+ex.getMessage());
		}
	}
	public String getString(String key) {
		String value = (String)stringTable.get(key);
		return value!=null?value:key;
	}
	public byte[] getBytes(String string) {
	    return string.getBytes();
	}
	public URI getURI(String string) {
	    try {
	    	return string.startsWith("http://") ? new URI(string) : new URI(getCodeBase()+string);
	    }catch(URISyntaxException e) {
	        return null;
	    }
	}
	
	// 演奏周り
	// 演奏
	void midiPlay(int starttime) {
		if (sequencer!=null) {
			try {
			    Sequence seq = doc.createSequence(starttime);
				if (seq != null) {
				    sequencer.setSequence(seq);
					sequencer.start();
				}
			}catch (InvalidMidiDataException e) {
				messageOut("Error: Failed to Play Sequence");
				return;
			}
		}
	}
	// 停止
	void midiStop() {
		if (sequencer!=null) {
			if (sequencer.isRunning()) {
				sequencer.stop();
			}
		}
	}
	// トラック内の音を鳴らす
	public void noteOn(int track, int note, int vel) {
		// 演奏
		if (sequencer!=null) {
			try {
				sequencer.setSequence(doc.tracks[track].createNoteOnTrack(track,note,vel));
				sequencer.start();
			}catch (InvalidMidiDataException e) {
				System.out.println("Error: Failed to Play Sequence");
				return;
			}
		}
	}
	// トラック内の音を消す
	public void noteOff(int track, int note) {
		if (sequencer!=null) {
			if (sequencer.isRunning()) {
				sequencer.stop();
			}
		}
	}
}
