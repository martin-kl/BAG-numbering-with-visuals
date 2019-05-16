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
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

public class MainController {
    //outer FXML structures, all on vbOuter
    @FXML
    public VBox vbOuter;
    @FXML
    public HBox hbHeader;
    @FXML
    public HBox hbButtons;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public BorderPane bpSettings;


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
    private Map<Kapelle, ArrayList<Kapelle>> dependencies = new HashMap<>(); //Kapellen mit Doppelmusikern

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
    public TableColumn<Kapelle, String> clmDependencies;
    @FXML
    public TableColumn<Kapelle, String> clmEdit;

    private ArrayList<Kapelle> allKapellen;

    public void initialize() {
        clearVbOuter();
        //add settings at the beginning
        vbOuter.getChildren().add(bpSettings);

        //load data from file
        try {
            allKapellen = Utils.readFromFile();
        } catch (IOException e) {
            showExceptionDialog(e,
                    "Fehler",
                    "Fehler beim Laden",
                    "Fehler beim Laden der Daten aus der Datei " + Utils.FILE_PATH + ".\n" +
                            "Siehe Meldung für mehr Details:");
        }

        //and init the whole table
        initTable();
    }

    private void clearVbOuter() {
        vbOuter.getChildren().clear();
        vbOuter.getChildren().add(hbHeader);
        vbOuter.getChildren().add(hbButtons);
    }

    public void openSettings(ActionEvent actionEvent) {
        //vbResult.getChildren().clear();
        //vbResult.getChildren().add(bpSettings);
        clearVbOuter();
        vbOuter.getChildren().add(bpSettings);
    }


    public void startAlgorithmNormally(ActionEvent actionEvent) {
        ArrayList<Kapelle> participants = Utils.deepCopyOfActiveOnes(allKapellen);
        if (!isDataValid(participants)) {
            return;
        }
        //refresh so that all calculated values are now seen in the table
        tblSettings.refresh();

        switchToOutput();

        StNrAlgorithm stNrAlgorithm = getInitializedStrNrAlgorithmInstance(participants);

        //stNrAlgorithm.startAlgorithmOnce(true, false);
        boolean result = stNrAlgorithm.startAlgorithmLoop(false);

        addResultToGraphicOutput(true, stNrAlgorithm.getResult(), result);
        if (!result) {
            showAlert(Alert.AlertType.ERROR, "Fehler", "Zuweisung fehlgeschlagen",
                    "Konnte selbst nach " + StNrAlgorithm.MAX_TRIES + " Versuchen keine Zurodnung finden!");
        }
    }

    public void startAlgorithmLoop(ActionEvent actionEvent) {
        ArrayList<Kapelle> participants = Utils.deepCopyOfActiveOnes(allKapellen);
        if (!isDataValid(participants)) {
            return;
        }
        //refresh so that all calculated values are now seen in the table
        tblSettings.refresh();

        switchToOutput();

        StNrAlgorithm stNrAlgorithm = getInitializedStrNrAlgorithmInstance(participants);

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
        if (LOOP_TIMES - posCounter == LOOP_TIMES) {
            showAlert(Alert.AlertType.ERROR, "Fehler", "Alle Zuweisungen fehlgeschlagen",
                    "Alle versuchten " + LOOP_TIMES + " Zuweisungen sind fehlgeschlagen!");
        }
    }

    private void switchToOutput() {
        clearVbOuter();
        vbOuter.getChildren().add(scrollPane);
    }


    private boolean isDataValid(ArrayList<Kapelle> participants) {
        String res = Utils.pruefeStNr(participants);
        if (!res.equals("")) {
            showAlert(Alert.AlertType.ERROR, "Fehler", "Daten nicht valide", res);
            return false;
        }

        res = Utils.pruefeAbhaengigkeit(dependencies);
        if (!res.equals("")) {
            showAlert(Alert.AlertType.ERROR, "Fehler", "Daten nicht valide", res);
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String headerText, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private StNrAlgorithm getInitializedStrNrAlgorithmInstance(ArrayList<Kapelle> participants) {
        Map<Kapelle, ArrayList<Kapelle>> participatingDependencies = new HashMap<>();
        for (Kapelle kap : dependencies.keySet()) {
            if (kap.isActive())
                participatingDependencies.put(kap, dependencies.get(kap));
        }
        StNrAlgorithm stNrAlgorithm = new StNrAlgorithm(participants, participatingDependencies);
        stNrAlgorithm.init();
        return stNrAlgorithm;
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


    private void initTable() {
        //initialize table

        //setting the table to editable is needed but done in the fxml file
        //tblSettings.setEditable(true);

        clmName.setCellValueFactory(new PropertyValueFactory<>("bez"));
        clmEarliest.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getFrStNr() == 0 ? "" : c.getValue().getFrStNr() + ""
        ));
        clmEarliest.setCellFactory(TextFieldTableCell.forTableColumn());
        //clmEarliest.setEditable(true);
        clmEarliest.setOnEditCommit(
                event -> event.getTableView().getItems().get(event.getTablePosition().getRow()).
                        setFrStNr(Integer.parseInt(event.getNewValue()))
        );
        clmLatest.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getSpStNr() == 0 ? "" : c.getValue().getSpStNr() + ""
        ));
        clmLatest.setCellFactory(TextFieldTableCell.forTableColumn());
        clmLatest.setOnEditCommit(
                event -> event.getTableView().getItems().get(event.getTablePosition().getRow()).
                        setSpStNr(Integer.parseInt(event.getNewValue()))
        );

        clmDependencies.setCellFactory(TextFieldTableCell.forTableColumn());
        clmDependencies.setCellValueFactory(c -> {
            if (dependencies.get(c.getValue()) == null)
                return new SimpleStringProperty("0");
            return new SimpleStringProperty(dependencies.get(c.getValue()).size() + "");
        });

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
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/edit.fxml"));
                                try {
                                    Parent parent = loader.load();
                                    EditController editController = loader.getController();
                                    editController.init(kap, dependencies.get(kap), data);
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
                                    dependencies.put(kap, editController.getDependencies());
                                    Utils.keepDependenciesUpToDate(dependencies, kap);
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

        data.addAll(allKapellen);
        tblSettings.setItems(data);
    }

    public void saveData(ActionEvent actionEvent) {
        try {
            Utils.writeToFile(data);
            showAlert(Alert.AlertType.INFORMATION, "Erfolgreich", "Erfolgreich gespeichert",
                    "Daten wurden erfolgreich gespeichert.");
        } catch (IOException e) {
            showExceptionDialog(e,
                    "IO - Fehler",
                    "Fehler beim Speichern der Daten",
                    "Konnte Daten nicht unter " + Utils.FILE_PATH + " speichern.");
        }
    }

    private void showExceptionDialog(Exception e, String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }
}
