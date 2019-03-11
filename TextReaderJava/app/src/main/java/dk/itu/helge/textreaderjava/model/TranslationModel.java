package dk.itu.helge.textreaderjava.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class TranslationModel extends RealmObject {

    //    {
    //        "translations" : [ {
    //        "translation" : "Cuénteme el significado de vivir"
    //    } ],
    //        "word_count" : 6,
    //            "character_count" : 27
    //    }
    @PrimaryKey
    private String text;
    private int wordCount;
    private int characterCount;
    private RealmList<String> translations;


    public String getText() {
        return text;
    }

    public int getWordCount() {
        return wordCount;
    }

    public int getCharacterCount() {
        return characterCount;
    }

    public RealmList<String> getTranslations() {
        return translations;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public void setCharacterCount(int characterCount) {
        this.characterCount = characterCount;
    }

    public void setTranslations(RealmList<String> translations) {
        this.translations = translations;
    }
}
