package com.sebasdev.gravilitytest.util;

import android.util.Log;

import com.sebasdev.gravilitytest.model.App;
import com.sebasdev.gravilitytest.model.Category;
import com.sebasdev.gravilitytest.network.ServiceRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by m4605 on 19/03/16.
 */
public class DataManager {

    private static final String SERVICE_URL = "https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json";
    private static ArrayList<App> apps = null;
    private static ArrayList<Category> categories = null;

    public static ArrayList<App> getApps() {

        return apps;
    }

    public static ArrayList<App> getAppsByCategory(Category category) {

        ArrayList<App> appsByCategory = new ArrayList<>();

        if (apps != null) {

            for (App app : apps) {

                if (app.getCategory().equals(category)){
                    appsByCategory.add(app);
                }
            }

            return appsByCategory;

        } else {
            return null;
        }
    }

    public static ArrayList<Category> getCategories() {

        return categories;
    }

    public static Category getCategory(int id) {
        for (Category category : categories) {
            if (category.getId() == id) {
                return category;
            }
        }
        return null;
    }

    public static boolean isCategory(int id) {
        for (Category category : categories) {
            if (category.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public static void getServiceData() {
        // TODO: 19/03/16 procesar json
        // TODO: 19/03/16 validar si hay conexion para obtenerlos de internet o de la bd

        ServiceRequest request = new ServiceRequest();
        try {
            String result = request.requestString(SERVICE_URL);

            JSONObject jsonResult = new JSONObject(result);

            JSONObject feed = jsonResult.getJSONObject("feed");
            JSONArray entry = feed.getJSONArray("entry");

            apps = new ArrayList<>();
            categories = new ArrayList<>();

            // procesar el json
            for (int i=0; i<entry.length(); i++) {

                App app = new App();
                JSONObject appJson = entry.getJSONObject(i);

                JSONObject appName = appJson.getJSONObject("im:name");
                app.setName(appName.getString("label"));

                JSONArray imgArray = appJson.getJSONArray("im:image");
                app.setImage(getUrlImage(imgArray));

                JSONObject appSummary = appJson.getJSONObject("summary");
                app.setDescription(appSummary.getString("label"));

                JSONObject appPriceAttributes = appJson.getJSONObject("im:price").getJSONObject("attributes");
                app.setPrice(appPriceAttributes.getString("currency") + " " + appPriceAttributes.getString("amount"));

                JSONObject appAuthor = appJson.getJSONObject("im:artist");
                app.setAuthor(appAuthor.getString("label"));

                JSONObject catAttributes = appJson.getJSONObject("category").getJSONObject("attributes");
                int idCategory = Integer.parseInt(catAttributes.getString("im:id"));

                // set the category if it exists, else create a new category
                if (isCategory(idCategory)) {
                    app.setCategory(getCategory(idCategory));

                } else {
                    Category category = new Category();

                    category.setId(idCategory);
                    category.setLabel(catAttributes.getString("label"));

                    app.setCategory(category);

                    categories.add(category);
                }

                apps.add(app);
            }

        } catch (IOException e) {
//            e.printStackTrace();
            Log.d("DataManager", "Error al obtener respuesta del servidor");
        } catch (JSONException e) {
//            e.printStackTrace();
            Log.d("DataManager", "Error al procesar la respuesta, parece que no es un JSON valido");
        }
    }

    // obtiene la url de la imagen
    private static String getUrlImage(JSONArray imgArray) throws JSONException {

        for (int i=0; i<imgArray.length(); i++) {

            JSONObject imgObj = imgArray.getJSONObject(i);
            JSONObject imgAttributes = imgObj.getJSONObject("attributes");

            if (imgAttributes.getString("height").equals("100")) {
                return imgObj.getString("label");
            }
        }

        return imgArray.getJSONObject(0).getString("label");
    }
}