package name.vampidroid.utils;

import android.util.Log;

import name.vampidroid.FilterState;

/**
 * Created by fxjr on 27/12/16.
 */

public class FilterStateQueryConverter {

    private static final String TAG = "FilterStateTranslator";

    private static final String GROUP_CRYPT_FILTER = "(_group in (?))";

    private static final String CAPACITY_CRYPT_FILTER = "Cast(capacity as integer) between ";
    private static final String CLAN_FILTER = "Clan = '?'";

    private static final String TYPE_FILTER = "Type = '?'";
    private static final String DISCIPLINE_CRYPT_FILTER = "Disciplines like '%?%'";
    private static final String DISCIPLINE_CRYPT_FILTER_BOTH_BASIC_ADVANCED = "lower(Disciplines) like '%?%'";


    private static final String[] GROUP_STRING_MAP = {"'ANY',", "'1',", "'2',", "'3',", "'4',", "'5',", "'6',"};



    private static final String DISCIPLINE_LIBRARY_FILTER = "Discipline like '%?%'";

    public static String getCryptFilter(FilterState filterState) {

        return getNameFilterQuery(filterState) + getCryptFilterQuery(filterState) + getOrderBy();

    }

    public static String getLibraryFilter(FilterState filterState) {

        return getNameFilterQuery(filterState) + getLibraryFilterQuery(filterState) + getOrderBy();

    }


    static String getNameFilterQuery(FilterState filterState) {
        // TODO Auto-generated method stub

        if (filterState.getName().length() == 0) {
            return "";
        }

        StringBuilder sbNameFilter = new StringBuilder();
        sbNameFilter.append("'%").append(filterState.getName().toString().trim().replace(' ' , '%').replace("'" , "''")).append("%'");

        String nameFilter = sbNameFilter.toString();

        if (filterState.isSearchInsideCardText()) {
            return " and (lower(Name) like " + nameFilter + " or lower(CardText) like " + nameFilter + ")";
        } else {
            return " and (lower(Name) like " + nameFilter + ")";
        }

    }

    static String getCryptFilterQuery(FilterState filterState) {

        StringBuilder result = new StringBuilder();

        String groupsQuery = getGroupsQuery(filterState);

        // Groups processing
        if (groupsQuery.length() > 0) {
            result.append(" and ");
            result.append(groupsQuery);

        }


        // Capacity processing

        result.append(" and ");
        result.append(CAPACITY_CRYPT_FILTER).append(filterState.getCapacityMin()).append(" AND ").append(filterState.getCapacityMax());


        // Clans processing

        if (filterState.getClans().size() > 0) {
            result.append(" and ( ");

            for (CharSequence clan : filterState.getClans()) {
                result.append(CLAN_FILTER.replace("?", clan)).append(" OR ");
            }


            // To avoid needing to remove the last 'OR'
            result.append(" 1 = 0 ) ");
        }


        // Disciplines processing

        if (filterState.getCryptDisciplines().size() > 0) {

            result.append(" and ( ");

            for (FilterState.CryptDiscipline discipline : filterState.getCryptDisciplines().values())
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

    static String getLibraryFilterQuery(FilterState filterState) {

        StringBuilder result = new StringBuilder();

        if (filterState.getCardTypes().size() > 0) {
            result.append(" and ( ");

            for (CharSequence cardType : filterState.getCardTypes()) {

                result.append(TYPE_FILTER.replace("?", cardType)).append(" OR ");
            }


            // To avoid needing to remove the last 'OR'
            result.append(" 1 = 0 ) ");
        }

        if (filterState.getLibraryDisciplines().size() > 0) {
            result.append(" and ( ");

            for (CharSequence libraryDiscipline : filterState.getLibraryDisciplines()) {

                result.append(DISCIPLINE_LIBRARY_FILTER.replace("?", libraryDiscipline)).append(" AND ");
            }


            // To avoid needing to remove the last 'OR'
            result.append(" 1 = 1 ) ");
        }

        Log.d(TAG, "getLibraryFilterQuery() returned: " + result);
        return result.toString();
    }

    static String getGroupsQuery(FilterState filterState) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < filterState.getGroups().length; i++) {
            if (filterState.getGroups()[i]) {
                result.append(GROUP_STRING_MAP[i]);
            }

        }

        // Remove last comma, if any.
        int commaIndex = result.lastIndexOf(",");

        if (commaIndex != -1) {
            result.deleteCharAt(commaIndex);
            return GROUP_CRYPT_FILTER.replace("?", result.toString());
        } else {
            return "";
        }

    }

    static String getOrderBy() {
        return " order by Name";
    }
}
