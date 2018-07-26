package project.bong.com.cinergetic.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import project.bong.com.cinergetic.MainActivity;
import project.bong.com.cinergetic.R;
import project.bong.com.cinergetic.model.WeeksMovie;

public class AdminCheckFragment extends Fragment {

    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    WeeksMovie movie;

    RadioGroup radioGroup;
    TextView text_name[] = new TextView[6];
    TextView text_time[] = new TextView[6];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_admin_check, container, false);

        // 뷰 초기화 함수
        initView(rootView);
        // 월요일의 DB를 불러와 movie 객체에 저장 및 테이블에 띄워주는 함수
        initDatabase();

        // 다른 라디오 버튼을 누를 때 DB를 바꿔주는 함수
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                changeTable(checkedId);
            }
        });

        return rootView;
    }

    private void initView( ViewGroup rootView){
        radioGroup = (RadioGroup)rootView.findViewById(R.id.radioGroup_inShow);

        text_name[0] = (TextView)rootView.findViewById(R.id.movieName_1);
        text_name[1] = (TextView)rootView.findViewById(R.id.movieName_2);
        text_name[2] = (TextView)rootView.findViewById(R.id.movieName_3);
        text_name[3] = (TextView)rootView.findViewById(R.id.movieName_4);
        text_name[4] = (TextView)rootView.findViewById(R.id.movieName_5);
        text_name[5] = (TextView)rootView.findViewById(R.id.movieName_6);

        text_time[0] = (TextView)rootView.findViewById(R.id.movieTime_1);
        text_time[1] = (TextView)rootView.findViewById(R.id.movieTime_2);
        text_time[2] = (TextView)rootView.findViewById(R.id.movieTime_3);
        text_time[3] = (TextView)rootView.findViewById(R.id.movieTime_4);
        text_time[4] = (TextView)rootView.findViewById(R.id.movieTime_5);
        text_time[5] = (TextView)rootView.findViewById(R.id.movieTime_6);
    };

    private void initDatabase(){
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("movieDB");

        // 처음 기본데이터는 월요일 데이터
        String dbName = "MONDB";

        // addListenerForSingleValueEvent : DB 경로의 전체 내용을 한 번 읽어오는 함수
        mReference.child(dbName).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                movie = dataSnapshot.getValue(WeeksMovie.class);

               // 월요일 DB를 테이블에 기본적으로 띄워준다.
              for(int i=0; i<6; i++){
                    text_name[i].setText(movie.getNameList().get(i));
                    text_time[i].setText(movie.getTimeList().get(i));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ERROR", "DB접근 실패 in AdminCheckFragment"  + databaseError.getMessage());
            }
        });
    }

    private void changeTable(int radio_id){
        String dbName = getRadioWeeks(radio_id) + "DB";



        // addListenerForSingleValueEvent : DB 경로의 전체 내용을 한 번 읽어오는 함수
        mReference.child(dbName).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                // 해당 요일의 데이터를 가진 객체를 불러와 movie에 저장
                // ex) 수요일 RadioButton의 id가 checked 되어있다면,
                // 수요일 DB인 WEDDB의 객체가 movie에 저장된다.
                movie = dataSnapshot.getValue(WeeksMovie.class);

                // movie의 데이터를 테이블에 띄워주는 함수
                for(int i=0; i<6; i++){
                    text_name[i].setText(movie.getNameList().get(i));
                    text_time[i].setText(movie.getTimeList().get(i));
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ERROR", "DB접근 실패 in ShowAcitivy");
            }
        });
    }

    public String getRadioWeeks(int id){
        String dayofweeks = "";
        switch (id){
            case R.id.radioButton8:
                dayofweeks = "MON"; break;
            case R.id.radioButton9:
                dayofweeks = "TUE"; break;
            case R.id.radioButton10:
                dayofweeks = "WED"; break;
            case R.id.radioButton11:
                dayofweeks = "THU"; break;
            case R.id.radioButton12:
                dayofweeks = "FRI"; break;
            case R.id.radioButton13:
                dayofweeks = "SAT"; break;
            case R.id.radioButton14:
                dayofweeks = "SUN"; break;
        }
        return dayofweeks;
    }
}

