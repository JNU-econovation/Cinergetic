package project.bong.com.cinergetic.fragment;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import project.bong.com.cinergetic.R;

public class AdminRegistFragmentDeleteDialog extends DialogFragment {

    DatabaseReference mDatabase;    // 데이터 삭제를 위한 파이어베이스 레퍼런스 객체

    Button btn_delete;      // 삭제 버튼
    TextView text_moveNm;   // 영화명 뷰
    ImageView img_poster;   // 영화 포스터 이미지
    ImageView img_exit;     // 닫기 이미지(x)
    Bitmap bitmap;          // url로부터 받아온 포스터 이미지를 저장할 bitmap

    // 프래그먼트로부터 전달받은
    // 선택한 리스트뷰 아이템의 영화이름 및 포스터 url
    String movieNm;
    String movieUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.item_registered_delete, container, false);
        movieNm = movieUrl = "";
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btn_delete = (Button)rootView.findViewById(R.id.btn_delete_dialog);
        text_moveNm = (TextView)rootView.findViewById(R.id.text_movieNm_delete_dialog);
        img_poster = (ImageView)rootView.findViewById(R.id.img_poster_delete_dialog);
        img_exit = (ImageView)rootView.findViewById(R.id.img_x_mark);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(movieNm.length() > 0){
                    mDatabase.child("MovieList").child(movieNm).setValue(null);
                    dismiss();
                }
            }
        });

        img_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // 전 프래그먼트에서 붙인 Bundle을 통해 영화제목, 포스터url을 가져온다.
        Bundle bundle = getArguments();
        movieNm = bundle.getString("title");
        text_moveNm.setText(movieNm);
        movieUrl = bundle.getString("url");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(movieUrl);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);  // 서버로부터 응답 수신
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    img_poster.setImageBitmap(bitmap);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        return rootView;
    }
}
