package project.bong.com.cinergetic.fragment;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import project.bong.com.cinergetic.MainActivity;
import project.bong.com.cinergetic.R;
import project.bong.com.cinergetic.model.Movie;
import project.bong.com.cinergetic.model.WeeksMovie;

public class AdminPostFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    DatabaseReference mDatabase;
    ArrayList<String> movieNmList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    RadioGroup radio_weeks;
    Spinner spinners[] = new Spinner[6];
    EditText edit_movie_time[] = new EditText[6];
    ImageView xmark[] = new ImageView[6];
    Button btn_post;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_admin_post, container, false);
        Toast.makeText(getContext(), "onCreateView는 언제실행되나", Toast.LENGTH_LONG).show();
        movieNmList.add("영화선택");
        initView(rootView); // 뷰 초기화
        initDatabase();     //DB 연결

        // firebase에서 불러온 영화데이터가 존재한다면
        // 각 스피너에 movieNmList를 아이템으로 하는 어댑터를 설정해준다.
        if(movieNmList.size() > 0){
            arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, movieNmList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            for(int i=0; i<6; i++)
                spinners[i].setAdapter(arrayAdapter);
        }

        // 라디오버튼 이벤트
        radio_weeks.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override   // 다른 라디오 버튼을 클릭했을 때 내용 초기화 해주기
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });
        // 게시하기 버튼 클릭 이벤트
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 시간표 입력 확인
                if(checkTimeTable()){

                    saveTimeTable(); // 시간표 DB 저장
                    for(int i=0; i<6; i++){ // 초기화
                        spinners[i].setSelection(0);
                        edit_movie_time[i].setText("");
                    }
                }

            }
        });

        // EditText 이벤트 리스너 테스트
        edit_movie_time[0].addTextChangedListener(new TextWatcher() {
            @Override   // 텍스트 입력 전에 호출
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override   // 입력된 텍스트에 변화가 있을 때 호출
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkTextRule(edit_movie_time[0]);
            }

            @Override   // 텍스트 입력이 끝나고 호출
            public void afterTextChanged(Editable s) {

            }
        });
        edit_movie_time[1].addTextChangedListener(new TextWatcher() {
            @Override   // 텍스트 입력 전에 호출
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override   // 입력된 텍스트에 변화가 있을 때 호출
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkTextRule(edit_movie_time[1]);
            }

            @Override   // 텍스트 입력이 끝나고 호출
            public void afterTextChanged(Editable s) {

            }
        });
        edit_movie_time[2].addTextChangedListener(new TextWatcher() {
            @Override   // 텍스트 입력 전에 호출
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override   // 입력된 텍스트에 변화가 있을 때 호출
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkTextRule(edit_movie_time[2]);
            }

            @Override   // 텍스트 입력이 끝나고 호출
            public void afterTextChanged(Editable s) {

            }
        });
        edit_movie_time[3].addTextChangedListener(new TextWatcher() {
            @Override   // 텍스트 입력 전에 호출
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override   // 입력된 텍스트에 변화가 있을 때 호출
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkTextRule(edit_movie_time[3]);
            }

            @Override   // 텍스트 입력이 끝나고 호출
            public void afterTextChanged(Editable s) {

            }
        });
        edit_movie_time[4].addTextChangedListener(new TextWatcher() {
            @Override   // 텍스트 입력 전에 호출
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override   // 입력된 텍스트에 변화가 있을 때 호출
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkTextRule(edit_movie_time[4]);
            }

            @Override   // 텍스트 입력이 끝나고 호출
            public void afterTextChanged(Editable s) {

            }
        });
        edit_movie_time[5].addTextChangedListener(new TextWatcher() {
            @Override   // 텍스트 입력 전에 호출
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override   // 입력된 텍스트에 변화가 있을 때 호출
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkTextRule(edit_movie_time[5]);
            }

            @Override   // 텍스트 입력이 끝나고 호출
            public void afterTextChanged(Editable s) {

            }
        });


        // X 버튼 누르면 입력 지우기
        xmark[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_movie_time[0].setText("");
            }
        });
        xmark[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_movie_time[1].setText("");
            }
        });
        xmark[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_movie_time[2].setText("");
            }
        });
        xmark[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_movie_time[3].setText("");
            }
        });
        xmark[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_movie_time[4].setText("");
            }
        });
        xmark[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_movie_time[5].setText("");
            }
        });

        return rootView;
    }

    private void initView(ViewGroup rootView) {
        radio_weeks = (RadioGroup)rootView.findViewById(R.id.radioGroup_inPost);
        edit_movie_time[0] = (EditText)rootView.findViewById(R.id.movie_time1);
        edit_movie_time[1] = (EditText)rootView.findViewById(R.id.movie_time2);
        edit_movie_time[2] = (EditText)rootView.findViewById(R.id.movie_time3);
        edit_movie_time[3] = (EditText)rootView.findViewById(R.id.movie_time4);
        edit_movie_time[4] = (EditText)rootView.findViewById(R.id.movie_time5);
        edit_movie_time[5] = (EditText)rootView.findViewById(R.id.movie_time6);
        spinners[0] = (Spinner)rootView.findViewById(R.id.movie_name1);
        spinners[1] = (Spinner)rootView.findViewById(R.id.movie_name2);
        spinners[2] = (Spinner)rootView.findViewById(R.id.movie_name3);
        spinners[3] = (Spinner)rootView.findViewById(R.id.movie_name4);
        spinners[4] = (Spinner)rootView.findViewById(R.id.movie_name5);
        spinners[5] = (Spinner)rootView.findViewById(R.id.movie_name6);
        xmark[0] = (ImageView)rootView.findViewById(R.id.xmark1);
        xmark[1] = (ImageView)rootView.findViewById(R.id.xmark2);
        xmark[2] = (ImageView)rootView.findViewById(R.id.xmark3);
        xmark[3] = (ImageView)rootView.findViewById(R.id.xmark4);
        xmark[4] = (ImageView)rootView.findViewById(R.id.xmark5);
        xmark[5] = (ImageView)rootView.findViewById(R.id.xmark6);
        btn_post = (Button)rootView.findViewById(R.id.btn_post);

        // 초기화 (다른화면갔다가 돌아올 때 처음 상태가 되도록 설정)
        for(int i=0; i<6; i++)
            spinners[i].setSelection(0);
        radio_weeks.setId(0);
    }


    // DB에 저장된 영화리스트(등록된 영화리스트)를 불러와 ArrayList에 저장하는 함수
    private void initDatabase(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("MovieList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot messageData : dataSnapshot.getChildren()){
                    Movie loadedMovie = messageData.getValue(Movie.class);
                    if(loadedMovie != null)
                        movieNmList.add(loadedMovie.getTitle());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ERROR", "DB 접근 실패 in AdminPostFragment" + databaseError.getMessage());
            }
        });
    }

    private boolean checkTimeTable(){
        for(int i=0; i<6; i++){
            // 영화명이 비어있는 경우
            if(spinners[i].getSelectedItem().toString().equals("영화선택")){
                if(edit_movie_time[i].getText().toString().length() > 0){
                    Toast.makeText(getContext(), "영화를 선택하세요.", Toast.LENGTH_LONG).show();
                    edit_movie_time[i].requestFocus();
                    return false;
                }
            } // 영화명이 입력된 경우
            else{   // 상영시간이 입력된 경우
                if(edit_movie_time[i].getText().toString().length() == 5){
                    // 올바르지 않은 시간이 입력된 경우  ex)70:88
                    for(int j=0; j<5; j++){
                        int hour = Integer.parseInt(edit_movie_time[i].getText().toString().substring(0,2));
                        int minute = Integer.parseInt(edit_movie_time[i].getText().toString().substring(3,5));
                        if(hour >= 24 || minute >= 60){
                            Toast.makeText(getContext(), "상영시간을 올바르게 입력하세요.", Toast.LENGTH_LONG).show();
                            edit_movie_time[i].requestFocus();
                            return false;
                        }
                    }
                }else{// 상영시간이 입력되지 않은 경우
                    Toast.makeText(getContext(), "상영시간을 올바르게 입력하세요.", Toast.LENGTH_LONG).show();
                    edit_movie_time[i].requestFocus();
                    return false;
                }
            }
        }
        return true;
    }

    private void saveTimeTable(){
        // DB에 저장할 영화 이름, 요일, 상영시간
        String dayofweeks = getDbName();
        WeeksMovie movie = new WeeksMovie(dayofweeks);

        // 요일객체에 상영영화 및 상영시간 데이터 추가
        for(int i=0; i<6; i++){
            String name = spinners[i].getSelectedItem().toString();
            if(name.equals("영화선택"))   // 스피너에 영화를 선택하지 않은 기본값이 선택되어 있을 때에
                name = "";              // 영화명을 공백으로 바꿔 저장한다.
            String time = edit_movie_time[i].getText().toString();
            movie.addData(name, time);
        }
        String db_name = dayofweeks+"DB";

        mDatabase.child("movieDB").child(db_name).setValue(movie);
        Toast.makeText(getContext(), "게시되었습니다.", Toast.LENGTH_LONG).show();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
    }

    private String getDbName(){
        // 체크되어있는 라디오버튼(요일) 확인
        int id = radio_weeks.getCheckedRadioButtonId();
        String dayofweeks = "";

        switch (id){
            case R.id.radioButton:
                dayofweeks = "MON"; break;
            case R.id.radioButton2:
                dayofweeks = "TUE"; break;
            case R.id.radioButton3:
                dayofweeks = "WED"; break;
            case R.id.radioButton4:
                dayofweeks = "THU"; break;
            case R.id.radioButton5:
                dayofweeks = "FRI"; break;
            case R.id.radioButton6:
                dayofweeks = "SAT"; break;
            case R.id.radioButton7:
                dayofweeks = "SUN"; break;
        }
        return dayofweeks;
    }


    // 영화시간표 입력형식 확인
    private void checkTextRule(EditText editText){
        String text = editText.getText().toString();

        if (text.length() > 0) {
            // 숫자가 아닌 다른 글자가 들어갔을 때
            if (!Character.isDigit(text.charAt(text.length()-1))
                    && text.charAt(text.length()-1) != ':') {
                Toast.makeText(getContext(), "숫자를 입력해주세요.", Toast.LENGTH_LONG).show();
                editText.setText(text.substring(0, text.length()-1));
                // 포커스 맞추기
                Editable editable = editText.getText();
                Selection.setSelection(editable, editable.length());
            } // 글자 수가 두 개면서, 그 두 글자가 모두 숫자일 경우, ':'을 붙여준다.
            else if (text.length() == 2){
                if (Character.isDigit(text.charAt(1))) {
                    editText.setText(text + ":");
                    // text변경 후 포커스를 제일 끝으로 맞춰준다.
                    Editable editable = editText.getText();
                    Selection.setSelection(editable, editable.length());
                }
            } else if(text.length() > 5){
                editText.setText(text.substring(0,5));
                // 포커스 맞추기
                Editable editable = editText.getText();
                Selection.setSelection(editable, editable.length());
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}