import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import java.util.Vector;

public class TCPServer {
	static Vector<ClientHandler> ar = new Vector<>();

	public static void main(String args[]) throws Exception {
		int SERVER_PORT = Integer.parseInt(args[0]);
		// String sensorData;
		// String message = "Ok";

		ServerSocket welcomeSocket = new ServerSocket(SERVER_PORT);
		System.out.println("SERVER IS RUNNING AT PORT " + SERVER_PORT + "...");

		while (true) {

			Socket socket = null;
			try {
				socket = welcomeSocket.accept();
				System.out.println("A new client is connected : " + socket);

				// Create input and out streams
				// DataInputStream dis = new DataInputStream(socket.getInputStream());
				// DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());

				//System.out.println("Assigning new thread for this client");

				ClientHandler myClientHandler = new ClientHandler(socket, inFromClient, outToClient);

				// create a new thread object
				Thread t = new Thread(myClientHandler);
				ar.add(myClientHandler);

				// Invoking the start() method
				t.start();

			} catch (Exception e) {
				socket.close();
				e.printStackTrace();
			}
		}
	}
}

// ClientHandler class
class ClientHandler extends Thread {

	final BufferedReader bufferedReader;
	final DataOutputStream outputStream;
	final Socket s;
	boolean isApp = false;

	// Constructor
	public ClientHandler(Socket s, BufferedReader bufferedReader, DataOutputStream outputStream) {
		this.s = s;
		this.bufferedReader = bufferedReader;
		this.outputStream = outputStream;
	}

	@Override
	public void run() {
		String received;

		while (true) {
			try {
				
				received = bufferedReader.readLine();

				System.out.println(received);

				if (received.equals("GET_TEMP")) {
					this.isApp = true;
					// outputStream.writeBytes("Hello App" + '\n');
					// s.close();
					// break;
				} else {

					StringTokenizer tokenizer = new StringTokenizer(received, "#");
					String Gateway_ID = tokenizer.nextToken();
					String Sensor_ID = tokenizer.nextToken();
					String Temp = tokenizer.nextToken();

					String toGateway = "GET " + Temp + " *C from " + Gateway_ID + " at Sensor: " + Sensor_ID;

					for (ClientHandler client : TCPServer.ar) {
						if (client.isApp == true) {
							System.out.println("SEND TO APP: " + received);
							try {
								client.outputStream.writeBytes(received + '\n');
							} catch (SocketException e) {
								e.printStackTrace();
								client.s.close();
							}

							client.isApp = false;

							// break;
						}

					}
					outputStream.writeBytes(toGateway + '\n');
				}

			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}
}