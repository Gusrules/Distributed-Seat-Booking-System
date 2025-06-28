/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * Nikolas Al- Bampoul
 * ICSD 321/2020004
 */

package shared;

/**
 *
 * @author nikol
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

// Η Diepafi poy kaloyn oi clients gia na kanoyn kratiseis mesw toy server 
public interface BookingSystem extends Remote {

    // Diaxeirisi xristwn
    boolean registerUser(User user) throws RemoteException;                         // Eggrafi neou xristi sto systima
    boolean deleteUser(String username, String password) throws RemoteException;    // Diafrafi xristi me vasi to username kai pass
    User loginUser(String username, String password) throws RemoteException;        // Syndesi xristi, epistrofi antikeimenou user

    // Diaxeirisi Event (admin)
    boolean addEvent(Event event, String adminUsername) throws RemoteException;     // Prosthiki event
    boolean deactivateEvent(String eventTitle, String adminUsername) throws RemoteException;    // Apenergopoiisi event

    // Anazitisi kai kratisi 
    List<Event> searchEvents(String keyword) throws RemoteException;                // Epistrofi listas event me vasi keyword
    boolean orderTickets(String eventTitle, Date date, int seats, String username, String creditCard) throws RemoteException;   // kratisi ticket gia sugkekrimeno event
    boolean cancelOrder(String eventTitle, Date date, String username) throws RemoteException;                                  // akyrwsi kratisis afou h imerominia den einai h shmerini
}
