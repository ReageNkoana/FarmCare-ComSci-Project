package Controller;


/**
 * Represents data related to vaccination counts.
 * 
 * This class stores the counts of vaccinated and unvaccinated animals.
 * 
 * @author Nkoana RRM
 * @version mini_project
 */
public class VaccinationData {
    private int vaccinatedCount;
    private int unvaccinatedCount;

    
    /**
     * Constructs a VaccinationData object with the specified counts.
     * 
     * @param vaccinatedCount   The count of vaccinated animals.
     * @param unvaccinatedCount The count of unvaccinated animals.
     */
    public VaccinationData(int vaccinatedCount, int unvaccinatedCount) {
        this.vaccinatedCount = vaccinatedCount;
        this.unvaccinatedCount = unvaccinatedCount;
    }

    
    /**
     * Gets the count of vaccinated animals.
     * 
     * @return The count of vaccinated animals.
     */
    public int getVaccinatedCount() {
        return vaccinatedCount;
    }

    
    /**
     * Gets the count of unvaccinated animals.
     * 
     * @return The count of unvaccinated animals.
     */
    public int getUnvaccinatedCount() {
        return unvaccinatedCount;
    }
}
