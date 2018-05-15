import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class Gateway {
	public static void main(String args[]) throws Exception {
		int GATEWAY_ID = Integer.parseInt(args[0]);
		int SERVER_PORT = 6789;
		DatagramSocket gateWay = new DatagramSocket(GATEWAY_ID);
		byte[] receiveData = new byte[1024];

		System.out.println("GATEWAY IS RUNNING AT PORT " + GATEWAY_ID);

		while (true) {
			try {

				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				gateWay.receive(receivePacket);
				String sensorData = (new String(receivePacket.getData())).trim();

				StringTokenizer tokenizer = new StringTokenizer(sensorData, "#");
				String Sensor_ID = tokenizer.nextToken();
				String Temp = tokenizer.nextToken();
				System.out.println("RECEIVED: " + Temp + " *C at Sensor_ID: " + Sensor_ID);

				// Connect To TCPServer
				Socket clientSocket = new Socket("localhost", SERVER_PORT);
				DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
				BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				try {
					String mess = new String(sensorData);

					outToServer.writeBytes(GATEWAY_ID + "#" + sensorData + '\n');
					mess = inFromServer.readLine();
					System.out.println("SERVER SAID: " + mess);

				} catch (Exception e) {
					clientSocket.close();
				}

			} catch (Exception e) {
				gateWay.close();
			}

		}
	}
}
