package memory;

/**
 * Simulates a memory space, made out of a number of memory cells. Each cell
 * holds an int value.
 * 
 * @author "Johan Holmberg, Malmö university"
 * @since 1.0
 */
class RawMemory {
	protected int[] cells;
	protected int offset = 0;
	
	/**
	 * Initializes a memory instance.
	 * 
	 * @param size The number of cells in the memory.
	 */
	public RawMemory(int size) {
		if (size < 1) {
			size = 1;
		}
		cells = new int[size];
	}
	
	/**
	 * Writes a piece of data to the memory. If you are using an offset,
	 * you need to take this into consideration when writing data. That
	 * means, that if you're writing metadata before the "real" data, you
	 * will have to negatively offset the address to reach the desired
	 * memory cells.
	 * 
	 * @param address The address to write to. 
	 * @param data The data to write.
	 */
	protected void write(int address, int[] data) {
		try {
			int limit = data.length + offset;
			for (int i = offset; i < limit; i++) {
				cells[address + i] = data[i - offset];
			}
		} catch (ArrayIndexOutOfBoundsException e) {}
	}
	
	/**
	 * Reads a piece of data from the memory.
	 * 
	 * @param address The address to read from.
	 * @param length The number of cells to read.
	 * 
	 * @return The data.
	 */
	protected int[] read(int address, int length) {
		int[] data = new int[length];
		try {
			for (int i = 0; i < length; i++) {
				data[i] = cells[address + i];
			}
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		return data;
	}

	/*
	 * Sets the offset for writing data. Note that this doesn't affect the
	 * reading method.
	 *
	 * @param offset The write offset used by the write() function.
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		for (int data : cells) {
			sb.append(data + "\n");
		}
		
		return sb.toString();
	}
}
