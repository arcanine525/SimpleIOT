
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;

public class Gateway {
	public static void main(String args[]) throws Exception {
		int GATEWAY_ID = Integer.parseInt(args[0]);
		int SERVER_PORT = 6789;
		DatagramSocket gateWay = new DatagramSocket(GATEWAY_ID);
		byte[] receiveData = new byte[1024];
		// String message = "Ok";
		System.out.println("GATEWAY IS RUNNING AT PORT " + GATEWAY_ID);

		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			gateWay.receive(receivePacket);
			String sensorData = (new String(receivePacket.getData())).trim();
			System.out.println("RECEIVED: " + sensorData);

			Socket clientSocket = new Socket("localhost", SERVER_PORT);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String mess = new String(sensorData);
			outToServer.writeBytes(GATEWAY_ID + " with temp: " + sensorData + '\n');
			mess = inFromServer.readLine();
			System.out.println("SERVER SAID: " + mess);
			clientSocket.close();

		}
		// gateWay.close();

	}
}
