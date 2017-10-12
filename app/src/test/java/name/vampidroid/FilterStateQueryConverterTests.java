package name.vampidroid;

import org.junit.Test;

import name.vampidroid.utils.FilterStateQueryConverter;

import static org.junit.Assert.assertEquals;


/**
 * Created by FranciscoJunior on 08/08/2016.
 */

public class FilterStateQueryConverterTests {

    @Test
    public void check_performance_get_crypt_query() {

        FilterState model = new FilterState();

        model.setDiscipline("for", true, true);
        model.setDiscipline("pre", true, true);
        model.setGroup(1, true);
        model.setClan("Tremere", true);

        long start = System.currentTimeMillis();

        for (int i = 0; i < 10; i++) {
            FilterStateQueryConverter.getCryptFilter(model);
        }


        System.out.println(String.format("time spent (ms): %s", System.currentTimeMillis() - start));

    }


    @Test
    public void check_group_performance() {

        FilterState state = new FilterState();

        state.setGroup(1, true);
        state.setGroup(2, true);
        state.setGroup(3, true);
        state.setGroup(4, true);
        state.setGroup(5, true);
        state.setGroup(6, true);


        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            FilterStateQueryConverter.getCryptFilter(state);
        }

        System.out.println(String.format("time spent (ms): %s", System.currentTimeMillis() - start));

        assertEquals(" and (`group` in ('1','2','3','4','5','6')) and Cast(capacity as integer) between 1 AND 11 order by Name", FilterStateQueryConverter.getCryptFilter(state));

    }


    @Test
    public void check_group_set_1() {

        FilterState state = new FilterState();

        state.setGroup(1, true);

        assertEquals(" and (`group` in ('1')) and Cast(capacity as integer) between 1 AND 11 order by Name", FilterStateQueryConverter.getCryptFilter(state));

    }


    @Test
    public void check_group_set_2() {

        FilterState model = new FilterState();

        model.setGroup(2, true);

        assertEquals("(`group` in ('2'))", FilterStateQueryConverter.getGroupsQuery(model));

    }

    @Test
    public void check_group_set_3() {

        FilterState model = new FilterState();

        model.setGroup(3, true);

        assertEquals("(`group` in ('3'))", FilterStateQueryConverter.getGroupsQuery(model));

    }

    @Test
    public void check_group_set_4() {

        FilterState model = new FilterState();

        model.setGroup(4, true);

        assertEquals("(`group` in ('4'))", FilterStateQueryConverter.getGroupsQuery(model));

    }

    @Test
    public void check_group_set_5() {

        FilterState model = new FilterState();

        model.setGroup(5, true);

        assertEquals("(`group` in ('5'))", FilterStateQueryConverter.getGroupsQuery(model));

    }

    @Test
    public void check_group_set_6() {


        FilterState model = new FilterState();

        model.setGroup(6, true);

        assertEquals("(`group` in ('6'))", FilterStateQueryConverter.getGroupsQuery(model));

    }

    @Test
    public void search_inside_card_name() {

        FilterState model = new FilterState();

        model.setName("Rich");

        assertEquals(" and (lower(Name) like '%Rich%') and Cast(capacity as integer) between 1 AND 11 order by Name", FilterStateQueryConverter.getCryptFilter(model));

    }

    @Test
    public void search_inside_card_name_and_card_text() {

        FilterState model = new FilterState();

        model.setName("Rich");
        model.setSearchInsideCardText(true);

        assertEquals(" and (lower(Name) like '%Rich%' or lower(Text) like '%Rich%') and Cast(capacity as integer) between 1 AND 11 order by Name", FilterStateQueryConverter.getCryptFilter(model));

    }

}


