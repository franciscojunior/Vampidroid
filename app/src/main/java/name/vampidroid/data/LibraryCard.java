package name.vampidroid.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;

/**
 * Created by francisco on 04/07/17.
 */
@Entity
public class LibraryCard extends Card {

    private String poolCost;

    private String bloodCost;

    private String convictionCost;

    public LibraryCard(String name, String type, String clan, String disciplines, String text, String poolCost, String bloodCost, String convictionCost, String artist, String setRarity) {
        super(name, text, type, clan, disciplines, setRarity, artist );
        this.poolCost = poolCost;
        this.bloodCost = bloodCost;
        this.convictionCost = convictionCost;
    }

    public String getPoolCost() {
        return poolCost;
    }

    public String getBloodCost() {
        return bloodCost;
    }

    public String getCost() {
        if (poolCost.length() > 0) {
            return poolCost;
        } else if (bloodCost.length() > 0) {
            return bloodCost;
        } else if (convictionCost.length() > 0) {
            return convictionCost;
        } else {
            return "0";
        }
    }

    public String getConvictionCost() {
        return convictionCost;
    }

    public void setConvictionCost(String convictionCost) {
        this.convictionCost = convictionCost;
    }
}
