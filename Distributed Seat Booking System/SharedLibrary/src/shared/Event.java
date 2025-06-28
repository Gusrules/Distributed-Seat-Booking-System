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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Klasi pou anaparista ena Theama
public class Event implements Serializable {
    private String title;                       // Titlos
    private String type;                        // Eidos 
    private boolean active;                     // Energo h oxi theama
    private List<Show> shows;                   // Lista me tis parastaseis 

    // Constructor Event
    public Event(String title, String type) {
        this.title = title;
        this.type = type;
        this.active = true;
        this.shows = new ArrayList<>();
    }

    // Synartisi prosthikis parastasis 
    public void addShow(Show show) {
        shows.add(show);
    }

    // Getter
    public String getTitle(){
        return title; 
    }
    // Getter
    public String getType(){ 
        return type; 
    }
    // Epistrefei an to event einai energo h oxi 
    public boolean isActive(){ 
        return active; 
    }
    // deactivate event
    public void deactivate(){ 
        this.active = false; 
    }
    // Epistrefei lista me parastaseis event 
    public List<Show> getShows(){ 
        return shows; 
    }

    @Override
    public String toString() {
        return "Title: " + title + " (" + type + "), Active: " + active + ", Shows: " + shows.size();
    }
}
