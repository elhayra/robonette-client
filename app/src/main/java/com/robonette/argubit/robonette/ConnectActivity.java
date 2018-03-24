package com.robonette.argubit.robonette;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.robonette.argubit.robonette.protocol.CompressedImgMsg;
import com.robonette.argubit.robonette.protocol.ConnectionListener;
import com.robonette.argubit.robonette.protocol.ConnectionManager;
import com.robonette.argubit.robonette.protocol.ImgMsg;
import com.robonette.argubit.robonette.protocol.InfoMsg;


public class ConnectActivity extends AppCompatActivity implements ConnectionListener
{

    private static final String TAG = "ConnectActivity";

    private EditText ipEditTxt;
    private EditText portEditTxt;
    private EditText pswdEditTxt;
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
        pswdEditTxt = findViewById(R.id.pswdEditTxt);
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
        String password = pswdEditTxt.getText().toString();

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

        Intent activityIntent = new Intent(this, ImgActivity.class);
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
