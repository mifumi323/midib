import java.io.IOException;
import java.io.InputStream;

public class HexStringInputStream extends InputStream {
	protected char chr[];
	protected int off = 0;
	public HexStringInputStream(char data[]) {
		chr = data;
	}
	public HexStringInputStream(String data) {
		chr = data.toCharArray();
	}
	public int read() throws IOException {
		int l,r;
		char c;
		while (true) {
			if (off>=chr.length-1) throw new IOException("Data End");
			c = chr[off];
			off++;
			if ('0'<=c&&c<='9') { l=c-'0'; }
			else if ('a'<=c&&c<='f') { l=c-'a'+0xA; }
			else if ('A'<=c&&c<='F') { l=c-'A'+0xA; }
			else { continue; }
			break;
		}
		while (true) {
			if (off>=chr.length) throw new IOException("Data End");
			c = chr[off];
			off++;
			if ('0'<=c&&c<='9') { r=c-'0'; }
			else if ('a'<=c&&c<='f') { r=c-'a'+0xA; }
			else if ('A'<=c&&c<='F') { r=c-'A'+0xA; }
			else { continue; }
			break;
		}
		return (l<<4)|r;
	}
}
