import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class WebReader {

	public static int webReader(String name) throws IOException {
		String str;
		//name = name.substring(0, name.indexOf(" ")) + "-" + name.substring(name.indexOf(" ")+1, name.length());
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
