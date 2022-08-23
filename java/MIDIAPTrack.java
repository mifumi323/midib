import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class MIDIAPTrack {
	int program = 0;
	TreeSet notes = new TreeSet(new Comparator(){
		public int compare(Object o1, Object o2){
			return o1.hashCode()-o2.hashCode();
		}
	});
	public void generateTrack(Sequence seq, int channel, int offset) throws Exception {
	    if (notes.size()==0) return;	// 音符ないやん！
		Track track = seq.createTrack();
		insertInitMessage(track, channel, offset);
		Iterator it = notes.iterator();
		MIDIAPNote note;
		long noteOffTime[] = new long[128];
		while (it.hasNext()) {
			note = (MIDIAPNote)it.next();
			if (note.start<offset) continue;
			if (note.getStart()<noteOffTime[note.getNote()]) throw new Exception("Note Overrapped");
			track.add(note.getNoteOnEvent(channel, offset));
			track.add(note.getNoteOffEvent(channel, offset));
			noteOffTime[note.getNote()] = note.getEnd();
		}
	}
	public void insertInitMessage(Track track, int channel, int offset) {
		int initPos = offset>=0?0:-offset/2;
		track.add(new MidiEvent(getProgramMessage(channel),initPos));
	}
	public ShortMessage getProgramMessage(int i) {
		ShortMessage msg = new ShortMessage();
		try {
			msg.setMessage(ShortMessage.PROGRAM_CHANGE, i, program, 0);
		}catch (InvalidMidiDataException e) {
			return null;
		}
		return msg;
	}
	public void addNote(MIDIAPNote n) {
		if (n!=null) notes.add(n);
	}
	public void removeNote(MIDIAPNote n) {
		if (n!=null) notes.remove(n);
	}
	public int getLength() {
		try {
			MIDIAPNote n2=(MIDIAPNote)notes.last();
			return n2.getStart();
		}catch (NoSuchElementException e) {
			return 0;
		}
	}
	public Sequence createNoteOnTrack(int channel, int note, int vel) {
		Sequence seq;
		try {
			seq = new Sequence(Sequence.PPQ,48);
		}catch (InvalidMidiDataException e) {
			System.out.println("Error: Failed to Create Sequence");
			return null;
		}
		// トラックを作る
		Track track = seq.createTrack();
		insertInitMessage(track, channel, 0);
		MIDIAPNote n = new MIDIAPNote(0,192,note,vel);
		track.add(n.getNoteOnEvent(channel,0));
		track.add(n.getNoteOffEvent(channel,0));
		return seq;
	}
}
