package Server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import CryptoPackage.Cryptography;
import acsse.csc03a3.Block;
import acsse.csc03a3.Blockchain;
import acsse.csc03a3.Transaction;


/**
 * The FarmHandler class handles client requests in the FarmCare application.
 * It processes various requests such as sign-up, login, account information retrieval,
 * saving updated account information, adding farm animal records, and viewing farm animal records.
 * 
 * @author Nkoana RRM
 * @version mini_project
 */
public class FarmHandler implements Runnable {

    private Socket s;
    private InputStream is;
    private OutputStream os;
    private DataInputStream dis;
    private DataOutputStream dos;
    private PrintWriter pw;
    private BufferedReader br;
 // Blockchain instance
    private Blockchain<String> blockchain;
   

    /**
     * Constructs a new FarmHandler instance with the specified socket representing the client connection.
     * 
     * @param s The socket representing the client connection.
     */
    public FarmHandler(Socket s) {
    	
    	
        
        
        
        this.s = s;

        try {

            is = s.getInputStream();
            os = s.getOutputStream();

            dis = new DataInputStream(is);
            dos = new DataOutputStream(os);

            pw = new PrintWriter(os);
            br = new BufferedReader(new InputStreamReader(is));
            
         // Initialize the blockchain
            blockchain = new Blockchain<>();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    
    /**
     * Starts handling client requests.
     */
    @Override
    public void run() {
        System.out.println("Handling client requests");
        boolean processing = true;

        try {
            while (processing) {
                String message = br.readLine();
                System.out.println("Received message from client: " + message);
                System.out.println(message);

                switch (message) {
                    case "SIGNUP":
                        handleSignupRequest();
                        break;
                    case "LOGIN":
                        handleLoginRequest();
                        break;
                    case "INFO":
                        handleAccountInfoRequest();
                        break;
                    case "SAVEINFO":
                    	handleSaveInfoRequest();
                        break;    
                    case "ADDRECORD":
                        handleAddRecordRequest();
                        break;
                    case "VIEWRECORDS":
                        handleViewRecordsRequest();
                        break;
                    default:
                        System.out.println("Unknown message received from client: " + message);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
 
    /**
     * Retrieves the last assigned user ID 
     * 
     * @return The last assigned user ID.
     */
    private int getLastAssignedUserId() {
        try {
            // Read the last assigned ID from the UserData.txt file
            BufferedReader reader = new BufferedReader(new FileReader("data/UserData.txt"));
            String line;
            String lastUserIdString = ""; // Initialize last user ID string
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("UserID: ")) {
                    lastUserIdString = line.substring("UserID: ".length());
                }
            }
            reader.close();

            // Parse the last assigned ID
            return Integer.parseInt(lastUserIdString);
        } catch (IOException e) {
            e.printStackTrace();
            return 0; // Default to 0 if there's an error
        }
    }
    
    
    
    /**
     * Stores the user's data in the UserData.txt file.
     * 
     * @param name     The user's name.
     * @param surname  The user's surname.
     * @param email    The user's email.
     * @param farmName The farm name associated with the user.
     * @param password The user's password.
     * @return True if the user sign-up was successful, false otherwise.
     */
    private boolean SignUpUser(String name, String surname, String email, String farmName, String password) {
        // Implement logic to store user data
    	
    	  try {
          	// Get the last assigned user ID
              int lastAssignedId = getLastAssignedUserId();
           // Increment the last assigned ID for the new user
              int newUserId = lastAssignedId + 1;
              
              File fileObject = new File("data/UserData.txt");
              FileWriter fileWriter = new FileWriter(fileObject, true);
              String hashedPassword = Cryptography.hashPassword(password);


              if (fileObject.createNewFile()) {
                  System.out.println("File created: " + fileObject.getName() + "\n");
              } else {
                  System.out.println("Connected to the existing UserData.txt file...\n");
              
              	 // Store the incremented ID as the user's ID
                  fileWriter.write("UserID: " + newUserId + "\n");
                  fileWriter.write("Name: " + name + "\n");
                  fileWriter.write("Surname: " + surname + "\n");
                  fileWriter.write("Email: " + email + "\n");
                  fileWriter.write("Farm Name: " + farmName+ "\n");
                  fileWriter.write("Password: " + hashedPassword  + "\n\n");

                  fileWriter.close();


                  
              }
          } catch (IOException e) {
              System.out.println("An error occurred.");
              e.printStackTrace();
          }
     
        return true; // Return true if successful, false otherwise
    }
    
    
    /**
     * Handles the sign-up request from the client.
     */
    private void handleSignupRequest() {
        try {
            String name = br.readLine();
            String surname = br.readLine();
            String email = br.readLine();
            String farmName = br.readLine();
            String password = br.readLine();

            boolean success = SignUpUser(name, surname, email, farmName, password);
            if (success) {
                pw.println("SIGNSUCCESS");
            } else {
                pw.println("SIGNFAILURE");
            }
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    /**
     * Validates the user's login credentials.
     * 
     * @param email    The user's email.
     * @param password The user's password.
     * @return True if the login is successful, false otherwise.
     */
    private boolean loginUser(String email, String password) {
        try {
            // Load user data from the file
            File fileObject = new File("data/UserData.txt");
            Scanner scannerReader = new Scanner(fileObject);

            // Loop through each line in the file
            while (scannerReader.hasNextLine()) {
                String line = scannerReader.nextLine();
                if (line.startsWith("UserID: ")) {
                    String userID = line.substring("UserID: ".length());
                    String name = scannerReader.nextLine().substring("Name: ".length());
                    String surname = scannerReader.nextLine().substring("Surname: ".length());
                    String userEmailAcc = scannerReader.nextLine().substring("Email: ".length());
                    String farmName = scannerReader.nextLine().substring("Farm Name: ".length());
                    String userPassword = scannerReader.nextLine().substring("Password: ".length());

                    // Check if the email and password match
                    if (userEmailAcc.equals(email) && userPassword.equals(password)) {
                        scannerReader.close();
                        return true; // Return true if login is successful
                    }
                }
            }

            // Close the scanner
            scannerReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("The file path that you have defined does not exist.");
            e.printStackTrace();
        }
        
        return false; // Return false if no matching user found
    }



    /**
     * Handles the login request from the client.
     */
    
    private void handleLoginRequest() {
        try {
            String email = br.readLine();
            String password = br.readLine();
            int userID=getUserIDByEmail(email);
            
            boolean success = loginUser(email, password);

            if (success && userID != -1) {
                pw.println("LOGINSUCCESS"+ userID);
                
            } else {
                pw.println("LOGINFAILURE");
            }
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the user ID associated with the given email address.
     * 
     * @param email The email address of the user.
     * @return The user ID associated with the email address, or -1 if no matching email is found.
     */
    private int getUserIDByEmail(String email) {
        try {
            // Load user data from the file
            File fileObject = new File("data/UserData.txt");
            Scanner scannerReader = new Scanner(fileObject);

            int userId = -1; // Initialize user ID to -1 (not found)

            // Loop through each line in the file
            while (scannerReader.hasNextLine()) {
                String line = scannerReader.nextLine();
                if (line.startsWith("UserID: ")) {
                    // Extract the user ID
                    userId = Integer.parseInt(line.substring("UserID: ".length()).trim());
                } else if (line.startsWith("Email: ") && line.substring("Email: ".length()).equals(email)) {
                    // If the email matches, return the found user ID
                    scannerReader.close();
                    return userId;
                }
            }

            // Close the scanner
            scannerReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("The file path that you have defined does not exist.");
            e.printStackTrace();
        }

        return -1; // Return -1 if no matching email is found
    }


    /**
     * Retrieves the email address associated with the given user ID.
     * 
     * @param userID The user ID for which to retrieve the email address.
     * @return The email address associated with the user ID, or null if no matching user ID is found.
     */
    private String getEmailByUserID(int userID) {
        try {
            // Load user data from the file
            File fileObject = new File("data/UserData.txt");
            Scanner scannerReader = new Scanner(fileObject);

            String email = null; // Initialize email to null (not found)

            // Loop through each line in the file
            while (scannerReader.hasNextLine()) {
                String line = scannerReader.nextLine();
                if (line.startsWith("UserID: ")) {
                    // Extract the user ID
                    int currentUserId = Integer.parseInt(line.substring("UserID: ".length()).trim());
                    if (currentUserId == userID) {
                        // Extract the email
                        email = scannerReader.nextLine().substring("Email: ".length());
                        break;
                    }
                }
            }

            // Close the scanner
            scannerReader.close();

            return email;
        } catch (FileNotFoundException e) {
            System.out.println("The file path that you have defined does not exist.");
            e.printStackTrace();
        }

        return null; // Return null if no matching user ID is found
    }

    
    /**
     * Retrieves the account information of a user based on their user ID.
     */
    private void handleAccountInfoRequest() {
        try {
            // Read user ID from client
            String userIdStr = br.readLine();
            int userId = Integer.parseInt(userIdStr);

            // Load user data from the file
            File fileObject = new File("data/UserData.txt");
            Scanner scannerReader = new Scanner(fileObject);

            // Loop through each line in the file
            while (scannerReader.hasNextLine()) {
                String line = scannerReader.nextLine();
                if (line.startsWith("UserID: ")) {
                    int currentUserId = Integer.parseInt(line.substring("UserID: ".length()).trim());
                    if (currentUserId == userId) {
                        // Extract account information
                        String name = scannerReader.nextLine().substring("Name: ".length());
                        String surname = scannerReader.nextLine().substring("Surname: ".length());
                        String email = scannerReader.nextLine().substring("Email: ".length());
                        String farmName = scannerReader.nextLine().substring("Farm Name: ".length());
                        String password = scannerReader.nextLine().substring("Password: ".length());

                        // Send account information to client
                        pw.println(name + "," + surname + "," + email + "," + farmName + "," + password);
                        pw.flush();
                        break;
                    }
                }
            }

            // Close the scanner
            scannerReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    /**
     * Saves updated user account information to the UserData.txt file.
     */
    private void handleSaveInfoRequest() {
        try {
            // Read user ID from client
            String userIdStr = br.readLine();
            int userId = Integer.parseInt(userIdStr);

            // Read updated information from client
            String name = br.readLine();
            String surname = br.readLine();
            String email = br.readLine();
            String farmName = br.readLine();

            // Update user's account information in the UserData.txt file
            boolean success = updateUserAccountInfo(userId, name, surname, email, farmName);

            // Send response to client
            if (success) {
                pw.println("INFOUPDATED");
            } else {
                pw.println("INFOUPDATEFAILED");
            }
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the account information of a user in the UserData.txt file.
     * 
     * @param userId   The ID of the user whose information is to be updated.
     * @param name     The new name of the user.
     * @param surname  The new surname of the user.
     * @param email    The new email of the user.
     * @param farmName The new farm name associated with the user.
     * @return True if the account information was successfully updated, false otherwise.
     */
    private boolean updateUserAccountInfo(int userId, String name, String surname, String email, String farmName) {
        try {
            // Load user data from the file
            File fileObject = new File("data/UserData.txt");
            Scanner scannerReader = new Scanner(fileObject);
            StringBuilder updatedData = new StringBuilder();

            // Loop through each line in the file
            while (scannerReader.hasNextLine()) {
                String line = scannerReader.nextLine();
                if (line.startsWith("UserID: ")) {
                    int currentUserId = Integer.parseInt(line.substring("UserID: ".length()).trim());
                    if (currentUserId == userId) {
                        // Skip the old data
                        for (int i = 0; i < 4; i++) {
                            scannerReader.nextLine();
                        }
                        // Write the new data
                        updatedData.append("UserID: ").append(userId).append("\n");
                        updatedData.append("Name: ").append(name).append("\n");
                        updatedData.append("Surname: ").append(surname).append("\n");
                        updatedData.append("Email: ").append(email).append("\n");
                        updatedData.append("Farm Name: ").append(farmName).append("\n");
                    } else {
                        updatedData.append(line).append("\n");
                    }
                } else {
                    updatedData.append(line).append("\n");
                }
            }

            // Close the scanner
            scannerReader.close();

            // Write updated data back to the file
            FileWriter fileWriter = new FileWriter(fileObject);
            fileWriter.write(updatedData.toString());
            fileWriter.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
   
    /**
     * Adds a new farm animal record to the blockchain and stores it in the AnimalRecord.txt file.
     */
    private void handleAddRecordRequest() {
        try {
            // Read user ID from client
            String userIdStr = br.readLine();
            int userId = Integer.parseInt(userIdStr);

            // Read email from get email by userid
            String email = getEmailByUserID(userId);
            
            
            // Get the last assigned Animal ID for this user
            int lastAssignedAnimalId = getLastAssignedAnimalId(userId);

            // Increment the last assigned Animal ID for the new record
            int newAnimalId = lastAssignedAnimalId + 1;
            

            // Register stake with email and userID
            blockchain.registerStake(email, userId);

            // Read animal record information from client
            String name = br.readLine();
            String type = br.readLine();
            String breed = br.readLine();
            String dob = br.readLine();
            String vaccinated = br.readLine();

            // Create a new transaction with the received data
            String transactionData = name + "," + type + "," + breed + "," + dob + "," + vaccinated;
            Transaction<String> transaction = new Transaction<>(transactionData, String.valueOf(userId), String.valueOf(userId));

            // Add the transaction to the blockchain along with email and userID
            blockchain.addBlock(List.of(transaction));
            
  
            // Serialize the blockchain data
            String blockchainData = serializeBlockchain();

         // Write blockchain data along with the userID and AnimalID to AnimalRecord.txt
           writeBlockchainData(userId, newAnimalId,blockchainData );
            //writeBlockchainData(userId, newAnimalId, transaction.toString()+"\n");


            
         // Display blockchain data on the console
            System.out.println(blockchain.toString());

            System.out.println(transaction.toString());
            
            
            // Display the data onto the console
            System.out.println("Record added successfully:");
            System.out.println("User ID: " + userId);
            System.out.println("Email: " + email);
            System.out.println("Animal ID: " + newAnimalId); // Display the new Animal ID
            System.out.println("Animal Name: " + name);
            System.out.println("Type: " + type);
            System.out.println("Breed: " + breed);
            System.out.println("Date of Birth: " + dob);
            System.out.println("Vaccinated: " + vaccinated);
    
          

            // Check if chain is valid
            boolean isChainValid = blockchain.isChainValid();

            // Send response to client
            pw.println("RECORDADDED");
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    
    
    
    /**
     * Serializes the blockchain data by converting it to a string format.
     * 
     * @return A string representation of the blockchain data.
     */
    
    private String serializeBlockchain() {
        // Serialize the blockchain data by converting it to a string format
        return blockchain.toString();
    }

    
    /**
     * Writes the blockchain data along with user ID and animal ID to the AnimalRecord.txt file.
     * 
     * @param userId        The ID of the user associated with the blockchain data.
     * @param animalId      The ID of the animal associated with the blockchain data.
     * @param blockchainData The serialized blockchain data to be written.
     */
    private void writeBlockchainData(int userId, int animalId, String blockchainData) {
    
    	
        try {
            // Append the blockchain data along with userID and AnimalID to AnimalRecord.txt
            FileWriter fileWriter = new FileWriter("data/AnimalRecord.txt", true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("UserID: " + userId + "\n");
            printWriter.print("AnimalID: " + animalId + "\n"); // Write the AnimalID
            printWriter.print(blockchainData + "\n");
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    /**
     * Retrieves the last assigned Animal ID for a specific user.
     * 
     * @param userId The ID of the user.
     * @return The last assigned Animal ID for the specified user.
     */
    private int getLastAssignedAnimalId(int userId) {
        try {
            // Read the AnimalRecord.txt file
            BufferedReader reader = new BufferedReader(new FileReader("data/AnimalRecord.txt"));
            String line;
            int lastAssignedAnimalId = 0; // Initialize last assigned Animal ID
            int currentUserId = -1; // Initialize current user ID
            int currentAnimalId = -1; // Initialize current Animal ID
            
            // Iterate through each line in the file
            while ((line = reader.readLine()) != null) {
                // Check if the line contains the user ID
                if (line.startsWith("UserID: ")) {
                    // Extract the user ID
                    currentUserId = Integer.parseInt(line.substring("UserID: ".length()).trim());
                } else if (line.startsWith("AnimalID: ") && currentUserId == userId) {
                    // Check if the line contains the Animal ID for the specified user
                    // Extract the Animal ID
                    currentAnimalId = Integer.parseInt(line.substring("AnimalID: ".length()).trim());
                    // Update the last assigned Animal ID if the current Animal ID is greater
                    if (currentAnimalId > lastAssignedAnimalId) {
                        lastAssignedAnimalId = currentAnimalId;
                    }
                }
            }
            // Close the reader
            reader.close();

            // Return the last assigned Animal ID for the specified user
            return lastAssignedAnimalId;
        } catch (IOException e) {
            e.printStackTrace();
            return -1; // Default to -1 if there's an error
        }
    }
   
    
    
    /**
     * Views farm animal records for a specific user.
     */
    private void handleViewRecordsRequest() {
        try {
            // Read user ID from client
            String userIdStr = br.readLine();
            int userId = Integer.parseInt(userIdStr);

            // Initialize a list to store the extracted records
            List<String> records = new ArrayList<>();

            // Open the AnimalRecord.txt file for reading
            File fileObject = new File("data/AnimalRecord.txt");
            Scanner scanner = new Scanner(fileObject);

            // Initialize variables to track current user and animal ID
            int currentUserId = -1;
            int currentAnimalId = -1;

            // Iterate through each line in the file
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("UserID: ")) {
                    // Extract the user ID
                    currentUserId = Integer.parseInt(line.substring("UserID: ".length()).trim());
                } else if (line.startsWith("AnimalID: ")) {
                    // Extract the animal ID
                    currentAnimalId = Integer.parseInt(line.substring("AnimalID: ".length()).trim());
                } else if (currentUserId == userId && currentAnimalId != -1) {
                    // Check if the line contains a transaction data
                    // Use pattern matcher to extract transaction data
                    Matcher matcher = Pattern.compile("Transaction\\{sender='([^,]+),([^,]+),([^,]+),([^,]+),([^']+)', receiver='[0-9]+'").matcher(line);
                    if (matcher.find()) {
                        // Extract transaction data and construct the record string
                        String transactionData = matcher.group(1) + "," + matcher.group(2) + "," + matcher.group(3) + "," + matcher.group(4) + "," + matcher.group(5);
                        String record = currentAnimalId + "," + transactionData;

                        // Add the record to the list
                        records.add(record);
                      
                    }
                }
            }
          
            System.out.println(records);
            pw.println(records); // Send each record
            pw.flush();
            
            // Close the scanner
            scanner.close();

                 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}


    
    

