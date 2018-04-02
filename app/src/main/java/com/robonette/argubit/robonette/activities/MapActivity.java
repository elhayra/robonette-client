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
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.robonette.argubit.robonette.R;
import com.robonette.argubit.robonette.protocol.messages.CompressedImgMsg;
import com.robonette.argubit.robonette.protocol.ConnectionListener;
import com.robonette.argubit.robonette.protocol.ConnectionManager;
import com.robonette.argubit.robonette.protocol.messages.ImgMsg;
import com.robonette.argubit.robonette.protocol.messages.InfoMsg;
import com.robonette.argubit.robonette.protocol.messages.MapMsg;

import com.robonette.argubit.robonette.utils.TouchImageView;

public class MapActivity extends AppCompatActivity implements ConnectionListener
{
    boolean strechMapToMatchScreen = false;
    TouchImageView mapView;
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
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.mapView);
        mapView.setZoom(0.99f);
        ConnectionManager.getInstance().subscribe(this);
    }

    @Override
    public void onIncomingMapMsg(MapMsg mapMsg)
    {
        final int BR_R = 100;
        final int BR_G = 150;
        final int BR_B = 255;
        final int UNOCCUPIED = -1;

        byte [] mapBytes = mapMsg.getData();
        int width = mapMsg.getWidth();
        int height = mapMsg.getHeight();
        final Bitmap bitmap = Bitmap.createBitmap  (width, height, Bitmap.Config.ARGB_8888);

        int[] colors = new int[mapBytes.length];
        int r,g,b;
        for (int px = 0; px < mapBytes.length; px++)
        {
            if (mapBytes[px] == UNOCCUPIED)
            {
                r = BR_R;
                g = BR_G;
                b = BR_G;
            }
            else
            {
                float p = (float)((mapBytes[px] & 0xFF) / 100.0);
                int a = 0;


                int x = (int)(255.0 - (255.0 * p));
                r = x;
                g = x;
                b = x;

                if (r != b || b != g || p > 1 || p < 0)
                {
                    a++;
                    a = 1;
                }
            }
            colors[px] = Color.rgb(r, g, b);
        }
        bitmap.setPixels(colors, 0, width, 0, 0, width, height);
        showBitMap(bitmap);
    }

    public void showBitMap(final Bitmap bitmap)
    {
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {

                mapView.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public void onIncomingImgMsg(ImgMsg imgMsg)
    {

    }

    @Override
    public void onIncomingCompressedImgMsg(CompressedImgMsg compressedImgMsg)
    {

    }

    @Override
    public void onIncomingInfoMsg(InfoMsg infoMsg)
    {

    }



    @Override
    public void onConnectedStatusChanged(boolean status)
    {

    }
}
