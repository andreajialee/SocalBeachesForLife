package com.example.socalbeachesforlife;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

import android.provider.ContactsContract;

import com.example.socalbeachesforlife.parsers.DataParser;
import com.google.android.gms.common.internal.Asserts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParserTest {
    JSONArray jsonArray = new JSONArray();

    JSONObject getBeach(String name, String vicinity, String reference, String lat, String lng) throws JSONException {
        JSONObject beach = new JSONObject();
        beach.put("name", name);
        beach.put("vicinity", vicinity);
        beach.put("reference", reference);

        JSONObject location = new JSONObject();
        location.put("lat", lat);
        location.put("lng", lng);

        JSONObject geometry = new JSONObject();
        geometry.put("location", location);
        beach.put("geometry", geometry);
        return beach;
    }

    public void initializeJSON() throws JSONException {
        JSONObject beach1 = getBeach("beach1", "vicinity", "reference", "0.0", "0.0");
        jsonArray.put(0,beach1);
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testValidPlace() throws JSONException {
        HashMap<String, String> beachanswer  = new HashMap<String, String>() {{
            put("place_name", "beach1");
            put("vicinity", "vicinity");
            put("reference", "reference");
            put("lat", "0.0");
            put("lng", "0.0");
        }};
        JSONObject jsonBeach = getBeach("beach1", "vicinity", "reference", "0.0", "0.0");

        DataParser dataParser = new DataParser();
        HashMap<String,String> testing = new HashMap<String,String>();
        testing = dataParser.getPlace(jsonBeach);
        assertEquals(testing, beachanswer);
    }

    @Test
    public void testNullName() throws JSONException {
        HashMap<String,String> testing = new HashMap<String,String>();
        try {
            JSONObject beach = getBeach(null, "vicinity", "reference", "0.0", "0.0");
            DataParser dataParser = new DataParser();
            testing = dataParser.getPlace(beach);
        } catch (JSONException e){
            assertNull(testing);
        }
    }

    @Test
    public void testNullVicinity() throws JSONException {
        HashMap<String,String> testing = new HashMap<String,String>();
        try {
            JSONObject beach = getBeach("name", null, "reference", "0.0", "0.0");
            DataParser dataParser = new DataParser();
            testing = dataParser.getPlace(beach);
        } catch (JSONException e){
            assertNull(testing);
        }
    }

    @Test
    public void testGetPlacesValid() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject beach1 = getBeach("beach1", "vicinity", "reference", "0.0", "0.0");
        JSONObject beach2 = getBeach("beach2", "vicinity2", "reference2", "1.0", "1.0");
        jsonArray.put(0,beach1);
        jsonArray.put(1,beach2);

        HashMap<String, String> beachm1  = new HashMap<String, String>() {{
            put("place_name", "beach1");
            put("vicinity", "vicinity");
            put("reference", "reference");
            put("lat", "0.0");
            put("lng", "0.0");
        }};
        HashMap<String, String> beachm2  = new HashMap<String, String>() {{
            put("place_name", "beach2");
            put("vicinity", "vicinity2");
            put("reference", "reference2");
            put("lat", "1.0");
            put("lng", "1.0");
        }};
        List<HashMap<String, String>> correct = new ArrayList<>();
        correct.add(0,beachm1);
        correct.add(1,beachm2);

        DataParser dataParser = new DataParser();
        assertEquals(correct, dataParser.getPlaces(jsonArray));
    }

}
