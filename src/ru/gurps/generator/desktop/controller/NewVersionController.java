package ru.gurps.generator.desktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import ru.gurps.generator.desktop.Main;
import ru.gurps.generator.desktop.controller.helpers.AbstractController;

import java.awt.*;
import java.io.IOException;

public class NewVersionController extends AbstractController {
    public AnchorPane newVersionHas;
    public AnchorPane noNewVersion;
    public Button yes;
    public Button no;
    public Button ok;

    @FXML
    private void initialize() throws Exception {
        Main.checkNewVersion();
        if(urlToLastVersion == null){
            noNewVersion.setVisible(true);
            ok.setOnAction(event -> ok.getScene().getWindow().hide());
        }
        else {
           newVersionHas.setVisible(true);
            yes.setOnAction(event -> {
                try {
                    Desktop.getDesktop().browse(urlToLastVersion);
                    yes.getScene().getWindow().hide();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            no.setOnAction(event -> no.getScene().getWindow().hide());
        }
    }
}