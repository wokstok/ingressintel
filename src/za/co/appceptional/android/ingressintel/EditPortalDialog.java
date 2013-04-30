package za.co.appceptional.android.ingressintel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;

public class EditPortalDialog extends DialogFragment
{
	PortalInfo portalInfo = null;
	EditText txtName;
	EditText txtLocation;
	Switch swTeam;
	SeekBar sbLevel;
	
	public EditPortalDialog ()
	{
		
	}
	
	@Override
	public void setArguments(Bundle args)
	{
		portalInfo = PortalInfo.fromBundle (args);
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.portaledit, container, false);
		txtName = (EditText)view.findViewById (R.id.txtPortalName);
		txtLocation = (EditText)view.findViewById (R.id.txtPortalLocation);
		swTeam = (Switch)view.findViewById(R.id.swPortalTeam);
		sbLevel = (SeekBar)view.findViewById(R.id.sbPortalLevel);
		
		if (portalInfo != null)
		{
			txtName.setText (portalInfo.name);
			txtLocation.setText (portalInfo.location);
			swTeam.setChecked(portalInfo.team.equals("Enlightenere"));
			sbLevel.setProgress(portalInfo.level);
		}
		
		Button btnBack = (Button)view.findViewById(R.id.buttonPortalBack);
		btnBack.setOnClickListener(new OnClickListener (){

			@Override
			public void onClick(View v) {
				getDialog().dismiss();
			}
		});
		
		Button btnSave = (Button)view.findViewById(R.id.buttonPortalSave);
		btnSave.setOnClickListener(new OnClickListener (){

			@Override
			public void onClick(View v) {
				if (portalInfo == null)
				{
					String currentTime = new SimpleDateFormat().format(Calendar.getInstance().getTime());
					PortalInfo portalInfo = new PortalInfo ();
					portalInfo.id = UUID.randomUUID().toString();
					portalInfo.name = txtName.getText().toString();
					portalInfo.location = txtLocation.getText().toString();
					portalInfo.dateCreated = currentTime;
					portalInfo.dateModified = currentTime;
					portalInfo.team = swTeam.isChecked() ? "Enlightened" : "Resistance";
					portalInfo.level = sbLevel.getProgress();
					
					AsyncAddPortal.run (((MainActivity)getActivity()).portalsFragment, portalInfo);
					
					getDialog().dismiss();
				} else
				{
					String currentTime = new SimpleDateFormat().format(Calendar.getInstance().getTime());
					portalInfo.name = txtName.getText().toString();
					portalInfo.location = txtLocation.getText().toString();
					portalInfo.dateModified = currentTime;
					portalInfo.team = swTeam.isChecked() ? "Enlightened" : "Resistance";
					portalInfo.level = sbLevel.getProgress();
					
					AsyncUpdatePortal.run (((MainActivity)getActivity()).portalsFragment, portalInfo);
					
					getDialog().dismiss();
				}
			}
		});

		getDialog ().setTitle ("Add a new portal");
		return view;
	}
}
