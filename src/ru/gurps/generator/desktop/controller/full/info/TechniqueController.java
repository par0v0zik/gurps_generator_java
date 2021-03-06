package ru.gurps.generator.desktop.controller.full.info;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import ru.gurps.generator.desktop.controller.helpers.AbstractController;
import ru.gurps.generator.desktop.models.rules.Technique;

public class TechniqueController extends AbstractController {
    private Technique technique;

    public Label name;
    public Label complexity;
    public Label defaultUse;
    public Label demands;
    public Text fullDescription;

    public TechniqueController(Technique technique) {
        this.technique = technique;
    }

    @FXML
    private void initialize() {
        name.setText(technique.name + " (" + technique.nameEn + " )");
        complexity.setText(complexity.getText() + technique.getComplexityFull());
        defaultUse.setText(defaultUse.getText() + technique.getDefaultUse());
        demands.setText(demands.getText() + technique.getDemands());
        fullDescription.setText(technique.description + "\n");
    }
}
