package Controller;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import Interface.FarmAnimal;
import Server.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


/**
 * dashboard controller for farm care
 *	@author Nkoana RRM
 * @version mini_project
 *
 */
public class DashboardController {
	
	

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

    


    /*
     * Action performed when side menu item Logout(btnLogout) is clicked.
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
 * navigate to loging page
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
    
  
    
}
