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

	public void onMessageReceived (MessageReceivedEvent event) {
		String message = event.getMessage().getContent();
		String name = event.getAuthor().getName();
		String[] details = itemReader(message);
		
		if (message.startsWith("!ready")) {
			event.getTextChannel().sendMessage(ready(name)).queue();
		}
		if (message.startsWith("+")) {
			int itemNum = -1;
			for (int i = 0; i < drops.length; ++i) {
				if (drops[i].contains(details[1])) {
					itemNum = i;
					i = drops.length;
				}
			}
			if (itemNum != -1) {
				event.getTextChannel().sendMessage("Adding " + details[0] + " " + drops[itemNum] + " to the drop log.").queue();
				dropAmnt[itemNum] += Integer.parseInt(details[0]);
				event.getTextChannel().sendMessage("Approximate Value: " + formatNumber(getPrice(drops[itemNum]))).queue();
				event.getTextChannel().sendMessage("Curennt total: " + dropAmnt[itemNum]).queue();;
			}
			writeFile();
		}
		if (message.startsWith("-")) {
			int itemNum = -1;
			for (int i = 0; i < drops.length; ++i) {
				if (drops[i].contains(details[1])) {
					itemNum = i;
					i = drops.length;
				}
			}
			if (itemNum != -1) {
				event.getTextChannel().sendMessage("Removing " + details[0] + " " + drops[itemNum] + " from the drop log.").queue();
				dropAmnt[itemNum] -= Integer.parseInt(details[0]);
				event.getTextChannel().sendMessage("Curennt total: " + dropAmnt[itemNum]).queue();;
			}
			writeFile();
		}
		if (message.startsWith("!price")) {
			int itemNum = 0;
			for (int i = 0; i < drops.length; ++i) {
				if (drops[i].contains(details[1])) {
					itemNum = i;
					i = drops.length;
				}
			}
			event.getTextChannel().sendMessage(details[1] + ": " + formatNumber(getPrice(details[1]))).queue();
		}
		if (message.startsWith("!printlog")) {
			int total = 0;
			event.getTextChannel().sendMessage(fileStrBuilder()).queue();
			itemPriceBuilder();
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
	public String[] itemReader (String message) {
		String[] details = new String[2];
		readFile();
		details[0] = String.valueOf(message.charAt(1));
		details[1] = message.substring(message.indexOf(" ") + 1, message.length());
		details[1] = Character.toUpperCase(details[1].charAt(0)) + details[1].substring(1);
		return details;
	}
	/*
	 * Converts the text file into an
	 * acceptable format for printing.
	 */
	public String fileStrBuilder () {
		readFile();
		String str = "";
		for (int i = 0; i < dropAmnt.length; ++i) {
			str += drops[i] + ": " + dropAmnt[i] + "\n";
		}
		return str;
	}
	
	public void itemPriceBuilder() {
		for (int i = 0; i < itemPrices.length; ++i) {
			try {
				itemPrices[i] = Integer.valueOf(Connection.webReader(drops[i]));
				System.out.println(itemPrices[i]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public int getPrice(String name) {
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

