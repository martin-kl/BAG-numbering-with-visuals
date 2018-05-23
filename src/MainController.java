import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MainController {

    @FXML
    public AnchorPane anchorPane;
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

    public void initialize() {
        vbResult.getChildren().clear();
    }

    public void startAlgorithmNormally(ActionEvent actionEvent) {
        StNrAlgorithm stNrAlgorithm = new StNrAlgorithm();
        //stNrAlgorithm.startAlgorithmOnce(true, false);
        boolean result = stNrAlgorithm.startAlgorithmLoop(false);

        addResultToGraphicOutput(true, stNrAlgorithm.getResult(), result);
    }

    public void startAlgorithmLoop(ActionEvent actionEvent) {
        StNrAlgorithm stNrAlgorithm = new StNrAlgorithm();
        int posCounter = 0;
        boolean lastResult;
        for (int i = 0; i < LOOP_TIMES - 1; i++) {
            System.out.println("\t\tDurchlauf Nummer = " + (i + 1));
            if (stNrAlgorithm.startAlgorithmOnce(false, true)) {
                posCounter++;
            }
        }
        System.out.println("\n\t\tDurchlauf Nummer = " + LOOP_TIMES);
        if ((lastResult = stNrAlgorithm.startAlgorithmOnce(true, true))) {
            posCounter++;
        }

        lbLoopTimes.setText("" + LOOP_TIMES);
        lbSuccesfull.setText("" + posCounter);
        lbResult.setText((LOOP_TIMES - posCounter) + " Zuweisungen sind nicht gelungen!");
        //spResults.setContent(gridPaneStatistical);
        vbResult.getChildren().clear();
        vbResult.getChildren().add(gridPaneStatistical);

        vbResult.getChildren().add(new Separator());
        vbResult.getChildren().add(new Label(""));
        Label tempLabel = new Label("Ergebnis der letzten Simulation:");
        tempLabel.setStyle("-fx-font: 13pt System;");
        vbResult.getChildren().add(tempLabel);
        vbResult.getChildren().add(new Label(""));

        addResultToGraphicOutput(false, stNrAlgorithm.getResult(), lastResult);
    }

    private void addResultToGraphicOutput(boolean deleteList, Kapelle[] result, boolean resultStatus) {
        if(deleteList) {
            vbResult.getChildren().clear();
        }
        if(resultStatus) {
            for (int i = 1; i < result.length; i++) {
                Label tempLabel = new Label(i + ":\t\t" + result[i].getBez());
                tempLabel.setStyle("-fx-font: 13pt System;");
                vbResult.getChildren()
                        .add(tempLabel);
            }
        }else {
            Label tempLabel = new Label("Zuweisung nicht gelungen");
            tempLabel.setStyle("-fx-font: 13pt System;");
            vbResult.getChildren()
                    .add(tempLabel);
        }
    }
}
