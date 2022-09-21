//imports
import java.util.ArrayList;
import java.util.Scanner;

public class Solution {
	private static boolean okay = true; // setting a boolean which will be used to determine when to prompt for input
	private static boolean running = true; // this boolean indicates whether the program is running or if it's been exited/aborted (set to false)
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		
		// This array is going to store the information provided by the Scanner
		String[] in = new String[2];
		
		// This ArrayList is mimicking a threadpool and storing all Threads that are initialised
		ArrayList<Thread> pool = new ArrayList<>();
		
		// This is storing an array of Ans objects, each containing the result of a specific SlowCalculator calculation that was completed
		ArrayList<Ans> names = new ArrayList<>();
		
		// main loop
		while(running) {
			if(okay) { // prompt for input
				System.out.println("Enter a command");
				
				okay = false; // wait for input
				
				in = s.nextLine().split(" "); 
				
				String command = in[0]; // this is the command that was entered (start, get, cancel)
				String name = ""; // initialising a variable to store the int entered as a String
				
				// catching an Exception if only one word is entered, meaning in[1] would be null
				try {
					name = in[1]; // getting the input number as a String
				}
				catch(ArrayIndexOutOfBoundsException e) {
					//do nothing					
				}
				
				int num = getNum(in); // helper method to parse the String in in[1] to an int
				
				long N = (long) num; // casting num to a long that can be accepted as an argument by the SlowCalculator

				/* if conditions that determine behaviour based on what command was input by the user are below
				 * 
				 * for certain commands (start, get and cancel), the operation && num != -1 is being used to validate that the user has entered 
				 * an int after these commands (-1 is a default value returned by getNum() if the user did not enter an int)
				 * 
				 * static helper methods have been used to improve readability*/			
				if(command.equalsIgnoreCase("start") && num != -1) {
					start(pool, names, name, N);
				}
				
				else if(command.equals("get") && num != -1) {
					get(names, name);
				}
				
				else if (command.equalsIgnoreCase("running")) {
					running(pool);
				}
				
				else if (command.equalsIgnoreCase("cancel") && num != -1) {
					cancel(pool, names, name);
				}
				
				else if (command.equalsIgnoreCase("exit")) {
					okay = false; // setting to false so that the user cannot enter any more input
					running = false; // breaking the loop/terminating the program after all calculations have completed
					
				}
				
				else if (command.equalsIgnoreCase("abort")) {
					for(Thread a : pool) {
						a.stop(); // stopping all running threads immediately
					}
					running = false; // terminating the program
				}
				
				else { // printing out the error message that results from all the previous error handling
					System.err.println("Invalid input"); 
					okay = true;
				}
			}
		}	
	}
	
	/** This method sets the default value of the num variable to -1.  If no int was input by the user, it will return -1.  Else, it will 
	 * return the value that the user input */	
	public static int getNum(String[] in) {
		int num = -1; // setting a default value
		
		try {
			num = Integer.parseInt(in[1]);
		}
		
		// this will catch an exception if no int is entered after a string (e.g. run), meaning in [1] would be null;
		catch(ArrayIndexOutOfBoundsException e) { 
		}
		
		// this will catch an exception if the user enters anything other than an int after a string (e.g. start start)
		catch(NumberFormatException e) {
		}
		
		finally {
			okay = true; // set okay to true in order to keep prompting for input
		}
		return num;
	}
	
	/** This method creates a Thread with a SlowCalculator as an argument.  An Ans object is initialised.  Once the Thread is started, the
	 * SlowCalculator will set the Ans' name to be the same as the name of the Thread running it, and will set the Ans object's answer 
	 * variable to be the result of its calculation.  This Ans object is then added to an ArrayList, to be referenced later when 
	 * executing the 'get' command	 */
	public static void start(ArrayList<Thread> pool, ArrayList<Ans> names, String name, long N) {
		
		Ans answer = new Ans();
		
		//setting the SlowCalculator's name to the number that was entered by the user (e.g. start 110100) in the constructor
		Thread thread = new Thread(new SlowCalculator(N, name, answer)); 
		thread.start();  
		
		thread.setName(name); // setting the thread name to the number that was entered by the user (e.g. start 110100)
		
		pool.add(thread); 
		
		names.add(answer); // adding the answer object, whose values are set within the SlowCalculator run method, to an ArrayList
		
		okay = true; // setting true to continue prompting for input
	}
	
	/** This method checks all the Threads in my threadpool (being mimicked by an ArrayList).  If a Thread is running, it 
     * is copied into a second ArrayList called 'alive'.  'alive' is then iterated through, counting how many elements are in it.  The number
     * of Threads still running and their respective names are then printed out*/
	public static void running(ArrayList<Thread> pool) {
		ArrayList<Thread> alive = new ArrayList<>();
		
		// checking which threads are still running and copying them into a new ArrayList
		for(Thread a: pool) {
			if (a.isAlive() && !a.isInterrupted()) {
				alive.add(a);
			}
		}
		
		int count = 0; // to keep count of how many running threads there are
		
		for(Thread a : alive) {
			count++;
		}
		
		String output = count + " calculations running: "; 
		
		for(Thread a : pool) {
			if(a.isAlive()) { // only print out the name if the Thread is alive, since no Threads are ever removed from this ArrayList
			output += a.getName() + " ";
			}
		}
		System.out.println(output);
	}
	
	/** This method iterates through the ArrayList of Ans objects and checks whether any of these objects has a name that matches the 
	 * value that the user input.  If so, the answer (i.e. the result of the SlowCalculator calculation associated with this object) is
	 * printed out.  Else, "calculating" is printed instead */
	public static void get(ArrayList<Ans> names, String name) {
		for(Ans a : names) {
			if(a.getName().equals(name)) {		
				
				if(a.getAnswer()==0) { // if the calculation is still running
					System.out.println("calculating");
					continue;
				}
				else if(a.getAnswer() == -2) { // if the calculation has been cancelled
					//do nothing
					continue;
				}
				
				else {
					System.out.println("result is " + a.getAnswer()); // if the calculation has completed
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	/** This method cancels a named thread and sets the Thread's associated result value to -2, indicating that the thread has been cancelled */
	public static void cancel(ArrayList<Thread> pool, ArrayList<Ans> names, String name) {
		for(Thread a : pool) {
			if(a.getName().equals(name)) { // stopping a thread if its name matches the int the user has input
				a.stop();
				}
				
				for(Ans b: names) { // telling the associated Ans object that the thread has been cancelled
					if(b.getName().equals(name)) {  
						b.setAnswer(-2);
				}
			}
		}
	}
}
