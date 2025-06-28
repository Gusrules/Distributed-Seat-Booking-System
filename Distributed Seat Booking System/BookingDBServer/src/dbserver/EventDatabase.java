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
import shared.Event;
import shared.Show;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// H EventDatabase apotelei mia in-memory vasi dedomenwn gia theamata kai parastaseis
public class EventDatabase {

    private static EventDatabase instance = null;                               // Krataei to monadiko instance tis EventDatabase
    private final Map<String, Event> events;                                    // Sygxronismenos xartis pou apothikeuei ta events me vasi ton titlo tous

    private EventDatabase() {
        events = new ConcurrentHashMap<>();
    }

    // Epistrefei to monadiko instance tis vasis (an den uparxei to dimiourgei)
    public static synchronized EventDatabase getInstance() {
        if (instance == null) {
            instance = new EventDatabase();
        }
        return instance;
    }

    // Add Event
    public synchronized boolean addEvent(Event event) {
        if (events.containsKey(event.getTitle())) {
            return false;                                                       // uparxei idi 
        }
        events.put(event.getTitle(), event);                                    // kataxwrisi neou event
        return true;
    }

    // Deactivate Event
    public synchronized boolean deactivateEvent(String title) {
        Event event = events.get(title);                                        // Eyresi event me vasi ton titlo
        if (event != null) {
            event.deactivate();                                                 // Kanei deactivate to event
            return true;
        }
        return false;
    }

    // Seatch Event
    public synchronized List<Event> searchEvents(String keyword) {
        List<Event> result = new ArrayList<>();
        for (Event e : events.values()) {
            // Prosthiki stin lista apotelesmatwn mono an einai active
            if (e.isActive() && (e.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                                 e.getType().toLowerCase().contains(keyword.toLowerCase()))) {
                result.add(e);
            }
        }
        return result;
    }

    // Reserve Seats
    public synchronized boolean reserveSeats(String title, Date date, int numSeats) {
        Event event = events.get(title);
        if (event == null || !event.isActive()) return false;                   // Den uparxei, alliws den einai energo

        for (Show show : event.getShows()) {                                    // Anazitisi parastasis me idia imerominia kai kratisi thesewn 
            if (sameDay(show.getDate(), date)) {
                return show.reserveSeats(numSeats);                             // Kratisi thesewn an uparxoun arketes
            }
        }
        return false;
    }

    // Cancel Seats
    public synchronized boolean cancelSeats(String title, Date date) {
        Event event = events.get(title);
        if (event == null) return false;

        for (Show show : event.getShows()) {
            if (sameDay(show.getDate(), date)) {
                show.cancelSeats(1);                                            // Akyrwnei 1 thesi
                return true;
            }
        }
        return false;
    }

    // Epistrofi Ypoloipwn thesewn 
    public synchronized int getRemainingSeats(String title, Date date) {
        Event event = events.get(title);
        if (event == null) return -1;

        for (Show show : event.getShows()) {
            if (sameDay(show.getDate(), date)) {
                return show.getAvailableSeats();                                // Epistrofi diathesimwn
            }
        }
        return -1;
    }

    // Elegxos an duo imerominies einai stin idia mera (Oxi wra) 
    private boolean sameDay(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
               c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }
}
