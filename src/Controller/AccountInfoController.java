package Controller;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import Server.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


/**
 * Controller class for managing user account information.
 * @author Nkoana RRM
 * @version mini_project
 */
public class AccountInfoController {


    @FXML
	private Button btnLogout;  
    @FXML
    private Button btnViewRecords;  
    @FXML
    private Button btnAddRecords;
    @FXML
    private Button btnReports;  
    @FXML
    private Button btnAccountInformation;
    
    
    @FXML
    private Button btnSave;

    
    @FXML
    private Label lblDashboard;
    
    
    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSurname;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtFarmName;

    @FXML
    private TextField txtPassword;
 
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private PrintWriter pw;


    /**
     * Initializes the controller. Disables text field editing and enables the save button.
     */
    public void initialize() {
    	
    	 // Disable editing of text fields
        txtName.setEditable(false);
        txtSurname.setEditable(false);
        txtEmail.setEditable(false);
        txtFarmName.setEditable(false);
    //    txtPassword.setEditable(false); // If you have a password field

        // Enable save button
        btnSave.setDisable(true);
    	
        try {
            if (socket == null || socket.isClosed()) {

            // Establish connection
            socket = new Socket("localhost", 2024);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            pw = new PrintWriter(dos); // Assign to DataOutputStream
            }

            // Retrieve user's account information
            int userId = SessionManager.getInstance().getUserId();
           
            sendCommand(pw, "INFO");
            sendCommand(pw, String.valueOf(userId));
            pw.flush();

            // Read account information from the server
            String response = readResponse(dis);
            System.out.println(response);
            
            // Parse and display account information (update text fields)
            String[] accountInfo = response.split(",");
            txtName.setText(accountInfo[0]);
            txtSurname.setText(accountInfo[1]);
            txtEmail.setText(accountInfo[2]);
            txtFarmName.setText(accountInfo[3]);
           // txtPassword.setText(accountInfo[4]);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     * Handles the action event for saving edited account information.
     * 
     * @param event The action event.
     */
    @FXML
    void handleSaveEdit(ActionEvent event) {
        // Get the updated information from the text fields
        String name = txtName.getText();
        String surname = txtSurname.getText();
        String email = txtEmail.getText();
        String farmName = txtFarmName.getText();

        try {
        	// Retrieve user's account information
            int userId = SessionManager.getInstance().getUserId();
           
            // Send command to indicate saving account info
            sendCommand(pw, "SAVEINFO");
            sendCommand(pw, String.valueOf(userId));
          

            // Send the updated information to the server
            sendCommand(pw, name);
            sendCommand(pw, surname);
            sendCommand(pw, email);
            sendCommand(pw, farmName);

            pw.flush();

            // Wait for server response if needed
            String response = readResponse(dis);
            System.out.println(response); // Print server response

            // Close connection
            socket.close();
            
            

            // Optionally, inform the user about the success/failure of the operation
            showAlert(Alert.AlertType.INFORMATION, "Success", "Account Information Updated",
                    "Your account information has been successfully updated.");
            
         // Enable editing of text fields
            txtName.setEditable(false);
            txtSurname.setEditable(false);
            txtEmail.setEditable(false);
            txtFarmName.setEditable(false);
           // txtPassword.setEditable(false); // If you have a password field

            // Enable save button
            btnSave.setDisable(true);
            
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to Update Account Information",
                    "An error occurred while trying to update your account information. Please try again.");
        }
    }

  
    /**
     * Handles the action event for editing account information.
     * 
     * @param event The action event.
     */
    @FXML
    void handleEdit(ActionEvent event) {
        // Enable editing of text fields
        txtName.setEditable(true);
        txtSurname.setEditable(true);
        txtEmail.setEditable(true);
        txtFarmName.setEditable(true);
        //txtPassword.setEditable(true); // If you have a password field

        // Enable save button
        btnSave.setDisable(false);
    }

    
    
    
    

    /**
     * Logs out the user.
     * 
     * @param event The action event.
     */
    @FXML
    void Logout(ActionEvent event) {
       

        // Call the logout method of SessionManager
        SessionManager.getInstance().logout();

        // Navigate to the login page
        navigateToLoginPage();
    }


    /*
     * navigates a user back to login page
     */
    private void navigateToLoginPage() {
    	
    	
         
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Interface/Login.fxml"));
            Scene scene = btnLogout.getScene();
            Stage thisStage = (Stage) scene.getWindow();
            thisStage.setMaximized(false);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
       
    }

    /**
     * Handles the action event for navigating to viewing farm records.
     * 
     * @param event The action event.
     */
    @FXML
    void handleViewFarmRecords(ActionEvent event) {
    	
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Interface/ViewRecord.fxml"));
            btnViewRecords.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int userId = SessionManager.getInstance().getUserId();
        System.out.println("User ID: " + userId);
    }
    
    
    /**
     * redirect to add a record for a new farm animal that was faccinated.
     *  @param event The action event.
     *  
     */
    @FXML
    void handleAddRecord(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Interface/AddRecord.fxml"));
            btnAddRecords.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int userId = SessionManager.getInstance().getUserId();
        System.out.println("User ID: " + userId);
    }
    
    
    
    /**
     * view the account information page
     *  @param event The action event.
     */
    @FXML
    void handleReports(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Interface/Reports.fxml"));
            btnReports.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int userId = SessionManager.getInstance().getUserId();
        System.out.println("User ID: " + userId);
    }
    
    
    /**
     * view the account information page
     *   @param event The action event.
     */
    @FXML
    void handleAccountInfo(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Interface/AccountInfo.fxml"));
            btnAccountInformation.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int userId = SessionManager.getInstance().getUserId();
        System.out.println("User ID: " + userId);
    }
    
    
    
    
    /**
     * view the dashboard home page
     *  @param event The action event.
     */
    @FXML
    void handleHome(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Interface/Dashboard.fxml"));
            lblDashboard.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int userId = SessionManager.getInstance().getUserId();
        System.out.println("User ID: " + userId);
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
    
}