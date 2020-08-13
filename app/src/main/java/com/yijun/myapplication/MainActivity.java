package com.yijun.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yijun.myapplication.api.NetworkClient;
import com.yijun.myapplication.api.UserApi;
import com.yijun.myapplication.model.UserReq;
import com.yijun.myapplication.model.UserRes;
import com.yijun.myapplication.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.yijun.myapplication.utils.Utils.BASE_URL;

public class MainActivity extends AppCompatActivity {
EditText edit_email;
EditText edit_passwd;
EditText edit_check_passwd;
Button btn_signup;
Button btn_login;
    SharedPreferences sp;

RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
        String token = sp.getString("token",null);
        // 자동 로그인 토큰이 있으면 웰컴 화면만
        if (token !=null){
            Intent i = new Intent(MainActivity.this,WelcomActivity.class);
            startActivity(i);
            finish();
        }


        requestQueue = Volley.newRequestQueue(MainActivity.this);

        edit_email = findViewById(R.id.edit_emial);
        edit_passwd = findViewById(R.id.edit_passwd);
        edit_check_passwd = findViewById(R.id.edit_check_passwd);
        btn_signup = findViewById(R.id.btn_signup);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Login.class);
                startActivity(i);
                finish();
            }
        });

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

                // body 셋팅
                UserReq userReq = new UserReq(email, passwd);


                // 서버로 이메일과 비밀번호를 전송한다.
                Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);
                UserApi userApi = retrofit.create(UserApi.class);

                Call<UserRes> call = userApi.createUser(userReq);

                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                       // 상태코드가 200 인지 확인
                        if (response.isSuccessful()){
                            // response.body() 가 UserRes.이다.
                           boolean success = response.body().isSuccess();
                           String token = response.body().getToken();
                           Log.i("AAAA","success : "+success +" token : " + token);

                           SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
                           SharedPreferences.Editor editor = sp.edit();
                           editor.putString("token",token);
                           editor.apply();

                           Intent i = new Intent(MainActivity.this,WelcomActivity.class);
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
    }
}
