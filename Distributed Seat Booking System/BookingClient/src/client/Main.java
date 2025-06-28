/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * Nikolas Al- Bampoul
 * ICSD 321/2020004
 */

package client;

/**
 *
 * @author nikol
 */
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import shared.BookingSystem;
import shared.Event;
import shared.Show;
import shared.User;

import java.rmi.Naming;
import java.text.SimpleDateFormat;
import java.util.List;

public class Main extends Application {

    private BookingSystem server;                                               // Antikeimeno BookingSystem gia RMI epikoinwnia

    @Override
    public void start(Stage stage) {
        try {
            server = (BookingSystem) Naming.lookup("rmi://localhost/BookingService");       // Anazitisi antikeimenou BookingSystem mesw RMI
        } catch (Exception e) {
            e.printStackTrace();
            showError("Could not connect to server.");                          // Error an apotuxei
            return;
        }

        showLoginScreen(stage);                                                 // Ekkinisi Efarmogis me othoni Login
    }

    private void showLoginScreen(Stage stage) {
        Label title = new Label("Login to Booking System");                     // Titlos Login othonis
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();                              // Pedio username
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();                      // Pedio password
        passwordField.setPromptText("Password");

        Label messageLabel = new Label();                                       // Emfanisi minimatwn

        Button loginButton = new Button("Login");                               // button Login
        Button registerButton = new Button("Register");                         // Button Register

        HBox buttons = new HBox(10, loginButton, registerButton);               // Omadopoiisi Buttons horizontally
        VBox root = new VBox(10, title, usernameField, passwordField, buttons, messageLabel);       // Ola ta components se katakoryfo layout
        root.setPadding(new Insets(20));                                       
        root.setStyle("-fx-background-color: #f2f2f2;");                        // grey backround
        root.setPrefWidth(300);
        root.setPrefHeight(250);

        loginButton.setOnAction(e -> {                                          // patima button Login
            try {
                String username = usernameField.getText();
                String password = passwordField.getText();
                User user = server.loginUser(username, password);               // Prospatheia sundesis me ton server
                if (user != null) {
                    showMainMenu(stage, user);                                  // successful login
                } else {
                    messageLabel.setText("Invalid login.");                     // else error message
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                messageLabel.setText("Error connecting to server.");
            }
        });

        registerButton.setOnAction(e -> showRegisterScene(stage));              // Patima button Register

        stage.setScene(new Scene(root));                                        // Emfanisi Register window
        stage.setTitle("Ticket Booking - Login");
        stage.show();
    }
    private void showMainMenu(Stage stage, User user) {                         
        Label title = new Label("Welcome, " + user.getFullName() + " (" + user.getRole() + ")");    // Welcome minima me to onoma kai ton rolo tou user
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button searchBtn = new Button("Search Events");                         // Button anazitisis Event
        Button logoutBtn = new Button("Logout");                                // Button gia logout

        VBox buttons = new VBox(10, searchBtn);                                 // Katakoryfo layout gia ta koumpia

        if (user.getRole().equals("user")) {                                    // if statement gia ton rolo (admin/user)
            Button bookBtn = new Button("Book Tickets");                        // Button kratisi eisitiriwn
            Button cancelBtn = new Button("Cancel Reservation");                // Button akurwsi kratisis 
            buttons.getChildren().addAll(bookBtn, cancelBtn);                   // Prosthiki sto layout

            bookBtn.setOnAction(e -> showBookingScene(stage, user));            // Kathorismos energeias button
            cancelBtn.setOnAction(e -> showCancelScene(stage, user));           // Kathorismos energeias button
        } else {
            // gia admin
            Button addBtn = new Button("Add Event");                            // Button Prosthikis neou Event
            Button deactivateBtn = new Button("Deactivate Event");              // Button Apenergopoiisi Event
            buttons.getChildren().addAll(addBtn, deactivateBtn);

            addBtn.setOnAction(e -> showAddEventScene(stage, user));
            deactivateBtn.setOnAction(e -> showDeactivateEventScene(stage, user));
        }

        buttons.getChildren().add(logoutBtn);                                   // Button Aposundesis

        searchBtn.setOnAction(e -> showSearchScene(stage, user));               // Energeia Button
        logoutBtn.setOnAction(e -> showLoginScreen(stage));                     // Energeia Button

        VBox layout = new VBox(15, title, buttons);                             // Layout
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #eef;");
        layout.setPrefWidth(350);

        stage.setScene(new Scene(layout));
    }
    private void showRegisterScene(Stage stage) {
        Label title = new Label("Create New Account");                          // Titlos parathyrou eggrafis
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField fullNameField = new TextField();                              // Eisagwgi Full Name
        fullNameField.setPromptText("Full Name");

        TextField emailField = new TextField();                                 // Eisagwgi Email
        emailField.setPromptText("Email");

        TextField phoneField = new TextField();                                 // Eisagwgi Phone
        phoneField.setPromptText("Phone");

        TextField usernameField = new TextField();                              // Eisagwgi Username
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();                      // Eisagwgi Password
        passwordField.setPromptText("Password");

        ComboBox<String> roleBox = new ComboBox<>();                            // Epilogi Rolou
        roleBox.getItems().addAll("user", "admin");
        roleBox.setPromptText("Select Role");

        Label resultLabel = new Label();                                        // Etiketa gia emfanisi minimatwn

        Button registerBtn = new Button("Register");                            // Button Eggrafis
        Button backBtn = new Button("Back");                                    // Button Epistrofis

        registerBtn.setOnAction(e -> {                                          // Energeia button Register
            try {
                // anagnwsi dedomenwn apo ta pedia 
                String fullName = fullNameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String username = usernameField.getText();
                String password = passwordField.getText();
                String role = roleBox.getValue();

                if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() ||     // if statement giga sumplirwsi olwn twn pediwn
                    username.isEmpty() || password.isEmpty() || role == null) {
                    resultLabel.setText("Please fill all fields.");
                    return;
                }

                User newUser = new User(fullName, phone, email, username, password, role);          // Dimiourgia antikeimenou xristi kai apostoli ston server
                boolean success = server.registerUser(newUser);
                resultLabel.setText(success ? "Registration successful!" : "User already exists.");
            } catch (Exception ex) {
                ex.printStackTrace();
                resultLabel.setText("Error during registration.");
            }
        });

        backBtn.setOnAction(e -> showLoginScreen(stage));                       // Energeia back button

        VBox layout = new VBox(10,                                              // layout
            title, fullNameField, emailField, phoneField,
            usernameField, passwordField, roleBox,
            registerBtn, resultLabel, backBtn
        );
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f0f0f0;");

        stage.setScene(new Scene(layout, 400, 450));                            // Emfanisi skinis eggrafis
    }

    private void showSearchScene(Stage stage, User user) {
        Label title = new Label("Search Events");                               // Titlos parathyrou anazitisis
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField keywordField = new TextField();                               // Pedio eisagwgis keyword gia tin anazitisi (titlos/eidos)
        keywordField.setPromptText("Enter keyword (title or type)");

        TextArea resultsArea = new TextArea();                                  // Perioxi keimenou gia emfanisi apotelesmatwn
        resultsArea.setEditable(false);                                         // Den epitrepei epeksergasia apo ton xristi
        resultsArea.setWrapText(true);                                          // Automati anadiplwsi keimenou
        resultsArea.setPrefHeight(200);                                         // Height

        Button searchBtn = new Button("Search");                                // Button search
        Button backBtn = new Button("Back");                                    // Button Back

        searchBtn.setOnAction(e -> {                                            // Energeia button anazitisis
            String keyword = keywordField.getText();                            // Pairnei to keyword apo ton xristi
            try {
                List<Event> results = server.searchEvents(keyword);             // Anazita ekdilwseis ston server
                if (results == null || results.isEmpty()) {
                    resultsArea.setText("No events found.");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (Event ev : results) {
                        sb.append(ev.toString()).append("\n");                  // Emfanizei tis ekdilwseis ws string
                    }
                    resultsArea.setText(sb.toString());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                resultsArea.setText("Error fetching events.");
            }
        });

        backBtn.setOnAction(e -> showMainMenu(stage, user));                    // Epistrofi sto main menu

        VBox layout = new VBox(10, title, keywordField, searchBtn, resultsArea, backBtn);           // layout
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #ffffff;");

        stage.setScene(new Scene(layout, 400, 350));                            // Rythmisi kai emfanisi skinis
    }

    private void showBookingScene(Stage stage, User user) {
        Label title = new Label("Book Tickets");                                // Titlos parathyrou
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField eventField = new TextField();                                 // Eisagwgi titlou
        eventField.setPromptText("Event title");

        TextField dateField = new TextField();                                  // Eisagwgi Date
        dateField.setPromptText("Date (dd-MM-yyyy)");

        TextField seatsField = new TextField();                                 // Eisagwgi arithmou eisitiriwn
        seatsField.setPromptText("Number of tickets");

        TextField cardField = new TextField();                                  // Eisagwgi arithmou kartas
        cardField.setPromptText("Credit card number");

        Label resultLabel = new Label();                                        // Etiketa gia emfanisis apotelesmatos kratisis

        Button bookBtn = new Button("Book");                                    // Button Book
        Button backBtn = new Button("Back");                                    // Button Back

        bookBtn.setOnAction(e -> {                                              // Energeia kratisis eisitiriwn
            try {
                // Anagnwsi timwn apo ta pedia
                String titleText = eventField.getText();
                String dateText = dateField.getText();
                int numSeats = Integer.parseInt(seatsField.getText());
                String card = cardField.getText();

                java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateText);   // Metatropi imerominias se Date antikeimeno

                boolean success = server.orderTickets(titleText, date, numSeats, user.getUsername(), card);     // Apostoli aitimatos kratisis server
                resultLabel.setText(success ? "Booking successful!" : "Booking failed.");
            } catch (Exception ex) {
                ex.printStackTrace();
                resultLabel.setText("Error: check input values.");
            }
        });

        backBtn.setOnAction(e -> showMainMenu(stage, user));                    // Epistrofi sto main menu

        VBox layout = new VBox(10, title, eventField, dateField, seatsField, cardField, bookBtn, resultLabel, backBtn);     // layout
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f4f4f4;");

        stage.setScene(new Scene(layout, 400, 400));                            // emfanisi skinis
    }

    private void showCancelScene(Stage stage, User user) {
        Label title = new Label("Cancel Reservation");                          // Titlos parathyrou
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField eventField = new TextField();                                 // Eisagwgi Title
        eventField.setPromptText("Event title");

        TextField dateField = new TextField();                                  // Eisagwgi Date
        dateField.setPromptText("Date (dd-MM-yyyy)");

        Label resultLabel = new Label();                                        // Etiketa gia emfanisi minimatwn

        Button cancelBtn = new Button("Cancel Reservation");                    // Button Cancel Reservation
        Button backBtn = new Button("Back");                                    // Button back

        cancelBtn.setOnAction(e -> {                                            // Energeia akyrwsis kratisis
            try {
                // Anagnwsi eisodou xristi
                String titleText = eventField.getText();
                String dateText = dateField.getText();
                java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateText);   // metatropi imerominias se antikeimeno Date

                boolean success = server.cancelOrder(titleText, date, user.getUsername());  // Klisi methodou tou server gia akyrwsi kratisis
                resultLabel.setText(success ? "Reservation cancelled!" : "Cannot cancel reservation.");
            } catch (Exception ex) {
                ex.printStackTrace();
                resultLabel.setText("Error: check input values.");
            }
        });

        backBtn.setOnAction(e -> showMainMenu(stage, user));                    // Epistrofi sto main menu

        VBox layout = new VBox(10, title, eventField, dateField, cancelBtn, resultLabel, backBtn);  // layout
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f9f9f9;");

        stage.setScene(new Scene(layout, 400, 300));                            // Emfanisi skinis sto parathyro
    }

    private void showAddEventScene(Stage stage, User user) {
        Label title = new Label("Add New Event");                               // Titlos parathyrou
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField eventTitleField = new TextField();                            // Eisagwgi title
        eventTitleField.setPromptText("Event title");

        TextField typeField = new TextField();                                  // Eisagwgi type
        typeField.setPromptText("Event type");

        TextField dateField = new TextField();                                  // Eisagwgi Date 
        dateField.setPromptText("Date (dd-MM-yyyy)");

        TextField timeField = new TextField();                                  // Eisagwgi time
        timeField.setPromptText("Time (e.g. 20:00)");

        TextField priceField = new TextField();                                 // Eisagwgi Price
        priceField.setPromptText("Price (€)");

        TextField seatsField = new TextField();                                 // Eisagwgi seats
        seatsField.setPromptText("Available seats");

        TextArea showListArea = new TextArea();                                 // Perioxi emfanisis prosthithemenwn parastasewn
        showListArea.setEditable(false);
        showListArea.setPrefHeight(120);

        Label resultLabel = new Label();                                        // Etiketa apotelesmatos gia emfanisi minimatwn

        Button addShowBtn = new Button("Add Show");                             // Button Add show
        Button submitBtn = new Button("Submit Event");                          // Button Submit event
        Button backBtn = new Button("Back");                                    // Back button

        Event[] eventHolder = new Event[1];                                     // Xrisi pinaka gia apothikeysi tou event

        addShowBtn.setOnAction(e -> {                                           // Logiki prosthikis parastasis stin ekdilwsi
            try {
                // Dimiourgia neou event an den exei idi dimiourgithei
                if (eventHolder[0] == null) {
                    eventHolder[0] = new Event(eventTitleField.getText(), typeField.getText());
                }

                // Anagnwsi timwn apo ta pedia
                java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateField.getText());
                String time = timeField.getText();
                double price = Double.parseDouble(priceField.getText());
                int seats = Integer.parseInt(seatsField.getText());

                // Dimiourgia kai prosthiki parastasis sto event
                Show show = new Show(date, time, price, seats);
                eventHolder[0].addShow(show);
                showListArea.appendText("Show added: " + dateField.getText() + " " + time + "\n");              // Prosthiki emfanisis sti lista emfanisewn 

                dateField.clear(); timeField.clear(); priceField.clear(); seatsField.clear();           // Katharismos pediwn
            } catch (Exception ex) {
                ex.printStackTrace();
                resultLabel.setText("Invalid input for show.");
            }
        });

        submitBtn.setOnAction(e -> {                                                    // Logiki upovolis ekdilwsis ston server
            try {
                if (eventHolder[0] != null) {
                    boolean success = server.addEvent(eventHolder[0], user.getUsername());              // Apostoli tou event ston server
                    resultLabel.setText(success ? "Event submitted successfully!" : "Failed to submit event.");
                    eventHolder[0] = null;
                    showListArea.clear();
                } else {
                    resultLabel.setText("No shows added.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                resultLabel.setText("Error during submission.");
            }
        });

        backBtn.setOnAction(e -> showMainMenu(stage, user));                    // Epistrofi sto main menu

        VBox layout = new VBox(10,                                              // layout
                title,
                eventTitleField, typeField,
                dateField, timeField, priceField, seatsField,
                addShowBtn, showListArea,
                submitBtn, resultLabel, backBtn
        );
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #fff;");
        layout.setPrefWidth(400);

        stage.setScene(new Scene(layout, 450, 550));
    }

    private void showDeactivateEventScene(Stage stage, User user) {
        Label title = new Label("Deactivate Event");                            // Titlos parathyrou
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField eventTitleField = new TextField();                            // Eisagwgi titloy gia deactivate
        eventTitleField.setPromptText("Event title to deactivate");

        Label resultLabel = new Label();                                        // Etiketa gia emfanisi apotelesmatos

        Button deactivateBtn = new Button("Deactivate");                        // Button Deactivate
        Button backBtn = new Button("Back");                                    // Button Back

        deactivateBtn.setOnAction(e -> {                                        // Logiki pou ekteleitai otan patithei to button deactivate
            try {
                String titleText = eventTitleField.getText();                   // Anagnwsi titlou ekdilwsis apo to pedio
                boolean success = server.deactivateEvent(titleText, user.getUsername());        // klisi tis methodou ston server gia apenergopoiisi tis ekdilwsis
                resultLabel.setText(success ? "Event deactivated." : "Failed to deactivate event.");
            } catch (Exception ex) {
                ex.printStackTrace();
                resultLabel.setText("Error during deactivation.");
            }
        });

        backBtn.setOnAction(e -> showMainMenu(stage, user));                    // Epistrofi sto main menu

        VBox layout = new VBox(10,                                              // layout
                title, eventTitleField,
                deactivateBtn, resultLabel, backBtn
        );
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f8f8f8;");

        stage.setScene(new Scene(layout, 400, 250));                            // Emfanisi skinis sto stage
    }

    private void showError(String message) {                                    // Emfanisi parathyrou me minima lathous ston xristi
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);    // Dimiourgei ena alert typou ERROR kai deixnei to sugkekrimeno minima 
        alert.showAndWait();                                                    // Emfanisi tou alert ston xristi 
    }

    public static void main(String[] args) {
        launch(args);                                                           // Ekkinisi tis JavaFX efarmogis
    }
}
