package memory;
import java.util.LinkedList;

/**
 * This memory model allocates memory cells based on the first-fit method. 
 * 
 * @author "Johan Holmberg, Malmö university"
 * @since 1.0
 */
public class FirstFit extends Memory {
	LinkedList<MemArea> memAreas;
	/**
	 * Initializes an instance of a first fit-based memory.
	 * 
	 * @param size The number of cells.
	 */
	public FirstFit(int size) {
		super(size);
		memAreas = new LinkedList<>();
		memAreas.addFirst(new MemArea(0, size, Status.FREE));
	}

	/**
	 * Allocates a number of memory cells. 
	 * 
	 * @param size the number of cells to allocate.
	 * @return The address of the first cell.
	 */
	@Override
	public Pointer alloc(int size) {
		Pointer returnValue = null;
			for (MemArea memArea : memAreas) {
				if (size < memArea.getSize() && memArea.getStatus() == Status.FREE) {
					returnValue = new Pointer(memArea.startValue, new RawMemory(size));
					memArea.setStatus(Status.ALLOCATED);
					memArea.setSize(memArea.getSize() - size);
					MemArea newMemArea = new MemArea(memArea.getStartValue() + size, memArea.getSize() - size, Status.FREE);
					memAreas.add(memArea.getStartValue()+1, newMemArea);
					break;
				}
				else if (size == memArea.getSize() && memArea.getStatus() == Status.FREE){
					returnValue = new Pointer(memArea.startValue, new RawMemory(size));
					memAreas.remove(memArea);
					break;
				}
			}
		return returnValue;
	}
	
	/**
	 * Releases a number of data cells
	 * 
	 * @param p The pointer to release.
	 */
	@Override
	public void release(Pointer p) {
		int startAddress = p.pointsAt();
		for (MemArea memArea : memAreas) {
			if(memArea.startValue == startAddress){
				if(memAreas.getFirst() == memArea && memAreas.getLast() != memArea){ //Checks if it is the first within the list and not last
					if(memAreas.get(memAreas.indexOf(memArea)+1).getStatus() == Status.FREE){ //If status of next is FREE
						memArea.setSize(memArea.getSize() + memAreas.get(memAreas.indexOf(memArea)+1).getSize()); //Add the size of next FREE obj
						memAreas.remove(memAreas.indexOf(memArea)+1); //Removes the next obj
					} else if (memAreas.getLast() == memArea && memAreas.getFirst() != memArea){ //Checks if its the last within the list and not first
						if(memAreas.get(memAreas.indexOf(memArea)-1).getStatus() == Status.FREE) { //If status of previous is FREE
							memArea.setSize(memArea.getSize() + memAreas.get(memAreas.indexOf(memArea) - 1).getSize()); //Add the size of previous FREE obj
							memArea.setStartValue(memAreas.get(memAreas.indexOf(memArea)-1).getStartValue()); //Sets startvalue of the previous
							memAreas.remove(memAreas.indexOf(memArea) - 1); //Removes the previous obj
						}
					}
				}else{
					if(memAreas.get(memAreas.indexOf(memArea)+1).getStatus() == Status.FREE && memAreas.get(memAreas.indexOf(memArea)-1).getStatus() == Status.FREE) {
						//Both previous and next is FREE
						memArea.setSize(memArea.getSize() + memAreas.get(memAreas.indexOf(memArea) +1).getSize() + memAreas.get(memAreas.indexOf(memArea) - 1).getSize()); //Add the size of previous and next FREE obj
						memArea.setStartValue(memAreas.get(memAreas.indexOf(memArea) - 1).getStartValue()); //Sets startvalue of the previous
						memAreas.remove(memAreas.indexOf(memArea) - 1); //Removes previous
						memAreas.remove(memAreas.indexOf(memArea) + 1); //Removes next
					}
				}
			}
		}
	}
	
	/**
	 * Prints a simple model of the memory. Example:
	 * 
	 * |    0 -  110 | Allocated
	 * |  111 -  150 | Free
	 * |  151 -  999 | Allocated
	 * | 1000 - 1024 | Free
	 */
	@Override
	public void printLayout() {
		for (MemArea memArea : memAreas) {
			System.out.println("| " + memArea.startValue + " - " + (memArea.size-memArea.startValue+1) + " | " + memArea.getStatus());
		}
	}
	
	/**
	 * Compacts the memory space.
	 */
	public void compact() {
		// TODO Implement this!
	}

	public class MemArea {
		private int startValue;
		private int size;
		private Status status;

		public Status getStatus() {
			return status;
		}

		public void setStatus(Status status) {
			this.status = status;
		}
		public MemArea(int startValue, int size, Status status){
			this.startValue = startValue;
			this.size = size;
			this.status = status;
		}

		public int getStartValue() {
			return startValue;
		}

		public void setStartValue(int startValue) {
			this.startValue = startValue;
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}
	}
}
