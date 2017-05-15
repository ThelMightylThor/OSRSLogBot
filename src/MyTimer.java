import java.util.Timer;
import java.util.TimerTask;

public class MyTimer {
	
	static int secondsPassed = 0;
	
	static Timer timer = new Timer();
	static TimerTask task = new TimerTask() {
		public void run() {
			++secondsPassed;
			for (int i = 0; i < MessageResponder.itemPrices.length; ++i) {
				MessageResponder.itemPrices[i] = MessageResponder.getPrice(MessageResponder.drops[i]);
			}
		}
	};
	
	public static void autoPriceUpdater() {
		timer.scheduleAtFixedRate(task, 0, 3600000);
	}

}