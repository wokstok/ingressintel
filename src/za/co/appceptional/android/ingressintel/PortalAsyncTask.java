package za.co.appceptional.android.ingressintel;

import java.io.IOException;
import java.util.List;

import za.co.appceptional.android.ingressintel.MainActivity.PortalsFragment;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

abstract class PortalAsyncTask extends AsyncTask<Void, Void, Boolean> {

	  final PortalsFragment fragment;
	  final com.google.api.services.fusiontables.Fusiontables client;
	  private final View progressBar;
	  private ListView listView;
	  protected List<PortalInfo> tasksList = null;

	  PortalAsyncTask(PortalsFragment fragment) {
	    this.fragment = fragment;
	    client = ((MainActivity)fragment.getActivity()).fusionService;
	    progressBar = fragment.getView().findViewById(R.id.portalRefresh);
	    listView = (ListView)fragment.getView().findViewById(R.id.portalList);
	  }

	  @Override
	  protected void onPreExecute() {
	    super.onPreExecute();
	    ((MainActivity)fragment.getActivity()).numAsyncTasks++;
	    progressBar.setVisibility(View.VISIBLE);
	  }

	  @Override
	  protected final Boolean doInBackground(Void... ignored) {
	    try {
	      doInBackground();
	      return true;
	    } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
	    	((MainActivity)fragment.getActivity()).showGooglePlayServicesAvailabilityErrorDialog(
	          availabilityException.getConnectionStatusCode());
	    } catch (UserRecoverableAuthIOException userRecoverableException) {
	      fragment.startActivityForResult(
	          userRecoverableException.getIntent(), MainActivity.REQUEST_AUTHORIZATION);
	    } catch (IOException e) {
	      Utils.logAndShow(((MainActivity)fragment.getActivity()), MainActivity.TAG, e);
	    }
	    return false;
	  }

	  @Override
	  protected final void onPostExecute(Boolean success) {
	    super.onPostExecute(success);
	    if (0 == --((MainActivity)fragment.getActivity()).numAsyncTasks) {
	      progressBar.setVisibility(View.GONE);
	    }
	    if (success) {
	      refreshView();
	    }
	  }

	  private void refreshView ()
	  {
		  if (tasksList != null)
		  {
			  ArrayAdapter<PortalInfo> adapter = new ArrayAdapter<PortalInfo>(((MainActivity)fragment.getActivity()), android.R.layout.simple_list_item_1, tasksList);
			  listView.setAdapter(adapter);
		  }
	  }
	  
	  abstract protected void doInBackground() throws IOException;
	}
