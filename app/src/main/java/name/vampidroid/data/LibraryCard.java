package name.vampidroid.data;

/**
 * Created by francisco on 04/07/17.
 */

public class LibraryCard {

    private String name;

    private String type;

    private String clan;

    private String disciplines;

    private String text;

    private String poolCost;

    private String bloodCost;

    private String artist;

    private String setRarity;


    public LibraryCard(String name, String type, String clan, String disciplines, String text, String poolCost, String bloodCost, String artist, String setRarity) {
        this.name = name;
        this.type = type;
        this.clan = clan;
        this.disciplines = disciplines;
        this.text = text;
        this.poolCost = poolCost;
        this.bloodCost = bloodCost;
        this.artist = artist;
        this.setRarity = setRarity;
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

    public String getPoolCost() {
        return poolCost;
    }

    public String getBloodCost() {
        return bloodCost;
    }

    public String getArtist() {
        return artist;
    }

    public String getSetRarity() {
        return setRarity;
    }

}
