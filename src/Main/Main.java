package Main;
	
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

import Controller.LoginController;
import Controller.SignUpController;
import Server.SessionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;


/**
 * The main class extending Application for running the JavaFX application.
 *
 * @author Nkoana RRM
 * @version mini_Project
 * 
 */

public class Main extends Application {
	
	//streams
	private Socket s;
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	private BufferedReader br;
	private PrintWriter pw; 
	
	
	
	/**
	 * Starts the JavaFX application, establishing connection with the server and loading the SignUp scene.
	 * If connection fails, displays an error dialog.
	 *
	 * @param primaryStage The primary stage of the application.
	 */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Establish connection
            s = new Socket("localhost", 2024);
            is = s.getInputStream();
            os = s.getOutputStream();
            dis = new DataInputStream(is);
            dos = new DataOutputStream(os);
            pw = new PrintWriter(os);

            // Load the SignUp FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/SignUp.fxml"));  
            Parent root = loader.load();         
            SignUpController signUpController = loader.getController();// Get the controller instance    
            signUpController.setSocket(s, dis, dos, pw); // Set the socket and streams in the controller

          
           
            // Set up the scene and stage
            Scene scene = new Scene(root, 1200, 750);
            scene.getStylesheets().add(getClass().getResource("/Main/application.css").toExternalForm());
            scene.setFill(Color.GREEN);
            primaryStage.setScene(scene);
            primaryStage.setTitle("FarmCare");
            primaryStage.show();
            
            
            
        } catch (ConnectException e) {
            showErrorDialog("Connection Error", "Failed to connect to the server.", "Please make sure the server is running and try again.");
            e.printStackTrace();
        } catch (IOException e) {
            showErrorDialog("Connection Error", "Failed to connect to the server.", "Please check your connection.");
            e.printStackTrace();
        }
    }

    
    /**
     * Displays an error dialog with the specified title, header, and content.
     *
     * @param title   The title of the error dialog.
     * @param header  The header text of the error dialog.
     * @param content The content text of the error dialog.
     */
    private void showErrorDialog(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    
    /**
     * Stops the JavaFX application by closing the socket and streams.
     */
    
    @Override
    public void stop() {
        // Close socket and streams
        try {
            if (pw != null) pw.close();
            if (dis != null) dis.close();
            if (dos != null) dos.close();
            if (is != null) is.close();
            if (os != null) os.close();
            if (s != null && !s.isClosed()) s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	

	
	public static void main(String[] args) {
		launch(args);
	}
}
