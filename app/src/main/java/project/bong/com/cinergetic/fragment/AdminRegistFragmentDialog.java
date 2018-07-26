package project.bong.com.cinergetic.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import project.bong.com.cinergetic.MainActivity;
import project.bong.com.cinergetic.R;

public class AdminRegistFragmentDialog extends DialogFragment {

    // 영상자료원 API 기본 URL 및 KEY
    final static String DEFAULT_URL = "http://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_xml.jsp?collection=kmdb_new&detail=N";
    final static String SERVICE_KEY = "9B306E2D33C59DB2E6A12AC6505775A8D4746DF289617D2DA42B27EDBB654FE";
    // 네이버 API
    final static String NAVERAPI_URL = "https://openapi.naver.com/v1/search/movie.xml";
    final static String CLIENT_ID="eBxPjL2uJiVPThBBgbP9";
    final static String CLIENT_SECRET = "Sjt42bgdSU";
    final static int display = 1;

    EditText edit_movieNm;
    Button btn_regist;
    Button btn_cancel;

    String movie_name;
    String movie_title;       // 영화명
    String movie_prodYear;    // 제작연도
    String movie_directorNm;  // 감독
    String movie_actorNm;     // 출연진
    String movie_nation;      // 상영국가
    String movie_company;     // 상영사
    String movie_plot;        // 줄거리
    String movie_runtime;     // 상영시간
    String movie_rating;      // 상영등급
    String movie_genre;       // 장르
    String movie_url;         // 포스터url

    public AdminRegistFragmentDialog() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.dialog_regist, container, false);
        edit_movieNm = rootView.findViewById(R.id.edit_regist_movieNm);
        btn_regist = rootView.findViewById(R.id.btn_regist);
        btn_cancel = rootView.findViewById(R.id.btn_cancel);
        movie_title = movie_prodYear = movie_directorNm = movie_actorNm = movie_nation = movie_company
                = movie_plot = movie_runtime = movie_rating = movie_genre = movie_url = "";

        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 네트워크 통신은 스레드를 사용해야 함
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        getMovieApi();      // 영화 상세정보 가져오기
                        getNaverApi();      // 영화 포스터 가져오기

                        // UI 변경 스레드
                        ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Bundle bundle = new Bundle(10); // 파라미터로 전달할 데이터 개수
                                bundle.putString("title", movie_title);
                                bundle.putString("prodYear", movie_prodYear);
                                bundle.putString("directorNm", movie_directorNm);
                                bundle.putString("actorNm", movie_actorNm);
                                bundle.putString("nation", movie_nation);
                                bundle.putString("company", movie_company);
                                bundle.putString("plot", movie_plot);
                                bundle.putString("runtime", movie_runtime);
                                bundle.putString("rating", movie_rating);
                                bundle.putString("genre", movie_genre);
                                bundle.putString("url", movie_url);
                                // AdminRegistFragment에 bundle 달기
                                ((MainActivity)getActivity()).getAdminRegistFragment().setArguments(bundle);

                                dismiss();

                        /* 공공데이터 제대로 파싱되었는지 확인
                        ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String MovieData = "영화명 : " + movie_title
                                            + "\n제작연도 : " + movie_prodYear
                                            + "\n감독 : " + movie_directorNm
                                            + "\n출연진 : " + movie_actorNm
                                            + "\n상영국가 : " + movie_nation
                                            + "\n상영사 : " + movie_company
                                            + "\n줄거리 : " + movie_plot
                                            + "\n상영시간 : " + movie_runtime
                                            + "\n상영등급 : " + movie_rating
                                            + "\n장르 : " + movie_genre;
                                Toast.makeText(getActivity(), MovieData, Toast.LENGTH_LONG).show();
                                dismiss();*/
                            }
                        });
                    }
                }).start();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();  // 다이얼로그 닫기
            }
        });



        return rootView;
    }

    // 한국영상자료원 API에서 영화정보 가져와 객체에 저장하는 함수
    private void getMovieApi(){

        // URL에 한글이 있으면 깨지기 때문에 인코딩을 해줘야함
        // 인코딩하는 함수 encode는 try-catch문이 필요
        try {
            movie_name = URLEncoder.encode(edit_movieNm.getText().toString(), "UTF-8");
        } catch (Exception e) {
            Log.d("EXEPTION", "Hanguel encoding error in MainFragment");
        }

        String queryURL = DEFAULT_URL + "&query=" + movie_name + "&ServiceKey=" + SERVICE_KEY;

        try{
            URL url = new URL(queryURL);
            InputStream stream = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(stream, "UTF-8"));

            String tag;
            parser.next();
            int eventType = parser.getEventType();
            boolean isEnd = false;
            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    // 태그 시작
                    case XmlPullParser.START_TAG:
                        tag = parser.getName();  // 태그 이름 얻어오기
                        if(tag.equals("title")){
                            parser.next();
                            movie_title = parser.getText().trim();
                        }
                        else if(tag.equals("prodYear")){
                            parser.next();
                            movie_prodYear = parser.getText().trim();
                        }
                        else if(tag.equals("directorNm")){
                            parser.next();
                            if(movie_directorNm.length() > 0)
                                movie_directorNm += ", ";
                            movie_directorNm += parser.getText().trim();
                        }
                        else if(tag.equals("actorNm")){
                            parser.next();
                            if(movie_actorNm.length() > 0)
                                movie_actorNm += ", ";
                            movie_actorNm += parser.getText().trim();
                        }
                        else if(tag.equals("nation")){
                            parser.next();
                            movie_nation = parser.getText().trim();
                        }
                        else if(tag.equals("company")){
                            parser.next();
                            movie_company = parser.getText().trim();
                        }
                        else if(tag.equals("plot")){
                            parser.next();
                            movie_plot = parser.getText().trim();
                        }
                        else if(tag.equals("runtime")){
                            parser.next();
                            movie_runtime = parser.getText().trim();
                        }
                        else if(tag.equals("rating")){
                            parser.next();
                            movie_rating = parser.getText().trim();
                        }else if(tag.equals("genre")){
                            parser.next();
                            movie_genre = parser.getText().trim();
                            isEnd = true;
                        }
                }
                if(!isEnd)
                    eventType = parser.next();
                else
                    break;
            }
        }catch (Exception e){
            e.getStackTrace();
        }

        // 태그 제거  ex) <!HS>쥬라기<!HE> <!HS>공원<!HE>  -> 쥬라기 공원
        removeTag();
    }

    // 네이버 API에서 영화 포스터 가져와 객체에 저장하는 함수
    private void getNaverApi(){
        try {
            movie_name = URLEncoder.encode(edit_movieNm.getText().toString(), "UTF-8");
        } catch (Exception e) {
            Log.d("EXEPTION", "Hanguel encoding error in MainFragment");
        }

        String queryURL = NAVERAPI_URL + "?query=" + movie_name + "&display=" + display + "&";
        try {
            URL url = new URL(queryURL);
            URLConnection urlConn = url.openConnection();
            urlConn.setRequestProperty("X-Naver-Client-Id", CLIENT_ID);
            urlConn.setRequestProperty("X-Naver-Client-Secret", CLIENT_SECRET);

            InputStreamReader streamReader = new InputStreamReader(urlConn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String data="";
            String msg = null;
            while((msg = br.readLine())!=null) {
                data += msg;
            }

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(data));
            int eventType= parser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                switch(eventType){
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName();
                        if(tag.equals("image")){
                            parser.next();
                            movie_url = parser.getText();
                        }
                        break;
                }
                eventType =parser.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void removeTag(){
        // 영화정보 공공데이터에 title, actorNm에 HS, HE 태그가 들어있기 때문에 제거
        movie_title = movie_title.replace("<!HS>", "");
        movie_title = movie_title.replace("<!HE>", "");
        movie_actorNm = movie_actorNm.replace("<!HS>", "");
        movie_actorNm = movie_actorNm.replace("<!HE>", "");
    }
}

