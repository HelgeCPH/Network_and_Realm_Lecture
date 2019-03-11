package dk.itu.helge.httpgetvolley;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private TextView textView;
    private static String urlString = "https://httpbin.org/uuid";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);

        httpGet(urlString);
    }

    public void httpGet(String urlString) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringReq = new StringRequest(Request.Method.GET, urlString,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    textView.setText(response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG,"Error :" + error.toString());
                }
            });
        queue.add(stringReq);
    }
}
