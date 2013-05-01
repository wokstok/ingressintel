package za.co.appceptional.android.ingressintel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

public class PortalsFragment extends Fragment {
	public PortalsFragment() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
