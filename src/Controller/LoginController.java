package Controller;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import CryptoPackage.Cryptography;
import Server.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.paint.ImagePattern;
import javafx.scene.control.Alert.AlertType;



/**
 * login  controller for farm care
 *	@author Nkoana RRM
 * @version mini_project
 *
 */
public class LoginController {

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Hyperlink signUpLink;

    // Socket and streams
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private PrintWriter pw;

    
    /**
     * Initializes the controller. Establishes connection to the server.
     * 
     * Displays an error dialog if connection fails.
     */
    public void initialize() {
    	try {
    	    // Establish connection
    	    socket = new Socket("localhost", 2024);
    	    dis = new DataInputStream(socket.getInputStream());
    	    dos = new DataOutputStream(socket.getOutputStream());
    	    pw = new PrintWriter(dos); // Assign to DataOutputStream
    	} catch (IOException e) {
    	    e.printStackTrace();
    	    showAlert(Alert.AlertType.ERROR, "Connection Error", "Failed to connect to the server.",
    	            "Please check your connection.");
    	}

    }
    
    /**
     * Handles the login button click event.
     * 
     * Validates user input, sends login request to the server,
     * reads server response, and navigates to the dashboard upon successful login.
     */

    @FXML
    void handleUserLogin() {
    	
         
        if (validateFields()) {
            try {
                // Send login request to the server
                sendCommand(pw, "LOGIN");
                sendCommand(pw, txtEmail.getText());
                sendCommand(pw,Cryptography.hashPassword(txtPassword.getText())); 
                pw.flush();

                String response = readResponse(dis); // Read server response
                System.out.println(response);
           	
                	
                	 // Use regular expression to extract the numeric part from the response since the reponse is sent as LOGINSUCCESS1 where 1 is the userID of a user
                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher = pattern.matcher(response);
                    
                    
                    
                    if (matcher.find()) {
                        int userId = Integer.parseInt(matcher.group()); // Extract user ID
                        SessionManager.getInstance().setUserId(userId); // Set the UserID in the SessionManager

                	
          
                    // Redirect to the dashboard
                    Parent root = FXMLLoader.load(getClass().getResource("/Interface/Dashboard.fxml"));
                    btnLogin.getScene().setRoot(root);
                    
                    System.out.println("User has successfully logged into their account...");
                    
              
     
                    System.out.println( "Email:"+ txtEmail.getText() + "\n");
                    System.out.println( "UserID: "+ userId + "\n");
          
                } else if (response.equals("LOGINFAILURE")){
                    // Show login failure message
                    showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Login Information",
                            "Please make sure that the email and password are correct.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Connection Error", "Failed to connect to the server.",
                        "Please check your connection.");
            }
        }
    }

    
    /**
     * Handles the sign-up hyperlink click event.
     * 
     * Navigates to the sign-up page upon clicking the hyperlink.
     */
    @FXML
    void handleSignUpRedirect() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Interface/SignUp.fxml"));
            signUpLink.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays an alert dialog with the specified type, title, header, and content.
     * 
     * @param type    The type of the alert.
     * @param title   The title of the alert.
     * @param header  The header text of the alert.
     * @param content The content text of the alert.
     */
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
        
    }
        
    /**
     * Reads the server's response from the input stream.
     * 
     * @param dis The DataInputStream to read from.
     * @return The server's response.
     * @throws IOException If an I/O error occurs.
     */
    private String readResponse(DataInputStream dis) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(dis));
        return br.readLine();
    }

    
    /**
     * Sends a command to the server.
     * 
     * @param pw  The PrintWriter to write to.
     * @param msg The command to send.
     */
    private void sendCommand(PrintWriter pw, String msg) {
        pw.println(msg);
        pw.flush();
    }

    /**
     * Validates the entries in the text fields.
     * 
     * Displays an error dialog if any required field is empty.
     * 
     * @return true if all fields are filled; false otherwise.
     */
    private boolean validateFields() {
        if (txtEmail.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            showAlert(AlertType.ERROR, "Validation Error", "Incomplete Application!",
                    "Please make sure that you fill in all the required fields.");
            return false;
        } 
        return true;
    }
}
