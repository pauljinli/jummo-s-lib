package com.stockholm.common.develop;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.stockholm.common.R;
import com.stockholm.common.toast.ToastHelper;
import com.stockholm.common.utils.NetworkTestUtil;


import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class DeveloperDialog implements View.OnClickListener {

    private Context context;
    private AlertDialog dialog;

    public DeveloperDialog(Context context) {
        this.context = context;
        View contentView = LayoutInflater.from(context).inflate(R.layout.v_env_selector, null);
        Button btnWifi = (Button) contentView.findViewById(R.id.btn_wifi);
        Button btnBle = (Button) contentView.findViewById(R.id.btn_ble);
        Button btnSystem = (Button) contentView.findViewById(R.id.btn_system);
        Button btnLauncher = (Button) contentView.findViewById(R.id.btn_launcher);
        Button btnNetwork = (Button) contentView.findViewById(R.id.btn_network);

        btnWifi.setOnClickListener(this);
        btnBle.setOnClickListener(this);
        btnSystem.setOnClickListener(this);
        btnLauncher.setOnClickListener(this);
        btnNetwork.setOnClickListener(this);

        dialog = new AlertDialog.Builder(context)
                .setView(contentView).setTitle("开发者选项")
                .setOnKeyListener((dialog1, keyCode, event) -> {
                    if (keyCode == KeyEvent.KEYCODE_D) {
                        dialog.dismiss();
                    }
                    return true;
                }).create();
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            if (v.getId() == R.id.btn_wifi) {
                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                dismiss();
            } else if (v.getId() == R.id.btn_ble) {
                context.startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                dismiss();
            } else if (v.getId() == R.id.btn_system) {
                context.startActivity(new Intent(Settings.ACTION_SETTINGS));
                dismiss();
            } else if (v.getId() == R.id.btn_launcher) {
                Intent intent = new Intent();
                intent.setClassName("com.stockholm.launcher", "com.stockholm.launcher.splash.SplashActivity");
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                dismiss();
            } else if (v.getId() == R.id.btn_network) {
                String url = context.getString(R.string.dev_url);
                new AsyncTask<String, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(String... params) {
                        return NetworkTestUtil.httpsTest(params[0]);
                    }

                    @Override
                    protected void onPostExecute(Boolean b) {
                        super.onPostExecute(b);
                        if (b) ToastHelper.getInstance(context).showLong("网络正常");
                        else ToastHelper.getInstance(context).showLong("网络异常");
                    }
                }.execute(url);
            }
        }

    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}
