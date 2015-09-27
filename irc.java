import java.io.*;
import java.net.*;

public class irc {
	
	/**
	 * Default 
	 */
	private String server;
	private String username;
	private String login;
	private String channel;
	private Socket socket;
	private BufferedWriter bufferedWriter;
	private BufferedReader bufferedReader;
	
	/**
	 * @return the server
	 */
	public String getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public void setServer(String server) {
		this.server = server;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}

	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * @param socket the socket to set
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * @return the bufferedWriter
	 */
	public BufferedWriter getBufferedWriter() {
		return bufferedWriter;
	}

	/**
	 * @param bufferedWriter the bufferedWriter to set
	 */
	public void setBufferedWriter(BufferedWriter bufferedWriter) {
		this.bufferedWriter = bufferedWriter;
	}

	/**
	 * @return the bufferedReader
	 */
	public BufferedReader getBufferedReader() {
		return bufferedReader;
	}

	/**
	 * @param bufferedReader the bufferedReader to set
	 */
	public void setBufferedReader(BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
	}

	/**
	 * Connect to IRC then login.
	 * @param server
	 * @param port
	 * @param username
	 * @param login
	 * @param channel
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public irc(String server, int port, String username, String login, String channel){
			try {	this.setSocket(new Socket(server, port));
					this.setBufferedWriter(new BufferedWriter(new OutputStreamWriter(this.getSocket().getOutputStream())));
					this.setBufferedReader(new BufferedReader(new InputStreamReader(this.getSocket().getInputStream())));
					this.useIrc(username, login, channel, this.getBufferedWriter(), this.getBufferedReader()); 
				} catch (IOException e) { System.err.println("IrcClient: Something went wrong here!?!");}
	}
	
	/**
	 * Login+keep alive to the IRC service.
	 * @param nick
	 * @param login
	 * @param channel
	 * @param bufferedWriter
	 * @param bufferedReader
	 * @throws IOException
	 */
	private void useIrc(String nick, String login, String channel, BufferedWriter bufferedWriter, BufferedReader bufferedReader) throws IOException{
		int joined = 0;
		int ping = 0;
		bufferedWriter.write("USER "+login+" foo foo foo :"+login+"\r\n");
		bufferedWriter.write("NICK " + nick + "\r\n");
		bufferedWriter.flush();
		String line = null;
		while((line = bufferedReader.readLine()) != null){
			System.out.println(line);
			if(line.matches("(.*)PING(.*)")){
				bufferedWriter.write("PONG " + line.substring(5) + "\r\n");
				bufferedWriter.flush();
                ping++;
			}else{
				if(joined == 0 && ping >= 2){
					String[] channels = channel.split(",", -1);
					for(int i = 0; i < channels.length; i++){
						bufferedWriter.write("JOIN "+ channels[i] + "\r\n");
					}
					bufferedWriter.flush();
			        joined = 1;
				}
			}
        }
	}
}
