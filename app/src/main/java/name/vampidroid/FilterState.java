package name.vampidroid;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by FranciscoJunior on 13/12/2016.
 */

public class FilterState implements Parcelable{

    private static final String TAG = "FilterState";

    private CharSequence name = "";

    boolean searchInsideCardText = false;

    private boolean[] groups = new boolean[7];

    private int capacityMin = 1;

    private int capacityMax = 11;
    private ArrayList<CharSequence> cardTypes = new ArrayList<>();


    private ArrayList<CharSequence> clans = new ArrayList<>();

    private ArrayList<CharSequence> libraryDisciplines = new ArrayList<>();


    private HashMap<CharSequence, FilterState.CryptDiscipline> cryptDisciplines = new HashMap<>();



    public FilterState() {

    }

    public void reset() {

        capacityMin = 1;
        capacityMax = 11;
        Arrays.fill(groups, false);
        cardTypes.clear();
        clans.clear();
        libraryDisciplines.clear();
        cryptDisciplines.clear();

    }


    public void setGroup(int group, boolean isSet) {
        groups[group] = isSet;
    }


    public void setName(CharSequence name) {

        if (!name.equals(this.name))
            this.name = name.toString();

    }


    public boolean isSearchInsideCardText() {
        return searchInsideCardText;
    }

    public void setSearchInsideCardText(boolean searchInsideCardText) {
        this.searchInsideCardText = searchInsideCardText;
    }

    public void setCapacityMin(int progress) {
        capacityMin = progress;

    }

    public void setCapacityMax(int progress) {
        capacityMax = progress;
    }

    public void setCardType(CharSequence cardType, boolean isSet) {

        if (isSet)
            cardTypes.add(cardType);
        else
            cardTypes.remove(cardType);

    }

    public void setClan(CharSequence clan, boolean isSet) {

        if (isSet)
            clans.add(clan);
        else
            clans.remove(clan);


    }

    public void setLibraryDiscipline(String libraryDiscipline, boolean isSet) {

        if (isSet)
            libraryDisciplines.add(libraryDiscipline);
        else
            libraryDisciplines.remove(libraryDiscipline);

    }


    public void setDiscipline(String discipline, boolean isBasic, boolean isSet) {


        Log.d(TAG, "setDiscipline: " + discipline);


        discipline = discipline.toLowerCase();

        FilterState.CryptDiscipline cryptDiscipline = cryptDisciplines.get(discipline);

        if (isSet) {
            if (cryptDiscipline == null) {
                Log.d(TAG, "setDiscipline: cryptDiscipline is null. Creating one...");
                cryptDiscipline = new FilterState.CryptDiscipline(discipline, isBasic, !isBasic);
                cryptDisciplines.put(discipline, cryptDiscipline);
            }

            if (isBasic)
                cryptDiscipline.setBasic(true);
            else
                cryptDiscipline.setAdvanced(true);


        } else {

            // If we are unsetting it is because the discipline was already inserted before.
            if (isBasic)
                cryptDiscipline.setBasic(false);
            else
                cryptDiscipline.setAdvanced(false);


            if (cryptDiscipline.isAllClear())
                cryptDisciplines.remove(discipline);

        }

    }


    protected FilterState(Parcel in) {

        name = in.readString();
        in.readBooleanArray(groups);
        in.readList(cardTypes, null);
        in.readList(clans, null);
        in.readMap(cryptDisciplines, FilterState.CryptDiscipline.class.getClassLoader());
        capacityMin = in.readInt();
        capacityMax = in.readInt();
        searchInsideCardText = in.readByte() != 0;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

        dest.writeString(name.toString());
        dest.writeBooleanArray(groups);
        dest.writeList(cardTypes);
        dest.writeList(clans);
        dest.writeMap(cryptDisciplines);
        dest.writeInt(capacityMin);
        dest.writeInt(capacityMax);
        dest.writeByte((byte) (searchInsideCardText ? 1 : 0));


    }

    public static final Parcelable.Creator<FilterState> CREATOR = new Parcelable.Creator<FilterState>() {
        @Override
        public FilterState createFromParcel(Parcel in) {
            return new FilterState(in);
        }

        @Override
        public FilterState[] newArray(int size) {
            return new FilterState[size];
        }
    };

    public CharSequence getName() {
        return name;
    }

    public boolean[] getGroups() {
        return groups;
    }


    public int getCapacityMin() {
        return capacityMin;
    }

    public int getCapacityMax() {
        return capacityMax;
    }

    public ArrayList<CharSequence> getCardTypes() {
        return cardTypes;
    }


    public ArrayList<CharSequence> getClans() {
        return clans;
    }


    public ArrayList<CharSequence> getLibraryDisciplines() {
        return libraryDisciplines;
    }


    public HashMap<CharSequence, CryptDiscipline> getCryptDisciplines() {
        return cryptDisciplines;
    }



    public static class CryptDiscipline implements Parcelable {

        private String name;
        private boolean basic;
        private boolean advanced;

        public String getName() {
            if (isBasic()) {
                return name.toLowerCase();
            } else {
                return name.toUpperCase();
            }

        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isBasic() {
            return basic;
        }

        public void setBasic(boolean basic) {
            this.basic = basic;
        }

        public boolean isAdvanced() {
            return advanced;
        }

        public void setAdvanced(boolean advanced) {
            this.advanced = advanced;
        }

        public CryptDiscipline(String name) {


        }

        public CryptDiscipline(String name, boolean basic, boolean advanced) {
            this.name = name;
            this.basic = basic;
            this.advanced = advanced;
        }


        public boolean isAllClear() {
            return !isBasic() && !isAdvanced();

        }

        public boolean isBothSet() {
            return isBasic() && isAdvanced();
        }


        private CryptDiscipline(Parcel in) {

            name = in.readString();
            basic = in.readByte() != 0;
            advanced = in.readByte() != 0;


        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeString(name);
            dest.writeByte((byte) (basic ? 1 : 0));
            dest.writeByte((byte) (advanced ? 1 : 0));
        }

        public static final Parcelable.Creator<FilterState.CryptDiscipline> CREATOR
                = new Parcelable.Creator<FilterState.CryptDiscipline>() {
            public FilterState.CryptDiscipline createFromParcel(Parcel in) {
                return new FilterState.CryptDiscipline(in);
            }

            public FilterState.CryptDiscipline[] newArray(int size) {
                return new FilterState.CryptDiscipline[size];
            }
        };
    }

}
