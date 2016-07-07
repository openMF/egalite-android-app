package com.bfsi.egalite.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;

/**
 * Utility class for all common system resource interactions like external
 * storage, network access, etc
 * 
 * @author Vijay
 * 
 */
public class SystemUtil {

	private static final String NETWORK_MOBILE = "MOBILE";
	private static final String NETWORK_WIFI = "WIFI";

	
	public static boolean isSimAvailable(Context context)
	{
		@SuppressWarnings("unused")
		boolean isSim = false;
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getSimState() != TelephonyManager.SIM_STATE_ABSENT){
		  return isSim=true;
		} else {
			return isSim=false;
		}
		
	}
	
	/**
	 * To check network availability
	 * 
	 * @param activity
	 * @return true if either
	 */
	public static boolean haveNetworkConnection(Context activity) {
		ConnectivityManager cm = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.isConnected()) {
				// TODO use preferred network later phases for doing specific
				// operation like sync in WIFI
				if (ni.getTypeName().equalsIgnoreCase(NETWORK_WIFI)
						|| ni.getTypeName().equalsIgnoreCase(NETWORK_MOBILE)) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Checks availability of external media for writing and availability
	 * 
	 * @return
	 */
	public static boolean checkExternalMedia() {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// Can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// Can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Can't read or write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		// TODO check for availability of space
		return (mExternalStorageAvailable && mExternalStorageWriteable);
	}

	/**
	 * Write image to File
	 * @param bitmap
	 * @param dir
	 * @param fileName
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void imageToFile(Bitmap bitmap,File dir,String fileName)throws FileNotFoundException,IOException
	{
		File file = new File(dir, fileName + ".png");
		FileOutputStream fos = new FileOutputStream(file);
		bitmap.compress(CompressFormat.PNG, 100, fos);
		fos.flush();
		fos.close();
		
	}
	
	/**
	 * Write transaction to a file
	 * 
	 * @param data
	 * @param dir
	 * @param filename
	 */
	public static void writeToFile(String data, File dir, String filename)
			throws FileNotFoundException, IOException {

		// Find the root of the external storage.
		File file = new File(dir, filename);
		FileOutputStream f = new FileOutputStream(file);
		PrintWriter pw = new PrintWriter(f);
		pw.println(data);
		pw.flush();
		pw.close();
		f.close();
	}

	public static String readFromFile(File dir, String filename)throws FileNotFoundException, IOException
	{
		String jString = null;
		File yourFile = new File(dir, filename);
		FileInputStream stream = new FileInputStream(yourFile);
		FileChannel fc = stream.getChannel();
		MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,fc.size());
		jString = Charset.defaultCharset().decode(bb).toString();
		stream.close();
		return jString;
		
	}
	
    @SuppressWarnings("deprecation")
	public int busyMemory()
    {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());   
        int total = (statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
        int free  = (statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
        int busy  = total - free;
        return busy;
    }
    
    /**
     * @return Number of bytes available on external storage
     */
    @SuppressWarnings("deprecation")
	public static long getExternalStorageAvailableSpace() {
        long availableSpace = -1L;
        try {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
                    .getPath());
            stat.restat(Environment.getExternalStorageDirectory().getPath());
            availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return availableSpace;
    }
}
