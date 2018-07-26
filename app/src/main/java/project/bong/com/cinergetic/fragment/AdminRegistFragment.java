package project.bong.com.cinergetic.fragment;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import project.bong.com.cinergetic.ListView.MovieListView;
import project.bong.com.cinergetic.MainActivity;
import project.bong.com.cinergetic.R;
import project.bong.com.cinergetic.model.Movie;

public class AdminRegistFragment extends Fragment implements AdapterView.OnItemClickListener {

    // API 기본 URL 및 KEY
    final static String DEFAULT_URL = "http://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_xml.jsp?collection=kmdb_new&detail=N";
    final static String SERVICE_KEY = "9B306E2D33C59DB2E6A12AC6505775A8D4746DF289617D2DA42B27EDBB654FE";

    DatabaseReference mDatabase;
    ArrayList<Movie> movieList;
    MovieListAdapter adapter;
    ListView listView;
    Button btn_add;
    ImageView refresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @NonNull Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_admin_regist, container, false);
        listView = (ListView)rootView.findViewById(R.id.listView_movie);
        btn_add = (Button)rootView.findViewById(R.id.btn_add);
        refresh = (ImageView)rootView.findViewById(R.id.regist_refresh);
        movieList = new ArrayList<>();
        adapter = new MovieListAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        initDatabase();


        // 프래그먼트 갱신 (리스트 업데이트)
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "버튼!", Toast.LENGTH_SHORT).show();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(((MainActivity)getActivity()).getAdminRegistFragment()).attach(((MainActivity)getActivity()).getAdminRegistFragment()).commit();
            }
        });

        // 새로운 영화 등록
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AdminRegistFragmentDialog fragmentDialog = new AdminRegistFragmentDialog();
                fragmentDialog.show(getActivity().getFragmentManager(), "registration dial");
            }
        });

        // AdminRegistFragmentDialog 에서 bundle을 달아준 것이 존재 할 때,
        // 새로운 Movie 객체를 만든 후 DB에 저장한다.
        if (getArguments() != null) {
            Movie searchMovie = new Movie();
            searchMovie.setTitle(getArguments().getString("title"));
            searchMovie.setProdYear(getArguments().getString("prodYear"));
            searchMovie.setDirectorNm(getArguments().getString("directorNm"));
            searchMovie.setActorNm(getArguments().getString("actorNm"));
            searchMovie.setNation(getArguments().getString("nation"));
            searchMovie.setCompany(getArguments().getString("company"));
            searchMovie.setPlot(getArguments().getString("plot"));
            searchMovie.setRuntime(getArguments().getString("runtime"));
            searchMovie.setRating(getArguments().getString("rating"));
            searchMovie.setGenre(getArguments().getString("genre"));
            searchMovie.setUrl(getArguments().getString("url"));

            // 새로고침을 눌렀을 때 아무것도 없는 데이터가 들어가지 않도록
            if(searchMovie.getTitle().length() > 0)
                mDatabase.child("MovieList").child(searchMovie.getTitle()).setValue(searchMovie);
            searchMovie = null; // 메모리 할당 해제
        }else{
           //Toast.makeText(getContext(), "null입니다", Toast.LENGTH_LONG).show();
        }
        return rootView;
    }

    // 리스트뷰 아이템 클릭 시 삭제 가능한 다이얼로그로 이동!
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Movie selectedData = movieList.get(position);
        // Fragment Dialog에 Bundle을 이용하여
        // 영화명 및 포스터url을 전달해준다.
        Bundle bundle = new Bundle();
        bundle.putString("title", selectedData.getTitle());
        bundle.putString("url", selectedData.getUrl());
        AdminRegistFragmentDeleteDialog deleteDialog = new AdminRegistFragmentDeleteDialog();
        deleteDialog.setArguments(bundle);
        deleteDialog.show(getActivity().getFragmentManager(), "delete dial");
    }

    //DB 연결 (DB에서 Movie객체 받아와 arrayList에 저장)
    private void initDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("MovieList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                movieList.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    movieList.add(messageData.getValue(Movie.class));
                }
                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public class MovieListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return movieList.size();
        }

        @Override
        public Object getItem(int position) {
            return movieList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MovieListView view = new MovieListView(getContext());
            Movie item = movieList.get(position);
            view.setMovieNm(item.getTitle());

            return view;
        }
    }
}
