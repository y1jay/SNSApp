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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yijun.myapplication.api.NetworkClient;
import com.yijun.myapplication.api.UserApi;
import com.yijun.myapplication.model.UserReq;
import com.yijun.myapplication.model.UserRes;
import com.yijun.myapplication.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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

                UserReq userReq = new UserReq(email,passwd);

                Retrofit retrofit = NetworkClient.getRetrofitClient(Login.this);
                UserApi userApi = retrofit.create(UserApi.class);

                Call<UserRes> call = userApi.loginUser(userReq);

                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        if (response.isSuccessful()){
                            // response.body() 가 UserRes.이다.
                            boolean success = response.body().isSuccess();
                            String token = response.body().getToken();
                            Log.i("AAAA","success : "+success +" token : " + token);

                            SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("token",token);
                            editor.apply();

                            Intent i = new Intent(Login.this,WelcomActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {
                    }
                });

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
