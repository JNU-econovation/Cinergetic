package project.bong.com.cinergetic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mindrot.jbcrypt.BCrypt;

import java.lang.reflect.Member;

import project.bong.com.cinergetic.fragment.MainFragment;
import project.bong.com.cinergetic.model.LoginToken;
import project.bong.com.cinergetic.model.User;

public class LoginActivity extends AppCompatActivity{

    private EditText editText_id, editText_pw;
    private Button btn_login;
    private TextView text_singup;
    DatabaseReference mReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mReference = FirebaseDatabase.getInstance().getReference();

        editText_id = (EditText)findViewById(R.id.editText_id);
        editText_pw = (EditText)findViewById(R.id.editText_pw);
        btn_login = (Button)findViewById(R.id.btn_login);
        text_singup = (TextView)findViewById(R.id.login_signuptext);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_id.getText().toString().length() > 0
                        && editText_pw.getText().toString().length() > 0){
                    checkLogin();
                }else{
                    Toast.makeText(getApplicationContext(), "정보를 입력해주세요", Toast.LENGTH_LONG).show();
                }
            }
        });
        text_singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent_singup = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent_singup, MainActivity.ACTIVITY_SIGNUP);
            }
        });
    }

    private void checkLogin(){
        mReference.child("members").child(editText_id.getText().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    // 동일한 아이디가 존재하는 경우
                    if(dataSnapshot.exists()){
                        User user = dataSnapshot.getValue(User.class);
                        // 비밀번호가 동일한 경우
                        if(BCrypt.checkpw(editText_pw.getText().toString(), user.getPw())){
                            MainActivity.userToken = new LoginToken(user.getId(), user.getName(), user.getIsAdmin());
                            finish();
                        }  // 비밀번호가 일치하지 않는 경우
                        else{
                            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                            editText_pw.setText("");
                        }
                    // 아이디가 존재하지 않는 경우
                    }else{
                        Toast.makeText(getApplicationContext(), "아이디가 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                        editText_pw.setText("");
                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}