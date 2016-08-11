package com.bfsi.egalite.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bfsi.egalite.listeners.CommandListener;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.view.R;

/**
 * Creates Template for common fragments
 * @author Vijay
 *
 */
public abstract class BaseActivity extends FragmentActivity implements OnClickListener {
    protected Logger LOG = LoggerFactory.getLogger(getClass());
    
    public static final int BTN_HOME = 0;
	public static final int BTN_LOAN = 1;
	public static final int BTN_DEPOSIT = 2;
	public static final int BTN_ENROL = 3;
    public static TextView mTitle;
    public static ImageView mBtnLeft, mBtnRight;
    public RelativeLayout mTitleLayout;
    public ViewGroup mMiddleFrame;
    private int command;
    private List<CommandListener> commandListeners = new ArrayList<CommandListener>();
    public static LinearLayout mLinearLayout;
    public static TextView mTxvErrorMsg;
    public static RelativeLayout rellayout;
    public static TextToSpeech ttsp;
    
    public void registerCommandListener(CommandListener listener) {
        if (commandListeners.indexOf(listener) == -1) {
            commandListeners.add(listener);
        }
    }
    /**
     * Unregister previously added listener belonging to the same class as the
     * current listener
     * @param listener
     */
    public void unregisterCommandListener(CommandListener listener) {
        CommandListener listenerToRemove = null;
        for (CommandListener l : commandListeners) {
            if (l.getClass().equals(listener.getClass())) {
                listenerToRemove = l;
                break;
            }
        }
        if (listenerToRemove != null) {
            commandListeners.remove(listenerToRemove);
        }
    }

    public void removeAllListener() {
        commandListeners.removeAll(null);
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
        notifyListeners();
    }

    /**
     * Notifies the event listeners
     */
    @SuppressWarnings("unchecked")
	protected void notifyListeners() {
        List<CommandListener> commandListenersClone = (List<CommandListener>) ((ArrayList<CommandListener>) commandListeners)
                .clone();
        for (CommandListener listener : commandListenersClone) {
            listener.update(getCommand());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        CommonContexts.onActivityCreateSetTheme(this);
        setContentView(R.layout.base_layout);
        init();        
    }

    public void logInfo(String message) {
        
    }
    
    /**
     * initialize the widgets
     */
    private void init() {
        mMiddleFrame = (ViewGroup) findViewById(R.id.middle_content_frame);
        mTitleLayout = (RelativeLayout) findViewById(R.id.title_layout);
        mTitle = (TextView) mTitleLayout.findViewById(R.id.title);
        mBtnLeft = (ImageView) mTitleLayout.findViewById(R.id.btn_left);
        mBtnRight = (ImageView) mTitleLayout.findViewById(R.id.btn_right);
        mLinearLayout =  (LinearLayout)findViewById(R.id.linlay_loginlayout);
		mTxvErrorMsg=(TextView)findViewById(R.id.txv_login_layout_error);
		rellayout= (RelativeLayout)findViewById(R.id.rellay_custom_title);
		ttsp = new TextToSpeech(BaseActivity.this, 
			      new TextToSpeech.OnInitListener() {
		      @Override
		      public void onInit(int status) {
		         if(status != TextToSpeech.ERROR){
		        	 ttsp.setLanguage(Locale.UK);
		            }				
		         }
		      });
		 mBtnLeft.setOnClickListener(onclickListener);
         mBtnRight.setOnClickListener(onclickListener);
    }
	OnClickListener onclickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
        	
			if (v == mBtnLeft) {
				onLeftAction();
			} else if (v == mBtnRight) {
				onRightAction();
			}
        }
    };
	
    
    protected void onLeftAction() {
        setCommand(CommandListener.CMD_LEFT_ACTION);
    }

    protected void onRightAction() {
        setCommand(CommandListener.CMD_RIGHT_ACTION);
    }

    @Override
    public void onClick(View view) {
    	
       
    }
}
