import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;

public class MIDIAPNote {
	protected int start;
	protected int duration;
	protected int note;
	protected int velocity;
	
	public MIDIAPNote(int start,int duration,int note,int velocity) {
		this.start = start;
		this.duration = duration;
		this.note = note;
		this.velocity = velocity;
	}
	public ShortMessage getNoteOnMessage(int channel) {
		ShortMessage msg = new ShortMessage();
		try {
			msg.setMessage(ShortMessage.NOTE_ON, channel, note, velocity);
		}catch (InvalidMidiDataException e) {
			return null;
		}
		return msg;
	}
	public ShortMessage getNoteOffMessage(int channel) {
		ShortMessage msg = new ShortMessage();
		try {
			msg.setMessage(ShortMessage.NOTE_OFF, channel, note, velocity);
		}catch (InvalidMidiDataException e) {
			return null;
		}
		return msg;
	}
	// MIDI�t�@�C���ɏ����o���Ƃ��K�v
	public MidiEvent getNoteOnEvent(int channel, int offset) {
		return new MidiEvent(getNoteOnMessage(channel),start-offset);
	}
	public MidiEvent getNoteOffEvent(int channel, int offset) {
		return new MidiEvent(getNoteOffMessage(channel),start+duration-offset);
	}
	public int hashCode() {
		// �����ꏊ�ɓ����m�[�g��u���̂�h�����ߕK�v
		return (start<<7)|(note&0x7f);
	}
	public int getStart() {
		return start;
	}
	public int getDuration() {
		return duration;
	}
	public int getEnd() {
		return start+duration;
	}
	public int getNote() {
		return note;
	}
	public String toString() {
		return "�J�n�F"+start+" �����F"+duration+" �����F"+note+" �傫���F"+velocity+" �n�b�V���F"+hashCode();
	}
}
