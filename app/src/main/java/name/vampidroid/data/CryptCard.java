package name.vampidroid.data;

/**
 * Created by FranciscoJunior on 17/03/2017.
 */

public class CryptCard {

    private String name;

    private String type;

    private String clan;

    private String disciplines;

    private String text;

    private String capacity;

    private String artist;

    private String setRarity;

    private String group;

    private String advanced;


    public CryptCard(String name, String type, String clan, String disciplines, String text, String capacity, String artist, String setRarity, String group, String advanced) {
        this.name = name;
        this.type = type;
        this.clan = clan;
        this.disciplines = disciplines;
        this.text = text;
        this.capacity = capacity;
        this.artist = artist;
        this.setRarity = setRarity;
        this.group = group;
        this.advanced = advanced;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getClan() {
        return clan;
    }

    public String getDisciplines() {
        return disciplines;
    }

    public String getText() {
        return text;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getArtist() {
        return artist;
    }

    public String getSetRarity() {
        return setRarity;
    }

    public String getGroup() {
        return group;
    }

    public String getAdvanced() {
        return advanced;
    }

    public boolean isAdvanced() {
        return advanced.length() > 0;

    }
}
