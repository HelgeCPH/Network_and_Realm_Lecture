package dk.itu.helge.textreaderjava;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import dk.itu.helge.textreaderjava.model.TranslationModel;
import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private Resources resources;
    private TextView textView;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Realm (just once per application)
        Realm.init(this);

        // Get a Realm instance for the main thread
        realm = Realm.getDefaultInstance();
        Log.i(TAG, "This comes from the DB:");
        RealmResults<TranslationModel> results = realm.where(TranslationModel.class).findAll();
        Log.i(TAG, results.toString());

        //get the application's resources
        resources = getResources();
        String completeText = "";
        try {
            // completeText = loadTxtFile("mobydick", true);
            completeText = loadTxtFile("mobydick.txt", false);
        } catch (IOException e) {
            // display an error toast message
            Toast toast = Toast.makeText(this, "File: not found!", Toast.LENGTH_LONG);
            toast.show();
        }

        textView = findViewById(R.id.text_view);

        textView.setText(completeText);

        // This solution is based on:
        //  * https://stackoverflow.com/a/52765047/9401646
        //  * https://stackoverflow.com/a/45701223
        CustomActionModeCallback callback = new CustomActionModeCallback(this);
        textView.setCustomSelectionActionModeCallback(callback);
    }

    public String getSelection() {
        final int selectionStart = textView.getSelectionStart();
        final int selectionEnd = textView.getSelectionEnd();

        final CharSequence selectedText = textView.getText().subSequence(selectionStart, selectionEnd);
        return selectedText.toString();
    }


    /**
     * Load the contents of a text file from the application's res/raw folder or assets folder.
     *
     * @param  fileName Name of the text file in the raw or assets folder
     * @param  loadFromRawFolder indicator if text file shall be read from raw folder (True) or from
     *                           assets folder (False)
     * @return Returns the contents of the text file as String
     */
    public String loadTxtFile(String fileName, boolean loadFromRawFolder) throws IOException {
        // Create an InputStream to read the file into
        InputStream inputStream;

        if (loadFromRawFolder) {
            // get the resource id from the file name
            int resourceID = resources.getIdentifier("dk.itu.helge.textreaderjava:raw/" + fileName,
                    null, null);
            // get the file as a stream
            inputStream = resources.openRawResource(resourceID);
        } else {
            // get the file as a stream
            inputStream = resources.getAssets().open(fileName);
        }

        // create a buffer that has the same size as the InputStream
        byte[] buffer = new byte[inputStream.available()];
        // read the text file as a stream, into the buffer
        inputStream.read(buffer);
        // create an output stream to write the buffer into
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // write this buffer to the output stream
        outputStream.write(buffer);
        // close the input and output streams
        outputStream.close();
        inputStream.close();

        // return the output stream as a String
        return outputStream.toString();
    }
}
