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
import shared.User;
import java.util.concurrent.ConcurrentHashMap;

// I klasi UserManager diaxeirizetai olous tous eggegrammenous xristes 
public class UserManager {
    private final ConcurrentHashMap<String, User> users;                        // Xartis pou sundeei username me antikeimeno xristi

    // Constructor
    public UserManager() {
        users = new ConcurrentHashMap<>();
    }

    // Eggrafi neou xristi
    public boolean register(User user) {
        if (users.containsKey(user.getUsername())) {                            // if statement an uparxei idi auto to username
            return false; 
        }
        users.put(user.getUsername(), user);                                    // prosthiki user ston xarti
        return true;
    }

    // Delete user
    public boolean delete(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {              // if statement pou elegxei an uparxei o xristis kai o kwdikos einai swstos
            users.remove(username);                                             // afairesi xristi
            return true;
        }
        return false;
    }

    // Sundesi xristi
    public User login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {              // if statement pou elegxei an uparxei o xristis kai o kwdikos einai swstos, tote epistrefei antikeimeno xristi
            return user;
        }
        return null;
    }

    // Euresi xristi 
    public User getUser(String username) {
        return users.get(username);                                             // Epistrefei to user object me vasi to username
    }
}
