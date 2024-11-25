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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;



/**
 * Controller class for viewing records.
 * 
 * Handles the logic for displaying all records, vaccinated records, unvaccinated records,
 * and filtered records based on animal type and name.
 * 
 * @author Nkoana RRM
 * @version mini_project
 */
public class ViewRecordController {


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
    private Label lblDashboard;
    
    @FXML
    private Button btnAllRecords;

    @FXML
    private Button btnVaccinated;

    @FXML
    private Button btnUnvaccinated;
    

    @FXML
    private TableView<Record> tblViewRecords;
    
    @FXML
    private TableColumn<Record, Integer> colAnimalID; 

    @FXML
    private TableColumn<Record, String> colName;

    @FXML
    private TableColumn<Record, String> colType;

    @FXML
    private TableColumn<Record, String> colBreed;

    @FXML
    private TableColumn<Record, String> colDOB;

    @FXML
    private TableColumn<Record, String> colVaccinated;
    
    
    @FXML
    private TextField txtFilterType;
    @FXML
    private Button btnFilterType;
    
    
    @FXML
    private TextField txtFilterName;
    @FXML
    private Button btnFilterName;
    
    
    
    

    // Socket and streams
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private PrintWriter pw;
    
    
    
    /**
     * Initializes the controller.
     * 
     * Establishes a connection to the server and retrieves user's records.
     */

    public void initialize() {
        try {
            if (socket == null || socket.isClosed()) {
                // Establish connection
                socket = new Socket("localhost", 2024);
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
                pw = new PrintWriter(dos); // Assign to DataOutputStream
            }

            // Retrieve user's id from session manager
            int userId = SessionManager.getInstance().getUserId();
            sendCommand(pw, "VIEWRECORDS");
            sendCommand(pw, String.valueOf(userId));
            pw.flush();

            // Read account information from the server
            String response = readResponse(dis);
            System.out.println(response);

            // Parse the response and populate the table
            populateTableAllRecords(response);
           
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Populates the table with all records.
     * 
     * @param response The server's response containing records.
     */
    private void populateTableAllRecords(String response) {
        try {
            // Remove square brackets from the beginning and end of the response
            response = response.substring(1, response.length() - 1);

            // Split the response string into individual records
            String[] records = response.split(", ");

            // Clear the table before populating it with new data
            tblViewRecords.getItems().clear();

            // Iterate through each record
            for (String record : records) {
                // Split the record into its components
                String[] components = record.split(",");

                // Extract data from components
                int animalId = Integer.parseInt(components[0]);
                String name = components[1];
                String type = components[2];
                String breed = components[3];
                String dob = components[4];
                String vaccinated = components[5];

                // Create a new Record object and add it to the table
                Record newRecord = new Record(animalId, name, type, breed, dob, vaccinated);
                tblViewRecords.getItems().add(newRecord);
                
                
                // Set cell value factories for each column
                colAnimalID.setCellValueFactory(new PropertyValueFactory<>("animalId"));
                colName.setCellValueFactory(new PropertyValueFactory<>("name"));
                colType.setCellValueFactory(new PropertyValueFactory<>("type"));
                colBreed.setCellValueFactory(new PropertyValueFactory<>("breed"));
                colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
                colVaccinated.setCellValueFactory(new PropertyValueFactory<>("vaccinated"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    /**
     * Populates the table with vaccinated records.
     * 
     * @param response The server's response containing records.
     */
    private void populateTableVaccinatedRecords(String response) {
        try {
            // Remove square brackets from the beginning and end of the response
            response = response.substring(1, response.length() - 1);

            // Split the response string into individual records
            String[] records = response.split(", ");

            // Clear the table before populating it with new data
            tblViewRecords.getItems().clear();

            // Iterate through each record
            for (String record : records) {
                // Split the record into its components
                String[] components = record.split(",");

                // Extract data from components
                int animalId = Integer.parseInt(components[0]);
                String name = components[1];
                String type = components[2];
                String breed = components[3];
                String dob = components[4];
                String vaccinated = components[5];

                // Check if the record is vaccinated
                if (vaccinated.equalsIgnoreCase("Yes")) {
                    // Create a new Record object and add it to the table
                    Record newRecord = new Record(animalId, name, type, breed, dob, vaccinated);
                    tblViewRecords.getItems().add(newRecord);
                }
            }

            // Set cell value factories for each column
            colAnimalID.setCellValueFactory(new PropertyValueFactory<>("animalId"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colType.setCellValueFactory(new PropertyValueFactory<>("type"));
            colBreed.setCellValueFactory(new PropertyValueFactory<>("breed"));
            colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
            colVaccinated.setCellValueFactory(new PropertyValueFactory<>("vaccinated"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    /**
     * Populates the table with unvaccinated records.
     * 
     * @param response The server's response containing records.
     */
    private void populateTableUnvaccinated(String response) {
        try {
            // Remove square brackets from the beginning and end of the response
            response = response.substring(1, response.length() - 1);

            // Split the response string into individual records
            String[] records = response.split(", ");

            // Clear the table before populating it with new data
            tblViewRecords.getItems().clear();

            // Iterate through each record
            for (String record : records) {
                // Split the record into its components
                String[] components = record.split(",");

                // Extract data from components
                int animalId = Integer.parseInt(components[0]);
                String name = components[1];
                String type = components[2];
                String breed = components[3];
                String dob = components[4];
                String vaccinated = components[5];

                // Check if the record is unvaccinated
                if (vaccinated.equalsIgnoreCase("No")) {
                    // Create a new Record object and add it to the table
                    Record newRecord = new Record(animalId, name, type, breed, dob, vaccinated);
                    tblViewRecords.getItems().add(newRecord);
                }
            }

            // Set cell value factories for each column
            colAnimalID.setCellValueFactory(new PropertyValueFactory<>("animalId"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colType.setCellValueFactory(new PropertyValueFactory<>("type"));
            colBreed.setCellValueFactory(new PropertyValueFactory<>("breed"));
            colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
            colVaccinated.setCellValueFactory(new PropertyValueFactory<>("vaccinated"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
    /**
     * Populates the table with filtered reccords by animal type records.
     * 
     * @param response The server's response containing records.
     */
    private void populateTableFilterAnimalType(String response) {
        try {
            // Remove square brackets from the beginning and end of the response
            response = response.substring(1, response.length() - 1);

            // Split the response string into individual records
            String[] records = response.split(", ");

            // Clear the table before populating it with new data
            tblViewRecords.getItems().clear();

            // Get the filter criteria from the text field
            String filterType = txtFilterType.getText().trim().toLowerCase();

            boolean found = false; // Flag to indicate if any records are found

            // Iterate through each record
            for (String record : records) {
                // Split the record into its components
                String[] components = record.split(",");

                // Extract data from components
                int animalId = Integer.parseInt(components[0]);
                String name = components[1];
                String type = components[2].toLowerCase(); // Convert type to lowercase for case-insensitive comparison
                String breed = components[3];
                String dob = components[4];
                String vaccinated = components[5];

                // Check if the record's type matches the filter criteria
                if (type.contains(filterType)) { // Use contains() for partial matching
                    // Create a new Record object and add it to the table
                    Record newRecord = new Record(animalId, name, type, breed, dob, vaccinated);
                    tblViewRecords.getItems().add(newRecord);
                    found = true; // Set found flag to true
                }
            }

            // Set cell value factories for each column
            colAnimalID.setCellValueFactory(new PropertyValueFactory<>("animalId"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colType.setCellValueFactory(new PropertyValueFactory<>("type"));
            colBreed.setCellValueFactory(new PropertyValueFactory<>("breed"));
            colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
            colVaccinated.setCellValueFactory(new PropertyValueFactory<>("vaccinated"));

            // Show alert if no records are found
            if (!found) {
                showAlert(Alert.AlertType.INFORMATION, "Information", "No Records Found",
                        "No animals with the specified type were found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Populates the table with filtered by name  records.
     * 
     * @param response The server's response containing records.
     */
    private void populateTableFilterAnimalName(String response) {
        try {
            // Remove square brackets from the beginning and end of the response
            response = response.substring(1, response.length() - 1);

            // Split the response string into individual records
            String[] records = response.split(", ");

            // Clear the table before populating it with new data
            tblViewRecords.getItems().clear();

            // Get the filter criteria from the text field
            String filterName = capitalizeFirstLetter(txtFilterName.getText().trim());

            boolean found = false; // Flag to indicate if any records are found

            // Iterate through each record
            for (String record : records) {
                // Split the record into its components
                String[] components = record.split(",");

                // Extract data from components
                int animalId = Integer.parseInt(components[0]);
                String name = components[1]; // Convert name to lowercase for case-insensitive comparison
                String type = components[2];
                String breed = components[3];
                String dob = components[4];
                String vaccinated = components[5];

                // Check if the record's name matches the filter criteria
                if (name.contains(filterName)) { // Use contains() for partial matching
                    // Create a new Record object and add it to the table
                    Record newRecord = new Record(animalId, name, type, breed, dob, vaccinated);
                    tblViewRecords.getItems().add(newRecord);
                    found = true; // Set found flag to true
                }
            }

            // Set cell value factories for each column
            colAnimalID.setCellValueFactory(new PropertyValueFactory<>("animalId"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colType.setCellValueFactory(new PropertyValueFactory<>("type"));
            colBreed.setCellValueFactory(new PropertyValueFactory<>("breed"));
            colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
            colVaccinated.setCellValueFactory(new PropertyValueFactory<>("vaccinated"));

            // Show alert if no records are found
            if (!found) {
                showAlert(Alert.AlertType.INFORMATION, "Information", "No Records Found",
                        "No animals with the specified name were found.");
            }
        } catch (Exception e) {
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
     */    private void sendCommand(PrintWriter pw, String msg) {
        pw.println(msg);
        pw.flush();
    }
    


    
    /*
     * shows all the records 
     */
    @FXML
    void handleAllrecords(ActionEvent event) {
    	
    	
    	try {
            if (socket == null || socket.isClosed()) {
                // Establish connection
                socket = new Socket("localhost", 2024);
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
                pw = new PrintWriter(dos); // Assign to DataOutputStream
            }

            // Retrieve user's id from session manager
            int userId = SessionManager.getInstance().getUserId();
            sendCommand(pw, "VIEWRECORDS");
            sendCommand(pw, String.valueOf(userId));
            pw.flush();

            // Read account information from the server
            String response = readResponse(dis);
            System.out.println(response);

            // Parse the response and populate the table
            populateTableAllRecords(response);
           
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	int userId = SessionManager.getInstance().getUserId();
        System.out.println("User ID: " + userId);
    	
    }
    
    
    
    /*
     * shows vaccinated records
     */
    @FXML
    void handleVaccinatedRecords(ActionEvent event) {
    	
    	
    	try {
            if (socket == null || socket.isClosed()) {
                // Establish connection
                socket = new Socket("localhost", 2024);
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
                pw = new PrintWriter(dos); // Assign to DataOutputStream
            }

            // Retrieve user's id from session manager
            int userId = SessionManager.getInstance().getUserId();
            sendCommand(pw, "VIEWRECORDS");
            sendCommand(pw, String.valueOf(userId));
            pw.flush();

            // Read account information from the server
            String response = readResponse(dis);
            System.out.println(response);

            // Parse the response and populate the table
            populateTableVaccinatedRecords(response);
           
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	int userId = SessionManager.getInstance().getUserId();
        System.out.println("User ID: " + userId);
    	
    }
    
    
    
    /*
     * shows unvaccinated records
     */
    @FXML
    void handleUnVaccinatedRecords(ActionEvent event) {
    	
    	
    	try {
            if (socket == null || socket.isClosed()) {
                // Establish connection
                socket = new Socket("localhost", 2024);
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
                pw = new PrintWriter(dos); // Assign to DataOutputStream
            }

            // Retrieve user's id from session manager
            int userId = SessionManager.getInstance().getUserId();
            sendCommand(pw, "VIEWRECORDS");
            sendCommand(pw, String.valueOf(userId));
            pw.flush();

            // Read account information from the server
            String response = readResponse(dis);
            System.out.println(response);

            // Parse the response and populate the table
            populateTableUnvaccinated(response);
           
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	int userId = SessionManager.getInstance().getUserId();
        System.out.println("User ID: " + userId);
    	
    }
    

    
    /*
     * shows filtered records by type records
     */
    @FXML
    void handleFilterByType(ActionEvent event) {
    	
    	
    	try {
            if (socket == null || socket.isClosed()) {
                // Establish connection
                socket = new Socket("localhost", 2024);
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
                pw = new PrintWriter(dos); // Assign to DataOutputStream
            }

            // Retrieve user's id from session manager
            int userId = SessionManager.getInstance().getUserId();
            sendCommand(pw, "VIEWRECORDS");
            sendCommand(pw, String.valueOf(userId));
            pw.flush();

            // Read account information from the server
            String response = readResponse(dis);
            System.out.println(response);

            // Parse the response and populate the table
            populateTableFilterAnimalType(response);
           
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	int userId = SessionManager.getInstance().getUserId();
        System.out.println("User ID: " + userId);
    	
    }
    
    
    /*
     * shows Filter Animal Name records
     */
    /*
     * shows Filter Animal Name records
     */
    @FXML
    void handleFilterAnimalName(ActionEvent event) {
        try {
            if (socket == null || socket.isClosed()) {
                // Establish connection
                socket = new Socket("localhost", 2024);
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
                pw = new PrintWriter(dos); // Assign to DataOutputStream
            }

            // Retrieve user's id from session manager
            int userId = SessionManager.getInstance().getUserId();
            sendCommand(pw, "VIEWRECORDS");
            sendCommand(pw, String.valueOf(userId));
            pw.flush();

            // Read account information from the server
            String response = readResponse(dis);
            System.out.println(response);

            // Parse the response and populate the table
            populateTableFilterAnimalName(response); 
        } catch (IOException e) {
            e.printStackTrace();
        }

        int userId = SessionManager.getInstance().getUserId();
        System.out.println("User ID: " + userId);
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
    
}
