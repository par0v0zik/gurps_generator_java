package ru.gurps.generator.models.characters;

import ru.gurps.generator.config.Model;

public class CharactersQuirk extends Model {
    public Integer id;
    public Integer characterId;
    public Integer quirkId;
    public Integer cost = 0;

    public CharactersQuirk() {
    }

    public CharactersQuirk(Integer characterId, Integer quirkId, Integer cost) {
        this.characterId = characterId;
        this.quirkId = quirkId;
        this.cost = cost;
    }
}
