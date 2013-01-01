package maximemeire.phantom.network;

import java.math.BigInteger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * This buffer wraps a ChannelBuffer and is primarily used for
 * reading purposes.
 * @author Maxime Meire
 *
 */
public class InboundBuffer {

	private ChannelBuffer buffer;
	
	/**
	 * Creates a new InboundBuffer with a dynamic buffer.
	 * @param estimatedLength The estimated length of the buffers capacity.
	 */
	public InboundBuffer(int estimatedLength) {
		buffer = ChannelBuffers.dynamicBuffer(estimatedLength);
	}
	
	/**
	 * Creates a new InboundBuffer with the specified ChannelBuffer.
	 * @param buffer The ChannelBuffer to wrap in this InboundBuffer.
	 */
	public InboundBuffer(ChannelBuffer buffer) {
		this.buffer = buffer;
	}

	/**
	 * Creates a new InboundBuffer with a dynamic buffer containing the provided source bytes.
	 * @param src The byte array containing the bytes to add to this buffer.
	 */
	public InboundBuffer(byte[] src) {
		buffer = ChannelBuffers.dynamicBuffer(src.length);
		buffer.writeBytes(src);
	}

	public void setBuffer(ChannelBuffer buffer) {
		this.buffer = buffer;
	}

	public ChannelBuffer getBuffer() {
		return buffer;
	}
	
	/**
	 * Whether there are still bytes left in this buffer to read.
	 * @return True if there are still bytes to read.
	 */
	public boolean readable() {
		return buffer.readable();
	}
	
	/**
	 * The amount of readable bytes left in this buffer.
	 * @return The amount of readable bytes.
	 */
	public int readableBytes() {
		return buffer.readableBytes();
	}
	
	/**
	 * Returns the current reader index of this buffer.
	 * @return The reader index of this buffer.
	 */
	public int readerIndex() {
		return buffer.readerIndex();
	}
	
	/**
	 * Sets the reader index of this buffer.
	 * @param readerIndex The index to set.
	 */
	public void readerIndex(int readerIndex) {
		buffer.readerIndex(readerIndex)	;	
	}
	
	/**
	 * Returns the current writer index of this buffer.
	 * @return The writer index of this buffer.
	 */
	public int writerIndex() {
		return buffer.writerIndex();
	}

	/**
	 * Increases the current readerIndex by the specified length in this buffer.
	 * @param length
	 */
	public void skipBytes(int length) {
		buffer.skipBytes(length);
	}
	
	/**
	 * Reads one byte and increases the reader index by one.
	 * @return The byte at the current reader index.
	 */
	public byte read8() {
		return buffer.readByte();
	}
	
	/**
	 * Reads one unsigned byte and increases the reader index by one.
	 * @return The byte at the current reader index made unsigned.
	 */
	public short readu8() {
		return buffer.readUnsignedByte();
	}
	
	/**
	 * Reads one short and increases the reader index by two.
	 * @return The short at the current reader index.
	 */
	public short read16() {
		return buffer.readShort();
	}
	
	/**
	 * Reads one unsigned short and increases the reader index by two.
	 * @return The short at the current reader index made unsigned.
	 */
	public int readu16() {
		return buffer.readUnsignedShort();
	}
	
	/**
	 * Reads one int and increases the reader index by four.
	 * @return The int read from the current reader index.
	 */
	public int read32() {
		return buffer.readInt();
	}
	
//	/**
//	 * Reads one int and increases the reader index by four.
//	 * @return The int read from the current reader index.
//	 */
//	public int read32() {
//		return (read8() << 24) 
//		+ (read8() << 16) 
//		+ (read8() << 8) 
//		+ read8();
//	}
	
	/**
	 * Reads a 24-bit medium int and increases the reader index by three.
	 * @return The 24-bit medium int.
	 */
	public int read24() {
		return buffer.readMedium();
	}

//	/**
//	 * Reads a 24-bit int and increases the reader index by three.
//	 * @return The 24-bit int.
//	 */
//	public int read24() {
//		return ((read8() << 16) & 0xffffff)
//		+ ((read8() << 8) & 0xffff)
//		+ (read8() & 0xff);
//	}
	
	/**
	 * Reads a long and increases the reader index by eight.
	 * @return The long.
	 */
	public long readLong() {
		return buffer.readLong();
	}
	
	/**
	 * Reads several smarts.
	 * @return The smarts together.
	 */
	public int readSmarts() {
		int i = 0;
		int j;
		for (j = readSmart(); j == 32767;) {
			j = readSmart();
			i += 32767;
		}
		i += j;
		return i;
	}

	/**
	 * Reads a smart.
	 * @return The smart.
	 */
	public int readSmart() {
		int i = buffer.getByte(buffer.readerIndex());
		if (i >= 128) {
			return readu16() - 32768;
		} else {
			return readu8();
		}
	}
	
	/**
	 * Reads a null terminated string.
	 * @return The string.
	 */
	public String readString() {	
		String s = "";
		int i = 0;
		while ((i = buffer.readByte()) != 10) {
			s += (char) i;
		}
		return s;
	}

	/**
	 * Transfers the specified source array's data to this 
	 * buffer starting at the current writerIndex and increases 
	 * the writerIndex by the number of the transferred bytes (= src.length).
	 * @param src The byte array to transfer to our buffer.
	 */
	public void writeBytes(byte[] src) {
		buffer.writeBytes(src);
	}

	/**
	 * Transfers this buffer's data to the specified destination starting 
	 * at the current readerIndex and increases the readerIndex by the 
	 * number of the transferred bytes (= dst.length).
	 * @param dst The destination byte array.
	 */
	public void readBytes(byte[] dst) {
		buffer.readBytes(dst);
	}

	/**
	 * Transfers this buffer's data to the specified destination starting 
	 * at the current readerIndex and increases the readerIndex by the number 
	 * of the transferred bytes (= length).
	 * @param dst The destination byte array.
	 * @param dstIndex The first index of the destination.
	 * @param length The number of bytes to transfer.
	 */
	public void readBytes(byte[] dst, int dstIndex, int length) {
		buffer.readBytes(dst, dstIndex, length);
	}
	
	/**
	 * This method will decrypt a xtea encrypted buffer.</br>
	 * NOTE: It's not working correctly.
	 * @param keys The XTEA key.
	 * @param start The start offset.
	 * @param end The end offset.
	 */
	public final void decodeXTEA(int keys[], int start, int end) {
		int l = buffer.readerIndex();
		buffer.readerIndex(start);
		int i1 = (end - start) / 8;
		for (int j1 = 0; j1 < i1; j1++) {
			int k1 = buffer.readInt();
			int l1 = buffer.readInt();
			int sum = 0xc6ef3720;
			int delta = 0x9e3779b9;
			for (int k2 = 32; k2-- > 0;) {
				l1 -= keys[(sum & 0x1c84) >>> 11] + sum ^ (k1 >>> 5 ^ k1 << 4) + k1;
				sum -= delta;
				k1 -= (l1 >>> 5 ^ l1 << 4) + l1 ^ keys[sum & 3] + sum;
			}
			buffer.readerIndex(buffer.readerIndex() - 8);
			buffer.writeInt(k1);
			buffer.writeInt(l1);
		}
		buffer.readerIndex(l);
	}

	/**
	 * Decrypts the given byte array assuming it was encrypted using RSA.
	 * This method expects the keys to be know.
	 * @param rsaEncryptedBuffer The encrypted buffer.
	 * @return Returns an InboundBuffer with the decrypted bytes.
	 */
	public static InboundBuffer decryptRSA(byte[] rsaEncryptedBuffer) {
		return new InboundBuffer(new BigInteger(rsaEncryptedBuffer).modPow(RSA_PRIVATE_KEY, RSA_MODULUS).toByteArray());
	}
	
	private final static BigInteger RSA_PRIVATE_KEY = new BigInteger("32792974422771952149983169688418851030571234042141058500313933792513926737111297943590683578172234554902874296444670507414031483442072180460981958576135623925626646812469615205909263640438817628163300151659877451331250726212605492878645438325222159588885336603507873519790507287601059856618982289555213201859");
	private final static BigInteger RSA_MODULUS = new BigInteger("65702660471943717364951205843233019130654682035351261460234521855430680346187087956787659335897489277937235277791900652063749026014018256089953242240190583481342446743609052208225869097753926506822459561174824250600959617147170935502081236213981939318134483061729886291630361616294923370988264946374455932549");
	@SuppressWarnings("unused")
	private final static BigInteger RSA_PUBLIC_KEY = new BigInteger("118689124517978949703152716765824750628298687777586304370341833767231736339124500271507610468655603039363754867397838713717468244488269960212535717187824928062965426657330513025187712197725580334464378157401885476940642683963969285614832959824498794231610478486739637554449909814357080753525538767863291166955");

}
