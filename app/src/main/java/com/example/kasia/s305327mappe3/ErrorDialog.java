package com.example.kasia.s305327mappe3;

import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Kasia on 01.12.2017.
 */

//Class for å vise error meldinger
public class ErrorDialog extends DialogFragment {

    private static int activity;
    private DialogClickListener callback;

    public interface DialogClickListener{
        public void exit();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            callback = (DialogClickListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Implement the interface");
        }
        super.onCreate(savedInstanceState);
    }

    public static ErrorDialog newInstance(String title, String message, int act) {
        ErrorDialog dialog = new ErrorDialog();
        Bundle args = new Bundle();
        args.putString("Title", title);
        args.putString("Message", message);
        activity = act;
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 200;
        params.y = 100;
        window.setAttributes(params);

        return inflater.inflate(activity, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("Title");
        String message = getArguments().getString("Message");
        return new AlertDialog.Builder(getActivity())
                .setTitle(title).setMessage(message).setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                callback.exit();
                            }
                        })
                .setCancelable(false).create();
    }
}
