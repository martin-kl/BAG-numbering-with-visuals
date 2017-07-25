package bag;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MainController {

    @FXML
    private Button btnAlgorithmNormal;
    @FXML
    private Button btnAlgorithmLoop;

    @FXML
    private Label lbLoopTimes;
    @FXML
    private Label lbSuccesfull;
    @FXML
    private Label lbResult;

    @FXML
    private GridPane gridPaneStatistical;
    @FXML
    private ScrollPane spResults;
    @FXML
    private VBox vbResult;

    private static final int LOOP_TIMES = 1000;

    public void startAlgorithmNormally(ActionEvent actionEvent) {
        System.out.println("start normally");

        StNrAlgorithm stNrAlgorithm = new StNrAlgorithm();
        stNrAlgorithm.starteAlgorithmus(true, false);

        Kapelle[] result = stNrAlgorithm.getResult();
        vbResult.getChildren().clear();
        for(Kapelle k : result) {
            vbResult.getChildren().add(new Label(k.getBez()));
        }
        spResults.setContent(vbResult);
    }

    public void startAlgorithmLoop(ActionEvent actionEvent) {
        System.out.println("start in loop");

        StNrAlgorithm stNrAlgorithm = new StNrAlgorithm();
        int posCounter = 0;
        for (int i = 0; i < LOOP_TIMES - 1; i++) {
            System.out.println("\t\tDurchlauf Nummer = " + (i + 1));
            if (stNrAlgorithm.starteAlgorithmus(false, true)) {
                posCounter++;
            }
        }
        System.out.println("\n\t\tDurchlauf Nummer = " + LOOP_TIMES);
        if (stNrAlgorithm.starteAlgorithmus(true, true)) {
            posCounter++;
        }

        lbLoopTimes.setText("" + LOOP_TIMES);
        lbSuccesfull.setText("" + posCounter);
        lbResult.setText((LOOP_TIMES - posCounter) + " Zuweisungen sind nicht gelungen!");
        //spResults.setContent(gridPaneStatistical);
        vbResult.getChildren().clear();
        vbResult.getChildren().add(gridPaneStatistical);

        /*
        System.out.println("\n\tGesamtanzahl der Zuweisungen: " + LOOP_TIMES);
        System.out.println("\tDavon gelungenen Zuweisungen: " + posCounter);
        System.out.println("\tDaraus folgt: " + (LOOP_TIMES - posCounter)
            + " Zuweisungen sind nicht gelungen!");
            */
    }
}
