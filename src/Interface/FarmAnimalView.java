package Interface;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class FarmAnimalView extends VBox {

    // VARIABLES
    private Label animalNameLabel;
    private Label animalTypeLabel;
    private Label animalBreedLabel;
    private Label animalDOBLabel;
    private Label vaccinatedStatusLabel;

    public FarmAnimalView(FarmAnimal animal) {
        animalNameLabel = new Label("Name: " + animal.getName());
        animalTypeLabel = new Label("Type: " + animal.getType());
        animalBreedLabel = new Label("Breed: " + animal.getBreed());
        animalDOBLabel = new Label("Date of Birth: " + animal.getDob());
        vaccinatedStatusLabel = new Label("Vaccinated: " + (animal.isVaccinated() ? "Yes" : "No"));

        getChildren().addAll(animalNameLabel, animalTypeLabel, animalBreedLabel, animalDOBLabel, vaccinatedStatusLabel);

        setSpacing(10);
        setStyle("-fx-border-color: black;");
    }
}
