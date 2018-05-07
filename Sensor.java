
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Sensor {
	public static void main(String[] args) throws Exception {
		int _ID = Integer.parseInt(args[0]);
		int GATEWAY_PORT = 9876;

		while (true) {
			Random rand = new Random();
			int sensorData = rand.nextInt(100);
			System.out.println("From sensor with ID " + _ID + ": " + sensorData);

			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName("localhost");

			String data = String.valueOf(sensorData + "*C at Sensor_ID: " + _ID);
			byte[] sendData = data.getBytes();

			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, GATEWAY_PORT);
			clientSocket.send(sendPacket);

			// DatagramPacket receivePacket = new DatagramPacket(receiveData,
			// receiveData.length);
			// clientSocket.receive(receivePacket);
			// String mess = new String(receivePacket.getData());
			// System.out.println("Gateway SAID: " + mess);

			clientSocket.close();

			TimeUnit.SECONDS.sleep(5);
		}
	}
}
