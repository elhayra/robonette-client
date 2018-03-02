package com.robonette.argubit.robonette;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.robonette.argubit.robonette.communication.TcpClientListener;
import com.robonette.argubit.robonette.communication.TcpClientSingletone;
import com.robonette.argubit.robonette.protocol.InfoMsg;

import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity implements TcpClientListener
{
    private static final String TAG = "InfoActivity";
    private ListView listView;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        listView = findViewById(R.id.infoListView);

        final InfoItem info1 = new InfoItem("Battery:", "88", "%");
        final InfoItem info2 = new InfoItem("Linear Speed:", "0.5", "rad/s");

        ArrayList<InfoItem> infoArrayList = new ArrayList<InfoItem>();
        infoArrayList.add(info1);
        infoArrayList.add(info2);

        InfoListAdapter adapter = new InfoListAdapter(this,
                                                      R.layout.info_adapter_layout,
                                                      infoArrayList);
        listView.setAdapter(adapter);

        TcpClientSingletone.getInstance().subscribe(this);
    }

    private void updateListViewItem(final int index, final String data)
    {
        this.runOnUiThread(new Runnable() {
            public void run() {
                View v = listView.getChildAt(index - listView.getFirstVisiblePosition());

                if(v == null)
                    return;

                TextView someText = (TextView) v.findViewById(R.id.textView2);
                someText.setText(data);
            }
        });
    }

    public void OnTcpConnected(boolean connected) {}

    public void OnTcpIncoming(byte [] bytes)
    {
        InfoMsg msg = new InfoMsg();
        msg.fromBytes(bytes);

        int data = msg.getDataInt();
        updateListViewItem(0, String.valueOf(data));
    }


}
