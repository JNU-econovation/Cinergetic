package project.bong.com.cinergetic.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import project.bong.com.cinergetic.ListView.CustomerView;
import project.bong.com.cinergetic.R;
import project.bong.com.cinergetic.model.Customer;

public class FootprintsFragment extends Fragment {

    ListView listView;
    CustomerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_footprints, container, false);
        listView = (ListView)rootView.findViewById(R.id.listView_customer);
        adapter = new CustomerAdapter();

        adapter.addItem(new Customer("김주찬", "KIA_kai@my.juchu"));
        adapter.addItem(new Customer("안치홍", "KIA_chichi@my.chihong"));
        adapter.addItem(new Customer("이범호", "KIA_flower@beau.ty"));
        listView.setAdapter(adapter);
        return rootView;
    }

    public class CustomerAdapter extends BaseAdapter{
        ArrayList<Customer> items = new ArrayList<>();

        public void addItem(Customer item){
            items.add(item);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CustomerView view = new CustomerView(getContext());
            Customer item = items.get(position);
            view.setName(item.getName());
            view.setEmail(item.getEmail());

            return view;
        }
    }
}


