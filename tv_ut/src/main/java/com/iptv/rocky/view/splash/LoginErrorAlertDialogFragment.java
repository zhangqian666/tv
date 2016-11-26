package com.iptv.rocky.view.splash;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;

public class LoginErrorAlertDialogFragment extends DialogFragment{

	private String title;
	private String message;
	
	 public interface DialogFragmentClickImpl {
	        void doPositiveClick();
	        void doNegativeClick();
	    }
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LogUtils.error("onCreateDialog");
        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_error)
                .setTitle(title)
                .setMessage(message)
                /*.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //DialogFragmentClickImpl impl = (DialogFragmentClickImpl) getActivity();
                            //impl.doPositiveClick();
                        }
                    }
                )
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //DialogFragmentClickImpl impl = (DialogFragmentClickImpl) getActivity();
                                //impl.doNegativeClick();
                            }
                        }
                )*/
                .create();
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
