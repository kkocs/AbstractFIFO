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
		AbstractFIFO fifo = new AbstractFIFO();
		
		Patient p1 = new Patient("John", "Malon", 26, "paper cut");
		
		fifo.insert(p1, p1.getIllnessCriticality());

		Patient p = (Patient) fifo.getNext();
		assertEquals("John", p.getFirstName());
		assertEquals("Malon", p.getLastName());
		assertEquals(26, p.getAge());
		assertEquals("paper cut", p.getIllness());
		
	}
	
	@Test
	public void insert_notEmptyFIFO_elementInserted(){
		AbstractFIFO fifo = new AbstractFIFO();

		Patient p1 = new Patient("John", "Malon", 26, "paper cut");
		Patient p2 = new Patient("Mike", "Mccain", 43, "gun shot wound");
		
		fifo.insert(p1, 1);
		fifo.insert(p2, 2);

		Patient p = (Patient) fifo.getNext();
		assertEquals("Mike", p.getFirstName());
		assertEquals("Mccain", p.getLastName());
		assertEquals(43, p.getAge());
		assertEquals("gun shot wound", p.getIllness());
		
	}
	
	@Test
	public void getNext_emptyFifo_null(){
		AbstractFIFO fifo = new AbstractFIFO();
		
		assertEquals(null,fifo.getNext());
	}
	
	@Test
	public void getNext_notEmptyFifo_getTheHighestPriorityElement(){
		AbstractFIFO fifo = new AbstractFIFO();
		
		String s1 = "A";
		String s2 = "B";
		String s3 = "C";
		String s4 = "D";
		
		fifo.insert(s1, 4);
		fifo.insert(s2, 6);
		fifo.insert(s3, 7);
		fifo.insert(s4, 2);

		assertEquals("C", (String)fifo.getNext());
	}
	
	@Test
	public void getNext_notEmptyFifo_getTheFirstHighestPriorityElement(){
		AbstractFIFO fifo = new AbstractFIFO();
		
		String s1 = "A";
		String s2 = "B";
		String s3 = "C";
		String s4 = "D";
		String s5 = "E";
		String s6 = "F";
		
		fifo.insert(s1, 4);
		fifo.insert(s2, 6);
		fifo.insert(s3, 7);
		fifo.insert(s4, 2);
		fifo.insert(s5, 7);
		fifo.insert(s6, 7);

		assertEquals("C", (String)fifo.getNext());
	}
	
	@Test
	public void changePriority_notEmptyList_lowestBecomesTheHighest(){
	AbstractFIFO fifo = new AbstractFIFO();
		
		String s1 = "A";
		String s2 = "B";
		String s3 = "C";
		String s4 = "D";
		String s5 = "E";
		String s6 = "F";
		
		fifo.insert(s1, 4);
		fifo.insert(s2, 6);
		fifo.insert(s3, 7);
		fifo.insert(s4, 2);
		fifo.insert(s5, 7);
		fifo.insert(s6, 7);
		
		fifo.changePriority(s4, 8);

		assertEquals("D", (String)fifo.getNext());
	}


	
	@Test
	public void getElementNr_multipleNextMethodCall_elementNrDecrements() {
		AbstractFIFO fifo = new AbstractFIFO();
		
		Patient p1 = new Patient("John", "Malon", 26, "paper cut");
		Patient p2 = new Patient("Mike", "Mccain", 43, "gun shot wound");
		Patient p3 = new Patient("Elisabeth", "Cortens", 23, "broken leg");
		Patient p4 = new Patient("James", "Feynman", 29, "paper cut");
		Patient p5 = new Patient("Charles", "Tuscon", 51, "gun shot wound");
	
		fifo.insert(p1, p1.getIllnessCriticality());
		fifo.insert(p2, p2.getIllnessCriticality());
		fifo.insert(p3, p3.getIllnessCriticality());
		fifo.insert(p4, p4.getIllnessCriticality());
		fifo.insert(p5, p5.getIllnessCriticality());
		
		
		for(int i=0; i<5; ++i){
			assertEquals(5-i, fifo.getElementNr());
			System.out.println(fifo.getElementNr());
			Patient p = (Patient) fifo.getNext();
			System.out.println(p.getFirstName() + " " + p.getLastName() + " " + p.getIllnessCriticality());
		}
		
		assertEquals(0, fifo.getElementNr());

		
		//fail("Not yet implemented");
	}
	
	@Test
	public void concurrencyTest() {
		AbstractFIFO fifo = new AbstractFIFO();
		
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
				Patient p = (Patient) fifo.getNext();
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
				Patient p = (Patient) fifo.getNext();
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
