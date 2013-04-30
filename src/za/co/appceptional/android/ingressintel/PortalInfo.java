package za.co.appceptional.android.ingressintel;

import android.os.Bundle;

import com.google.api.client.util.DateTime;

public class PortalInfo
{
	public static String ID = "ID";
	public static String NAME = "NAME";
	public static String LOCATION = "LOCATION";
	public static String DATE_CREATED = "DATE_CREATED";
	public static String DATE_MODIFIED = "DATE_MODIFIED";
	public static String TEAM = "TEAM";
	public static String LEVEL = "LEVEL";
	
	String id;
	String name;
	String location;
	String dateCreated;
	String dateModified;
	String team;
	int level;
	
	public static PortalInfo fromBundle (Bundle bundle)
	{
		PortalInfo portalInfo = new PortalInfo ();
		portalInfo.id = bundle.getString(ID);
		portalInfo.name = bundle.getString(NAME);
		portalInfo.location = bundle.getString(LOCATION);
		portalInfo.dateCreated = bundle.getString(DATE_CREATED);
		portalInfo.dateModified = bundle.getString(DATE_MODIFIED);
		portalInfo.team = bundle.getString(TEAM);
		portalInfo.level = bundle.getInt(LEVEL);
		return portalInfo;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	public Bundle getBundle ()
	{
		Bundle b = new Bundle ();
		b.putString(ID, id);
		b.putString(NAME, name);
		b.putString(LOCATION, location);
		b.putString(DATE_CREATED, dateCreated);
		b.putString(DATE_MODIFIED, dateModified);
		b.putString(TEAM, team);
		b.putInt(LEVEL, level);
		return b;
	}
}
