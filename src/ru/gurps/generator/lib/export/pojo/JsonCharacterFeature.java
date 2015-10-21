package ru.gurps.generator.lib.export.pojo;

import javafx.collections.ObservableList;
import ru.gurps.generator.models.characters.CharactersAddon;
import ru.gurps.generator.models.characters.CharactersFeature;

public class JsonCharacterFeature {
    public CharactersFeature charactersFeature;
    public ObservableList<CharactersAddon> charactersAddons;

    public JsonCharacterFeature() {
    }
}
