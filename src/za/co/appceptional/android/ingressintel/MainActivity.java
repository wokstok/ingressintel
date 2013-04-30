package za.co.appceptional.android.ingressintel;

import java.util.zip.Inflater;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.FusiontablesScopes;

import android.accounts.AccountManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private static final String PREF_ACCOUNT_NAME = "accountName";
	static final String TAG = "IngressIntel";
	static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
	static final int REQUEST_AUTHORIZATION = 1;
	static final int REQUEST_ACCOUNT_PICKER = 2;
	final HttpTransport transport = AndroidHttp.newCompatibleTransport();
	final JsonFactory jsonFactory = new GsonFactory();
	GoogleAccountCredential credential;
	Fusiontables fusionService;
	int numAsyncTasks;
	PortalsFragment portalsFragment;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Create the adapter that will return a fragment for each of the three
		// primary sections
		// of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab.
		// We can also use ActionBar.Tab#select() to do this if we have a
		// reference to the
		// Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter.
			// Also specify this Activity object, which implements the
			// TabListener interface, as the
			// listener for when this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		fusionService = getFusionTablesService();
	}

	private Fusiontables getFusionTablesService() {
		// Google Accounts
		credential = GoogleAccountCredential.usingOAuth2(this,
				FusiontablesScopes.FUSIONTABLES);
		SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
		credential.setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME,
				null));
		// Tasks client
		return new com.google.api.services.fusiontables.Fusiontables.Builder(
				transport, jsonFactory, credential).setApplicationName(
				"Google-TasksAndroidSample/1.0").build();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			if (portalsFragment != null)
				AsyncLoadPortals.run(portalsFragment);
			break;
		case R.id.menu_accounts:
			chooseAccount();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		public static final String ARG_SECTION_NUMBER = "section_number";

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment;
			Bundle args;

			switch (i) {
			case 0:
				portalsFragment = new PortalsFragment();
				args = new Bundle();
				args.putInt(ARG_SECTION_NUMBER, i);
				portalsFragment.setArguments(args);
				return portalsFragment;
			default:
				fragment = new DummySectionFragment();
				args = new Bundle();
				args.putInt(ARG_SECTION_NUMBER, i + 1);
				fragment.setArguments(args);
				return fragment;
			}
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase();
			case 1:
				return getString(R.string.title_section2).toUpperCase();
			case 2:
				return getString(R.string.title_section3).toUpperCase();
			}
			return null;
		}
	}

	void showGooglePlayServicesAvailabilityErrorDialog(
			final int connectionStatusCode) {
		runOnUiThread(new Runnable() {
			public void run() {
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
						connectionStatusCode, MainActivity.this,
						REQUEST_GOOGLE_PLAY_SERVICES);
				dialog.show();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (checkGooglePlayServicesAvailable()) {
			haveGooglePlayServices();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_GOOGLE_PLAY_SERVICES:
			if (resultCode == Activity.RESULT_OK) {
				haveGooglePlayServices();
			} else {
				checkGooglePlayServicesAvailable();
			}
			break;
		case REQUEST_AUTHORIZATION:
			if (resultCode == Activity.RESULT_OK) {
				if (portalsFragment != null)
					AsyncLoadPortals.run(portalsFragment);
			} else {
				chooseAccount();
			}
			break;
		case REQUEST_ACCOUNT_PICKER:
			if (resultCode == Activity.RESULT_OK && data != null
					&& data.getExtras() != null) {
				String accountName = data.getExtras().getString(
						AccountManager.KEY_ACCOUNT_NAME);
				if (accountName != null) {
					credential.setSelectedAccountName(accountName);
					SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString(PREF_ACCOUNT_NAME, accountName);
					editor.commit();
					if (portalsFragment != null)
						AsyncLoadPortals.run(portalsFragment);
				}
			}
			break;
		}
	}

	/** Check that Google Play services APK is installed and up to date. */
	private boolean checkGooglePlayServicesAvailable() {
		final int connectionStatusCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
			showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
			return false;
		}
		return true;
	}

	private void haveGooglePlayServices() {
		// check if there is already an account selected
		if (credential.getSelectedAccountName() == null) {
			// ask user to choose account
			chooseAccount();
		} else {
			// load calendars
			if (portalsFragment != null)
				AsyncLoadPortals.run(portalsFragment);
		}
	}

	private void chooseAccount() {
		startActivityForResult(credential.newChooseAccountIntent(),
				REQUEST_ACCOUNT_PICKER);
	}

	public static class PortalsFragment extends Fragment {
		public PortalsFragment() {
		}

		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.portalview, container, false);
			Button btnAddPortal = (Button)view.findViewById(R.id.addPortal);
			btnAddPortal.setOnClickListener(new OnClickListener ()
			{
				@Override
				public void onClick(View v)
				{
					android.app.FragmentManager fm = getActivity().getFragmentManager();
					new EditPortalDialog().show (fm, "New Portal");
				}
			});
			
			final ListView lstPortals = (ListView)view.findViewById(R.id.portalList);
			lstPortals.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View v,
						int index, long arg3)
				{
					PortalInfo portalInfo = (PortalInfo)lstPortals.getItemAtPosition(index);
					android.app.FragmentManager fm = getActivity().getFragmentManager();
					EditPortalDialog dlg = new EditPortalDialog();
					dlg.setArguments(portalInfo.getBundle());
					dlg.show(fm, "Edit Portal");
					return true;
				}
			});
			return view;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			Bundle args = getArguments();
			textView.setText(Integer.toString(args
					.getInt(SectionsPagerAdapter.ARG_SECTION_NUMBER)));
			return textView;
		}
	}

}
