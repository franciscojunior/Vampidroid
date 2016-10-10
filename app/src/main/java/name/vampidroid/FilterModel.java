package name.vampidroid;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class FilterModel implements Parcelable {

    private static final String TAG = "FilterModel";

    CharSequence name = "";

    boolean searchInsideCardText = false;

    private final String GROUP_CRYPT_FILTER = "(_group = '*' or _group in (?))";

    private final String CAPACITY_CRYPT_FILTER = "Cast(capacity as integer) between ";
    private final String CLAN_FILTER = "Clan = '?'";

    private final String TYPE_FILTER = "Type = '?'";
    private final String DISCIPLINE_CRYPT_FILTER = "Disciplines like '%?%'";
    private final String DISCIPLINE_CRYPT_FILTER_BOTH_BASIC_ADVANCED = "lower(Disciplines) like '%?%'";


    private final String DISCIPLINE_LIBRARY_FILTER = "Discipline like '%?%'";


    boolean groups[] = new boolean[6];

    boolean groupsFilterChanged;
    String groupsFilterCached = "";
    int capacityMin = 1;

    int capacityMax = 11;
    ArrayList<CharSequence> cardTypes = new ArrayList<>();


    ArrayList<CharSequence> clans = new ArrayList<>();

    ArrayList<CharSequence> libraryDisciplines = new ArrayList<>();


    HashMap<CharSequence, CryptDiscipline> cryptDisciplines = new HashMap<>();

//    ArrayList<CryptDiscipline> cryptDisciplines = new ArrayList<>();


    public FilterModel() {

    }



    public String getGroupsQuery() {


        if (groupsFilterChanged) {
            StringBuilder result = new StringBuilder();


            for (int i = 0; i < groups.length; i++) {
                if (groups[i])
                    result.append("'").append(i + 1).append("',");

            }

            // Remove last comma, if any.
            int commaIndex = result.lastIndexOf(",");

            if (commaIndex != -1) {
                result.deleteCharAt(commaIndex);
                groupsFilterCached = GROUP_CRYPT_FILTER.replace("?", result.toString());
            } else {
                groupsFilterCached = "";
            }
        }

        return groupsFilterCached;
    }


    public void setGroup(int group, boolean isSet) {
        groups[group - 1] = isSet;
        groupsFilterChanged = true;
    }


    public void setName(CharSequence name) {

        if (!name.equals(this.name))
            this.name = name;

    }


    public void setSearchInsideCardText(boolean searchInsideCardText) {
        this.searchInsideCardText = searchInsideCardText;
    }

    public String getNameFilterQuery() {
        // TODO Auto-generated method stub

        if (name.length() == 0)
            return "";
        else if (searchInsideCardText) {
            return " and (lower(Name) like '%" + name + "%' or lower(CardText) like '%" + name + "%')";
        } else {
            return " and (lower(Name) like '%" + name + "%')";
        }


    }

    public String getCryptFilterQuery() {

        StringBuilder result = new StringBuilder();

        String groupsQuery = getGroupsQuery();

        // Groups processing
        if (groupsQuery.length() > 0) {
            result.append(" and ");
            result.append(groupsQuery);

        }


        // Capacity processing

        result.append(" and ");
        result.append(CAPACITY_CRYPT_FILTER).append(capacityMin).append(" AND ").append(capacityMax);


        // Clans processing

        if (clans.size() > 0) {
            result.append(" and ( ");

            for (CharSequence clan : clans) {
                result.append(CLAN_FILTER.replace("?", clan)).append(" OR ");
            }


            // To avoid needing to remove the last 'OR'
            result.append(" 1 = 0 ) ");
        }


        // Disciplines processing

        if (cryptDisciplines.size() > 0) {

            result.append(" and ( ");

            for (CryptDiscipline discipline : cryptDisciplines.values())
                if (discipline.isBothSet()) {
                    result.append(DISCIPLINE_CRYPT_FILTER_BOTH_BASIC_ADVANCED.replace("?", discipline.getName().toLowerCase())).append(" AND ");
                } else {
                    result.append(DISCIPLINE_CRYPT_FILTER.replace("?", discipline.getName())).append(" AND ");
                }


            // To avoid needing to remove the last 'OR'
            result.append(" 1 = 1 ) ");

        }


        Log.d(TAG, "getCryptFilterQuery() returned: " + result);

        return result.toString();


    }

    public String getLibraryFilterQuery() {

        StringBuilder result = new StringBuilder();

        if (cardTypes.size() > 0) {
            result.append(" and ( ");

            for (CharSequence cardType : cardTypes) {

                    result.append(TYPE_FILTER.replace("?", cardType)).append(" OR ");
            }


            // To avoid needing to remove the last 'OR'
            result.append(" 1 = 0 ) ");
        }

        if (libraryDisciplines.size() > 0) {
            result.append(" and ( ");

            for (CharSequence libraryDiscipline : libraryDisciplines) {

                result.append(DISCIPLINE_LIBRARY_FILTER.replace("?", libraryDiscipline)).append(" AND ");
            }


            // To avoid needing to remove the last 'OR'
            result.append(" 1 = 1 ) ");
        }

        Log.d(TAG, "getLibraryFilterQuery() returned: " + result);
        return result.toString();
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


    public String getOrderBy() {
        return " order by Name";
    }

    public void setDiscipline(String discipline, boolean isBasic, boolean isSet) {


        Log.d(TAG, "setDiscipline: " + discipline);


        discipline = discipline.toLowerCase();

        CryptDiscipline cryptDiscipline = cryptDisciplines.get(discipline);

        if (isSet) {
            if (cryptDiscipline == null) {
                Log.d(TAG, "setDiscipline: cryptDiscipline is null. Creating one...");
                cryptDiscipline = new CryptDiscipline(discipline, isBasic, !isBasic);
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

    private FilterModel(Parcel in) {

        in.readBooleanArray(groups);
        in.readList(cardTypes, null);
        in.readList(clans, null);
        in.readMap(cryptDisciplines, CryptDiscipline.class.getClassLoader());
        capacityMin = in.readInt();
        capacityMax = in.readInt();
        searchInsideCardText = in.readByte() != 0;

        groupsFilterChanged = true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

//        Log.d(TAG, "FilterModel: groups:" + groups[0] + groups[1] +groups[2] +groups[3] +groups[4] +groups[5]);

        dest.writeBooleanArray(groups);
        dest.writeList(cardTypes);
        dest.writeList(clans);
        dest.writeMap(cryptDisciplines);
        dest.writeInt(capacityMin);
        dest.writeInt(capacityMax);
        dest.writeByte((byte) (searchInsideCardText ? 1 : 0));


    }

    public static final Parcelable.Creator<FilterModel> CREATOR
            = new Parcelable.Creator<FilterModel>() {
        public FilterModel createFromParcel(Parcel in) {
            return new FilterModel(in);
        }

        public FilterModel[] newArray(int size) {
            return new FilterModel[size];
        }
    };


    private static class CryptDiscipline implements Parcelable {

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


        private CryptDiscipline (Parcel in) {

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

        public static final Parcelable.Creator<CryptDiscipline> CREATOR
                = new Parcelable.Creator<CryptDiscipline>() {
            public CryptDiscipline createFromParcel(Parcel in) {
                return new CryptDiscipline(in);
            }

            public CryptDiscipline[] newArray(int size) {
                return new CryptDiscipline[size];
            }
        };
    }

}
