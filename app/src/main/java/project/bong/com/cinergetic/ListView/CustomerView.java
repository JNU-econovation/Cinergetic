package project.bong.com.cinergetic.ListView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;


import project.bong.com.cinergetic.R;

public class CustomerView extends LinearLayout {

    TextView name;
    TextView email;

    public CustomerView(Context context) {
        super(context);
        init(context);
    }

    public CustomerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_footsteps, this, true);
        name = (TextView)findViewById(R.id.item_name);
        email = (TextView)findViewById(R.id.item_email);
    }

    public void setName(String name){
        this.name.setText(name);
    }

    public void setEmail(String email){
        this.email.setText(email);
    }
}
