import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HexStringOutputStream extends FilterOutputStream {
	public void write(int b) throws IOException {
		int a=(b>>4)&0xF;
		out.write((a<0xA)?('0'+a):('A'+a-0xA));
		a=b&0xF;
		out.write((a<0xA)?('0'+a):('A'+a-0xA));
	}
	public HexStringOutputStream(OutputStream out) {
		super(out);
	}
	static public String toString(byte b[]) {
		try {
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			HexStringOutputStream hs = new HexStringOutputStream(bs);
			hs.write(b);
			hs.close();
			return new String(bs.toByteArray(),"US-ASCII");
		}catch (Exception ex) {
			return null;
		}
	}
}
