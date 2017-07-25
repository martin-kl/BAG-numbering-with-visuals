package bag;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
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

        addResult(true, stNrAlgorithm.getResult());
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

        vbResult.getChildren().add(new Separator());
        vbResult.getChildren().add(new Label("Ergebnis der letzten Simulation:"));

        addResult(false, stNrAlgorithm.getResult());
    }

    private void addResult(boolean deleteList, Kapelle[] result) {
        if(deleteList) {
            vbResult.getChildren().clear();
        }
        for (int i = 1; i < result.length; i++) {
            vbResult.getChildren()
                .add(new Label("Startnummer " + i + ":\t\t" + result[i].getBez()));
        }
    }
}
