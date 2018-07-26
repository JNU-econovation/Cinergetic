package project.bong.com.cinergetic.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import project.bong.com.cinergetic.MainActivity;
import project.bong.com.cinergetic.R;
import project.bong.com.cinergetic.model.Movie;

public class DetailFragment extends Fragment {

    DatabaseReference mReference;
    Movie loadedMovie;
    Bitmap bitmap;

    TextView name, grade, prodYear, overview, runtime, directorNm, actorNm, plot;
    ImageView poster;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_detail, container, false);
        loadedMovie = new Movie();
        poster = (ImageView)rootView.findViewById(R.id.detail_poster);
        name = (TextView)rootView.findViewById(R.id.detail_movieNm);
        runtime = (TextView)rootView.findViewById(R.id.detial_runtime);
        grade = (TextView)rootView.findViewById(R.id.detail_grade);
        prodYear = (TextView)rootView.findViewById(R.id.detail_prodYear);
        overview = (TextView)rootView.findViewById(R.id.detail_overview);
        directorNm = (TextView)rootView.findViewById(R.id.detail_directorNm);
        actorNm = (TextView)rootView.findViewById(R.id.detail_actorNm);
        plot = (TextView)rootView.findViewById(R.id.detail_plot);

        // MainFragment에서 Bundle을 통해 영화 이름을 보내준 것을 받음
        Bundle bundle = getArguments();
        getDatabase(bundle.getString("movieNm"));

        // 뒤로가기 버튼을 눌렀을 때
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    ((MainActivity)getActivity()).changeFragment(((MainActivity)getActivity()).FRAGMENT_MAIN);
                    return true;
                }else{
                    return false;
                }
            }
        });

        return rootView;
    }

    private void getDatabase(String movieName){
        mReference = FirebaseDatabase.getInstance().getReference();
        mReference.child("MovieList").child(movieName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loadedMovie = dataSnapshot.getValue(Movie.class);
                setDetailData();   // 영화 세부정보 데이터 화면에 불러오기
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setDetailData(){
        name.setText(loadedMovie.getTitle());
        grade.setText(loadedMovie.getRating());
        prodYear.setText(loadedMovie.getProdYear() + "(" + loadedMovie.getNation() + ")");
        overview.setText(loadedMovie.getGenre());
        runtime.setText(loadedMovie.getRuntime() + "분");
        directorNm.setText(loadedMovie.getDirectorNm());
        actorNm.setText(loadedMovie.getActorNm());
        plot.setText(loadedMovie.getPlot());

        // URL로 포스터 이미지 가져오기
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(loadedMovie.getUrl());
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            poster.setImageBitmap(bitmap);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


}