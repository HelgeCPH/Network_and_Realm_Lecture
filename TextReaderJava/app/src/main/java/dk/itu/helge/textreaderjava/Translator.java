package dk.itu.helge.textreaderjava;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dk.itu.helge.textreaderjava.model.TranslationModel;
import io.realm.Realm;
import io.realm.RealmList;


public class Translator {

    public static List<String> getTranslations(String text) {

        Realm realm = Realm.getDefaultInstance();
        List<String> translations = new ArrayList<>();

        // check if the text was already translated earlier...
        TranslationModel document = realm.where(TranslationModel.class).equalTo("text", text).findFirst();
        if (document != null) {
            RealmList<String> localTranslations = document.getTranslations();
            translations.addAll(localTranslations);
        } else {
            // if not, let IBM Watson translate it for us...
            String remoteTranslation = new HTTPRequester().sendTranslationRequest(text);

            // Create entry otherwise...
            TranslationModel tm = Translator.createModel(text, remoteTranslation);

            // push it to the DB
            realm.beginTransaction();
            realm.copyToRealm(tm);
            realm.commitTransaction();

            RealmList<String> remoteTranslations = tm.getTranslations();
            translations.addAll(remoteTranslations);
            realm.close();
        }

        return translations;
    }

    public static String getTranslation(String text) {
        return Translator.getTranslations(text).get(0);
    }

    private static TranslationModel createModel(String originalText, String jsonResponse) {
        TranslationModel tm = new TranslationModel();
        try {
            JSONObject jsonBody = new JSONObject(jsonResponse);
            JSONArray translationsFromResponse = jsonBody.getJSONArray("translations");
            int characterCount = jsonBody.getInt("character_count");
            int wordCount = jsonBody.getInt("word_count");

            RealmList<String> translations = new RealmList<>();
            for (int i = 0; i < translationsFromResponse.length(); i++) {
                JSONObject translationJsonObject = translationsFromResponse.getJSONObject(i);

                String translation = translationJsonObject.getString("translation");
                translations.add(translation);
            }

            tm.setText(originalText);
            tm.setCharacterCount(characterCount);
            tm.setWordCount(wordCount);
            tm.setTranslations(translations);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tm;
    }
}
