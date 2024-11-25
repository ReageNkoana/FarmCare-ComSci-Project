package Controller;


/**
* Represents a record of an animal.
 * 
 * This class encapsulates information about an animal, including its ID, name, type, breed,
 * date of birth, and vaccination status.
 * 
 *	@author Nkoana RRM
 * @version mini_project
 *
 */
public class Record {
	
	
    private int animalId;
    private String name;
    private String type;
    private String breed;
    private String dob;
    private String vaccinated;

    
    /**
     * Constructs a new Record object with the specified attributes.
     * 
     * @param animalId    The ID of the animal.
     * @param name        The name of the animal.
     * @param type        The type of the animal.
     * @param breed       The breed of the animal.
     * @param dob         The date of birth of the animal.
     * @param vaccinated  The vaccination status of the animal.
     */
    public Record(int animalId, String name, String type, String breed, String dob, String vaccinated) {
        this.animalId = animalId;
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.dob = dob;
        this.vaccinated = vaccinated;
    }

    /**
     * Gets the ID of the animal.
     * 
     * @return The animal ID.
     */
    public int getAnimalId() {
        return animalId;
    }
    
    
    

    /**
     * Gets the name of the animal.
     * 
     * @return The animal name.
     */
    public String getName() {
        return name;
    }

    
    /**
     * Gets the type of the animal.
     * 
     * @return The animal type.
     */
    public String getType() {
        return type;
    }

    
    /**
     * Gets the breed of the animal.
     * 
     * @return The animal breed.
     */
    public String getBreed() {
        return breed;
    }

    
    /**
     * Gets the date of birth of the animal.
     * 
     * @return The animal's date of birth.
     */
    public String getDob() {
        return dob;
    }

    
    /**
     * Gets the vaccination status of the animal.
     * 
     * @return The vaccination status.
     */
    public String getVaccinated() {
        return vaccinated;
    }

    /**
     * Sets the ID of the animal.
     * 
     * @param animalId The animal ID to set.
     */
    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    
    /**
     * Sets the name of the animal.
     * 
     * @param name The animal name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    
    /**
     * Sets the type of the animal.
     * 
     * @param type The animal type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    
    /**
     * Sets the breed of the animal.
     * 
     * @param breed The animal breed to set.
     */
    public void setBreed(String breed) {
        this.breed = breed;
    }

    
    /**
     * Sets the date of birth of the animal.
     * 
     * @param dob The date of birth to set.
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    
    /**
     * Sets the vaccination status of the animal.
     * 
     * @param vaccinated The vaccination status to set.
     */
    public void setVaccinated(String vaccinated) {
        this.vaccinated = vaccinated;
    }
}
