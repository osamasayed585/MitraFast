package com.linkpcom.mitrafast.Classes.DI.Modules;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.PagerSnapHelper;

import com.visionstech.whatsappview.Chat;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ActivityContext;

@Module
@InstallIn(ActivityComponent.class)
public class ActivityModule {

    @Provides
    public PagerSnapHelper providePagerSnapHelper() {
        return new PagerSnapHelper();
    }

    @Provides
    public AlertDialog providerAlertDialog(@ActivityContext Context context) {
        return new AlertDialog.Builder(context).create();
    }
    @Provides
    public Chat providerChatDialog() {
        return new Chat();
    }

}
