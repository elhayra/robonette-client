package com.robonette.argubit.robonette;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.robonette.argubit.robonette.protocol.ConnectionListener;
import com.robonette.argubit.robonette.protocol.ImgMsg;
import com.robonette.argubit.robonette.protocol.InfoMsg;

public class ImgActivity extends AppCompatActivity implements ConnectionListener
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_control);

        ImageView imgView = findViewById(R.id.imgView);

    }

    @Override
    public void onIncomingImgMsg(ImgMsg imgMsg)
    {

    }

    public void onIncomingInfoMsg(InfoMsg infoMsg) { /* do nothing */ }

    @Override
    public void onConnectedStatusChanged(boolean status)
    {

    }
}
