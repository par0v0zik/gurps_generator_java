package ru.gurps.generator.controller;

import javafx.beans.property.SimpleBooleanProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.gurps.generator.Main;
import ru.gurps.generator.models.Addon;
import ru.gurps.generator.models.Feature;
import ru.gurps.generator.models.FeatureAddon;
import ru.gurps.generator.models.UserFeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FeaturesAbstractController extends AbstractController {
    private TableView<Feature> tableView;
    private TableColumn<Feature, String> title;
    private TableColumn<Feature, String> titleEn;
    private TableColumn<Feature, String> type;
    private TableColumn<Feature, String> cost;
    private TableColumn<Feature, String> description;

    private AnchorPane bottomMenu;
    private ComboBox<Integer> lvlComboBox;
    private Label lvlLabel;
    private TextField lvlText;
    private Button add;
    private Button remove;
    private Button full;
    private Label finalCost;
    private TextField finalCostText;

    private TableView<Addon> addonsTableView;
    private TableColumn<Addon, Boolean> activate;
    private TableColumn<Addon, String> addonName;
    private TableColumn<Addon, String> addonNameEn;
    private TableColumn<Addon, String> addonLevel;
    private TableColumn<Addon, String> addonCost;

    private CheckMenuItem checkBox1;
    private CheckMenuItem checkBox2;
    private CheckMenuItem checkBox3;
    private CheckMenuItem checkBox4;
    private CheckMenuItem checkBox5;

    private MenuButton searchButton;
    private MenuItem searchAll;
    private MenuItem searchTitle;
    private MenuItem searchTitleEn;
    private MenuItem searchCost;
    private MenuItem searchDescription;
    private TextField searchText;
    private boolean isAdvantage;

    protected ArrayList<Integer> featuresNumbers = new ArrayList<>();
    protected ObservableList<Feature> data = FXCollections.observableArrayList();

    public FeaturesAbstractController(TableView<Feature> tableView, TableView<Addon> addonsTableView, TableColumn<Feature, String> title, TableColumn<Feature, String> titleEn, TableColumn<Feature, String> type, TableColumn<Feature, String> cost, TableColumn<Feature, String> description, AnchorPane bottomMenu, ComboBox<Integer> lvlComboBox, Label lvlLabel, TextField lvlText, Button add, Button remove, Button full, Label finalCost, TextField finalCostText, TableColumn<Addon, Boolean> activate, TableColumn<Addon, String> addonName, TableColumn<Addon, String> addonNameEn, TableColumn<Addon, String> addonLevel, TableColumn<Addon, String> addonCost, CheckMenuItem checkBox1, CheckMenuItem checkBox2, CheckMenuItem checkBox3, CheckMenuItem checkBox4, CheckMenuItem checkBox5, MenuButton searchButton, MenuItem searchAll, MenuItem searchTitle, MenuItem searchTitleEn, MenuItem searchCost, MenuItem searchDescription, TextField searchText, boolean isAdvantage) {
        this.tableView = tableView;
        this.addonsTableView = addonsTableView;
        this.title = title;
        this.titleEn = titleEn;
        this.type = type;
        this.cost = cost;
        this.description = description;
        this.bottomMenu = bottomMenu;
        this.lvlComboBox = lvlComboBox;
        this.lvlLabel = lvlLabel;
        this.lvlText = lvlText;
        this.add = add;
        this.remove = remove;
        this.full = full;
        this.finalCost = finalCost;
        this.finalCostText = finalCostText;
        this.activate = activate;
        this.addonName = addonName;
        this.addonNameEn = addonNameEn;
        this.addonLevel = addonLevel;
        this.addonCost = addonCost;
        this.checkBox1 = checkBox1;
        this.checkBox2 = checkBox2;
        this.checkBox3 = checkBox3;
        this.checkBox4 = checkBox4;
        this.checkBox5 = checkBox5;
        this.searchButton = searchButton;
        this.searchAll = searchAll;
        this.searchTitle = searchTitle;
        this.searchTitleEn = searchTitleEn;
        this.searchCost = searchCost;
        this.searchDescription = searchDescription;
        this.searchText = searchText;
        this.isAdvantage = isAdvantage;
        for(int i = 1; 5 >= i; i++) featuresNumbers.add(i);

        run();
    }

    private void run(){
        data.addAll(new Feature().where("advantage", isAdvantage));
        FeatureEventHandler featureEventHandler = new FeatureEventHandler();

        tableView.setRowFactory(tv -> {
            TableRow<Feature> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_CLICKED, featureEventHandler);
            return row;
        });

        setFeatures();
        setSearch();
        checkBoxEvents();
    }

    private void setFeatures(){
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleEn.setCellValueFactory(new PropertyValueFactory<>("titleEn"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));

        if(isAdvantage) tableView.setPlaceholder(new Label("Преимуществ нет"));
        else tableView.setPlaceholder(new Label("Недостаков нет"));
        tableView.setItems(data);
    }

    protected void setSearch(){
        searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) searchButton.setDisable(true);
            else searchButton.setDisable(false);
        });

        searchAll.setOnAction(event -> {
            String query = "advantage=" + isAdvantage + " and UPPER(title) like UPPER('%" + searchText.getText() + "%') or " +
                    "advantage=" + isAdvantage + " and UPPER(titleEn) like UPPER('%" + searchText.getText() + "%') or " +
                    "advantage=" + isAdvantage + " and UPPER(cost) like UPPER('%" + searchText.getText() + "%') or " +
                    "advantage=" + isAdvantage + " and UPPER(description) like UPPER('%" + searchText.getText() + "%')";
            tableView.setItems(new Feature().where(query));
        });

        for(String feature : new String[] {"Title", "TitleEn", "Cost", "Description"}){
            try {
                MenuItem menuItem = (MenuItem) this.getClass().getDeclaredField("search" + feature).get(this);
                menuItem.setOnAction(event ->{
                    String query = "advantage="+ isAdvantage +" and UPPER("+ feature + ") like UPPER('%" + searchText.getText() + "%')";
                    tableView.setItems(new Feature().where(query));
                });
            } catch(IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    protected void checkBoxEvents(){
        Integer[] numbers = {1, 2, 3, 4, 5};
        for(Integer number : numbers) {
            try {
                CheckMenuItem checkBox = (CheckMenuItem) this.getClass().getDeclaredField("checkBox" + number).get(this);
                checkBox.setSelected(true);
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    String query = "advantage=" + isAdvantage + " and type like ";
                    if(newValue) featuresNumbers.add(number);
                    else featuresNumbers.remove(number);
                    for(Integer lNumber : featuresNumbers) {
                        if(query.equals("advantage=" + isAdvantage + " and type like ")) query += "'%" + lNumber + "%'";
                        else query += " or advantage=" + isAdvantage + " and type like '%" + lNumber + "%'";
                    }
                    if(query.equals("advantage=" + isAdvantage + " and type like ")) query = "advantage=" + isAdvantage + " and type='6'";
                    tableView.setItems(new Feature().where(query));
                });
            } catch(NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    class FeatureEventHandler implements EventHandler<MouseEvent> {
        private int lastId;
        private ObservableList<Addon> addonsArray;
        private Feature feature;
        private String currentLvl;

        public FeatureEventHandler() {
        }

        @Override
        public void handle(MouseEvent t) {
            TableRow row = (TableRow) t.getSource();
            feature = tableView.getItems().get(row.getIndex());
            setupBottomMenu();
            setCurrentLvl();
            setCurrentCost();

            if(feature.id.equals(lastId)) return;

            HashMap<String, Object> params = new HashMap<>();
            params.put("userId", user.id);
            params.put("featureId", feature.id);
            UserFeature userFeature = (UserFeature) new UserFeature().find_by(params);

            feature.add = userFeature.id != null;

            if(feature.add) {
                add.setVisible(false);
                remove.setVisible(true);
            } else {
                add.setVisible(true);
                remove.setVisible(false);
            }

            defaultParams();
            allAddons();

            if(!addonsArray.isEmpty()) {
                userAddons(userFeature.id);
                setCells();

                addonsTableView.setItems(addonsArray);
                addonsTableView.setEditable(true);
                addonsTableView.setVisible(true);
            } else {
                addonsTableView.setVisible(false);
            }

            buttonsActions();

            if(feature.add) finalCost.setText(Integer.toString(userFeature.cost));
        }

        private double currentAddonCost(int cost, int level) {
            return cost * level / 100.0;
        }

        private void addonCost(double cost, Addon addon) {
            int lastCost;
            int intFinalCost = intCost();
            int result = (int) (feature.getCost() * Double.parseDouble(currentLvl) * cost);

            if(addon.active) {
                lastCost = intFinalCost + result;
                if(feature.add)  setCurrentPoints(Integer.parseInt(globalCost.getText()) + result);
            } else {
                lastCost = intFinalCost - result;
                if(feature.add)  setCurrentPoints(Integer.parseInt(globalCost.getText()) - result);
            }
            newCost(lastCost);
        }

        private void userAddons(Integer id) {
            if(id == null) return;
            ObservableList<FeatureAddon> featureAddons = new FeatureAddon().where("userFeatureId", id);
            for(FeatureAddon featureAddon : featureAddons) {
                for(Addon addon : addonsArray) {
                    if(addon.id == featureAddon.addonId) {
                        addon.active = true;
                        addon.cost = featureAddon.cost;
                        addon.level = featureAddon.level;
                    }
                }
            }
        }

        private void setCells() {
            activate.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
            activate.setCellFactory(p -> new ButtonCell());

            addonName.setCellValueFactory(new PropertyValueFactory<>("title"));
            addonNameEn.setCellValueFactory(new PropertyValueFactory<>("titleEn"));
            addonLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
            addonLevel.setEditable(true);
            addonCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
            addonCost.setEditable(true);

            addonLevel.setCellFactory(TextFieldTableCell.forTableColumn());
            addonLevel.setOnEditCommit(event -> {
                if(!event.getNewValue().matches("\\d+") || event.getNewValue().equals("0")) return;
                Addon addon = addonsTableView.getItems().get(event.getTablePosition().getRow());
                if(addon.maxLevel != 1) addon.level = event.getNewValue();
            });

            addonCost.setCellFactory(TextFieldTableCell.forTableColumn());
            addonCost.setOnEditCommit(event -> {
                if(event.getNewValue().equals("0")) return;
                Addon addon = addonsTableView.getItems().get(event.getTablePosition().getRow());
                try {
                    Integer.parseInt(event.getNewValue());
                } catch(NumberFormatException e){
                    return;
                }

                if(addon.cost.equals("0")) addon.cost = event.getNewValue();
            });
        }

        //Define the button cell
        private class ButtonCell extends TableCell<Addon, Boolean> {
            Button addButton = new Button("Добавить");
            Button removeButton = new Button("Удалить");

            ButtonCell() {
                addButton.setOnAction(t -> {
                    Addon addon = (Addon) getTableRow().getItem();
                    new FeatureAddon().delete(addon.id);
                    new FeatureAddon(feature.id, addon.id, addon.cost, addon.level).create();
                    addon.active = true;
                    double addonCost = currentAddonCost(Integer.parseInt(addon.cost), Integer.parseInt(addon.level));
                    addonCost(addonCost, addon);
                    setGraphic(removeButton);
                });

                removeButton.setOnAction(t -> {
                    Addon addon = (Addon) getTableRow().getItem();
                    new FeatureAddon().delete(addon.id);

                    addon.active = false;
                    double addonCost = currentAddonCost(Integer.parseInt(addon.cost), Integer.parseInt(addon.level));
                    addonCost(addonCost, addon);
                    setGraphic(addButton);
                });
            }

            @Override
            protected void updateItem(Boolean t, boolean empty) {
                super.updateItem(t, empty);
                if(empty) return;
                Addon addon = (Addon) getTableRow().getItem();
                if(addon == null) return;
                setGraphic(addon.active ? removeButton : addButton);
            }
        }

        private void allAddons() {
            ObservableList addons = new Addon().where("featureId", lastId);
            addonsArray.removeAll();
            addonsArray.addAll(addons);
        }

        private void setCurrentLvl() {
            if(feature.maxLevel == 0) {
                currentLvl = "1";
                lvlText.setText("1");
                lvlText.textProperty().addListener((observableValue, oldValue, newValue) -> {
                    if(!oldValue.equals(newValue) || !newValue.equals("")) finalCost(Integer.parseInt(newValue));
                });

                lvlLabel.setVisible(true);
                lvlText.setVisible(true);
                lvlComboBox.setVisible(false);
            } else if(feature.maxLevel > 1) {
                lvlLabel.setVisible(true);
                lvlText.setVisible(false);
                lvlComboBox.setVisible(true);
                ObservableList<Integer> levels = FXCollections.observableArrayList();
                for(int i = 1; feature.maxLevel >= i; i++) levels.add(i);
                lvlComboBox.setItems(levels);
                lvlComboBox.setValue(1);
                lvlComboBox.valueProperty().addListener((observable, oldValue, newValue) -> finalCost((Integer) newValue));
            } else {
                lvlText.setText("1");
                currentLvl = "1";
                lvlLabel.setVisible(false);
                lvlComboBox.setVisible(false);
                lvlText.setVisible(false);
            }
        }

        private void setCurrentCost() {
            int cost = feature.getCost();
            if(cost == 0) {
                finalCost.setVisible(false);
                finalCostText.setVisible(true);
                finalCostText.textProperty().addListener((observableValue, oldValue, newValue) -> {
                    if(!oldValue.equals(newValue) || !newValue.equals("")) newCost(newValue);
                });
            } else {
                finalCost.setVisible(true);
                finalCostText.setVisible(false);
            }
        }

        private void finalCost(int newLevel) {
            int finalCost = intCost() / feature.oldLevel * newLevel;
            feature.oldLevel = newLevel;
            newCost(finalCost);
        }

        private void newCost(int changeCost) {
            String result = Integer.toString(changeCost);
            finalCost.setText(result);
            finalCostText.setText(result);
        }

        private void newCost(String changeCost) {
            finalCost.setText(changeCost);
            finalCostText.setText(changeCost);
        }

        private int intCost() {
            return finalCost.isVisible() ? Integer.parseInt(finalCost.getText()) : Integer.parseInt(finalCostText.getText());
        }

        private void setupBottomMenu() {
            final double bottomMenuSize = 134.0;
            AnchorPane.setBottomAnchor(tableView, bottomMenuSize);
            bottomMenu.setVisible(true);
        }

        private void defaultParams() {
            lastId = feature.id;
            addonsArray = FXCollections.observableArrayList();
            addonsArray.clear();
            lvlText.setText("1");
            finalCost.setText(Integer.toString(feature.cost));
            finalCostText.setText(Integer.toString(feature.cost));
        }

        private void buttonsActions() {
            full.setOnAction(actionEvent -> {
                Stage childrenStage = new Stage();
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("resources/views/featureFull.fxml"));
                FeatureFullController controller = new FeatureFullController(feature);
                loader.setController(controller);
                Parent childrenRoot;
                try {
                    childrenRoot = loader.load();
                    childrenStage.setResizable(false);
                    childrenStage.setScene(new Scene(childrenRoot, 635, 572));
                    childrenStage.setTitle("GURPSGenerator - " + feature.getTitle() + " (" + feature.getTitleEn() + ")");
                    childrenStage.show();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            });

            add.setOnAction(actionEvent -> {
                UserFeature user_feature = (UserFeature) new UserFeature(user.id, feature.id, intCost(), Integer.parseInt(currentLvl)).create();
                setCurrentPoints(intCost() + Integer.parseInt(user.currentPoints));
                feature.add = true;
                add.setVisible(false);
                remove.setVisible(true);

                if(!addonsTableView.isVisible()) return;
                addonsArray.stream().filter(addon -> addon.active).forEach(addon -> {
                    new FeatureAddon().delete(addon.id);
                    new FeatureAddon(user_feature.id, addon.id, addon.cost, addon.level).create();
                });
            });

            remove.setOnAction(actionEvent -> {
                HashMap<String, Object> params1 = new HashMap<>();
                params1.put("userId", Integer.toString(user.id));
                params1.put("featureId", feature.id);
                UserFeature user_feature = (UserFeature) new UserFeature().find_by(params1);
                setCurrentPoints(Integer.parseInt(user.currentPoints) - user_feature.cost);
                user_feature.delete();
                feature.add = false;
                add.setVisible(true);
                remove.setVisible(false);
                if(!addonsTableView.isVisible()) return;
                ObservableList<FeatureAddon> featureAddons = new FeatureAddon().where("userFeatureId", user_feature.id);
                for(FeatureAddon featureAddon : featureAddons) featureAddon.delete();
            });
        }
    }
}
