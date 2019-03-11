package dk.itu.helge.textreaderjava;

import android.content.Context;
import android.os.AsyncTask;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class CustomActionModeCallback implements ActionMode.Callback {

    private final Context context;
    private String textToTranslate;

    public CustomActionModeCallback(Context context) {
        this.context = context;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        menu.clear();
        mode.getMenuInflater().inflate(R.menu.translate_ctx_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.translate_item_1) {

            if (context instanceof MainActivity) {
                MainActivity contextActivity = (MainActivity) context;
                textToTranslate = contextActivity.getSelection();

                // This is what you might want to do but it will
                // HTTPRequester translationRequester = new HTTPRequester();
                // txt = translationRequester.sendTranslationRequest(txt);

                new TranslationTask().execute();
                // Toast.makeText(context, translatedText, Toast.LENGTH_LONG).show();
            }
            mode.finish();
            return true;
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    private class TranslationTask extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... params) {
            return Translator.getTranslation(textToTranslate);
        }

        @Override
        protected void onPostExecute(String translatedText) {
            Toast.makeText(context, translatedText, Toast.LENGTH_LONG).show();
        }
    }
}