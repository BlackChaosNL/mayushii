/**
 * @author black
 *
 */
public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		irc irc = new irc("irc.eu.darenet.org", 6667, "Mayushii-desu", null, "#avans");
		System.out.println(irc.getBufferedReader());
	}
}
