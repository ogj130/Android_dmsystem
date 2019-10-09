package com.example.myapplication101;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadView {
    /**
     * 加载框
     */
    private ProgressDialog progressDialog;
    public void buildProgressDialog(String values, Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        progressDialog.setMessage(values);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
    /**
     *取消加载框
     */
    public void cancelProgressDialog() {
        if (progressDialog != null)
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
    }
}
