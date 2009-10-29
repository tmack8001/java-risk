package javaRisk;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

/**
 * The ClientProxy is the interface between the Risk game back end
 * and the network.
 */
public class ClientProxy {

	private ServerModel model;
	private Player me;

	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	
	public ClientProxy(Socket socket) {
		this.socket = socket;
		try {
			this.input = new DataInputStream(socket.getInputStream());
			this.output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setPlayer(Player player) {
		me = player;
	}
	
	public void setModel(ServerModel model) {
		this.model = model;
	}
	
	public void attackLaunched(int src, int dest) {
		model.attack(src, dest);
	}
	
	/**
	 * Attaches the attack results onto the socket to send over to the client.
	 * 
	 * @param attackResult	[0] - attacking territory index
	 * 						[1] - defending territory index
	 * 						[2] - attack roll
	 * 						[3] - defend roll
	 */
	public void attackMade(int[] attackResult) throws IOException {
		output.writeByte(Constants.ATTACK_MADE);
		output.writeInt(attackResult[0]);
		output.writeInt(attackResult[1]);
		output.writeInt(attackResult[2]);
		output.writeInt(attackResult[3]);
		output.flush();
	}
	
	/**
	 * 
	 * @param territoryStatus
	 * @throws IOException
	 */
	public void updateTerritory(int[] territoryStatus) throws IOException {
		System.out.println("updateTerritory");
		output.writeByte(Constants.TERRITORY_STATUS);
		output.writeInt(territoryStatus[0]);
		output.writeInt(territoryStatus[1]);
		output.writeInt(territoryStatus[2]);
		output.flush();
	}
	
	/**
	 * 
	 * @param currentTurn
	 * @throws IOException
	 */
	public void sendTurn(int currentTurn) throws IOException {
		System.out.println("currentTurn is " + currentTurn);
		output.writeByte(Constants.TURN_IND);
		output.writeInt(currentTurn);
		output.flush();
	}
	
	public void signalEndTurn() {
		model.incrementMove();
	}
	
	public void surrender() {
		model.removeListener(this);
	}
	
	public void signalReady() {
		model.ready(me);
	}
	
	public void joinGame(String name)
	{
		ServerModel model = SessionMap.getSession(name);
		//TODO check if game already started
		model.addListener(this);
		this.setModel(model);
	}
	
	public void start()
	{
		new Reader().start();
	}
	
	private class Reader extends Thread {

		public void run() {
			try {
				while(true)	{
					byte curr = input.readByte();

					switch(curr){
					case Constants.ATTACK:
						int src = input.readInt();
						int dest = input.readInt();
						
						attackLaunched(src, dest);
						break;
					case Constants.END_TURN:
						signalEndTurn();
						break;
					case Constants.READY:
						signalReady();
						break;
					case Constants.SURRENDER:
						surrender();	
						break;
					case Constants.GAME_TO_JOIN:
						String gameName = input.readUTF();
						joinGame(gameName);
						break;
					default:
						break;
					}
				}
			} catch(EOFException exc) {
			} catch(IOException exc) {
				System.err.println("ClientProxy.Reader.run(): I/O error");
				exc.printStackTrace();
			} finally { 
				try {
					surrender();
					socket.close();
				}catch(IOException exc) {
				}
			}
		}
	}
}