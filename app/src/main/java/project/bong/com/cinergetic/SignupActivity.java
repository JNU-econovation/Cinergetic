package project.bong.com.cinergetic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.bong.com.cinergetic.model.User;

public class SignupActivity extends AppCompatActivity {

    private EditText text_id, text_pw, text_pw_check, text_email, text_name;
    private Button btn_singup;
    private TextView text_login;
    boolean id_overlap, pw_differ;
    DatabaseReference mReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initView();
        id_overlap = pw_differ = false;
        mReference = FirebaseDatabase.getInstance().getReference();

        btn_singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_overlap = pw_differ = false; // boolean 변수초기화
                // 입력값 유효성 검사
                checkUserData();
            }
        });
        text_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent_singup = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent_singup, MainActivity.ACTIVITY_LOGIN);
            }
        });
    }

    private void initView(){
        text_id = (EditText)findViewById(R.id.text_id);
        text_pw = (EditText)findViewById(R.id.text_pw);
        text_pw_check = (EditText)findViewById(R.id.text_pw_check);
        text_email = (EditText)findViewById(R.id.text_email);
        text_name = (EditText)findViewById(R.id.text_name);
        btn_singup = (Button)findViewById(R.id.btn_signup);
        text_login = (TextView)findViewById(R.id.signup_logintext);
    }

    /* 회원가입 데이터 확인 */
    private void checkUserData() {
        // 빈칸 입력 확인
        if(text_id.getText().toString().length() == 0
                || text_pw.getText().toString().length() == 0
                || text_pw_check.getText().toString().length() == 0
                || text_email.getText().toString().length() == 0
                || text_name.getText().toString().length() == 0)
            Toast.makeText(getApplicationContext(), "빈칸을 모두 작성해주세요.", Toast.LENGTH_LONG).show();

        // 아이디 자리수 확인
        else if(text_id.getText().toString().length() < 6) {
            Toast.makeText(getApplicationContext(), "6자리 이상의 아이디를 입력해주세요.", Toast.LENGTH_LONG).show();
            text_id.requestFocus();
        }

        // 아이디 중복 확인
        else if(check_ID_overlap())
            Toast.makeText(getApplicationContext(), "동일한 아이디가 존재합니다.", Toast.LENGTH_LONG).show();

        // 비밀번호 비밀번호확인 일치여부 확인
        else if(!text_pw.getText().toString().equals(text_pw_check.getText().toString()))
            Toast.makeText(getApplicationContext(), "비밀번호가 동일하지 않습니다.", Toast.LENGTH_LONG).show();

        // 비밀번호 자리수 확인
        else if(text_pw.getText().toString().length() < 8) {
            Toast.makeText(getApplicationContext(), "8자리 이상의 비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();
            text_pw_check.setText("");
        }

        // 이메일 형식 확인
        else if(!check_Email())
            Toast.makeText(getApplicationContext(), "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_LONG).show();

        // 올바르게 데이터가 입력된 경우 데이터를 DB에 저장하는 함수 호출
        else
            saveUserData();
    }

    private boolean check_ID_overlap(){
        /*아이디 중복 확인*/
        mReference.child("members").child(text_id.getText().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // 동일한 id를 가지고 있는 child가 존재한다면 아이디 중복!
                        if(dataSnapshot.exists())
                            id_overlap = true;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // DB 접근에 실패한 경우
                    }
                });

            return id_overlap;
    }

    private boolean check_Email(){
        return Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+",
                text_email.getText().toString().trim());
    }

    /* 사용자 데이터 저장 */
    private void saveUserData() {
        String id = text_id.getText().toString();
        // BCrypt 사용하여 비밀번호 암호화
        String pw = BCrypt.hashpw(text_pw.getText().toString(), BCrypt.gensalt());
        String email = text_email.getText().toString();
        String name = text_name.getText().toString();

        User user = new User(id, pw, email, name);
        mReference.child("members").child(text_id.getText().toString()).setValue(user);
        Toast.makeText(getApplicationContext(), "회원가입 되었습니다.", Toast.LENGTH_LONG).show();
        finish();
    }

}
