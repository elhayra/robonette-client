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

import com.robonette.argubit.robonette.communication.TcpClientListener;
import com.robonette.argubit.robonette.communication.TcpClientSingletone;
import com.robonette.argubit.robonette.protocol.InfoMsg;


public class ConnectActivity extends AppCompatActivity implements TcpClientListener
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

        TcpClientSingletone.getInstance().subscribe(this);
        TcpClientSingletone.getInstance().requestConnect(srvrIp, srvrPort, InfoMsg.SIZE);

        connectingPbar.setVisibility(View.VISIBLE);
        connStateTxtView.setVisibility(View.VISIBLE);
        connStateTxtView.setText("Loading...");
    }

    public void OnTcpConnected(boolean connected)
    {
        TcpClientSingletone.getInstance().unsubscribe(this);
        final boolean success = connected;
        thisActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (success)
                {
                    connectingPbar.setVisibility(View.INVISIBLE);
                    connStateTxtView.setVisibility(View.INVISIBLE);
                    goToControllerActivity();
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

    public void OnTcpIncoming(byte[] bytes) {}

    public void goToControllerActivity()
    {
        Intent activityIntent = new Intent(this, InfoActivity.class);
        startActivity(activityIntent);
    }

}
