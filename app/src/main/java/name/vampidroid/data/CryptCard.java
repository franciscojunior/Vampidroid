package name.vampidroid.data;

import android.arch.persistence.room.Entity;

/**
 * Created by FranciscoJunior on 17/03/2017.
 */
@Entity
public class CryptCard extends Card {

    private String capacity;

    private String group;

    private String advanced;


    public CryptCard(String name, String type, String clan, String disciplines, String text, String capacity, String artist, String setRarity, String group, String advanced) {
        super(name, text, type, clan, disciplines, setRarity, artist);
        this.capacity = capacity;
        this.group = group;
        this.advanced = advanced;
    }

    public String getCapacity() {
        return capacity;
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
