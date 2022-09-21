//imports
import java.util.ArrayList;

public class SlowCalculator implements Runnable {

    private final long N;
   
    // assigning each SlowCalculator its own Ans object to store the results of its calculation
    private Ans answer = new Ans(); 
    
    private String name;

    public SlowCalculator(final long N, String name, Ans answer) {
        this.N = N;
        this.name = name; // setting the name of the SlowCalculator to be the same as the Thread running this object
        this.answer = answer;
    }

    public void run() {
    	answer.setName(name); // setting the name of the Ans object to be the same as the Thread which is running this SlowCalculator
        final int result = calculate(N);
        answer.setAnswer(result); // setting the answer variable within the Ans object to be result, allowing us to store this value externally
            
    }

    private static int calculate(final long N) {
        // This (very inefficiently) finds and returns the number of unique prime factors of |N|
        // You don't need to think about the mathematical details; what's important is that it does some slow calculation taking N as input
        // You should not modify the calculation performed by this class, but you may want to add support for interruption
        int count = 0;
        for (long candidate = 2; candidate < N; ++candidate) {
            if (isPrime(candidate)) {
                if (Math.abs(N) % candidate == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private static boolean isPrime(final long n) {
        // This (very inefficiently) checks whether n is prime
        // You should not modify this method
        for (long candidate = 2; candidate < Math.sqrt(n); ++candidate) {
            if (n % candidate == 0) {
                return false;
            }
        }
        return true;
    }
}
