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
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

//I klasi BookingSystemImpl einai i kuria ulopoisi tis RMI diepafis BookingSystem
public class BookingSystemImpl extends UnicastRemoteObject implements BookingSystem {

    private final UserManager userManager;                                      // Diaxeiristis xristwn
    private final SocketClientToDB socketClient;                                // Antikeimeno epikoinwnias me DBServer

    // Constructor
    public BookingSystemImpl() throws RemoteException {
        super();                                                                // kalei ton constructor tis UnicastRemoteObject gia RMI
        this.userManager = new UserManager();
        this.socketClient = new SocketClientToDB("localhost", 6000);            // Sundesi sto DBServer (localhost: 6000)
    }

    // USERS
    @Override
    public boolean registerUser(User user) throws RemoteException {
        return userManager.register(user);                                      // Eggrafi neou xristi mesw UserManager
    }

    @Override
    public boolean deleteUser(String username, String password) throws RemoteException {
        return userManager.delete(username, password);                          // Diagrafi xristi an to password einai swsto
    }

    @Override
    public User loginUser(String username, String password) throws RemoteException {
        return userManager.login(username, password);                           // Sundesi kai epistrofi tou antikeimenou user
    }

    // EVENTS
    @Override
    // add Event mono gia admins
    public boolean addEvent(Event event, String adminUsername) throws RemoteException {
        User user = userManager.getUser(adminUsername);
        if (user != null && user.getRole().equals("admin")) {
            return socketClient.sendAddEvent(event);                            // Apostoli ston DBServer
        }
        return false;
    }

    @Override
    // Deactivate mono gia admins
    public boolean deactivateEvent(String eventTitle, String adminUsername) throws RemoteException {
        User user = userManager.getUser(adminUsername);
        if (user != null && user.getRole().equals("admin")) {
            return socketClient.sendDeactivateEvent(eventTitle);
        }
        return false;
    }

    @Override
    // Anazitisi event me vasi ton titlo i to eidos
    public List<Event> searchEvents(String keyword) throws RemoteException {
        return socketClient.sendSearch(keyword);
    }

    // Order Tickets
    @Override
    public boolean orderTickets(String eventTitle, Date date, int seats, String username, String creditCard) throws RemoteException {
        return socketClient.sendTicketOrder(eventTitle, date, seats, username, creditCard);
    }

    // Akurwsi paraggelias eisitiriwn gia sugkekrimeni ekdilwsi kai xristi
    @Override
    public boolean cancelOrder(String eventTitle, Date date, String username) throws RemoteException {
        return socketClient.sendCancel(eventTitle, date, username);
    }
}
