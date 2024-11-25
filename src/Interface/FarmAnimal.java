package Interface;

import java.time.LocalDate;

public class FarmAnimal {
    private String name;
    private String type;
    private String breed;
    private String dob;
    private boolean vaccinated;

    public FarmAnimal(String name, String type, String breed, String dob, boolean vaccinated) {
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.dob = dob;
        this.vaccinated = vaccinated;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public boolean isVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        this.vaccinated = vaccinated;
    }
}
