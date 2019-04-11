package com.foxware.sites;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Estados extends Fragment {
    String TYPE;
    TextView Title;
    Bundle args;
    Button reload;

    /* renamed from: com.foxware.sites.Estados$1 */
    class C03571 implements OnClickListener {
        C03571() {
        }

        public void onClick(View v) {
            Estados.this.checkConnection();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alerts, container, false);
        this.Title = (TextView) view.findViewById(R.id.finish2);
        this.reload = (Button) view.findViewById(R.id.btn_end);
        this.args = getArguments();
        this.TYPE = this.args.getString("TYPE");
        if (this.TYPE.contains("-Empty")) {
            String Tit = this.args.getString("Title");
            String Btn = this.args.getString("Button");
            this.Title.setText(Tit);
            if (Btn != "") {
                this.reload.setText(Btn);
            }
            this.reload.setText(Btn);
        }
        this.reload.setOnClickListener(new C03571());
        return view;
    }

    public void checkConnection() {
        NetworkInfo activeNetwork = ((ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected && this.TYPE.equals("Cuentas-Off")) {
            startActivity(new Intent(getActivity(), Home.class));
        }
    }
}
