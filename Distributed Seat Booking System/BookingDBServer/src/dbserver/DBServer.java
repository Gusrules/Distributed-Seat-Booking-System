/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * Nikolas Al- Bampoul
 * ICSD 321/2020004
 */

package dbserver;

/**
 *
 * @author nikol
 */
import java.net.ServerSocket;
import java.net.Socket;

// I klasi DBServer apotelei ton 2o eksupiretiti tou systimatos
public class DBServer {

    public static void main(String[] args) {
        final int PORT = 6000;                                                  // Port sto opoio akouei o server gia syndeseis apo ton BookingServer

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("BookingDBServer PORT: " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();                                //  Perimenei gia syndesi pelati
                System.out.println("New connection from: " + clientSocket.getInetAddress());    // emfanizei tin dieythinsi toy pelati pou syndethike
                new Thread(new DBWorker(clientSocket)).start();                             // Dimiourgei neo thread gia na eksupiretisei to aitima toy pelati ara epitrepontai polles sundeseis
            }

        } catch (Exception e) {
            System.err.println("Error on DBServer: " + e.getMessage());                  // An uparksei Error kata tin ekkinisi i tin leitoyrgia tou server
        }
    }
}
