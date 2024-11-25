package Controller;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import Server.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


/**
 * Controller class for generating reports.
 * 
 * This class handles actions related to generating reports, including populating charts and handling user interactions.
 * 
 * @author Nkoana RRM
 * @version mini_project
 */
public class ReportsController {

	
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
    private PieChart piechart;
    @FXML
    private BarChart<String, Integer> barchart;
    @FXML
    private LineChart<String, Integer> linechart;


    
    
    // Socket and streams
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private PrintWriter pw;
	
    /**
     * Initializes the controller.
     * 
     * This method is automatically called after the fxml file has been loaded.
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
           populatePieChart(response);
           populateBarChart(response);
           populateLineChart(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    
    /**
     * Populates the pie chart with data based on the server response.
     * 
     * @param response The response received from the server.
     */
    private void populatePieChart(String response) {
        try {
            // Remove square brackets from the beginning and end of the response
            response = response.substring(1, response.length() - 1);

            // Split the response string into individual records
            String[] records = response.split(", ");

            // Counters for vaccinated and unvaccinated animals
            int vaccinatedCount = 0;
            int unvaccinatedCount = 0;

            // Iterate through each record
            for (String record : records) {
                // Split the record into its components
                String[] components = record.split(",");

                // Extract the vaccination status from the components
                String vaccinated = components[5].trim();

                // Update the counters based on vaccination status
                if (vaccinated.equalsIgnoreCase("yes")) {
                    vaccinatedCount++;
                } else {
                    unvaccinatedCount++;
                }
            }

            // Create a new VaccinationData object
            VaccinationData data = new VaccinationData(vaccinatedCount, unvaccinatedCount);

            // Calculate percentages
            int totalAnimals = vaccinatedCount + unvaccinatedCount;
            double vaccinatedPercentage = ((double) vaccinatedCount / totalAnimals) * 100;
            double unvaccinatedPercentage = ((double) unvaccinatedCount / totalAnimals) * 100;

            // Populate the PieChart
            piechart.getData().clear();
            piechart.getData().add(new PieChart.Data("Vaccinated (" + String.format("%.2f", vaccinatedPercentage) + "%)", data.getVaccinatedCount()));
            piechart.getData().add(new PieChart.Data("Unvaccinated (" + String.format("%.2f", unvaccinatedPercentage) + "%)", data.getUnvaccinatedCount()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * Populates the bar chart with data based on the server response.
     * 
     * @param response The response received from the server.
     */
    private void populateBarChart(String response) {
        try {
            // Remove square brackets from the beginning and end of the response
            response = response.substring(1, response.length() - 1);

            // Split the response string into individual records
            String[] records = response.split(", ");

            // Create a map to store the count of each animal type
            Map<String, Integer> animalCounts = new HashMap<>();

            // Iterate through each record
            for (String record : records) {
                // Split the record into its components
                String[] components = record.split(",");

                // Extract the type of animal from the components
                String animalType = components[2].trim().toLowerCase(); // Convert to lowercase

                // Extract the vaccination status from the components
                String vaccinated = components[5].trim();

                // Update the animalCounts map if the animal is not vaccinated
                if (!vaccinated.equalsIgnoreCase("yes")) {
                    animalCounts.put(animalType, animalCounts.getOrDefault(animalType, 0) + 1);
                }
            }

            // Create a new series for the BarChart
            XYChart.Series<String, Integer> series = new XYChart.Series<>();

            // Add data to the series
            for (Map.Entry<String, Integer> entry : animalCounts.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            // Populate the BarChart
            barchart.getData().clear();
            barchart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * Populates the line chart with data based on the server response.
     * 
     * @param response The response received from the server.
     */
    private void populateLineChart(String response) {
        try {
            // Remove square brackets from the beginning and end of the response
            response = response.substring(1, response.length() - 1);

            // Split the response string into individual records
            String[] records = response.split(", ");

            // Create a map to store the count of vaccinated animals for each animal type
            Map<String, Integer> vaccinatedAnimalCounts = new HashMap<>();

            // Iterate through each record
            for (String record : records) {
                // Split the record into its components
                String[] components = record.split(",");

                // Extract the type of animal from the components
                String animalType = components[2].trim().toLowerCase(); // Convert to lowercase

                // Extract the vaccination status from the components
                String vaccinated = components[5].trim();

                // Update the vaccinatedAnimalCounts map if the animal is vaccinated
                if (vaccinated.equalsIgnoreCase("yes")) {
                    vaccinatedAnimalCounts.put(animalType, vaccinatedAnimalCounts.getOrDefault(animalType, 0) + 1);
                }
            }

            // Create a new series for the LineChart
            XYChart.Series<String, Integer> series = new XYChart.Series<>();

            // Add data to the series
            for (Map.Entry<String, Integer> entry : vaccinatedAnimalCounts.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            // Populate the LineChart
            linechart.getData().clear();
            linechart.getData().add(series);
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
    


    
    
	/**
     * logs a user out
     *  @param event The action event.
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
     */    @FXML
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
