package com.linkpcom.mitrafast.Classes.DI.Modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import timber.log.Timber;

@Module
@InstallIn(SingletonComponent.class)
public class ApplicationModule {

    @Provides
    @Singleton
    public Timber.DebugTree debugTree() {
        return new Timber.DebugTree();
    }


}
