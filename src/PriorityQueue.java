
import java.util.logging.Level;
import java.util.logging.Logger;

import sun.misc.Lock;

/**
 * The AbstractFIFO class provides a special FIFO
 * implementation that supports user-defined priority.
 * NOTE: Classes must override equals method.
 */
public class PriorityQueue{
	
	private Object[] elements;
	private int nrOfElements;
	
	private int[] priorities;
	
	private int arrayIncrementSize = 50;
	private int arrayCurrentLength;
	
	private Lock lock;
	
	private static final Logger LOGGER = Logger.getLogger(PriorityQueue.class.getName());

	
	public PriorityQueue(){
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
			elements[nrOfElements] = element;
			priorities[nrOfElements] = priority;
			nrOfElements++;
			
		} catch (InterruptedException e) {
			LOGGER.log(Level.FINE, "Thread was interrupted.");
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
			
			boolean found = false;
			for(int i=0; i<nrOfElements; ++i){
				if(elements[i].equals(element)){
					priorities[i] = priority;
					found = true;
				}
			}
			

			if(!found){
				String logMessage = "The specified element with class" + element.getClass().getName() + "was not found in the queue during method changePriority!";
				LOGGER.log(Level.FINE, logMessage);
				throw new PriorityQueueException(logMessage);
			}
				
		} catch (InterruptedException e) {
			String logMessage = "Thread was interrupted during method call changePriority on a " + getClass().getName() + " object.";
			LOGGER.log(Level.FINE, logMessage);
			throw new PriorityQueueException(logMessage);
		}finally{
			lock.unlock();
		}
	}
	
	/*
	 * Returns the first element in based on it's
	 *  priority and removes it from the queue.
	 * The arrays maximum size is not shrinked 
	 * when elements are removed, because the 
	 * extra space will probably be needed later.
	 * 
	 * Time Complexity: O(2n)
	 */
	public Object removeFirst(){
		try {
			lock.lock();
			
			/*
			 * If the internal array would be stored always sorted
			 * insert would be O(2*n)
		 	 * getNext would be O(1)           
			 * the complexity of the two functions above have the exactly reversed complexity
			 * the changePriority would have the same complexity because binary search can't be applied to make it O( log(n) )
			 * sorting the array would be motivated if the remove should be faster than the insert
			*/
			
			// Throw exception if there are no elements.
			if(nrOfElements<1){
				String logMessage = "No element to remove from the top in call removeFirst.";
				LOGGER.log(Level.FINE, logMessage);
				throw new PriorityQueueException(logMessage);
			}
			
			/*
			 * Searching from left to right the maximum priority object.
			 * Elements are inserted in to the end of the array, 
			 * so the leftmost is needed with the maximum priority.
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
			String logMessage = "Thread was interrupted during method call removeFirst on a " + getClass().getName() + " object.";
			LOGGER.log(Level.FINE, logMessage);
			throw new PriorityQueueException(logMessage);

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
