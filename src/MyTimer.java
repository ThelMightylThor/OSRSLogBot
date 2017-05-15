import java.util.Timer;
import java.util.TimerTask;

public class MyTimer {
	
	static int secondsPassed = 0;
	
	static Timer timer = new Timer();
	static TimerTask task = new TimerTask() {
		public void run() {
			++secondsPassed;
			for (int i = 0; i < MessageResponder.itemPrices.length; ++i) {
				//MessageResponder.itemPrices[i] = MessageResponder.getPrice(MessageResponder.drops[i]);
				int price = MessageResponder.getPrice(MessageResponder.drops[i]);
				if (MessageResponder.itemPrices[i] != 0 && price == 0) {
					MessageResponder.itemPrices[i] = MessageResponder.itemPrices[i];
				} else if (MessageResponder.itemPrices[i] == 0 && price != 0) {
					MessageResponder.itemPrices[i] = price;
				} else if (MessageResponder.itemPrices[i] != 0 && price != 0) {
					MessageResponder.itemPrices[i] = price;
				}
			}
		}
	};
	
	public static void autoPriceUpdater() {
		timer.scheduleAtFixedRate(task, 0, 10800000);
	}

}