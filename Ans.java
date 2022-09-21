/**
 * This class creates an object (Ans) which is designed to store the result of a single SlowCalculator calculation. 
 */
public class Ans {
	private String name; // this will be set to be the same as the name of the Thread it is associated with
	private int answer; // this is the result of a specific SlowCalculator calculation
	
	public Ans() {
		// empty constructor as variable values will be set once a Thread has started.
	}
	
	// getters and setters
	public int getAnswer() {
		return answer;
	}

	public void setAnswer(int ans) {
		this.answer = ans;
	}
	
	
	/** This accessor method allows us to compare the name of this object to the name of a Thread.  If the names match, we will return the
	 * answer (int ans) stored for the particular calculation the Thread was responsible for executing.
	 */
	public String getName() {  
		return name;
	}
	
	/** This mutator method allows us to set the name of this object to be the same as the Thread running a particular SlowCalculator
	 * calculation.  By assigning them identical names, we can later use this name to retrieve the result (int ans) of a specific
	 * SlowCalculator calculation from its associated Ans object.
	 */
	public void setName(String name) {
		this.name = name;
	}	
}
