package com.foxware.sites;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Extras {
    public static FirebaseDatabase DATABASE = FirebaseDatabase.getInstance();
    public static FirebaseStorage STORAGE = FirebaseStorage.getInstance();
    public static FirebaseUser USER = FirebaseAuth.getInstance().getCurrentUser();
    private static String newName;


    public static String getUserID() {
        return USER.getUid();
    }

    public static String FirstName(String name) {
        newName = name.split(" ")[0].trim();
        return newName;
    }

    public static void makeText(Activity context, String Text) {
        View v = context.getLayoutInflater().inflate(R.layout.toast, (ViewGroup) context.findViewById(R.id.custom_layout));
        TextView toast_txt = (TextView) v.findViewById(R.id.toast_txt);
        Toast x = Toast.makeText(context, Text, Toast.LENGTH_SHORT);
        toast_txt.setText(Text);
        x.setGravity(80, 0, 0);
        x.setView(v);
        x.show();
    }

    public static String[] separate(String sentence) {
        return sentence.split(",");
    }

    public static boolean checkConnectivity(Context context) {
        NetworkInfo activeNetwork = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void checkConnection2(Activity activity) {
        NetworkInfo activeNetwork = ((ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        FragmentManager fm = activity.getFragmentManager();
        Fragment Alert = new Estados();
        if (!isConnected) {
            Bundle args = new Bundle();
            args.putString("TYPE", "Cuentas-Off");
            Alert.setArguments(args);
            fm.beginTransaction().replace(R.id.Contenedor, Alert).commit();
        }
    }

    public static String IdFormat(String title) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        String hora = new SimpleDateFormat("HH:mm:ss").format(date);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(FirstName(title));
        stringBuilder.append(fecha);
        stringBuilder.append(hora);
        return stringBuilder.toString();
    }

    public static String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        String hora = new SimpleDateFormat("HH:mm").format(date);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(fecha);
        stringBuilder.append(hora);
        return stringBuilder.toString();
    }

    public static String Spaces(String regex) {
        return regex.replace(" ", "");
    }

    public static void Empty(Activity activity) {
        FragmentManager fm = activity.getFragmentManager();
        Fragment Alert = new Estados();
        Bundle args = new Bundle();
        args.putString("TYPE", "Empleados-Empty");
        args.putString("Title", activity.getString(R.string.state_title1));
        args.putString("Button", "");
        Alert.setArguments(args);
        fm.beginTransaction().replace(R.id.Contenedor, Alert).commitAllowingStateLoss();
    }

    public static void Fill(Activity activity) {
        FragmentManager fm = activity.getFragmentManager();
        fm.beginTransaction().replace(R.id.Contenedor, new Fragment()).commitAllowingStateLoss();
    }

    public static String convert(String string){

        char[] caracteres = string.toCharArray();
        caracteres[0] = Character.toUpperCase(caracteres[0]);
        for (int i = 0; i < string.length() -1; i++){
            if(caracteres[i] == ' ' || caracteres[i] == '.' || caracteres[i] == ','){
                caracteres[i+1] = Character.toUpperCase(caracteres[i+1]);
            }else{
                caracteres[i+1] = Character.toLowerCase(caracteres[i+1]);
            }
        }return new String(caracteres);
    }
}
