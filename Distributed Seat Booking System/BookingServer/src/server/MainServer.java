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
import shared.BookingSystem;

import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;
import java.rmi.RemoteException;

// I klasi MainServer einai to simeio ekkinisis gia ton RMI
public class MainServer {
    public static void main(String[] args) {
        try {
            // Ekkinisi RMI registry topika sto port 1099
            try {
                LocateRegistry.createRegistry(1099);
                System.out.println("RMI Registry On Port: 1099");
            } catch (RemoteException e) {
                System.out.println("RMI Registry Already Running");             // An trexei idi, sunexizei kanonika 
            }

            // Dimiourgia antikeimenou BookingSystem
            BookingSystem system = new BookingSystemImpl();                     // Vasiki ulopoiisi tis RMI diepafis

            // Kataxwrisi ston registry me onoma BookingService gia anazitisi apo clients.
            Naming.rebind("BookingService", system);
            System.out.println("BookingServer Ready for RMI connections.");

        } catch (Exception e) {
            System.err.println("Error on start of BookingServer:");
            e.printStackTrace();
        }
    }
}
