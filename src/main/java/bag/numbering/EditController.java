package bag.numbering;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class EditController {
    @FXML
    public BorderPane settingsPane;
    @FXML
    public TextField txEarliest;
    @FXML
    public TextField txName;
    @FXML
    public TextField txLatest;
    @FXML
    public CheckBox cbActive;
    @FXML
    public ChoiceBox<Kapelle> cbDep1;
    @FXML
    public ChoiceBox<Kapelle> cbDep2;
    @FXML
    public ChoiceBox<Kapelle> cbDep3;
    @FXML
    public ChoiceBox<Kapelle> cbDep4;
    @FXML
    public ChoiceBox<Kapelle> cbDep5;

    private ObservableList<Kapelle> kapellen;
    private ArrayList<Kapelle> dependencies = new ArrayList<>(5);
    private Kapelle kap;

    public void saveEdit(ActionEvent actionEvent) {
        kap.setBez(txName.getText());
        kap.setFrStNr(Integer.parseInt(txEarliest.getText()));
        kap.setSpStNr(Integer.parseInt(txLatest.getText()));
        kap.setActive(cbActive.isSelected());

        //get dependencies
        getDependencies(cbDep1);
        getDependencies(cbDep2);
        getDependencies(cbDep3);
        getDependencies(cbDep4);
        getDependencies(cbDep5);
        //finally close the stage
        Stage stage = (Stage) (settingsPane.getScene().getWindow());
        stage.close();
    }

    private void getDependencies(ChoiceBox<Kapelle> cbDep) {
        if (cbDep.getValue() != null) {
            dependencies.add(cbDep.getValue());
        }
    }

    public void init(Kapelle kap, ArrayList<Kapelle> dep, ObservableList<Kapelle> kapellen) {
        this.kap = kap;
        this.kapellen = kapellen;

        txName.setText(kap.getBez());
        txEarliest.setText(kap.getFrStNr() + "");
        txLatest.setText(kap.getSpStNr() + "");
        cbActive.setSelected(kap.isActive());
        cbDep1.setItems(kapellen);
        cbDep2.setItems(kapellen);
        cbDep3.setItems(kapellen);
        cbDep4.setItems(kapellen);
        cbDep5.setItems(kapellen);

        //init dependencies
        if (dep != null && dep.size() > 0) {
            if (dep.size() > 5)
                System.err.println(kap.getBez() + " has " + dep.size() + " dependencies but only 5 can be selected !!");
            if (dep.get(0) != null)
                cbDep1.setValue(dep.get(0));
            if (dep.size() > 1)
                cbDep2.setValue(dep.get(1));
            if (dep.size() > 2)
                cbDep3.setValue(dep.get(2));
            if (dep.size() > 3)
                cbDep4.setValue(dep.get(3));
            if (dep.size() > 4)
                cbDep5.setValue(dep.get(4));
        }
    }

    public Kapelle getKapelle() {
        return kap;
    }

    public ArrayList<Kapelle> getDependencies() {
        return dependencies;
    }
}
