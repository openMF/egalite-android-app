package com.bfsi.egalite.listeners;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.bfsi.egalite.main.Splash;
import com.bfsi.egalite.util.CommonContexts;

/**
 * To access network providers timestamp
 * @author vijay
 *
 */
public class NetworkLocationListener implements LocationListener {

	public void onLocationChanged(Location loc)
	{
		CommonContexts.NETWORKTIMESTAMP = loc.getTime();
		if(CommonContexts.NETWORKTIMESTAMP != 0)
		{
			Splash spalsh = new Splash();
			spalsh.removeLocationUpdates();
		}
	}

	public void onProviderDisabled(String provider)
	{
		//print "Currently GPS is Disabled";
	}
	
	public void onProviderEnabled(String provider)
	{
		//print "GPS got Enabled";
	}
	
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
	}
}
