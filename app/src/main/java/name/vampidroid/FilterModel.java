package name.vampidroid;

import android.util.Log;

import java.util.ArrayList;

public class FilterModel {

    private static final String TAG = "FilterModel";

    CharSequence name = "";

    boolean searchCardText = false;

    private final String GROUP_CRYPT_FILTER = "(_group = '*' or _group in (?))";

    private final String CAPACITY_CRYPT_FILTER = "Cast(capacity as integer) between ";
    private final String CLAN_FILTER = "Clan = '?'";
    boolean groups[] = new boolean[6];

    boolean groupsFilterChanged;
    String groupsFilterCached = "";
    int capacityMin = 1;

    int capacityMax = 11;
    ArrayList<CharSequence> cardTypes = new ArrayList<>();


    ArrayList<CharSequence> clans = new ArrayList<>();


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


    public void setGroup(CharSequence text, boolean isSet) {
        groups[Integer.parseInt(text.toString()) - 1] = isSet;
        groupsFilterChanged = true;
    }


    public void setName(CharSequence name) {

        if (!name.equals(this.name))
            this.name = name;

    }


    public void setSearchCardText(boolean searchCardText) {
        this.searchCardText = searchCardText;
    }

    public String getNameFilterQuery() {
        // TODO Auto-generated method stub

        if (name.length() == 0)
            return "";
        else if (searchCardText) {
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

        Log.d(TAG, "getCryptFilterQuery() returned: " + result);

        return result.toString();


    }

    public String getLibraryFilterQuery() {
        return "";
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


}

