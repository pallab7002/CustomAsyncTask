package com.customasynctask.com;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.customasynctask.com.Utility.CustomAsyncTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startAsyncTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startAsyncTaskButton = (Button) findViewById(R.id.startAsyncTask);

        startAsyncTaskButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.startAsyncTask){
            new BackgroundTask(MainActivity.this).execute();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ((CustomAsyncTaskApplication) getApplication()).mAsyncTaskHelper.detach(this);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        ((CustomAsyncTaskApplication) getApplication()).mAsyncTaskHelper.attach(this);
    }

    private static class BackgroundTask extends CustomAsyncTask<Void, Integer, Void> {

        private static final String TAG = BackgroundTask.class.getSimpleName();

        private ProgressDialog mProgress;
        private int mCurrProgress;

        public BackgroundTask(Activity activity) {
            super(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected void onActivityDetached() {
            if (mProgress != null) {
                mProgress.dismiss();
                mProgress = null;
            }
        }

        @Override
        protected void onActivityAttached() {
            showProgressDialog();
        }

        private void showProgressDialog() {
            mProgress = new ProgressDialog(mActivity);
            mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgress.setMessage("Please Wait...");
            mProgress.setCancelable(true);
            mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(true);
                }
            });

            mProgress.show();
            mProgress.setProgress(mCurrProgress);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (int i = 0; i < 100; i+=5) {
                    Thread.sleep(1000);
                    this.publishProgress(i);
                }

            }
            catch (InterruptedException e) {
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            mCurrProgress = progress[0];
            if (mActivity != null) {
                mProgress.setProgress(mCurrProgress);
            }
            else {
                Log.d(TAG, "Progress updated while no Activity was attached.");
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (mActivity != null) {
                mProgress.dismiss();
                Toast.makeText(mActivity, "AsyncTask finished", Toast.LENGTH_LONG).show();
            }
            else {
                Log.d(TAG, "AsyncTask finished while no Activity was attached.");
            }
        }
    }
}
