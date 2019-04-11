package com.foxware.sites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

public class AddSite extends AppCompatActivity {

    Button btn_search;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add);
        btn_search = (Button) findViewById(R.id.btn_search);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(AddSite.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                Intent x =new Intent(AddSite.this, AddSite2.class);
                x.putExtra("type", "Add");
                x.putExtra("site_id", place.getId());
                x.putExtra("site_address", String.valueOf(place.getAddress()));
                x.putExtra("site_name", place.getName().toString());
                x.putExtra("site_phone", String.valueOf(place.getPhoneNumber()));
                x.putExtra("site_web",  String.valueOf(place.getWebsiteUri()));
                x.putExtra("site_rating", String.valueOf(place.getRating()));
                x.putExtra("site_lat", String.valueOf(place.getLatLng().latitude));
                x.putExtra("site_long", String.valueOf(place.getLatLng().longitude));
                startActivity(x);
                finish();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i("PLACES", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
}
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
