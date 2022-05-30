package com.visionstech.whatsappview;

import android.Manifest;
import android.app.Activity;
import android.media.MediaRecorder;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.List;

import timber.log.Timber;

public class VoiceRecorder {

    Activity mActivity;
    private String filePath = null;
    private MediaRecorder recorder = null;


    public VoiceRecorder(Activity activity) {
        mActivity = activity;
        // Record to the external cache directory for visibility
        filePath = mActivity.getExternalCacheDir().getAbsolutePath();
        filePath += "/AudioRecord.3gp";


        Dexter.withContext(mActivity)
                .withPermissions(
                        Manifest.permission.RECORD_AUDIO
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (!report.areAllPermissionsGranted()) {
                    mActivity.finish();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();

    }

    public void onStop() {
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
    }

    public void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(filePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Timber.e("prepare() failed");
        }
        recorder.start();
    }

    public void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void stopRecording() {

        try {
            recorder.stop();
        } catch (RuntimeException stopException) {
            recorder.release();
            recorder = null;
        }
    }

    public String getFilePath() {
        return filePath;
    }
}
