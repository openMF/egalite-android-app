package com.bfsi.egalite.exceptions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.bfsi.egalite.view.R;

/**
 * Exception handler for all type of errors
 * @author Vijay
 *
 */
public class ExceptionHandler {
	public static void makeExceptionAlert(Context context, Exception ex) {
		String headerText = "";
		String messageText = "";

		if (ex instanceof ArrayIndexOutOfBoundsException) {
			headerText = context.getText(R.string.error).toString();
			messageText = context.getText(
					R.string.ArrayIndexOutOfBoundsException).toString();
		} else if (ex instanceof ClassNotFoundException) {
			headerText = context.getText(R.string.error).toString();
			messageText = context.getText(R.string.ClassNotFoundException)
					.toString();
		} else if (ex instanceof IndexOutOfBoundsException) {
			headerText = context.getText(R.string.error).toString();
			messageText = context.getText(R.string.IndexOutOfBoundsException)
					.toString();
		} else if (ex instanceof NullPointerException) {
			headerText = context.getText(R.string.error).toString();
			messageText = context.getText(R.string.NullPointerException)
					.toString();
		} else if (ex instanceof NumberFormatException) {
			headerText = context.getText(R.string.error).toString();
			messageText = context.getText(R.string.NumberFormatException)
					.toString();
		} else if (ex instanceof RuntimeException) {
			headerText = context.getText(R.string.error).toString();
			messageText = context.getText(R.string.RuntimeException).toString();
		} else {
			headerText = context.getText(R.string.error).toString();
			messageText = context.getText(R.string.Exception).toString()
					+ ex.getMessage();
		}
		showErrorDialog(context, headerText, messageText);
	}
	
	public static void makeExceptionAlert(Context context, Exception ex, String userDefined) {
		String headerText = "";
		String messageText = "";

		if (ex instanceof ArrayIndexOutOfBoundsException) {
			headerText = context.getText(R.string.error).toString();
			messageText = userDefined + context.getString(R.string.ArrayIndexOutOfBoundsException).toString();
		} else if (ex instanceof ClassNotFoundException) {
			headerText = context.getText(R.string.error).toString();
			messageText = userDefined + context.getText(R.string.ClassNotFoundException)
					.toString();
		} else if (ex instanceof IndexOutOfBoundsException) {
			headerText = context.getText(R.string.error).toString();
			messageText = userDefined + context.getText(R.string.IndexOutOfBoundsException)
					.toString();
		} else if (ex instanceof NullPointerException) {
			headerText = context.getText(R.string.error).toString();
			messageText = userDefined + context.getText(R.string.NullPointerException)
					.toString();
		} else if (ex instanceof NumberFormatException) {
			headerText = context.getText(R.string.error).toString();
			messageText = userDefined + context.getText(R.string.NumberFormatException)
					.toString();
		} else if (ex instanceof RuntimeException) {
			headerText = context.getText(R.string.error).toString();
			messageText = userDefined + context.getText(R.string.RuntimeException).toString();
		} else {
			headerText = context.getText(R.string.error).toString();
			messageText = userDefined  + context.getText(R.string.Exception).toString()
					+ ex.getMessage();
		}
		showErrorDialog(context, headerText, messageText);
	}

	private static void showErrorDialog(Context context, String titletext,
			String messagetext) {
		new AlertDialog.Builder(context)
				.setTitle(titletext)
				.setMessage(messagetext)
				.setPositiveButton(R.string.error_close,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}

						}).show();
	}

	public static void exceptionThrower(Exception ex) throws Exception {
		if (ex != null) {
			if (ex instanceof ArrayIndexOutOfBoundsException) {
				throw (ArrayIndexOutOfBoundsException) ex;
			} else if (ex instanceof ClassNotFoundException) {
				throw (ClassNotFoundException) ex;
			} else if (ex instanceof IndexOutOfBoundsException) {
				throw (IndexOutOfBoundsException) ex;
			} else if (ex instanceof NullPointerException) {
				throw (NullPointerException) ex;
			} else if (ex instanceof NumberFormatException) {
				throw (NumberFormatException) ex;
			} else if (ex instanceof RuntimeException) {
				throw (RuntimeException) ex;
			} else {
				throw (Exception) ex;
			}
		}
	}
}
