package com.robonette.argubit.robonette;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.robonette.argubit.robonette.protocol.CompressedImgMsg;
import com.robonette.argubit.robonette.protocol.ConnectionListener;
import com.robonette.argubit.robonette.protocol.ConnectionManager;
import com.robonette.argubit.robonette.protocol.ImgMsg;
import com.robonette.argubit.robonette.protocol.InfoMsg;
import com.robonette.argubit.robonette.protocol.RbntHeader;

import java.util.ArrayList;
import java.util.HashMap;

public class InfoActivity extends AppCompatActivity implements ConnectionListener
{
    private static final String TAG = "InfoActivity";
    private ListView listView;
    private HashMap<String, Integer> listViewHash;
    ArrayList<InfoItem> infoArrayList;
    InfoListAdapter listAdapter;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        listViewHash = new HashMap<String, Integer>();
        infoArrayList = new ArrayList<InfoItem>();
        listView = findViewById(R.id.infoListView);
        listAdapter = new InfoListAdapter(this,
                                          R.layout.info_adapter_layout,
                                          infoArrayList);
        listView.setAdapter(listAdapter);

        ConnectionManager.getInstance().subscribe(this);
    }

    public void onIncomingInfoMsg(InfoMsg infoMsg)
    {
        final String dataVal;

        InfoMsg.DataType dataType = infoMsg.getDataType();
        switch (dataType)
        {
            case INT32:
            {
                dataVal = String.valueOf(infoMsg.getDataInt());
                break;
            }
            case FLOAT32:
            {
                dataVal = String.valueOf(infoMsg.getDataFloat32());
                break;
            }
            case FLOAT64:
            {
                dataVal = String.valueOf(infoMsg.getDataFloat64());
                break;
            }
            case BYTE:
            {
                dataVal = String.valueOf(infoMsg.getDataByte());
                break;
            }
            case BOOL:
            {
                dataVal = String.valueOf(infoMsg.getDataBool());
                break;
            }
            case STRING:
            {
                dataVal = infoMsg.getDataString();
                break;
            }
            default:
                dataVal = "N/A";
        }


        final String dataTag = infoMsg.getDataTag();
        final String dataUnits = infoMsg.getDataUnits();

        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
            // item does not exist, so create it
            if (!listViewHash.containsKey(dataTag))
            {
                listViewHash.put(dataTag, listView.getAdapter().getCount());

                final InfoItem newItem = new InfoItem(dataTag,
                        dataVal,
                        dataUnits);

                infoArrayList.add(newItem);
                listAdapter.notifyDataSetChanged();
            }
            else // item exist, so update it
            {
                int itemIndx = listViewHash.get(dataTag);
                View v = listView.getChildAt(itemIndx - listView.getFirstVisiblePosition());

                if(v == null)
                    return;

                TextView dataTxt = (TextView) v.findViewById(R.id.textView2);
                dataTxt.setText(dataVal);
            }
            }
        });
    }

    @Override
    public void onConnectedStatusChanged(boolean status)
    {

    }

    public void onIncomingCompressedImgMsg(CompressedImgMsg compressedImgMsg)
    {

    }

    public void onIncomingImgMsg(ImgMsg imgMsg) { /* do nothing */ }
}
