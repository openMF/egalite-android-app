
package com.bfsi.egalite.view;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Application;
import ch.qos.logback.classic.Level;

import com.bfsi.egalite.evolute.BluetoothComm;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.util.LogUtil;

/**
 * To create application contexts. Irrespective of activity context, in-general
 * scenarios the same can be used
 * 
 * @author Vijay
 */
public class MfiApplication extends Application {
    private static MfiApplication instance;
    protected Logger LOG;
    /**Bluetooth communication connection object*/
   	public BluetoothComm mBTcomm = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // initialize logging
        initLogging();
        LOG = LoggerFactory.getLogger(getClass());
        LOG.info(Constants.EGALITE_MFI_app);
    }

    public static MfiApplication getInstance() {
        return instance;
    }

    /**
     * Initializes logging with log file path
     */
    public void initLogging() {
        File logDir = CommonContexts.getSdFileDirectory(CommonContexts.LOG_FILE_PATH);
        String logFileName = CommonContexts.LOG_FILE_NAME;
        LogUtil.configureLogger(new File(logDir, logFileName), Level.ERROR);
    }
    
    /**
	 * Set up a Bluetooth connection 
	 * @param String sMac Bluetooth hardware address 
	 * @return Boolean
	 * */
	public boolean createConn(String sName){
		if (null == this.mBTcomm)
		{
			this.mBTcomm = new BluetoothComm(sName);
			if (this.mBTcomm.createConn())
				return true;
			else{
				this.mBTcomm = null;
				return false;
			}
		}
		else
			return true;
	}
	
	/**
	 * Close and release the connection
	 * @return void
	 * */
	public void closeConn(){
		if (null != this.mBTcomm){
			this.mBTcomm.closeConn();
			this.mBTcomm = null;
		}
	}
}
