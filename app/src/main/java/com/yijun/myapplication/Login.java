package com.yijun.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yijun.myapplication.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import static com.yijun.myapplication.utils.Utils.BASE_URL;
import static com.yijun.myapplication.utils.Utils.PREFERENCES_NAME;

public class Login extends AppCompatActivity {
EditText editEmail;
EditText editPasswd;
Button btnLogin;
Button btnSignup;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editEmail = findViewById(R.id.editEmail);
        editPasswd = findViewById(R.id.editPasswd);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editEmail.getText().toString().trim();
                String passwd = editPasswd.getText().toString().trim();
                if (email.contains("@")==false){
                    Toast.makeText(Login.this,"이메일이 아닙니다",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(passwd.isEmpty()||passwd.length()<4 || passwd.length()>12){
                    Toast.makeText(Login.this,
                            "비밀번호 길이는 4이상 12 이하로 하셈",Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject  object = new JSONObject();
                try {
                    object.put("email",email);
                    object.put("passwd",passwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        Utils.BASE_URL + "/api/v1/sns_users/login",
                        object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("로그인", response.toString());

                                try {
                                    String token = response.getString("token");
                                    sp = getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
                                    SharedPreferences.Editor editor= sp.edit();
                                    editor.putString("token", token);
                                    editor.apply();

                                    Intent i = new Intent(Login.this,WelcomActivity.class);
                                    startActivity(i);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // 요기요
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("로그인", error.toString());
                            }
                        }

                );

                Volley.newRequestQueue(Login.this).add(request);
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
