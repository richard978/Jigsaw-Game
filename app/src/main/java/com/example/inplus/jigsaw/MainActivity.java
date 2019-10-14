package com.example.inplus.jigsaw;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    String username = "guest";
    String password = "guest";

    String URL = "http://172.18.157.173:8080/jigsawServer/LogLet";
    String URL_register = "http://172.18.157.173:8080/jigsawServer/RegLet";

    TextView tips;

    // 返回的数据
    //private String responseStr;

    //MyHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tips = (TextView)findViewById(R.id.tips);

        //获取用户名
        final EditText usr = (EditText) findViewById(R.id.username);

        //获取密码
        final EditText pwd = (EditText) findViewById(R.id.password);


        //登录按钮设置监听
        final Button bt_login = (Button) findViewById(R.id.bt_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usr.getText().toString();
                password = pwd.getText().toString();
                Log.d("1111", "onClick: ");
                //LoginHttpClient();
                Intent intent = new Intent(MainActivity.this, MainFrameActivity.class);
                startActivity(intent);
            }
        });

        //忘记密码通过手机短信或邮箱找回密码，此功能省略
        final TextView bt_forget = (TextView) findViewById(R.id.bt_forget);
        bt_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usr.getText().toString();
                password = pwd.getText().toString();
                Log.d("1111", "onClick: ");
                //RegisterHttpClient();
            }
        });

        //注册
        final TextView bt_register = (TextView) findViewById(R.id.bt_register);
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送数据到服务器端
                //接收服务器端返回的信息
                //若注册成功，提示注册成功
                //若注册失败，提示注册失败
                username = usr.getText().toString();
                password = pwd.getText().toString();
                Log.d("1111", "onClick: ");
                RegisterHttpClient();
            }
        });
    }

    // 同样的消息机制
    private Handler handler_login = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tips.setText("");
            if (msg.what == 1) {
                if (msg.obj.toString().equals("true")) {
                    Intent intent =new Intent(MainActivity.this,MainFrameActivity.class);
                    startActivity(intent);
                }
                else
                    tips.setText("用户名或密码错误");

            }
        }
    };

    private void LoginHttpClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String uri = URL + "?username=" + username + "&password=" + password;
                HttpClient client = new DefaultHttpClient(); // HttpClient 是一个接口，无法实例化，所以我们通常会创建一个DefaultHttpClient实例
                HttpGet get = new HttpGet(uri); // 发起GET请求就使用HttpGet，发起POST请求则使用HttpPost，这里我们先使用HttpGet
                try {
                    HttpResponse httpResponse = client.execute(get); // 调用HttpClient对象的execute()方法
                    // 状态码200说明响应成功
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity(); // 取出报文的具体内容
                        String response = EntityUtils.toString(entity, "utf-8"); // 报文编码

                        System.out.println("Success");
                        // 发送消息
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = response;
                        handler_login.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 同样的消息机制
    private Handler handler_register = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tips.setText("");
            if (msg.what == 1) {
                if (msg.obj.toString().equals("0")) {
                    tips.setText("注册失败");
                }
                else if (msg.obj.toString().equals("1"))
                    tips.setText("注册成功");
                else if (msg.obj.toString().equals("2"))
                    tips.setText("注册失败，用户已存在");
            }
        }
    };

    private void RegisterHttpClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String uri = URL_register + "?username=" + username + "&password=" + password;
                HttpClient client = new DefaultHttpClient(); // HttpClient 是一个接口，无法实例化，所以我们通常会创建一个DefaultHttpClient实例
                HttpGet get = new HttpGet(uri); // 发起GET请求就使用HttpGet，发起POST请求则使用HttpPost，这里我们先使用HttpGet
                try {
                    HttpResponse httpResponse = client.execute(get); // 调用HttpClient对象的execute()方法
                    // 状态码200说明响应成功
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity(); // 取出报文的具体内容
                        String response = EntityUtils.toString(entity, "utf-8"); // 报文编码

                        System.out.println("Success");
                        // 发送消息
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = response;
                        handler_register.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}


