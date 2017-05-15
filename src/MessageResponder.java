import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageResponder extends ListenerAdapter {
	
	public static String[] drops = new String[] {"Twisted Bow",
			  "Elder Maul",
			  "Dexterous Prayer Scroll",
			  "Arcane Prayer Scroll",
			  "Dinhs Bulwark",
			  "Dragon Claws",
			  "Dragon Hunter Crossbow",
			  "Dragon Harpoon",
			  "Dragon Sword",
			  "Kodai Insignia",
			  "Ancestral Hat",
			  "Ancestral Robe Top",
			  "Ancestral Robe Bottom",
			  "Twisted Buckler"};
	public static int[] dropAmnt = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	public static int[] itemPrices = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	MyTimer myTimer = new MyTimer();
	int amount = 0;
	String itemName = "";

	public void onMessageReceived (MessageReceivedEvent event) {
		String message = event.getMessage().getContent();
		String name = event.getAuthor().getName();
		
		if (message.startsWith("!ready")) {
			event.getTextChannel().sendMessage(ready(name)).queue();
		}
		if (message.startsWith("+")) {
			itemReader(message);
			int itemNum = -1;
			for (int i = 0; i < drops.length; ++i) {
				if (drops[i].contains(itemName)) {
					itemNum = i;
					i = drops.length;
				}
			}
			if (itemNum != -1) {
				event.getTextChannel().sendMessage("Adding " + amount + " " + drops[itemNum] + " to the drop log.").queue();
				dropAmnt[itemNum] += amount;
				event.getTextChannel().sendMessage("Approximate Value: " + formatNumber(getPrice(drops[itemNum]))).queue();
				event.getTextChannel().sendMessage("Current total: " + dropAmnt[itemNum]).queue();;
			}
			writeFile();
		}
		if (message.startsWith("-")) {
			itemReader(message);
			int itemNum = -1;
			for (int i = 0; i < drops.length; ++i) {
				if (drops[i].contains(itemName)) {
					itemNum = i;
					i = drops.length;
				}
			}
			if (itemNum != -1) {
				event.getTextChannel().sendMessage("Removing " + amount + " " + drops[itemNum] + " from the drop log.").queue();
				dropAmnt[itemNum] -= amount;
				event.getTextChannel().sendMessage("Current total: " + dropAmnt[itemNum]).queue();;
			}
			writeFile();
		}
		if (message.startsWith("!price")) {
			itemName = message.substring(7);
			event.getTextChannel().sendMessage(itemName + ": " + formatNumber(getPrice(itemName))).queue();
		}
		if (message.startsWith("!printlog")) {
			int total = 0;
			event.getTextChannel().sendMessage(fileStrBuilder()).queue();
			/*for (int i = 0; i < itemPrices.length; ++i) {
				itemPrices[i] = getPrice(drops[i]);
			}*/
			for (int i = 0; i < itemPrices.length; ++i) {
				total += (itemPrices[i] * dropAmnt[i]);
			}
			event.getTextChannel().sendMessage("Total Value: " + formatNumber(total)).queue();
		}
	}
	/*
	 * Returns the user's user-name and a message
	 * to the user.
	 */
	public String ready (String name) {
		return name + ", I am ready.";
	}
	/*
	 * Takes a string in the format of +y x
	 * Where y is the number of items to add
	 * and x is a substring of the item name. 
	 * Returns an array of x and y properly
	 * formatted for future validation.
	 * Example: +1 bow or -1 buckler
	 */
	public void itemReader(String message) {
		readFile();
		amount = Integer.valueOf(message.substring(1, message.indexOf(" ")));
		itemName = message.substring(message.indexOf(" ") + 1, message.length());
		itemName = Character.toUpperCase(itemName.charAt(0)) + itemName.substring(1).toLowerCase();
		System.out.println(itemName);
	}
	/*
	 * Converts the text file into an
	 * acceptable format for printing.
	 */
	public String fileStrBuilder() {
		readFile();
		String str = "";
		for (int i = 0; i < dropAmnt.length; ++i) {
			str += drops[i] + ": " + dropAmnt[i] + "\n";
		}
		return str;
	}
	
	public static int getPrice(String name) {
		int price = 0;
		try {
			price = Connection.webReader(name);
			System.out.println(price);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return price;
	}
	
	public String formatNumber(int number) {
		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
	    return numberFormat.format(number);
	}

	public void readFile () {
		// Read current totals from file
		try {
			Connection.readFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeFile () {
		// Commit changes to file
		try {
			Connection.writeFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

