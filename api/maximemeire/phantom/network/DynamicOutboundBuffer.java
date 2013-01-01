package maximemeire.phantom.network;

import org.jboss.netty.buffer.DynamicChannelBuffer;

/**
 * This class represents a DynamicChannelBuffer with added data type
 * methods. This buffer is mainly used for outbound data.
 * @author Maxime Meire
 *
 */
public class DynamicOutboundBuffer extends DynamicChannelBuffer {
	
	private int bitPos = 0;
	
    private static int[] bitMask = new int[32];


	/**
	 * Creates a new OutboundBuffer with the specified estimated length.
	 * @param estimatedLength The estimated capacity of this buffer.
	 */
	public DynamicOutboundBuffer(int estimatedLength) {
		super(estimatedLength);
	}
	
	public DynamicOutboundBuffer(int estimatedLength, boolean updateBuffer) {
		super(estimatedLength);
		for (int i = 0; i < estimatedLength; i++) {
			writeByte(0);
		}
		writerIndex(0);
	}

	public DynamicOutboundBuffer(byte[] message) {
		super(message.length);
		writeBytes(message);
	}

	/**
	 * Writes one byte to this buffer and increases the writer index by one.
	 * @param i The value of the byte to write to this buffer.
	 * @return This buffer.
	 */
	public DynamicOutboundBuffer put8(int i) {
		super.writeByte((byte) i);
		return this;
	}
	
	public DynamicOutboundBuffer put8_n(int i) {
		return put8(-i);
	}
	
	public DynamicOutboundBuffer put8_p128(int i) {
		return put8(i + 128);
	}

	/**
	 * Writes one short to this buffer and increases the writer index by two.
	 * @param i The value of the short to write to this buffer.
	 * @return This buffer.
	 */
	public DynamicOutboundBuffer put16(int i) {
		super.writeShort((short) i);
		return this;
	}
	
	public DynamicOutboundBuffer put16_p128(int i) {
		return put8(i >> 8)
		.put8(i + 128);
	}
	
	public DynamicOutboundBuffer put16_le(int i) {
		return put8(i)
		.put8(i >> 8);
	}
	
	public DynamicOutboundBuffer put16_le_p128(int i) {
		return put8(i + 128)
		.put8(i >> 8);
	}

	/**
	 * Writes one int to this buffer and increases the writer index by two.
	 * @param i The value of the int to write to this buffer.
	 * @return This buffer.
	 */
	public DynamicOutboundBuffer put32(int i) {
		super.writeInt((int) i);
		return this;
	}
	
	public DynamicOutboundBuffer put32_mbe(int i) {
		return put8(i >> 16)
		.put8(i >> 24)
		.put8(i)
		.put8(i >> 8);
	}
	
	public DynamicOutboundBuffer put64(long l) {
		super.writeLong(l);
		return this;
	}
	
	public DynamicOutboundBuffer start_bit_access() {
		bitPos = writerIndex() << 3;
		return this;
	}
	
	public DynamicOutboundBuffer end_bit_access() {
		writerIndex((bitPos + 7) >> 3);
		return this;
	}
	
	private DynamicOutboundBuffer XORBit(int position, int mask) {
		setByte(position, (byte) (getByte(position) & ~mask));
		return this;
	}
	
	private DynamicOutboundBuffer ANDBit(int position, int mask) {
		setByte(position, (byte) (getByte(position) | mask));
		return this;
	}
	
	public DynamicOutboundBuffer writeBits(int length, int value) {
		int bytePos = bitPos >> 3;
		int bitOffset = 8 - (bitPos & 7);
		bitPos += length;
		for (; length > bitOffset; bitOffset = 8) {
			XORBit(bytePos, bitMask[bitOffset]);
			ANDBit(bytePos++, (value >> (length - bitOffset)) & bitMask[bitOffset]); 
			length -= bitOffset;
		}
		if (length == bitOffset) {
			XORBit(bytePos, bitMask[bitOffset]);
			ANDBit(bytePos, value & bitMask[bitOffset]);
		} else {
			XORBit(bytePos, bitMask[length] << (bitOffset - length));
			ANDBit(bytePos, (value & bitMask[bitOffset]) << (bitOffset - length));
		}
		return this;
	}
	
	static {
		for (int i = 0; i < 32; i++) {
			bitMask[i] = (1 << i) - 1;
		}
	}

}
