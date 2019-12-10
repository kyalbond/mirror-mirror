package mirrormirror.swen302.mirrormirrorandroid.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import mirrormirror.swen302.mirrormirrorandroid.R;
import mirrormirror.swen302.mirrormirrorandroid.utilities.ServerController;
import mirrormirror.swen302.mirrormirrorandroid.utilities.WeightPopupDialog;

/**
 * Created by hayandr1 on 5/10/17.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Checks for saved UserID, will skip this activity and start the HomeActivity if found.
        startHomeActivity();
        //UserID not found, create LoginActivivty.
        setContentView(R.layout.login_activity);
        ServerController.setSocketLoginListener(this);
        Button login = (Button) findViewById(R.id.login_button);
        login.setOnClickListener(this);
    }

    public void startHomeActivity(){
        SharedPreferences sharedPreferences = this.getSharedPreferences(this.getString(R.string.login_details), Context.MODE_PRIVATE);
        if(sharedPreferences.contains("uid")) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.login_button){
            attemptLogin();
        }
    }

    public void attemptLogin(){
        TextView userName = (TextView) findViewById(R.id.user_name_field);
        EditText password = (EditText) findViewById(R.id.password_field);
        ServerController.sendLoginDetails(this, userName.getText().toString(), password.getText().toString());
    }

    public void onResponse(int uid){
        if(uid > 0){
            SharedPreferences sharedPreferences = this.getSharedPreferences(this.getString(R.string.login_details), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("uid", String.valueOf(uid));
            editor.apply();
            startHomeActivity();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this, "Incorrect username/password", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void onConnection(){
        ServerController.sendAndroidIdEvent(this);
    }

}
