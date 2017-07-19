
import sun.misc.Lock;

/**
 * The AbstractFIFO class provides a special FIFO
 * implementation that supports user-defined priority.
 * NOTE: Classes must override equals method.
 */
public class AbstractFIFO{
	
	private Object[] elements;
	private int nrOfElements;
	
	private int[] priorities;
	
	private int arrayIncrementSize = 50;
	private int arrayCurrentLength;
	
	private Lock lock;
	
	public AbstractFIFO(){
		lock = new Lock();
		elements = new Object[arrayIncrementSize];
		priorities = new int[arrayIncrementSize];
		nrOfElements = 0;
		arrayCurrentLength = arrayIncrementSize;
	}
	
	/*
	 * Inserts an element in the FIFO 
	 * with a given priority.
	 * 
	 * Time Complexity: O(1)
	 */
	public void insert(Object element, int priority){
		try {
			lock.lock();
			
			// check if there is space for the new element
			// otherwise elements will be copied into a bigger array.
			if(nrOfElements == arrayCurrentLength){
				arrayCurrentLength += arrayIncrementSize;
				Object[] biggerElementArray = new Object[arrayCurrentLength];
				for(int i=0; i<nrOfElements; ++i){
					biggerElementArray[i] = elements[i];
				}
				elements = biggerElementArray;
			}
			
			// Insert the last element and it's priority.
			elements[nrOfElements]=element;
			priorities[nrOfElements]=priority;
			nrOfElements++;
			
		} catch (InterruptedException e) {
			System.out.println("Thread was interrupted");
		}finally{
			lock.unlock();
		}
		
	}
	
	/*
	 * Changes the priority of an element 
	 * in the FIFO to the given priority.
	 * 
	 * Time Complexity: O(n)
	 */
	public void changePriority(Object element, int priority){
		try {
			lock.lock();
			
			for(int i=0; i<nrOfElements; ++i){
				if(elements[i].equals(element)){
					priorities[i]=priority;
				}
			}		
		} catch (InterruptedException e) {
			System.out.println("Thread was interrupted");
			//e.printStackTrace();
		}finally{
			lock.unlock();
		}
	}
	
	/*
	 * Returns the next element 
	 * and removes it from the FIFO.
	 * The arrays maximum size  is not shrinked 
	 * when elements are removed, because the 
	 * extra space will probably be needed later.
	 * 
	 * Time Complexity: O(n)
	 */
	public Object getNext(){
		try {
			lock.lock();
			
			// Return null if there are no elements.
			if(nrOfElements<1){
				return null;
			}
			
			/*
			 * Searching from left to right the maximum priority object.
			 * Elements are inserted in to the end of the array, 
			 * so the leftmost is needed.
			 */
			int max = -1;
			Object next = null;
			int maxIndex=0;
			for(int i=0; i<nrOfElements; ++i){
				if(priorities[i] > max){
					next = elements[i];
					max = priorities[i];
					maxIndex=i;
				}
			}
			
			/*
			 * Removeing the element from the FIFO by shifting to 
			 * the left the elements positioned to the right of it.
			 */
			for(int i=maxIndex; i<nrOfElements-1; ++i){
				elements[i] = elements[i+1];
				priorities[i] = priorities[i+1];
			}
			nrOfElements--;
			return next;
		} catch (InterruptedException e) {
			return null;

		}finally{
			lock.unlock();
		}
		
	}
	
	/*
	 * Return the current number of elements in the FIFO.
	 * 
	 * Time Complexity: O(1)
	 */
	public int getElementNr(){
		return nrOfElements;
	}
}
