package com.example.demobai2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LINK_GET = "https://jsonplaceholder.typicode.com/todos/1";
    private static final String LINK_POST = "https://jsonplaceholder.typicode.com/posts";
    private TextView tvShow;
    private Button btnGet, btnPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        tvShow = findViewById(R.id.tvShow);
        btnGet = findViewById(R.id.btnGet);
        btnPost = findViewById(R.id.btnPost);

        btnGet.setOnClickListener(this);
        btnPost.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGet:
                new MyLoader().execute(LINK_GET, "get");
                break;
            case R.id.btnPost:
                new MyLoader().execute(LINK_POST, "post");
                break;
        }
    }

    class MyLoader extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            if (strings[1] == "get") {
                Log.e("get", strings[0]);
                return getHttp(strings[0]);
            } else {
                return postHttp(strings[0]);
            }
        }

        @Override
        protected void onPostExecute(final String data) {
            super.onPostExecute(data);
            tvShow.setText(data);
        }

        private String postHttp(String string) {
            try {
                URL url = new URL(string);
                Log.e("link", string);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                StringBuilder params = new StringBuilder();

                params.append("title");
                params.append("=");
                params.append("Hello");

                params.append("&");
                params.append("body");
                params.append("Hoàng");

                params.append("&");
                params.append("userId");
                params.append("=");
                params.append("0311");

                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                //đưa params vào body của request
                writer.append(params);

                //giải phóng bộ nhớ
                writer.flush();

                //kết thúc truyền dữ liệu vào output
                writer.close();
                os.close();

                //Lấy dữ liệu trả về
                StringBuilder res = new StringBuilder();

                int resCode = httpURLConnection.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    while ((line = br.readLine()) != null)
                        res.append(line);
                } else if (resCode == 201) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    while ((line = br.readLine()) != null)
                        res.append(line);
                } else {
                    return httpURLConnection.getResponseCode() + " ";
                }

                return res.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        private String getHttp(String link) {
            try {
                URL url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                String data = "";

                Scanner sc = new Scanner(inputStream);
                while (sc.hasNext()) {
                    data = data + sc.nextLine();
                }
                sc.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


}
