package com.example.socalbeachesforlife.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RouteParser {
    private HashMap<String, String> getRoute(JSONObject googleRoutesJson)
    {
        HashMap<String, String> googleRoutesMap = new HashMap<>();
        String duration = "";

        try {
            duration = googleRoutesJson.getJSONArray("legs").getJSONObject(0).getJSONObject("duration").getString("value");

            googleRoutesMap.put("duration", duration);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return googleRoutesMap;
    }

    private List<HashMap<String, String>> getRoutes(JSONArray jsonArray)
    {
        int count = jsonArray.length();
        List<HashMap<String, String>> routelist = new ArrayList<>();
        HashMap<String, String> routeMap = null;

        for(int i = 0; i < count; i++) {
            try {
                routeMap = getRoute((JSONObject) jsonArray.get(i));
                routelist.add(routeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return routelist;
    }

    public List<HashMap<String, String>> parse(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("routes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getRoutes(jsonArray);
    }
}
