package name.vampidroid.data;

import android.arch.persistence.room.PrimaryKey;

/**
 * Created by francisco on 11/09/17.
 */

public abstract class Card {

    @PrimaryKey(autoGenerate = true)
    private Long uid;
    private String name;
    private String text;
    private final String type;
    private final String clan;
    private final String disciplines;
    private final String setRarity;
    private final String artist;

    public Card(String name, String text, String type, String clan, String disciplines, String setRarity, String artist) {
        this.name = name;
        this.text = text;
        this.type = type;
        this.clan = clan;
        this.disciplines = disciplines;
        this.setRarity = setRarity;
        this.artist = artist;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getSetRarity() {
        return setRarity;
    }

    public String getArtist() {
        return artist;
    }
}
