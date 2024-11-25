package Controller;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import Server.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


/**
 * Controller class for adding animal records.
 * @author Nkoana RRM
 * @version mini_project
 *
 */
public class AddRecordController{

	
	
	
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
    private Button btnAdd;
	
	
    @FXML
    private Label lblDashboard; 
    
    @FXML
    private TextField txtName;
    
    @FXML
    private TextField txtType;
    
    @FXML
    private TextField txtBreed;
    
    @FXML
    private TextField txtDOB;
    
    @FXML
    private TextField txtVaccinated;
    
    @FXML
    private ComboBox<String> cbVaccinated;
    
 
	
    // Socket and streams
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private PrintWriter pw;
	
    
    /**
     * Initializes the controller. Establishes connection to the server.
     */
    public void initialize() {
    	
    	// Initialize ComboBox items
        ObservableList<String> options = FXCollections.observableArrayList("Yes", "No");
        cbVaccinated.setItems(options);
        
    	 try  {

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
     * Handles the action event for adding an animal record.
     * 
     * @param event The action event.
     */
    @FXML
    void handleAddAnimalRecord(ActionEvent event)  {
    	
 
    	 // Get the updated information from the text fields
        String name = capitalizeFirstLetter(txtName.getText());
        String type = txtType.getText().toLowerCase();
        String breed = txtBreed.getText().toLowerCase();
        String DOB = txtDOB.getText();
   
        //String vaccinated = txtVaccinated.getText(); //yes or no
        String vaccinated = cbVaccinated.getValue();
        
        

        try {
        	// Retrieve user's account information
            int userId = SessionManager.getInstance().getUserId();
           
            // Send command to indicate saving account info
            sendCommand(pw, "ADDRECORD");
            sendCommand(pw, String.valueOf(userId));
          

            // Send the updated information to the server
            sendCommand(pw, name);
            sendCommand(pw, type);
            sendCommand(pw, breed);
            sendCommand(pw, DOB);
            sendCommand(pw, vaccinated);

            pw.flush();

            // Wait for server response if needed
            String response = readResponse(dis);
            System.out.println(response); // Print server response

            // Close connection
            socket.close();
            
            

            // Optionally, inform the user about the success/failure of the operation
            showAlert(Alert.AlertType.INFORMATION, "Success", "An animal record has been added",
                    "To view the record go to View Record Page .");
            
         
           Parent root = FXMLLoader.load(getClass().getResource("/Interface/ViewRecord.fxml"));
           btnViewRecords.getScene().setRoot(root);
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to Add Animal Record",
                    "An error occurred while trying to add animal record. Please try again.");
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
     * Capitalizes the first letter of a string.
     * 
     * @param name The string to capitalize.
     * @return The string with the first letter capitalized.
     */
    private String capitalizeFirstLetter(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase();
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
     * redirect to add a record for a new farm animal that was vaccinated.
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
