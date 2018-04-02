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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.robonette.argubit.robonette.R;
import com.robonette.argubit.robonette.protocol.messages.CompressedImgMsg;
import com.robonette.argubit.robonette.protocol.ConnectionListener;
import com.robonette.argubit.robonette.protocol.ConnectionManager;
import com.robonette.argubit.robonette.protocol.messages.ImgMsg;
import com.robonette.argubit.robonette.protocol.messages.InfoMsg;
import com.robonette.argubit.robonette.protocol.messages.MapMsg;


public class ConnectActivity extends AppCompatActivity implements ConnectionListener
{

    private static final String TAG = "ConnectActivity";

    private EditText ipEditTxt;
    private EditText portEditTxt;
    private TextView connStateTxtView;
    private ProgressBar connectingPbar;

    final Activity thisActivity = this;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        Button connectBtn = findViewById(R.id.connectBtn);
        ipEditTxt = findViewById(R.id.ipEditTxt);
        portEditTxt = findViewById(R.id.portEditTxt);
        connStateTxtView = findViewById(R.id.connStateTxtView);
        connectingPbar = findViewById(R.id.connectingPbar);

        connectBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                asyncTryConnect();
            }
        });
    }

    private void asyncTryConnect()
    {
        String srvrIp = ipEditTxt.getText().toString();
        int srvrPort = Integer.parseInt(portEditTxt.getText().toString());

        ConnectionManager.getInstance().subscribe(this);
        ConnectionManager.getInstance().connect(srvrIp, srvrPort, 20);

        connectingPbar.setVisibility(View.VISIBLE);
        connStateTxtView.setVisibility(View.VISIBLE);
        connStateTxtView.setText("Loading...");
    }

    public void navigateToInfoActivity()
    {
        //Intent activityIntent = new Intent(this, InfoActivity.class);
        //startActivity(activityIntent);

        //Intent activityIntent = new Intent(this, ImgActivity.class);
        //startActivity(activityIntent);

        Intent activityIntent = new Intent(this, MapActivity.class);
        startActivity(activityIntent);
    }

    @Override
    public void onIncomingImgMsg(ImgMsg imgMsg)
    {

    }

    @Override
    public void onIncomingInfoMsg(InfoMsg infoMsg)
    {

    }

    @Override
    public void onIncomingMapMsg(MapMsg mapMsg)
    {

    }

    @Override
    public void onIncomingCompressedImgMsg(CompressedImgMsg compressedImgMsg)
    {

    }

    public void onConnectedStatusChanged(boolean status)
    {
        ConnectionManager.getInstance().unsubscribe(this);
        final boolean success = status;
        thisActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (success)
                {
                    connectingPbar.setVisibility(View.INVISIBLE);
                    connStateTxtView.setVisibility(View.INVISIBLE);
                    navigateToInfoActivity();
                }
                else
                {
                    connectingPbar.setVisibility(View.INVISIBLE);
                    connStateTxtView.setVisibility(View.VISIBLE);
                    connStateTxtView.setText("Connection Failed");
                }
            }
        });
    }
}
