import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

public class MIDIDocument {
	protected MIDIApplet applet;
	int tempo = 120;;
	String title = "";
	String author = "";
	MIDIAPTrack tracks[] ={
			new MIDIAPTrack(),	new MIDIAPTrack(),	new MIDIAPTrack(),	new MIDIAPTrack(),
			new MIDIAPTrack(),	new MIDIAPTrack(),	new MIDIAPTrack(),	new MIDIAPTrack(),
			new MIDIAPTrack(),	new MIDIAPTrack(),	new MIDIAPTrack(),	new MIDIAPTrack(),
			new MIDIAPTrack(),	new MIDIAPTrack(),	new MIDIAPTrack(),	new MIDIAPTrack(),
	}; // �����ɂ܂���@��(�ł����U�@�ł͂���悤��)
	static int ID_NOTE = 0;
	static int ID_PROGRAM = 1;
	static int ID_VOLUME = 2;
	static int ID_PAN = 3;
	static int ID_TEMPO = 4;
	static int ID_CHARACTERCODE = 5;
	static int ID_TITLE = 6;
	static int ID_AUTHOR = 7;
	static int ID_END = -1;
	public MIDIDocument(MIDIApplet parent) {
		applet = parent;
	}
	// �f�[�^��CGI�Ɏ󂯓n��
	public void Send() {
	    if (applet.uri_save==null) return;
		// ���M�f�[�^�쐬
		if (getLength()<=0) {
			applet.showError("Data Too Short");
			return;
		}
		Sequence seq = createSequence(0);
		if (seq==null) {
			applet.showError("Failed to Get Sequence");
			return;
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		if ("323DIA01".equals(applet.getParameter("format"))) {
		    // ���t�H�[�}�b�g(MIDI+�o�b�N�A�b�v)
			// �w�b�_
			try {
			    stream.write("323DIA01".getBytes("US-ASCII"));
			}catch (IOException e) {
				applet.showError("Failed to Write Header");
				return;
			}
			// MIDI�f�[�^�{��
			try {
				ByteArrayOutputStream s = new ByteArrayOutputStream();
				MidiSystem.write(seq,1,s);
				stream.write(new byte[] { (byte)((s.size()>>0)&0xff),(byte)((s.size()>>8)&0xff),(byte)((s.size()>>16)&0xff),(byte)((s.size()>>24)&0xff), });
				s.writeTo(stream);
			}catch (IOException e) {
				applet.showError("Failed to Write MIDI");
				return;
			}
			// �o�b�N�A�b�v�f�[�^
			try {
				String s = getBackupString();
				stream.write(new byte[] { (byte)((s.length()>>0)&0xff),(byte)((s.length()>>8)&0xff),(byte)((s.length()>>16)&0xff),(byte)((s.length()>>24)&0xff), });
				stream.write(s.getBytes("US-ASCII"));
			}catch (IOException e) {
				applet.showError("Failed to Write Backup");
				return;
			}
		}else if ("323DIA02".equals(applet.getParameter("format"))) {
		    // ���t�H�[�}�b�g(�w�b�_+MIDI+�o�b�N�A�b�v)
			// �w�b�_
			try {
			    stream.write("323DIA02".getBytes("US-ASCII"));
			}catch (IOException e) {
				applet.showError("Failed to Write Header");
				return;
			}
			// �w�b�_�Q
			try {
				String s = applet.getParameter("header");
				stream.write(new byte[] { (byte)((s.length()>>0)&0xff),(byte)((s.length()>>8)&0xff),(byte)((s.length()>>16)&0xff),(byte)((s.length()>>24)&0xff), });
				stream.write(s.getBytes("US-ASCII"));
			}catch (IOException e) {
				applet.showError("Failed to Write Header");
				return;
			}
			// MIDI�f�[�^�{��
			try {
				ByteArrayOutputStream s = new ByteArrayOutputStream();
				MidiSystem.write(seq,1,s);
				stream.write(new byte[] { (byte)((s.size()>>0)&0xff),(byte)((s.size()>>8)&0xff),(byte)((s.size()>>16)&0xff),(byte)((s.size()>>24)&0xff), });
				s.writeTo(stream);
			}catch (IOException e) {
				applet.showError("Failed to Write MIDI");
				return;
			}
			// �o�b�N�A�b�v�f�[�^
			try {
				String s = getBackupString();
				stream.write(new byte[] { (byte)((s.length()>>0)&0xff),(byte)((s.length()>>8)&0xff),(byte)((s.length()>>16)&0xff),(byte)((s.length()>>24)&0xff), });
				stream.write(s.getBytes("US-ASCII"));
			}catch (IOException e) {
				applet.showError("Failed to Write Backup");
				return;
			}
		}else {
		    // MIDI�̂�
			try {
				MidiSystem.write(seq,1,stream);
			}catch (IOException e) {
				applet.showError("Failed to Write MIDI");
				return;
			}
		}
		
		// ���M!!
		Socket socket;
		String host = applet.uri_save.getHost();
		String path = applet.uri_save.getPath();
		int port = applet.uri_save.getPort();
		if (port==-1) port=80;
		try {
			socket = new Socket(host,port);
		}catch(Exception e) {
			applet.showError("Failed to Connect Server");
			return;
		}
		PrintWriter print;
		try {
			print = new PrintWriter(socket.getOutputStream());
		}catch (IOException e) {
			applet.showError("Failed");
			return;
		}
/*		print.println("POST "+path+" HTTP/1.1");
		print.println("Host: "+host+":80");
		print.println("Content-Type: multipart/form-data; boundary=__MIDIAP_BOUNDARY__");
		print.println("Content-Length: "+stream.size());
		print.println("Connection: close");
		print.println();
		print.println("__MIDIAP_BOUNDARY__");
		print.flush();
		try {
			stream.writeTo(socket.getOutputStream());
			print.println("__MIDIAP_BOUNDARY__");
			print.flush();
			socket.getOutputStream().flush();
		} catch(IOException e) {
			// �G���[�����߂�ǂ����[�I�����܂ł��Ď��s����킯�ˁ[�����I
			applet.showError("Failed to Upload");
			return;
		}*/
		print.println("POST "+path+" HTTP/1.1");
		print.println("Host: "+host+":"+Integer.toString(port));
		print.println("Content-Type: application/octet-stream");
		print.println("Content-Length: "+stream.size());
		print.println("Connection: close");
		print.println();
		print.flush();
		try {
			stream.writeTo(socket.getOutputStream());
			socket.getOutputStream().flush();
		} catch(IOException e) {
			// �G���[�����߂�ǂ����[�I�����܂ł��Ď��s����킯�ˁ[�����I
			applet.showError("Failed to Upload");
			return;
		}
		// CGI����̉���
		try {
			InputStreamReader isr = new InputStreamReader(socket.getInputStream(),"SJIS");
			BufferedReader br = new BufferedReader(isr);
			String line;
			if ((line = br.readLine()).indexOf(" 200 ")==-1) {
				// ���X�|���X�s
				applet.showError("Status Code must Be 200");
				return;
			}
			while (!(line = br.readLine()).equals("")) {
				// �w�b�_
			}
			while ((line = br.readLine()) != null) {
				// �f�[�^
				if (line.startsWith("Error: ")) {
					applet.messageOut(line);
					return;
				}else if (line.startsWith("Location: ")) {
				    applet.uri_exit = applet.getURI(line.substring("Location: ".length()));
				}
			}
			br.close();
			isr.close();
		}catch(Exception e) {
		}
		// �ʐM�I��
		try {
			socket.close();
		}catch (IOException e) {
		}
		//System.out.println("Save Completed!!");
		applet.doBackup = false;	// �ۑ����������f�[�^�Ȃ̂Ńo�b�N�A�b�v�s�v
		
		// �ړ�
	    if (applet.uri_exit==null) return;
		try {
			applet.getAppletContext().showDocument(applet.uri_exit.toURL());
		}catch (Exception ex) {
		}
	}
	// Sequence����
	Sequence createSequence(int starttime) {
		Sequence seq;
		try {
			seq = new Sequence(Sequence.PPQ,48);
		}catch (InvalidMidiDataException e) {
			applet.messageOut("Error: Failed to Create Sequence");
			return null;
		}
		insertInitTrack(seq, starttime);
		for (int i=0; i<16; i++) {
		    try {
				tracks[i].generateTrack(seq,i,starttime);
		    }catch (Exception ex){
				applet.messageOut("Error: "+ex.getMessage());
				return null;
		    }
		}
		return seq;
	}
	int getLength() {
		int length=0;
		int l;
		for (int i=0; i<tracks.length; i++) {
			l=tracks[i].getLength();
			if (length<l) length = l;
		}
		return length;
	}
	void insertInitTrack(Sequence seq, int offset) {
	    try {
			Track track = seq.createTrack();
			MetaMessage mmsg;
			// �e���|
			mmsg = new MetaMessage();
			int l = 60*1000*1000/tempo;
			mmsg.setMessage(0x51, 
                    new byte[]{(byte)((l>>16)&0xff), (byte)((l>>8)&0xff), (byte)((l>>0)&0xff)}, 
                    3);
			track.add(new MidiEvent(mmsg, 0));
			// ���
			if (author.length()>0) {
			    byte buf[] = applet.getBytes(author);
				mmsg = new MetaMessage();
				mmsg.setMessage(0x02,
	                    buf, 
	                    buf.length);
				track.add(new MidiEvent(mmsg, 0));
			}
			// �Ȗ�
			if (title.length()>0) {
			    byte buf[] = applet.getBytes(title);
				mmsg = new MetaMessage();
				mmsg.setMessage(0x03,
	                    buf, 
	                    buf.length);
				track.add(new MidiEvent(mmsg, 0));
			}
	    } catch (Exception e) {
	    }
	}
	
	String getBackupString() {
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			DataOutputStream dout = new DataOutputStream(new DeflaterOutputStream(new HexStringOutputStream(bout)));
			// �e���|
			dout.write(ID_TEMPO);
			dout.writeInt(tempo);
			// �f�[�^
			for (int i=0; i<16; i++) {
				// ���F
				dout.writeByte(ID_PROGRAM);
				dout.writeByte(i);
				dout.writeByte(tracks[i].program);
				// �m�[�g
				Iterator it = tracks[i].notes.iterator();
				MIDIAPNote note;
				while (it.hasNext()) {
					note = (MIDIAPNote)it.next();
					dout.writeByte(ID_NOTE);
					dout.writeByte(i);
					dout.writeInt(note.start);
					dout.writeInt(note.duration);
					dout.writeByte(note.note);
					dout.writeByte(note.velocity);
				}
			}
			dout.close();
			return new String(bout.toByteArray(),"US-ASCII");
		}catch(Exception ex) {
			System.out.println(ex.toString());
			return null;
		}
	}
	void restoreFromBackup(String data) {
		MIDIAPTrack tracks[] ={
				new MIDIAPTrack(),	new MIDIAPTrack(),new MIDIAPTrack(),	new MIDIAPTrack(),
				new MIDIAPTrack(),	new MIDIAPTrack(),new MIDIAPTrack(),	new MIDIAPTrack(),
				new MIDIAPTrack(),	new MIDIAPTrack(),new MIDIAPTrack(),	new MIDIAPTrack(),
				new MIDIAPTrack(),	new MIDIAPTrack(),new MIDIAPTrack(),	new MIDIAPTrack(),
		};
		DataInputStream din = new DataInputStream(new InflaterInputStream(new HexStringInputStream(data)));
		int id;
		while (true) {
			try {
				id = din.read();
			}catch (IOException ex) {
				applet.showError("Invalid Data");
				return;
			}
			try {
				if (id==ID_NOTE) {
					tracks[din.read()].addNote(new MIDIAPNote(din.readInt(),din.readInt(),din.read(),din.read()));
				}else if (id==ID_PROGRAM) {
					tracks[din.read()].program = din.read();
				}else if (id==ID_TEMPO) {
				    tempo = din.readInt();
				}else if (id==ID_END) {
					break;
				}else {
					throw new Exception();
				}
			}catch (Exception ex) {
				applet.showError("Invalid Data");
				return;
			}
		}
		this.tracks = tracks;
		applet.messageOut("Complete");
	}
}
