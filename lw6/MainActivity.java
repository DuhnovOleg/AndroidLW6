package com.example.lw6;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

public class MainActivity extends Activity
{
    Intent openIntent;
    private String TAG = "Жизненный цикл";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        final Button btn_Save, btn_Load;
        DatabaseHandler db = new DatabaseHandler(this);
        final Handler h = new Handler();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openIntent = new Intent(MainActivity.this, SecondActivity.class);

        Context context = this;
        Button comeIn = (Button) findViewById(R.id.comeIn);
        Button comeReg = (Button) findViewById(R.id.comeReg);

        comeIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditText loginText = (EditText) findViewById(R.id.login);
                EditText passText  = (EditText) findViewById(R.id.password);


                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        List<User> userList = db.getAllUsers();

                        boolean check = false;
                        for (int i = 0; i < userList.size(); i++)
                        {
                            User tempUser = userList.get(i);
                            if (Objects.equals(tempUser._login, loginText.getText().toString()) && Objects.equals(tempUser._pass, passText.getText().toString()))
                            {
                                check = true;
                                break;
                            }
                        }

                        if (check == true)
                        {
                            String login = loginText.getText().toString();
                            openIntent.putExtra("login", login);
                            startActivity(openIntent);
                        }
                        else
                        {
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "Неверный логин или пароль!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        comeReg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        EditText loginText = (EditText) findViewById(R.id.login);
                        EditText passText  = (EditText) findViewById(R.id.password);

                        List<User> userList = db.getAllUsers();

                        boolean check = false;
                        for (int i = 0; i < userList.size(); i++)
                        {
                            User tempUser = userList.get(i);
                            if (Objects.equals(tempUser._login, loginText.getText().toString()))
                            {
                                h.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Toast.makeText(context, "Пользователь с таким логином существует!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                check = true;
                            }
                        }

                        if (check == false)
                        {
                            User user = new User(loginText.getText().toString(), passText.getText().toString());
                            db.addUser(user);
                        }
                    }
                }).start();
            }
        });
    }
}
