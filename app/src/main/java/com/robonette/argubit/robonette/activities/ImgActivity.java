/*******************************************************************************
 * Copyright (c) 2018, Elchay Rauper
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of Elchay Rauper nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/

package com.robonette.argubit.robonette.activities;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.robonette.argubit.robonette.R;
import com.robonette.argubit.robonette.protocol.messages.CompressedImgMsg;
import com.robonette.argubit.robonette.protocol.ConnectionListener;
import com.robonette.argubit.robonette.protocol.ConnectionManager;
import com.robonette.argubit.robonette.protocol.messages.ImgMsg;
import com.robonette.argubit.robonette.protocol.messages.InfoMsg;
import com.robonette.argubit.robonette.protocol.messages.MapMsg;
import com.robonette.argubit.robonette.utils.JoystickView;

import java.io.ByteArrayInputStream;

public class ImgActivity extends AppCompatActivity implements ConnectionListener, JoystickView.JoystickListener
{
    boolean strechImgToMatchScreen = true;
    ImageView imgView;
    final String TAG = "ImgActivity";

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getSupportActionBar().hide();
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Start activity in landscape mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_img_control);

        imgView = findViewById(R.id.imgView);

        Switch strechSwitch = findViewById(R.id.stretchSwitch);
        strechSwitch.setChecked(true);
        strechSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                strechImgToMatchScreen = isChecked;
            }
        });

        ConnectionManager.getInstance().subscribe(this);
    }

    public void onIncomingImgMsg(ImgMsg imgMsg)
    {
        byte [] imgBytes = imgMsg.getData();
        int width = imgMsg.getWidth();
        int height = imgMsg.getHeight();
        final Bitmap bitmap = Bitmap.createBitmap  (width, height, Bitmap.Config.ARGB_8888);

        int[] colors = new int[width * height];
        int r,g,b;
        for (int ci = 0; ci < colors.length; ci++)
        {
            b = (int)(0xFF & imgBytes[ci*3]);
            g = (int)(0xFF & imgBytes[ci*3 + 1]);
            r = (int)(0xFF & imgBytes[ci*3 + 2]);
            colors[ci] = Color.rgb(r, g, b);
        }
        bitmap.setPixels(colors, 0, width, 0, 0, width, height);
        showBitMap(bitmap);
    }

    public void onIncomingCompressedImgMsg(CompressedImgMsg compressedImgMsg)
    {

        final byte [] imgBytes = compressedImgMsg.getData();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imgBytes);
        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        // get display dimensions
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        if (strechImgToMatchScreen)
            showBitMap(Bitmap.createScaledBitmap(bitmap, screenWidth, screenHeight, false));
        else
            showBitMap(bitmap);


    }

    public void showBitMap(final Bitmap bitmap)
    {
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                imgView.setImageBitmap(bitmap);
            }
        });
    }

    public void onIncomingInfoMsg(InfoMsg infoMsg)
    {

    }

    @Override
    public void onIncomingMapMsg(MapMsg mapMsg)
    {

    }

    @Override
    public void onConnectedStatusChanged(boolean status)
    {

    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id)
    {
        Log.i(TAG, "x: " + xPercent + "| y: " + yPercent);
    }
}