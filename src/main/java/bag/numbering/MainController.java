package bag.numbering;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainController {
    @FXML
    public AnchorPane anchorPane;
    @FXML
    private VBox vbResult;
    @FXML
    private GridPane gridPaneStatistical;

    @FXML
    private Label lbLoopTimes;
    @FXML
    private Label lbSuccessful;
    @FXML
    private Label lbResult;

    private static final int LOOP_TIMES = 1000;

    //all variables for settings:
    private Map<Kapelle, ArrayList<Kapelle>> kapellenMitAbhaengigkeit = new HashMap<>(); //Kapellen mit Doppelmusikern

    @FXML
    public BorderPane bpSettings;
    private ObservableList<Kapelle> data = FXCollections.observableArrayList();
    @FXML
    public TableView<Kapelle> tblSettings;
    @FXML
    public TableColumn<Kapelle, String> clmName;
    @FXML
    public TableColumn<Kapelle, String> clmEarliest;
    @FXML
    public TableColumn<Kapelle, String> clmLatest;
    @FXML
    public TableColumn<Kapelle, Boolean> clmActive;
    @FXML
    public TableColumn<Kapelle, String> clmEdit;

    //Create all participants
    private final int PARTICIPANTS = 20; //number of participants

    Kapelle goellersdorf = new Kapelle("Blasmusikkapelle Göllersdorf", 311, 1, 5); //Festzelt(2)
    Kapelle guntersdorf = new Kapelle("Trachtenkapelle Guntersdorf", 101, 1, PARTICIPANTS);
    Kapelle hadres = new Kapelle("Dorfmusik Hadres im Pulkautal", 345, 1, PARTICIPANTS);
    Kapelle hardegg = new Kapelle("Waldviertler Grenzlandkapelle Hardegg", 71, 1, PARTICIPANTS);
    //Kapelle heldenberg = new Kapelle("Jugend-Radetzkykapelle Heldenberg", 999, 1, PARTICIPANTS);
    //Kapelle hollabrunn = new Kapelle("Stadtmusik Hollabrunn", 998, 1, PARTICIPANTS);
    Kapelle mailberg = new Kapelle("Weinviertler Hauerkapelle Mailberg", 340, 4, 4); //wirklich 4. Kapelle?
    Kapelle maissau = new Kapelle("Stadtmusik Maissau", 238, 1, 1);         //Gastkapelle
    Kapelle obermarkersdorf = new Kapelle("Musikkapelle Obermarkersdorf", 248, 1, PARTICIPANTS);
    Kapelle pulkau = new Kapelle("Trachtenkapelle Pulkau", 187, 1, 6); //Festzelt(3)
    Kapelle ravelsbach = new Kapelle("Jugend-Deutschmeisterkapelle Ravelsbach", 338, 1,
            PARTICIPANTS);
    Kapelle retz = new Kapelle("Stadtkapelle Retz", 278, 1, PARTICIPANTS);
    Kapelle retzbach = new Kapelle("Trachtenkapelle Retzbach", 191, 1, PARTICIPANTS);
    Kapelle roeschitz = new Kapelle("Musikverein Röschitz", 122, 1, PARTICIPANTS);
    //Kapelle roseldorf = new Kapelle("Musikkapelle Roseldorf", 378, 1, PARTICIPANTS);
    Kapelle schmidatal = new Kapelle("Musikverein Schmidatal & Musikkapelle Roseldorf", 154, 1, PARTICIPANTS); //2018 gemeinsam angetreten
    Kapelle theras = new Kapelle("Trachtenkapelle Theras", 245, 1, PARTICIPANTS);
    Kapelle unterduernbach = new Kapelle("Musikverein Unterdürnbach", 997, 1, PARTICIPANTS);
    Kapelle wullersdorf = new Kapelle("Jugend-Musikverein Wullersdorf", 435, 1, PARTICIPANTS);
    Kapelle zellerndorf = new Kapelle("Musikkapelle Zellerndorf", 170, 1, 2); //Festzelt(1)
    Kapelle ziersdorf = new Kapelle("Trachtenkapelle Ziersdorf und Umgebung", 369, 1, 8);
    Kapelle kirchberg = new Kapelle("Musikverein Kirchberg am Wagram", 999, 1, PARTICIPANTS);
    Kapelle angerberg_mariastein = new Kapelle("Bundesmusikkapelle Angerberg/Mariastein", 999, 10, PARTICIPANTS);

    private final ArrayList<Kapelle> allParticipants = new ArrayList<>(Arrays.asList(goellersdorf, guntersdorf, hadres,
            hardegg, mailberg, maissau, obermarkersdorf, pulkau, ravelsbach, retz, retzbach, roeschitz, schmidatal,
            theras, unterduernbach, wullersdorf, zellerndorf, ziersdorf, kirchberg, angerberg_mariastein));

    public void initialize() {
        vbResult.getChildren().clear();

        //initialize table

        //setting the table to editable is needed but done in the fxml file
        //tblSettings.setEditable(true);

        clmName.setCellValueFactory(new PropertyValueFactory<>("bez"));
        clmEarliest.setCellValueFactory((c) -> new SimpleStringProperty(c.getValue().getFrStNr() + ""));
        clmEarliest.setCellFactory(TextFieldTableCell.forTableColumn());
        //clmEarliest.setEditable(true);
        clmEarliest.setOnEditCommit(
                event -> event.getTableView().getItems().get(event.getTablePosition().getRow()).
                        setFrStNr(Integer.parseInt(event.getNewValue()))
        );
        clmLatest.setCellValueFactory((c) -> new SimpleStringProperty(c.getValue().getSpStNr() + ""));
        clmLatest.setCellFactory(TextFieldTableCell.forTableColumn());
        clmLatest.setOnEditCommit(
                event -> event.getTableView().getItems().get(event.getTablePosition().getRow()).
                        setSpStNr(Integer.parseInt(event.getNewValue()))
        );

        clmActive.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isActive()));

        clmEdit.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
        clmEdit.setCellFactory(new Callback<TableColumn<Kapelle, String>, TableCell<Kapelle, String>>() {
            @Override
            public TableCell<Kapelle, String> call(TableColumn<Kapelle, String> param) {
                return new TableCell<Kapelle, String>() {
                    final Button btnEdit = new Button("Edit");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btnEdit.setOnAction(event -> {
                                Kapelle kap = getTableView().getItems().get(getIndex());
                                //TODO handle this correctly
                                System.out.println(" klick on -> " + kap.getBez());
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/edit.fxml"));
                                try {
                                    Parent parent = loader.load();
                                    EditController editController = loader.getController();
                                    editController.init(kap, kapellenMitAbhaengigkeit.get(kap), data);
                                    Stage stage = new Stage();
                                    stage.setTitle("Kapelle bearbeiten");
                                    stage.initOwner(tblSettings.getScene().getWindow());
                                    stage.setScene(new Scene(parent));
                                    stage.setOnCloseRequest(c -> editController.saveEdit(null));
                                    //block until windows is closed
                                    stage.showAndWait();

                                    //get value back
                                    Kapelle newKap = editController.getKapelle();
                                    kap.setBez(newKap.getBez());
                                    kap.setFrStNr(newKap.getFrStNr());
                                    kap.setSpStNr(newKap.getSpStNr());
                                    kap.setActive(newKap.isActive());
                                    //and get dependencies
                                    kapellenMitAbhaengigkeit.put(kap, editController.getDependencies());
                                    tblSettings.refresh();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            setGraphic(btnEdit);
                            setText(null);
                        }
                    }
                };
            }
        });

        data.addAll(allParticipants);
        tblSettings.setItems(data);
    }

    public void openSettings(ActionEvent actionEvent) {
        vbResult.getChildren().clear();
        vbResult.getChildren().add(bpSettings);
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
            //System.out.println("\t\tDurchlauf Nummer = " + (i + 1));
            if (stNrAlgorithm.startAlgorithmOnce(false, true)) {
                posCounter++;
            }
        }
        //System.out.println("\n\t\tDurchlauf Nummer = " + LOOP_TIMES);
        if ((lastResult = stNrAlgorithm.startAlgorithmOnce(true, true))) {
            posCounter++;
        }

        lbLoopTimes.setText("" + LOOP_TIMES);
        lbSuccessful.setText("" + posCounter);
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
        if (deleteList) {
            vbResult.getChildren().clear();
        }
        if (resultStatus) {
            for (int i = 1; i < result.length; i++) {
                Label tempLabel = new Label(i + ":\t\t" + result[i].getBez());
                tempLabel.setStyle("-fx-font: 13pt System;");
                vbResult.getChildren()
                        .add(tempLabel);
            }
        } else {
            Label tempLabel = new Label("Zuweisung nicht gelungen");
            tempLabel.setStyle("-fx-font: 13pt System;");
            vbResult.getChildren()
                    .add(tempLabel);
        }
    }
}
