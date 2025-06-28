/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * Nikolas Al- Bampoul
 * ICSD 321/2020004
 */

package server;

/**
 *
 * @author nikol
 */
import shared.*;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.List;

// I klasi SocketClientToDB analamvanei tin epikoinwnia tou BookingServer me ton DBServer mesw sockets
public class SocketClientToDB {

    private final String host;                                                  // Dieuthinsi tou DBServer (localhost)
    private final int port;                                                     // To port pou akouei o DBServer (6000) 

    // Constructor 
    public SocketClientToDB(String host, int port) {
        this.host = host;
        this.port = port;
    }

    // Methodos epikoinwnias 
    private Object sendRequest(String command, Object... data) {
        try (
            Socket socket = new Socket(host, port);                             // Dimiourgia socket 
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());      // Apostoli dedomenwn 
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())           // Lipsi apantisis
        ) {
            // Apostoli entolis 
            out.writeObject(command);
            // Apostoli dedomenwn
            for (Object o : data) {
                out.writeObject(o);
            }
            out.flush();

            // Apantisi apo ton server
            return in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Communication Error with DB Server: " + e.getMessage());
            return null;
        }
    }

    // Apostoli neou event gia apothikeusi
    public boolean sendAddEvent(Event event) {
        Object response = sendRequest("ADD_EVENT", event);
        return response instanceof Boolean && (Boolean) response;
    }

    // Apostoli aitimatos gia deactivation 
    public boolean sendDeactivateEvent(String eventTitle) {
        Object response = sendRequest("DEACTIVATE_EVENT", eventTitle);
        return response instanceof Boolean && (Boolean) response;
    }

    // Apostoli keyword gia anazitisi event
    @SuppressWarnings("unchecked")
    public List<Event> sendSearch(String keyword) {
        Object response = sendRequest("SEARCH_EVENT", keyword);
        if (response instanceof List) {
            return (List<Event>) response;
        }
        return null;
    }

    // Apostoli aitimatos kratisis eisitiriwn
    public boolean sendTicketOrder(String eventTitle, Date date, int seats, String username, String creditCard) {
        Object response = sendRequest("ORDER_TICKET", eventTitle, date, seats, username, creditCard);
        return response instanceof Boolean && (Boolean) response;
    }

    // Apostoli aitimatos akyrwsis kratisis
    public boolean sendCancel(String eventTitle, Date date, String username) {
        Object response = sendRequest("CANCEL_ORDER", eventTitle, date, username);
        return response instanceof Boolean && (Boolean) response;
    }

    // Arithmos upoleipomenwn thesewn gia sygkekrimeni imerominia 
    public int getRemainingSeats(String eventTitle, Date date) {
        Object response = sendRequest("REMAINING_SEATS", eventTitle, date);
        if (response instanceof Integer) {
            return (Integer) response;
        }
        return -1;
    }
}
