package com.robonette.argubit.robonette.protocol;

import android.icu.text.IDNA;

public interface ConnectionListener
{
    public void onIncomingImgMsg(ImgMsg imgMsg);
    public void onIncomingInfoMsg(InfoMsg infoMsg);
    public void onConnectedStatusChanged(boolean status);
}
