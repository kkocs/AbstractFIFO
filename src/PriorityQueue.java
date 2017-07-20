
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import sun.misc.Lock;

/**
 * The PriorityQueue class provides a special FIFO
 * implementation that supports user-defined priority.
 * NOTE: Classes must override equals method.
 */
public class PriorityQueue{
	
	/*
	* The elements are stored in linkedlists which are 
	* segregated by categories using the key of the HashMap.
	*/
	private Map<Integer, List<Object>> elements;

	/*
	* orderedPriorities helps to identify the current maximum priority
	* When the last element with the actual maximu is removed, the first
	* element in the set is removed as well.
	*/
	private SortedSet<Integer> orderedPriorities;
	
	private int nrOfElements;
	
	
	private Lock lock;
	
	private static final Logger LOGGER = Logger.getLogger(PriorityQueue.class.getName());

	
	public PriorityQueue(){
		lock = new Lock();
		elements = new HashMap<Integer, List<Object>>();
		orderedPriorities = new TreeSet<Integer>();
		nrOfElements = 0;
	}
	
	/*
	 * Inserts an element in the Queue
	 * with a given priority.
	 * 
	 * Time Complexity: O(log n)
	 */
	public void insert(Object element, int priority){
		try {
			lock.lock();
			
			
			/*
			* Insert the element into a list placed which is 
			* placed in the HashMapunder the key which 
			* corrensponds with the element's priority.
			*/
			
			List<Object> elementsWithGivenPriority = elements.get(priority);
		
			if(elementsWithGivenPriority != null){
			// If there are elements with the given priority.
				elementsWithGivenPriority.add(element);
			}else{
			// If this is the first element with the given priority
				elementsWithGivenPriority = new LinkedList<Object>();
				elementsWithGivenPriority.add(element);
				elements.put(priority, elementsWithGivenPriority);
			/*
			 *  After inserting the list, we add 
			 *  the priority to the orderedPriorityList
			 *  which is an ordered set.
			 */
				orderedPriorities.add(priority);
			}


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
	 * Time Complexity: O(log n)
	 */
	public void changePriority(Object element, int oldPriority, int newPriority){
		try {
			lock.lock();
			
			
			List<Object> listOfElementsWithThatPriority =  elements.get(oldPriority);
			String logMessage = null;
			
			// Check if the element is in the hashmap having that priority.
			if(listOfElementsWithThatPriority != null){
				boolean removed = listOfElementsWithThatPriority.remove(element);
				
				/*
				* If the element was succesfully removed from the list 
				* at the old position in the HashMap it has t obe inserted 
				* to it's new position in the HashMap.
				*/
				if(removed){
					List<Object> listOfElementsWithNewPriority = elements.get(newPriority);
					if(listOfElementsWithNewPriority == null){
						listOfElementsWithNewPriority = new LinkedList<Object>();
					}
					listOfElementsWithNewPriority.add(element);
					elements.put(newPriority, listOfElementsWithNewPriority);
					
					orderedPriorities.add(newPriority);
					

				}else{
					logMessage = "The specified element with class " + element.getClass().getName() + " was not found in the queue during method changePriority!";
				}
				
			}else{
				logMessage = "The specified element with class " + element.getClass().getName() + "and priority " + oldPriority + " was not found in the queue during method changePriority!";
			}
			
			

			if(logMessage != null){
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
	 * Time Complexity: O(1)
	 */
	public Object removeFirst(){
		try {
			lock.lock();
			
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
			
			
			/*
			* Based on the current maximum which is the first element in
			* the orderedPriorities the element is removed and returned.
			*/
			
			int max = orderedPriorities.last();
			
			Object highestPriorityElement;
			List<Object> highestPriorityElements = elements.get(max);
			
			highestPriorityElement = highestPriorityElements.get(0);
			highestPriorityElements.remove(0);
			
			/*
			* If the last element with the maximum priority was removed the 
			* priority is removed so the next maximum will be considered from now.
			*/
			if(highestPriorityElements.isEmpty()){
				orderedPriorities.remove(max);
			}
			
			nrOfElements--;
			return highestPriorityElement;
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
