/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package za.co.appceptional.android.ingressintel;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This shows how to retain a map across activity restarts (e.g., from screen
 * rotations), which can be faster than relying on state serialization.
 */
public class RetainMapActivity extends Fragment {

	private GoogleMap mMap;
	private static final LatLng ADELAIDE = new LatLng(-34.92873, 138.59995);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mapview, container, false);
		setUpMapIfNeeded();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	/**
	 * Checks if the map is ready (which depends on whether the Google Play
	 * services APK is available. This should be called prior to calling any
	 * methods on GoogleMap.
	 */
	private boolean checkReady() {
		if (mMap == null) {
//			Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT)
//					.show();
			Log.d("RetainMapActivity","Map not ready");
			return false;
		}
		return true;
	}

	private void setUpMap() {
		mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title(
				"Marker"));

		if (checkReady()) {
			// Enables/disables the my location layer (i.e., the dot/chevron on
			// the
			// map). If enabled, it
			// will also cause the my location button to show (if it is
			// enabled); if
			// disabled, the my
			// location button will never show.
			mMap.setMyLocationEnabled(true);

			// Show the traffic layer
			mMap.setTrafficEnabled(true);

			// Create a settings object to alter the map properties
			UiSettings mUiSettings = mMap.getUiSettings();

			// Enables/disables the zoom controls (+/- buttons in the bottom
			// right
			// of the map).
			mUiSettings.setZoomControlsEnabled(true);
			// Enables/disables the compass (icon in the top left that indicates
			// the
			// orientation of the
			// map).
			mUiSettings.setCompassEnabled(true);
			// Enables/disables the my location button (this DOES NOT
			// enable/disable
			// the my location
			// dot/chevron on the map). The my location button will never appear
			// if
			// the my location
			// layer is not enabled.
			mUiSettings.setMyLocationButtonEnabled(true);

			// Enables/disables scroll gestures (i.e. panning the map).
			mUiSettings.setScrollGesturesEnabled(true);
			// Enables/disables zoom gestures (i.e., double tap, pinch &
			// stretch).
			mUiSettings.setZoomGesturesEnabled(true);
			// Enables/disables tilt gestures.
			mUiSettings.setTiltGesturesEnabled(true);
			// Enables/disables rotate gestures.
			mUiSettings.setRotateGesturesEnabled(true);
		}

	}

}
