package com.example.socalbeachesforlife;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

import com.example.socalbeachesforlife.parsers.RestaurantParser;
import com.google.android.gms.common.internal.Asserts;

import java.util.HashMap;
import java.util.Map;

public class RestaurantParserTest {
    JSONArray jsonArray = new JSONArray();

    JSONObject getRest(String name, String rating, String open, String lat, String lng) throws JSONException {
        JSONObject beach = new JSONObject();
        beach.put("name", name);
        beach.put("rating", rating);

        JSONObject opening = new JSONObject();
        opening.put("open_now", open);
        beach.put("opening_hours", opening);

        JSONObject location = new JSONObject();
        location.put("lat", lat);
        location.put("lng", lng);

        JSONObject geometry = new JSONObject();
        geometry.put("location", location);
        beach.put("geometry", geometry);
        return beach;
    }


    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testValidPlace() throws JSONException {
        HashMap<String, String> rest = new HashMap<String, String>() {{
            put("lat", "0.0");
            put("lng", "0.0");
            put("name", "name");
            put("open", "open");
            put("rating", "5");
        }};
        JSONObject jsonRest = getRest("name", "5", "open", "0.0", "0.0");
        RestaurantParser rParser = new RestaurantParser();
        HashMap<String,String> testing = new HashMap<String,String>();
        testing = rParser.getPlace(jsonRest);
        assertEquals(testing, rest);
    }

    @Test
    public void testNullRating() throws JSONException {
        HashMap<String,String> testing = new HashMap<String,String>();
        try {
            JSONObject jsonRest = getRest("name", null, "open", "0.0", "0.0");
            RestaurantParser dataParser = new RestaurantParser();
            testing = dataParser.getPlace(jsonRest);
        } catch (JSONException e){
            assertNull(testing);
        }
    }

    @Test
    public void testNullOpening() throws JSONException {
        HashMap<String,String> testing = new HashMap<String,String>();
        try {
            JSONObject jsonRest = getRest("name", "5", null, "0.0", "0.0");
            RestaurantParser dataParser = new RestaurantParser();
            testing = dataParser.getPlace(jsonRest);
        } catch (JSONException e){
            assertNull(testing);
        }
    }

    @Test
    public void testNullLat() throws JSONException {
        HashMap<String,String> testing = new HashMap<String,String>();
        try {
            JSONObject jsonRest = getRest("name", "5", "open", null, "0.0");
            RestaurantParser dataParser = new RestaurantParser();
            testing = dataParser.getPlace(jsonRest);
        } catch (JSONException e){
            assertNull(testing);
        }
    }

    @Test
    public void testNullLng() throws JSONException {
        HashMap<String,String> testing = new HashMap<String,String>();
        try {
            JSONObject jsonRest = getRest("name", "5", "open", "0.0", null);
            RestaurantParser dataParser = new RestaurantParser();
            testing = dataParser.getPlace(jsonRest);
        } catch (JSONException e){
            assertNull(testing);
        }
    }

    @Test
    public void testNullName() throws JSONException {
        HashMap<String,String> testing = new HashMap<String,String>();
        try {
            JSONObject jsonRest = getRest(null, "5", "open", "0.0", null);
            RestaurantParser dataParser = new RestaurantParser();
            testing = dataParser.getPlace(jsonRest);
        } catch (JSONException e){
            assertNull(testing);
        }
    }
}
