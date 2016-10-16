package com.example.ivanildo.appbus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private Camera mCamera;
    private boolean lFlashLigado = false;
    Button btled;
    private TextToSpeech tts;
    private static String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    String URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btled = (Button) findViewById(R.id.Led);
        tts = new TextToSpeech(this, this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Carregando");
        pDialog.setCancelable(false);
    }

    @Override
    protected void onStart(){
        super.onStart();
        //put here time 1000 milliseconds=1 second
            makeJsonArrayRequest();
    }

    private void ligaDesligaFlash(boolean lDesliga) {
        mCamera = Camera.open();
        if (lFlashLigado) {
            if (mCamera != null) {
                Camera.Parameters params = mCamera.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(params);
                mCamera.startPreview();
                lFlashLigado = false;
                LiberaCamera();
            }
        } else {
            if (mCamera != null) {
                Camera.Parameters params = mCamera.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(params);
                mCamera.startPreview();

                lFlashLigado = true;
            }
        }
    }

    public String getHoraAtual() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Calendar.getInstance().getTime());
        calendar.add(Calendar.SECOND, -1);
        String ab= String.valueOf(calendar.getTimeInMillis());
        return ab;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void LiberaCamera() {
        mCamera.release();
        mCamera = null;
    }

    public void FalacomLuz() {
        speakOut("8700-10, TERMINAL CAMPO LIMPO, Embarque Agora");
        if (lFlashLigado) {
            LiberaCamera();
        }
            ligaDesligaFlash(lFlashLigado);
    }
    public void SinalApenas(){
        if (lFlashLigado) {
            LiberaCamera();
        }
        ligaDesligaFlash(lFlashLigado);
    }

    // Metodos para Fala de Texto
    @Override
    public void onDestroy() {
        // Don't forget to shutdown!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        // TODO Auto-generated method stub

        if (status == TextToSpeech.SUCCESS) {

            Locale locale = new Locale("PT", "BR");

            int result = tts.setLanguage(locale);

            //tts.setPitch(5); // set pitch level

            tts.setSpeechRate(1); // set speech speed rate

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported");
            }
        } else {
            Log.e("TTS", "Initilization Failed");
        }
    }

    private void speakOut(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }// Fim de Falas


    private boolean makeJsonArrayRequest() {
        showpDialog();
        URL = "http://api.hackathon.konkerlabs.net:80/sub/noj92kl4it7s/Android?offset=" + getHoraAtual();
        Toast.makeText(getApplicationContext(),
                URL,
                Toast.LENGTH_LONG).show();
        JsonArrayRequest req = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            String store = "";
                            int status = 0;
                            for (int i = 0; i < response.length(); i++) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                JSONObject person = (JSONObject) response.get(i);
                                JSONObject posts = person.getJSONObject("data");

                                store=posts.getString("status");

                            }
                            Toast.makeText(getApplicationContext(),
                                    "MSG " +store,
                                    Toast.LENGTH_LONG).show();
                            if(!store.equals("")){
                                status=Integer.parseInt(store);
                            }
                            if(status==3){
                                FalacomLuz();
                            }
                            else if(status==2){
                                SinalApenas();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Erro",
                                    Toast.LENGTH_LONG).show();
                        }
                        hidepDialog();
                        makeJsonArrayRequest();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Erro"+error.getMessage(),
                        Toast.LENGTH_LONG).show();
                hidepDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credential = "noj92kl4it7s:urUAxHBIIzmD";
                String auth = "Basic " + Base64.encodeToString(credential.getBytes(), Base64.DEFAULT);
                headers.put("Authorization", auth);
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(req);
            return true;
    }

    private void showpDialog() {
        if (!pDialog.isShowing()) {
            pDialog.
            pDialog.show();

            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                }
            }, 0, 3000);
        }
    }
    private void hidepDialog() {
        if (pDialog.isShowing()){
            pDialog.dismiss();
        }
    }
}
