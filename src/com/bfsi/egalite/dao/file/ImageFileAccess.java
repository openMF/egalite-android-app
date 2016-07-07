package com.bfsi.egalite.dao.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.graphics.Bitmap;

import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.util.SystemUtil;

public class ImageFileAccess {

	private static ImageFileAccess instance = new ImageFileAccess();

	private ImageFileAccess() {
	}

	public static ImageFileAccess getInstance() {
		return instance;
	}

	public void writeBitmap(Bitmap myBitmap2, String fileName)
			throws DataAccessException {
		Bitmap bitmap = myBitmap2;
		File dir = CommonContexts
				.getSdFileDirectory(CommonContexts.TRANSACTION_FILE_DIRECTORY);
		try {
			SystemUtil.imageToFile(bitmap, dir, fileName);
		} catch (FileNotFoundException e) {
			throw new DataAccessException(Constants.ERR_WRITNG_IMAGE
					+ e.getMessage(), e);
		} catch (IOException e) {
			throw new DataAccessException(Constants.ERR_WRITNG_IMAGE
					+ e.getMessage(), e);
		} catch (Exception e) {
			throw new DataAccessException(Constants.ERR_WRITNG_IMAGE
					+ e.getMessage(), e);
		}

	}

	/**
	 * @param txnReference
	 * @return
	 * @throws DataAccessException
	 */

	public Bitmap readBitmap(String txnReference) throws DataAccessException {
		

		return null;
	}

}
