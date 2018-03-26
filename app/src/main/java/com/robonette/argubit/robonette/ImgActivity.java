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

package com.robonette.argubit.robonette;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.util.Preconditions;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.robonette.argubit.robonette.protocol.CompressedImgMsg;
import com.robonette.argubit.robonette.protocol.ConnectionListener;
import com.robonette.argubit.robonette.protocol.ConnectionManager;
import com.robonette.argubit.robonette.protocol.ImgMsg;
import com.robonette.argubit.robonette.protocol.InfoMsg;

import java.io.ByteArrayInputStream;

public class ImgActivity extends AppCompatActivity implements ConnectionListener
{
    ImageView imgView;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_control);

        imgView = findViewById(R.id.imgView);
        ConnectionManager.getInstance().subscribe(this);
    }

    public void onIncomingImgMsg(ImgMsg imgMsg)
    {
        byte [] imgBytes = imgMsg.getData();
        int width = imgMsg.getWidth();
        int height = imgMsg.getHeight();
        final Bitmap bitmap = Bitmap.createBitmap  (width, height, Bitmap.Config.ARGB_8888);

        int[] colors = new int[imgBytes.length];
        int r,g,b;
        for (int ci = 0; ci < colors.length-2; ci++)
        {
            b = (int)(0xFF & imgBytes[ci]);
            g = (int)(0xFF & imgBytes[ci+1]);
            r = (int)(0xFF & imgBytes[ci+2]);
        }
        bitmap.setPixels(colors, 0, width, 0, 0, width, height);
        showBitMap(bitmap);
    }

    public void onIncomingCompressedImgMsg(CompressedImgMsg compressedImgMsg)
    {
        final byte [] imgBytes = compressedImgMsg.getData();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imgBytes);
        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
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
    public void onConnectedStatusChanged(boolean status)
    {

    }
}
