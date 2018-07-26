
package project.bong.com.cinergetic.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import project.bong.com.cinergetic.MainActivity;
import project.bong.com.cinergetic.R;
import project.bong.com.cinergetic.model.Movie;
import project.bong.com.cinergetic.model.WeeksMovie;


public class MainFragment extends Fragment {

    DatabaseReference mReference;
    ArrayAdapter<String> arrayAdapter;
    String[] spinnerItem;
    WeeksMovie movieTimeTable;   // 해당 요일의 영화 시간표
    ArrayList<String> urlList;
    ArrayList<String> tempUrlList;
    Bitmap[] bitmap = new Bitmap[6];

    Spinner spinner;
    ImageView[] imageViews = new ImageView[6];
    HorizontalScrollView scrollView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);
        initView(rootView);

        mReference = FirebaseDatabase.getInstance().getReference();
        movieTimeTable = new WeeksMovie();
        urlList = new ArrayList<>();
        tempUrlList = new ArrayList<>();

        // 오늘의 요일 얻어오기
        /*
        Calendar calendar = Calendar.getInstance();
        String[] calendarweek = {"일", "월", "화", "수", "목", "금", "토"};
        String today = calendarweek[calendar.get(Calendar.DAY_OF_WEEK)];
*/
        // 스피너 설정
        spinnerItem = new String[]{"월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"};
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerItem);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        //initSpinner(today);     // 오늘에 해당하는 요일을 스피너의 초기값으로 설정

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override  // 스피너에서 다른 아이템을 클릭했을 때 실행되는 함수
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 포스터 이미지 초기화
                for(int i=0; i<6; i++){
                    imageViews[i].setImageResource(R.drawable.pposter);
                }

                // 스크롤뷰 포커스 설정
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_LEFT);
                    }
                });

                getTimeTable(spinnerItem[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 이미지를 클릭하면 영화 세부 페이지로 이동
        imageViews[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bundle에 영화 이름을 붙여준 뒤 세부정보 페이지로 이동
                Bundle bundle = new Bundle(1);
                bundle.putString("movieNm", movieTimeTable.getNameList().get(0));
                ((MainActivity)getActivity()).getDetailFragment().setArguments(bundle);

                ((MainActivity)getActivity()).changeFragment(((MainActivity) getActivity()).FRAGMENT_DETAIL);
            }
        });

        imageViews[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle(1);
                bundle.putString("movieNm", movieTimeTable.getNameList().get(1));
                ((MainActivity)getActivity()).getDetailFragment().setArguments(bundle);

                ((MainActivity)getActivity()).changeFragment(((MainActivity) getActivity()).FRAGMENT_DETAIL);
            }
        });

        imageViews[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle(1);
                bundle.putString("movieNm", movieTimeTable.getNameList().get(2));
                ((MainActivity)getActivity()).getDetailFragment().setArguments(bundle);

                ((MainActivity)getActivity()).changeFragment(((MainActivity) getActivity()).FRAGMENT_DETAIL);
            }
        });

        imageViews[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle(1);
                bundle.putString("movieNm", movieTimeTable.getNameList().get(3));
                ((MainActivity)getActivity()).getDetailFragment().setArguments(bundle);

                ((MainActivity)getActivity()).changeFragment(((MainActivity) getActivity()).FRAGMENT_DETAIL);
            }
        });

        imageViews[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle(1);
                bundle.putString("movieNm", movieTimeTable.getNameList().get(4));
                ((MainActivity)getActivity()).getDetailFragment().setArguments(bundle);

                ((MainActivity)getActivity()).changeFragment(((MainActivity) getActivity()).FRAGMENT_DETAIL);
            }
        });

        imageViews[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle(1);
                bundle.putString("movieNm", movieTimeTable.getNameList().get(5));
                ((MainActivity)getActivity()).getDetailFragment().setArguments(bundle);

                ((MainActivity)getActivity()).changeFragment(((MainActivity) getActivity()).FRAGMENT_DETAIL);
            }
        });
        return rootView;
    }

    private void initView(ViewGroup rootView){
        spinner = (Spinner)rootView.findViewById(R.id.main_spinner);
        imageViews[0] = (ImageView)rootView.findViewById(R.id.movie_poster1);
        imageViews[1] = (ImageView)rootView.findViewById(R.id.movie_poster2);
        imageViews[2] = (ImageView)rootView.findViewById(R.id.movie_poster3);
        imageViews[3] = (ImageView)rootView.findViewById(R.id.movie_poster4);
        imageViews[4] = (ImageView)rootView.findViewById(R.id.movie_poster5);
        imageViews[5] = (ImageView)rootView.findViewById(R.id.movie_poster6);
        scrollView = (HorizontalScrollView)rootView.findViewById(R.id.horizontalScrollView);
    }

    // 오늘에 해당하는 요일을 스피너의 초기값으로 설정
    private void initSpinner(String today){
        if(today.equals("월"))
            spinner.setSelection(0);
        else if(today.equals("화"))
            spinner.setSelection(1);
        else if(today.equals("수"))
            spinner.setSelection(2);
        else if(today.equals("목"))
            spinner.setSelection(3);
        else if(today.equals("금"))
            spinner.setSelection(4);
        else if(today.equals("토"))
            spinner.setSelection(5);
        else if(today.equals("일"))
            spinner.setSelection(6);
    }


    // 해당 요일의 영화 시간표 가져오기 (WeeksMovie이용)
    private void getTimeTable(String dayofweeks) {
        String DBName = "";
        if(dayofweeks.equals("월요일"))          DBName = "MONDB";
        else if(dayofweeks.equals("화요일"))     DBName = "TUEDB";
        else if(dayofweeks.equals("수요일"))     DBName = "WEDDB";
        else if(dayofweeks.equals("목요일"))     DBName = "THUDB";
        else if(dayofweeks.equals("금요일"))     DBName = "FRIDB";
        else if(dayofweeks.equals("토요일"))     DBName = "SATDB";
        else if(dayofweeks.equals("일요일"))     DBName = "SUNDB";


        mReference.child("movieDB").child(DBName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                movieTimeTable = dataSnapshot.getValue(WeeksMovie.class);
                // 해당 요일의 각 상영영화 이름을 파라미터로 보내 Url을 받아온다.
                for(int i=0; i<6; i++)
                    getMovieUrl(movieTimeTable.getNameList().get(i));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("[TAG]", "Firebase \"MovieDB\" data access fail in getTimeTable()  // MainFragment");
            }
        });
    }

    // 시간표별 상영영화 포스터Url 가져오기 (MovieList이용)
    private void getMovieUrl(String movieName){
        mReference.child("MovieList").child(movieName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Movie loadedMovie = dataSnapshot.getValue(Movie.class);
                if(loadedMovie != null) {
                    urlList.add(loadedMovie.getUrl());
                    if(urlList.size() == 6){            // url 데이터가 다 가져와지면 url에서 이미지를 불러오는 함수 실행
                        tempUrlList.clear();
                        for(int i=0; i<6; i++){
                            tempUrlList.add(urlList.get(i));
                        }
                        urlList.clear();
                        getImgFromUrl();
                    }
                }else{
                    Log.d("[TAG]", "loaded Movie data is null in getMovieUrl() // MainFragment");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("[TAG]", "Firebase \"MovieList\" data access fail in getMovieUrl()  // MainFragment");
            }
        });

    }

    // Url리스트에서 이미지 불러와 화면에 띄우기
    private void getImgFromUrl(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(!tempUrlList.get(0).equals("")){
                        URL url = new URL(tempUrlList.get(0));
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        conn.setDoInput(true);  // 서버로부터 응답 수신
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        bitmap[0] = BitmapFactory.decodeStream(is);
                        // UI 변경할때는 runOnUiThread를 사용해야 함!
                        // 일반 스레드에서 UI 변경 코드를 돌리면 메인스레드가 제대로 실행되지 않음
                        ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageViews[0].setImageBitmap(bitmap[0]);
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(!tempUrlList.get(1).equals("")){
                        URL url = new URL(tempUrlList.get(1));
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        conn.setDoInput(true);  // 서버로부터 응답 수신
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        bitmap[1] = BitmapFactory.decodeStream(is);
                        ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageViews[1].setImageBitmap(bitmap[1]);
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(!tempUrlList.get(2).equals("")){
                        URL url = new URL(tempUrlList.get(2));
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        conn.setDoInput(true);  // 서버로부터 응답 수신
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        bitmap[2] = BitmapFactory.decodeStream(is);
                        ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageViews[2].setImageBitmap(bitmap[2]);
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(!tempUrlList.get(3).equals("")){
                        URL url = new URL(tempUrlList.get(3));
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        conn.setDoInput(true);  // 서버로부터 응답 수신
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        bitmap[3] = BitmapFactory.decodeStream(is);
                        ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageViews[3].setImageBitmap(bitmap[3]);
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(!tempUrlList.get(4).equals("")){
                        URL url = new URL(tempUrlList.get(4));
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        conn.setDoInput(true);  // 서버로부터 응답 수신
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        bitmap[4] = BitmapFactory.decodeStream(is);
                        ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageViews[4].setImageBitmap(bitmap[4]);
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(!tempUrlList.get(5).equals("")){
                        URL url = new URL(tempUrlList.get(5));
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        conn.setDoInput(true);  // 서버로부터 응답 수신
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        bitmap[5] = BitmapFactory.decodeStream(is);
                        ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageViews[5].setImageBitmap(bitmap[5]);
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }
}