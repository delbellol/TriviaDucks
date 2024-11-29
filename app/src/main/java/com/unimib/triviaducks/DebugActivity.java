package com.unimib.triviaducks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class DebugActivity extends AppCompatActivity {
    private QuizData answer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_debug);
        Button test = findViewById(R.id.test);


        test.setOnClickListener(view -> {
            new Thread(() -> {
                try {
                    buttonClick();

                    runOnUiThread(() -> {
                        Log.d("DebugActivity", answer.toString());
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Log.d("DebugActivity", "Errore: " + e.getMessage());});
                }
            }).start();
        });

    }
    private void buttonClick () throws Exception{
        String result = "";

        URL url = new URL("https://opentdb.com/api.php?amount=10");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                result += line;
            }
            jsonToObject(result);
        }
    }

    private void jsonToObject (String json) throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        answer = objectMapper.readValue(json, QuizData.class);
    }
}