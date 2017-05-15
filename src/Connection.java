import java.io.*;
import java.util.*;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Connection {
	public static void main(String[] args) {
		JDA discord = null;
		
		try {
			discord = new JDABuilder(AccountType.BOT).setToken(Constants.discordToken).buildBlocking();
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RateLimitedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		discord.addEventListener(new MessageResponder());
		MyTimer.autoPriceUpdater();
	}
	
	public static void readFile() throws IOException {
		Scanner input;
		input = new Scanner(new File("dropAmnt.txt"));
		int cnt = 0;
		while(input.hasNext()) {
			MessageResponder.dropAmnt[cnt] = Integer.parseInt(input.nextLine());
			++cnt;
		}
		input.close();
	}
	
	public static void writeFile() throws IOException {
		PrintWriter output;
		String filename = "dropAmnt.txt";
		output = new PrintWriter(filename);
		for (int i = 0; i < MessageResponder.dropAmnt.length; ++i) {
			output.println(MessageResponder.dropAmnt[i]);
		}
		output.close();
	}
	
	public static int webReader(String name) throws IOException {
		name = name.replace(" ", "-");
		name = name.toLowerCase();
		if (name.contains("dinh")) {
			name = "dinh-s-bulwark";
		}
		if (name.contains("dex")) {
			name = "prayer-scroll";
		}
		if (name.contains("kodai")) {
			name = "kodai-wand";
		}
		String address = "https://www.ge-tracker.com/item/" + name;
		URL pageLocation = new URL(address);
		Scanner in = new Scanner(pageLocation.openStream());
		
		while (in.hasNext()) {
			String line = in.next();
			if (line.contains("item_stat_overall")) {
				int from = line.indexOf("\">");
				int to = line.indexOf("</");
				return Integer.parseInt((line.substring(from + 2, to).replace(",", "")));
			}
		}
		
		return 0;
	}
}
