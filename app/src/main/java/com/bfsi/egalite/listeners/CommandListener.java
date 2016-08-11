package com.bfsi.egalite.listeners;


/**
 * To handle BaseActivity <left and right> buttons
 * @author vijay
 *
 */
public interface CommandListener {
	public static final int CMD_LEFT_ACTION = 1001;
	public static final int CMD_RIGHT_ACTION = 1002;
	
	public void update(int command);
}
