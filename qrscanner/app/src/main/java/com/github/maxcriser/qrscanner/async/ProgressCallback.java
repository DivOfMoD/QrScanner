package com.github.maxcriser.qrscanner.async;

interface ProgressCallback<Progress> {

    void onProgressChanged(Progress progress);

}