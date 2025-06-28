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

// I klasi User montelopoiei enan xristi (admin/user)
public class User implements Serializable {
    private String fullName;                                                    // Onoma, epwnumo
    private String phone;                                                       // thlefwno
    private String email;                                                       // email
    private String username;                                                    // username (unique)
    private String password;                                                    // password
    private String role;                                                        // admin or user

    // Constructor
    public User(String fullName, String phone, String email, String username, String password, String role) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters 
    public String getFullName(){ 
        return fullName; 
    }
    public String getPhone(){ 
        return phone; 
    }
    public String getEmail(){ 
        return email; 
    }
    public String getUsername(){ 
        return username; 
    }
    public String getPassword(){ 
        return password; 
    }
    public String getRole() { 
        return role; 
    }

    // toString
    @Override
    public String toString() {
        return fullName + " (" + username + ")";
    }
}
