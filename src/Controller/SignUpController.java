package Controller;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert.AlertType;


/**
 * Controller class for signing up users.
 * 
 * This class handles actions related to user registration, including validation of input fields and sending registration data to the server.
 * 
 * @author Nkoana RRM
 * @version mini_project
 */
public class SignUpController {
	
	

	
    @FXML
    private Button btnSignUp;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtSurname;
    @FXML
    private TextField txtFarmName; 
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmPassword; 
    @FXML
    private Hyperlink LoginLink;

  
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;
	private PrintWriter pw;
    
	 /**
     * Handles the user sign-up action.
     * 
     * This method is invoked when the user clicks the sign-up button.
     * 
     * @param event The action event.
     */
	public void setSocket(Socket socket, DataInputStream dis2, DataOutputStream dos2, PrintWriter pw2) throws IOException {
        this.socket = socket;
        this.dis = dis2;
		this.dos = dos2;
		this.pw = pw2;
    }

	
	 /**
     * Handles the user sign-up action.
     * 
     * This method is invoked when the user clicks the sign-up button.
     * 
     * @param event The action event.
     */
    @FXML
    void handleUserSignUp(ActionEvent event) {
        if (validateFields()) {
            try {
            	
            	// Establish connection
        	    socket = new Socket("localhost", 2024);
        	    dis = new DataInputStream(socket.getInputStream());
        	    dos = new DataOutputStream(socket.getOutputStream());
        	    pw = new PrintWriter(dos); // Assign to DataOutputStream
        	    
            	sendCommand(pw, "SIGNUP");
                sendCommand(pw, txtName.getText());
                sendCommand(pw, txtSurname.getText());
                sendCommand(pw, txtEmail.getText());
                sendCommand(pw, txtFarmName.getText());
                sendCommand(pw, txtPassword.getText());
                pw.flush();

                String response = readResponse(dis);
                if (response.equals("SIGNSUCCESS")) {
                    showAlert(AlertType.INFORMATION, "Registration Successful", "Congratulations!", "You have successfully registered.");
                    Parent root = FXMLLoader.load(getClass().getResource("/Interface/Login.fxml"));
                    btnSignUp.getScene().setRoot(root);
                    
                    System.out.println("Successfully saved user to the UserData.txt file...");
                    System.out.println("User name: " + txtName.getText());
                    System.out.println("User surname: " + txtSurname.getText());
                    System.out.println("User email account: " + txtEmail.getText()+ "\n\n");
                    
                    
                } else if (response.equals("SIGNFAILURE")) {
                    showAlert(AlertType.ERROR, "Registration Failed", "An error occurred during registration.", "Please try again later.");
                }

            } catch (IOException e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Connection Error", "Failed to connect to the server.", "Please check your connection.");
                
            }
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
    private void showAlert(AlertType type, String title, String header, String content) {
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
        if (txtName.getText().isEmpty() || txtSurname.getText().isEmpty() ||
            txtEmail.getText().isEmpty() || txtFarmName.getText().isEmpty() ||
            txtPassword.getText().isEmpty() || txtConfirmPassword.getText().isEmpty()) {
            showAlert(AlertType.ERROR, "Validation Error", "Incomplete Application!",
                    "Please make sure that you fill in all the required fields.");
            return false;
        } else if (!txtPassword.getText().equals(txtConfirmPassword.getText())) {
            showAlert(AlertType.ERROR, "Validation Error", "Passwords do not match!",
                    "Please make sure that the passwords you provide match.");
            return false;
        }
        return true;
        
    }
    
    /**
     * redirect to login page
     * @param event
     */
    @FXML
    void handleLoginRedirect(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Interface/Login.fxml"));
            LoginLink.getScene().setRoot(root); // Use LoginLink instead of btnSignUp
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
