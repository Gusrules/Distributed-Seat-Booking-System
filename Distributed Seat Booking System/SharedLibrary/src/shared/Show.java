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
import java.util.Date;

// I klasi Show anaparista mia parastasi gia ena sugkekrimeno event
public class Show implements Serializable {
    private Date date;                                                          // Imerominia parastasis
    private String time;                                                        // wra parastasis
    private double price;                                                       // price
    private int totalSeats;                                                     // Synolikes diathesimes theseis
    private int availableSeats;                                                 // Theseis pou apomenoun 

    // Constructor
    public Show(Date date, String time, double price, int seats) {
        this.date = date;
        this.time = time;
        this.price = price;
        this.totalSeats = seats;
        this.availableSeats = seats;
    }

    // Kratisi thesewn
    public boolean reserveSeats(int count) {
        if (availableSeats >= count) {
            availableSeats -= count;                                            // meiwsi diathesimwn
            return true;
        }
        return false;                                                           // Den uparxoyn diathesimes 
    }

    // Akyrwsi thesewn (se peiptwsi akyrwsis kratisis)
    public void cancelSeats(int count) {
        availableSeats += count;
        if (availableSeats > totalSeats) availableSeats = totalSeats;           // if statement gia na min ksepernaei tis sunolikes theseis
    }

    // Getters
    public Date getDate(){ 
        return date; 
    }
    public String getTime(){ 
        return time; 
    }
    public double getPrice(){ 
        return price; 
    }
    public int getAvailableSeats(){ 
        return availableSeats; 
    }
    public int getTotalSeats(){ 
        return totalSeats; 
    }

    // toString
    @Override
    public String toString() {
        return "Date: " + date + ", Time: " + time + ", Price: " + price + "€, Seats: " + availableSeats + "/" + totalSeats;
    }
}
