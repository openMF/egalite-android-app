package com.bfsi.egalite.evolute;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;

/**
 * Use of this class, you need to have the following two permissions <br /> 
  * & Lt; uses-permission android: name = "android.permission.BLUETOOTH" / & gt; <br /> 
  * & Lt; uses-permission android: name = "android.permission.BLUETOOTH_ADMIN" / & gt; <br /> 
  * Android supported versions LEVEL 4 or more, and LEVEL 17 support bluetooth 4 of ble equipment
 * */
@SuppressLint("NewApi")
public class BluetoothComm{
	/**常�?:SPP的Service UUID*/
	public final static String UUID_STR = "00001101-0000-1000-8000-00805F9B34FB";
	/**Bluetooth address code*/
	private String msName;
	/**Bluetooth connection status*/
	private boolean mbConectOk = false;

	/* Get Default Adapter */
	private BluetoothAdapter mBT = BluetoothAdapter.getDefaultAdapter();
	/**Bluetooth serial port connection object*/
	private BluetoothSocket mbsSocket = null;
	/** Input stream object */
	public static InputStream misIn = null;
	/** Output stream object */
	public static OutputStream mosOut = null;
	/**Constant: The current Adnroid SDK version number*/
	private static final int SDK_VER;
	static{
		SDK_VER = Build.VERSION.SDK_INT;
	};

	/**
	 * Constructor 
	 * @param sMAC Bluetooth device MAC address required to connect
	 * */
	public BluetoothComm(String sName){
		this.msName = sName;
	}
	/**
	 * Disconnect the Bluetooth device connection
	 * @return void
	 * */
	public void closeConn(){
		if ( this.mbConectOk ){
			try{
				if (null != this.misIn)
					this.misIn.close();
				if (null != this.mosOut)
					this.mosOut.close();
				if (null != this.mbsSocket)
					this.mbsSocket.close();
				this.mbConectOk = false;//Mark the connection has been closed
			}catch (IOException e){
				//Any part of the error, will be forced to close socket connection
				this.misIn = null;
				this.mosOut = null;
				this.mbsSocket = null;
				this.mbConectOk = false;//Mark the connection has been closed
			}
		}
	}

	/**
	 * Bluetooth devices establish serial communication connection <br /> 
	 * This function is best to put the thread to call, because it will block the system when calling 
	 * @return Boolean false: connection creation failed / true: the connection is created successfully
	 * */
	final public boolean createConn(){
		if (! mBT.isEnabled())
			return false;

		//If a connection already exists, disconnect
		if (mbConectOk)
			this.closeConn();

		/*Start Connecting a Bluetooth device*/
    	final BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(this.msName);
    	final UUID uuidComm = UUID.fromString(UUID_STR);
		try{
			//Get devices connected immediately after the connection is created 
			if (SDK_VER >= 10)//2.3.3 more devices need to create a communication connection in this way
				this.mbsSocket = device.createInsecureRfcommSocketToServiceRecord(uuidComm);
			else //Create  connection API level 5
				this.mbsSocket = device.createRfcommSocketToServiceRecord(uuidComm);
			
			this.mbsSocket.connect();
			this.mosOut = this.mbsSocket.getOutputStream();//Get global output stream object
			this.misIn = this.mbsSocket.getInputStream(); //Get global streaming input object
			this.mbConectOk = true; //Device is connected successfully
			
		}catch (IOException e){
			this.closeConn();//Disconnect
			return false;
		}
		return true;
	}
	
	/**
	 * If the communication device has been established 
	 * @return Boolean true: communication has been established / false: communication lost
	 * */
	public boolean isConnect()	{
		return this.mbConectOk;
	}

}
