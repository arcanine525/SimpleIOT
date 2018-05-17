import javax.print.DocFlavor.STRING;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.net.*;

public class App extends JFrame {

    public static void main(String[] args) throws Exception {
        int SERVER_PORT = Integer.parseInt(args[0]);
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        model.addColumn("#No");
        model.addColumn("Gatway_ID");
        model.addColumn("Sensor_ID");
        model.addColumn("Temperature");

        JFrame f = new JFrame();
        f.setSize(450, 450);
        f.add(new JScrollPane(table));
        f.setVisible(true);
        int id = 0;

        while (true) {
            // Connect To TCPServer
            Socket clientSocket = new Socket("localhost", SERVER_PORT);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            try {
                String mess;
                outToServer.writeBytes("GET_TEMP" + '\n');
                mess = inFromServer.readLine();
                System.out.println("SERVER SAID: " + mess);

                StringTokenizer tokenizer = new StringTokenizer(mess, "#");
                String Gateway_ID = tokenizer.nextToken();
                String Sensor_ID = tokenizer.nextToken();
                String Temp = tokenizer.nextToken();
                model.addRow(new Object[] { id, Gateway_ID, Sensor_ID, Temp });

            } catch (Exception e) {
                clientSocket.close();
            }
            id++;
            System.out.println("REQ: " + id);
            // TimeUnit.SECONDS.sleep(5);
        }
    }
}
