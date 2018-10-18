package com.example.bosswebtech.secrye.Network.Callback;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;

/**
 * Created by Brajendr on 4/11/2017.
 */

abstract public class NwCall implements NetworkCallback {
    private Context context;

    private boolean isContextUsed = true;
    private ProgressDialog progressDialog;
    private View progressBar;


    public NwCall(View view) {
        this.progressBar = view;
        isContextUsed = false;
    }

    public NwCall(Context context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
    }

    public void onStart() {
        if (isContextUsed) {
            progressDialog.show();
        } else {
            if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void onFinish() {
        if (isContextUsed) {
            progressDialog.hide();

        } else {
            if (progressBar != null) progressBar.setVisibility(View.GONE);
        }
    }
}
