
import java.io.*;
import java.net.*;

public class TCPServer {
	public static void main(String argv[]) throws Exception {
		int SERVER_PORT = 6789;
		String sensorData;
		String message = "Ok";
		ServerSocket welcomeSocket = new ServerSocket(SERVER_PORT);
		System.out.println("SERVER IS RUNNING AT PORT " + SERVER_PORT + "...");
		while (true) {
			Socket connectionSocket = welcomeSocket.accept();

			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

			sensorData = inFromClient.readLine();
			System.out.println("Received from gateway: " + sensorData);

			outToClient.writeBytes(message + '\n');
		}
	}
}
