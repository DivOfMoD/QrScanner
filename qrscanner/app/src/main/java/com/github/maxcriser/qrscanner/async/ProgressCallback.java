package com.github.maxcriser.qrscanner.async;

public interface ProgressCallback<Progress> {

    void onProgressChanged(Progress progress);

}