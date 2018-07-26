package project.bong.com.cinergetic.ListView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import project.bong.com.cinergetic.R;

public class MovieListView extends LinearLayout{

    TextView movieNm;

    public MovieListView(Context context) {
        super(context);
        init(context);
    }

    public MovieListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_registered_movie, this, true);
        movieNm = (TextView)findViewById(R.id.text_registed_movieNm);
    }

    public void setMovieNm(String name){
        this.movieNm.setText(name);
    }

}
