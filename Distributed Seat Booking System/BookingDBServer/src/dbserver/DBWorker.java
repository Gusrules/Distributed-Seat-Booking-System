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
import shared.*;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.List;

// I klasi DBWorker xeirizetai ena aitima pelati pou sundethike ston DBServer mesw socket
public class DBWorker implements Runnable {
    private final Socket socket;                                                // To socket epikoinwnias me ton BookingServer
    private final EventDatabase db;                                             // H vasi dedomenwn twn tehamatwn

    // Constructor lamvanei to socket toy pelati 
    public DBWorker(Socket socket) {
        this.socket = socket;
        this.db = EventDatabase.getInstance(); // singleton για κοινά δεδομένα
    }

    @Override
    public void run() {
        try (
            // Xrisi Object Streams gia apostoli/ lipsi antikeimenwn mesw tou socket 
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            // Diavazei tin proti entoli apo ton client
            String command = (String) in.readObject();

            // Analoga tin entoli ekteleitai h antistoixi leitourgia
            switch (command) {
                case "ADD_EVENT":
                    Event event = (Event) in.readObject();                      // Lipsi antikeimenou Event
                    boolean added = db.addEvent(event);                         // Prosthiki stin vasi
                    out.writeObject(added);                                     // Epistrofi apotelesmatos
                    break;

                case "DEACTIVATE_EVENT":
                    String title = (String) in.readObject();                    // Lipsi titloy
                    boolean deactivated = db.deactivateEvent(title);            // deactivate
                    out.writeObject(deactivated);
                    break;

                case "SEARCH_EVENT":
                    String keyword = (String) in.readObject();                  // Lipsi keyword
                    List<Event> results = db.searchEvents(keyword);             // anazitisi stin vasi
                    out.writeObject(results);
                    break;

                case "ORDER_TICKET":
                    String eTitle = (String) in.readObject();
                    Date date = (Date) in.readObject();
                    int seats = (int) in.readObject();
                    String user = (String) in.readObject();
                    String cc = (String) in.readObject();                       // placeholder
                    boolean ordered = db.reserveSeats(eTitle, date, seats);     // kratisi
                    out.writeObject(ordered);
                    break;

                case "CANCEL_ORDER":
                    String cancelTitle = (String) in.readObject();              // Lipsi titloy
                    Date cancelDate = (Date) in.readObject();                   // lipsi imerominias
                    boolean canceled = db.cancelSeats(cancelTitle, cancelDate); // Cancellation
                    out.writeObject(canceled);
                    break;

                case "REMAINING_SEATS":
                    String rTitle = (String) in.readObject();                   // Lipsi titloy
                    Date rDate = (Date) in.readObject();                        // Lipsi imerominias
                    int remaining = db.getRemainingSeats(rTitle, rDate);        // Diathesimotita
                    out.writeObject(remaining);
                    break;

                default:
                    out.writeObject(null);
                    break;
            }

            // Ola ta data apostellontai 
            out.flush();
        } catch (Exception e) {
            // Error 
            System.err.println("DBWorker Error: " + e.getMessage());
        }
    }
}
