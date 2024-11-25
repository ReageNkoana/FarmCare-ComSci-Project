package Server;



/**
 * The SessionManager class manages user sessions in the FarmCare application.
 * It provides methods for setting and getting the user ID, as well as logging out users.
 * @author Nkoana RRM
 * @version mini_project
 */
public class SessionManager {
	
    private static SessionManager instance;
    private int userId;

    
    /**
     * Private constructor to prevent instantiation.
     */
    private SessionManager() {
        // Private constructor to prevent instantiation
    }

    
    /**
     * Returns the singleton instance of SessionManager.
     *
     * @return The singleton instance of SessionManager.
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    
    /**
     * Sets the user ID.
     *
     * @param userId The ID of the user to set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    
    /**
     * Gets the user ID.
     *
     * @return The ID of the user.
     */
    public int getUserId() {
        return userId;
    }
    
    /**
     * Clears the user's session when they log out.
     * This method sets the user ID to a value that represents no user logged in.
     */
    public void logout() {
        // Clear the user ID when logging out
        userId = -1; // Or any other value that represents no user logged in
        System.out.println("User has logged out");
    }
}
