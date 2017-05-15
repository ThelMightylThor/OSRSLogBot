import java.util.Timer;
import java.util.TimerTask;

public class Test {
	
	static int secondsPassed = 0;
	
	static Timer timer = new Timer();
	static TimerTask task = new TimerTask() {
		public void run() {
			++secondsPassed;
			System.out.println("TEST");
		}
	};
	
	public static void start() {
		timer.scheduleAtFixedRate(task, 1000, 1000);
	}

	public static void main(String[] args) {
		Test myTimer = new Test();
		start();
	}

}
