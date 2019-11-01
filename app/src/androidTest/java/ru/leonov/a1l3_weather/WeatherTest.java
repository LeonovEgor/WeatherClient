package ru.leonov.a1l3_weather;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import ru.leonov.a1l3_weather.Storages.SettingsHelper;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class WeatherTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("ru.leonov.a1l3_weather", appContext.getPackageName());
    }

    @Test
    public void getCitesStringFromList_test() {
        List<String> list = Arrays.asList("city1", "city2", "city3");
        String requiredResult = "city1,city2,city3";
        String test = SettingsHelper.getCitesStringFromList(list);

        assertEquals(test, requiredResult);
    }

    @Test
    public void getCityListFromString_test() {
        List<String> requeredList = Arrays.asList("city1", "city2", "city3");
        String cities = "city1,city2,city3";
        List<String> test = SettingsHelper.getCityListFromString(cities);

        assertArrayEquals(requeredList.toArray(), test.toArray());
    }
}
