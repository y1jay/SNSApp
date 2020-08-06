package com.yijun.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
EditText edit_email;
EditText edit_passwd;
EditText edit_check_passwd;
Button btn_signup;

String baseUrl = "http://snsserver-env.eba-dmyzzcij.ap-northeast-2.elasticbeanstalk.com";

RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        edit_email = findViewById(R.id.edit_emial);
        edit_passwd = findViewById(R.id.edit_passwd);
        edit_check_passwd = findViewById(R.id.edit_check_passwd);
        btn_signup = findViewById(R.id.btn_signup);



        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = edit_email.getText().toString().trim();
                final String passwd = edit_passwd.getText().toString().trim();
                String check_passwd = edit_check_passwd.getText().toString().trim();
                // 클라이언트에서 1차적으로 체크, 서버에서 2차적으로 체크한다 보안을 위해
                if(email.contains("@")==false){
                    Toast.makeText(MainActivity.this,
                            "이메일 아니자나",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwd.length()<4 || passwd.length()>12){
                    Toast.makeText(MainActivity.this,
                            "비밀번호 길이는 4이상 12 이하로 하셈",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwd.equalsIgnoreCase(check_passwd)==false){
                    Toast.makeText(MainActivity.this,
                            "ㅄ",Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject  object = new JSONObject();
                try {
                    object.put("email",email);
                    object.put("passwd",passwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // 서버로 이메일과 비밀번호를 전송한다.
                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        baseUrl + "/api/v1/sns_users",
                        object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("회원가입", response.toString());
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("회원가입",error.toString());
                            }
                        }
                );


                // 네트워크 타고 DB로보내는 것
                requestQueue.add(request);


            }
        });
    }
}