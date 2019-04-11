package com.foxware.sites;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Alerts extends Fragment {

    Button reload;
    Bundle args;
    String TYPE;
    TextView Title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_alerts, container, false);
        Title = view.findViewById(R.id.finish2);
        reload = (Button) view.findViewById(R.id.btn_end);

        args = getArguments();
        TYPE = args.getString("TYPE");

        if (TYPE.equals("Empty") || TYPE.equals("NOSAVED")){
            String Tit = args.getString("Title");
            String Btn = args.getString("Button");

            Title.setText(Tit);
            reload.setText(Btn);
        }

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
            }
        });

        return view;
    }

    public void checkConnection(){
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        final FragmentManager fm= getActivity().getSupportFragmentManager();

        if(isConnected){
            if(TYPE.equals("Near")){
                fm.beginTransaction().replace(R.id.Contenedor, new NearMe()).commit();
            }else if(TYPE.equals("Start")){
                fm.beginTransaction().replace(R.id.Contenedor, new StartFragment()).commit();
            }else if(TYPE.equals("Empty")){
                Intent x = new Intent(getContext(), AddSite.class);
                startActivity(x);
            }else if(TYPE.equals("Save")){
                fm.beginTransaction().replace(R.id.Contenedor, new Notifications()).commit();
            }else if(TYPE.equals("NOSAVED")){
                fm.beginTransaction().replace(R.id.Contenedor, new StartFragment()).commit();
            }
        }
    }


}
