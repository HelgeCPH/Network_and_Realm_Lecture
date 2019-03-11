package dk.itu.helge.textreaderjava;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.util.Base64;

import dk.itu.helge.textreaderjava.model.TranslationModel;
import io.realm.RealmList;


public class HTTPRequester {

    private static final String BASE_URL = "https://gateway-lon.watsonplatform.net/language-translator/api/v3/translate?version=2018-05-01";
    private static final String API_KEY = "-BV_YJRiNag-61Gb99oGLZFqV8tiXzckdO0zB-hvbOMz";

    /**
     * This code comes from chapter 25 in _Android Programming The Big Nerd Ranch Guide 3rd Edition_
     * It was take from the corresponding repository at
     * https://github.com/rsippl/AndroidProgramming3e/blob/master/25_HTTPBackgroundTasks/java/PhotoGallery/app/src/main/java/com/bignerdranch/android/photogallery/FlickrFetchr.java

     * @param urlString
     * @param postData
     * @return
     * @throws IOException
     */
    public byte[] getUrlBytes(String urlString, String postData) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Length", "" + postData.getBytes().length);

        String userCredentials = "apikey:" + HTTPRequester.API_KEY;
        String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(),
                                      Base64.DEFAULT);
                //Base64.getEncoder().encodeToString(userCredentials.getBytes());
        connection.setRequestProperty("Authorization", basicAuth);


        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write(postData.getBytes());
        os.flush();
        os.close();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {

                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlString);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }


    public String sendTranslationRequest(String textToTranslate) {
        String jsonString = "{\"translations\": [{\"translation\" : \"???\"}]}";

        String cleanOriginal = textToTranslate.replaceAll("\\s+","");
        if (!cleanOriginal.isEmpty()) {
            String queryString = "{\"text\":[\"" + textToTranslate + "\"],\"model_id\":\"en-es\"}";
            try {
                jsonString = new String(getUrlBytes(BASE_URL, queryString));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonString;
    }
}


