import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Testcase {

	private class Patient{
		private String firstName;
		private String lastName;
		private String illness;
		private int age;
		
		public Patient(String firstName, String lastName, int age, String illness){
			this.firstName = firstName;
			this.lastName = lastName;
			this.age = age;
			this.illness = illness;
			}
		
		
		public int getIllnessCriticality(){
			if(illness.equals("paper cut")){
				return 1;
			}else if(illness.equals("broken leg")){
				return 2;
			}else if(illness.equals("gun shot wound")){
				return 3;
			}
			
			return 0;
		}
		
		@Override
		public boolean equals(Object o){
			if(o instanceof Patient){
				Patient p = (Patient) o;
				return (firstName.equals(p.getFirstName()) && lastName.equals(p.getLastName()) && age==p.getAge() && illness.equals(p.getIllness()) );
			}
			return false;
		}


		public String getFirstName() {
			return firstName;
		}


		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}


		public String getLastName() {
			return lastName;
		}


		public void setLastName(String lastName) {
			this.lastName = lastName;
		}


		public String getIllness() {
			return illness;
		}


		public void setIllness(String illness) {
			this.illness = illness;
		}


		public int getAge() {
			return age;
		}


		public void setAge(int age) {
			this.age = age;
		}
	}
	
	@Test
	public void insert_emptyFIFO_elementInserted(){
		PriorityQueue queue = new PriorityQueue();
		
		Patient p1 = new Patient("John", "Malon", 26, "paper cut");
		
		queue.insert(p1, p1.getIllnessCriticality());

		Patient p = (Patient) queue.removeTop();
		assertEquals("John", p.getFirstName());
		assertEquals("Malon", p.getLastName());
		assertEquals(26, p.getAge());
		assertEquals("paper cut", p.getIllness());
		
	}
	
	@Test
	public void insert_notEmptyFIFO_elementInserted(){
		PriorityQueue queue = new PriorityQueue();

		Patient p1 = new Patient("John", "Malon", 26, "paper cut");
		Patient p2 = new Patient("Mike", "Mccain", 43, "gun shot wound");
		
		queue.insert(p1, 1);
		queue.insert(p2, 2);

		Patient p = (Patient) queue.removeTop();
		assertEquals("Mike", p.getFirstName());
		assertEquals("Mccain", p.getLastName());
		assertEquals(43, p.getAge());
		assertEquals("gun shot wound", p.getIllness());
		
	}
	
	@Test(expected = PriorityQueueException.class)
	public void getNext_emptyFifo_null(){
		PriorityQueue queue = new PriorityQueue();
		
		queue.removeTop();
	}
	
	@Test
	public void getNext_notEmptyFifo_getTheHighestPriorityElement(){
		PriorityQueue queue = new PriorityQueue();
		
		String s1 = "A";
		String s2 = "B";
		String s3 = "C";
		String s4 = "D";
		
		queue.insert(s1, 4);
		queue.insert(s2, 6);
		queue.insert(s3, 7);
		queue.insert(s4, 2);

		assertEquals("C", (String)queue.removeTop());
	}
	
	@Test
	public void getNext_notEmptyFifo_getTheFirstHighestPriorityElement(){
		PriorityQueue queue = new PriorityQueue();
		
		String s1 = "A";
		String s2 = "B";
		String s3 = "C";
		String s4 = "D";
		String s5 = "E";
		String s6 = "F";
		
		queue.insert(s1, 4);
		queue.insert(s2, 6);
		queue.insert(s3, 7);
		queue.insert(s4, 2);
		queue.insert(s5, 7);
		queue.insert(s6, 7);

		assertEquals("C", (String)queue.removeTop());
	}
	
	@Test
	public void changePriority_notEmptyList_lowestBecomesTheHighest(){
	PriorityQueue queue = new PriorityQueue();
		
		String s1 = "A";
		String s2 = "B";
		String s3 = "C";
		String s4 = "D";
		String s5 = "E";
		String s6 = "F";
		
		queue.insert(s1, 4);
		queue.insert(s2, 6);
		queue.insert(s3, 7);
		queue.insert(s4, 2);
		queue.insert(s5, 7);
		queue.insert(s6, 7);
		
		queue.changePriority(s4, 8);

		assertEquals("D", (String)queue.removeTop());
	}


	
	@Test
	public void getElementNr_multipleNextMethodCall_elementNrDecrements() {
		PriorityQueue queue = new PriorityQueue();
		
		Patient p1 = new Patient("John", "Malon", 26, "paper cut");
		Patient p2 = new Patient("Mike", "Mccain", 43, "gun shot wound");
		Patient p3 = new Patient("Elisabeth", "Cortens", 23, "broken leg");
		Patient p4 = new Patient("James", "Feynman", 29, "paper cut");
		Patient p5 = new Patient("Charles", "Tuscon", 51, "gun shot wound");
	
		queue.insert(p1, p1.getIllnessCriticality());
		queue.insert(p2, p2.getIllnessCriticality());
		queue.insert(p3, p3.getIllnessCriticality());
		queue.insert(p4, p4.getIllnessCriticality());
		queue.insert(p5, p5.getIllnessCriticality());
		
		
		for(int i=0; i<5; ++i){
			assertEquals(5-i, queue.getElementNr());
			System.out.println(queue.getElementNr());
			Patient p = (Patient) queue.removeTop();
			System.out.println(p.getFirstName() + " " + p.getLastName() + " " + p.getIllnessCriticality());
		}
		
		assertEquals(0, queue.getElementNr());

	}
	
	@Test
	public void concurrencyTest() {
		PriorityQueue fifo = new PriorityQueue();
		
		Patient p1 = new Patient("John", "Malon", 26, "paper cut");
		Patient p2 = new Patient("Mike", "Mccain", 43, "gun shot wound");
		Patient p3 = new Patient("Elisabeth", "Cortens", 23, "broken leg");
		Patient p4 = new Patient("James", "Feynman", 29, "paper cut");
		Patient p5 = new Patient("Charles", "Tuscon", 51, "gun shot wound");
		
		Runnable insertAndGetTask1 = () -> {
			fifo.insert(p1, p1.getIllnessCriticality());
			fifo.insert(p2, p2.getIllnessCriticality());
			fifo.insert(p3, p3.getIllnessCriticality());
			
			try {
				Thread.sleep(5000);
			} catch (Exception e) {}
			
			for(int i=0; i<3; ++i){
				System.out.println(fifo.getElementNr());
				Patient p = (Patient) fifo.removeTop();
				System.out.println(p.getFirstName() + " " + p.getLastName() + " " + p.getIllnessCriticality());
			}
		};
		

		Runnable insertAndGetTask2 = () -> {
			fifo.insert(p4, p4.getIllnessCriticality());
			fifo.insert(p5, p5.getIllnessCriticality());
			
			try {
				Thread.sleep(5000);
			} catch (Exception e) {}
			
			for(int i=3; i<5; ++i){
				System.out.println(fifo.getElementNr());
				Patient p = (Patient) fifo.removeTop();
				System.out.println(p.getFirstName() + " " + p.getLastName() + " " + p.getIllnessCriticality());
			}
		};
		
		Thread t1 = new Thread(insertAndGetTask1);
		Thread t2 = new Thread(insertAndGetTask2);
		
		t1.start();
		t2.start();
		
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			System.out.println();
		}
	}

}
