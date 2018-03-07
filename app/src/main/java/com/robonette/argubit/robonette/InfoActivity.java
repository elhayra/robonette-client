package com.robonette.argubit.robonette;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.robonette.argubit.robonette.communication.TcpClientListener;
import com.robonette.argubit.robonette.communication.TcpClientSingletone;
import com.robonette.argubit.robonette.protocol.CellTypes.BoolCell;
import com.robonette.argubit.robonette.protocol.CellTypes.ByteCell;
import com.robonette.argubit.robonette.protocol.CellTypes.Float32Cell;
import com.robonette.argubit.robonette.protocol.CellTypes.Float64Cell;
import com.robonette.argubit.robonette.protocol.CellTypes.Int32Cell;
import com.robonette.argubit.robonette.protocol.CellTypes.StringCell;
import com.robonette.argubit.robonette.protocol.InfoMsg;

import java.util.ArrayList;
import java.util.HashMap;

public class InfoActivity extends AppCompatActivity implements TcpClientListener
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



        TcpClientSingletone.getInstance().subscribe(this);
    }

    private void updateListViewItem(final int index,
                                    /*final String tag,*/
                                    final String data
                                    /*final String units*/)
    {
        this.runOnUiThread(new Runnable() {
            public void run() {
                View v = listView.getChildAt(index - listView.getFirstVisiblePosition());

                if(v == null)
                    return;

                //TextView tagTxt = (TextView) v.findViewById(R.id.textView1);
                TextView dataTxt = (TextView) v.findViewById(R.id.textView2);
                //TextView unitsTxt = (TextView) v.findViewById(R.id.textView3);

                //tagTxt.setText(tag);
                dataTxt.setText(data);
                //unitsTxt.setText(units);
            }
        });
    }

    public void OnTcpConnected(boolean connected) {}

    public void OnTcpIncoming(byte [] bytes)
    {
        InfoMsg msg = new InfoMsg();

        final String dataVal;
        if (msg.fromBytes(bytes))
        {
            InfoMsg.DataType dataType = msg.getDataType();
            switch (dataType)
            {
                case INT32:
                {
                    dataVal = String.valueOf(msg.getDataInt());
                    break;
                }
                case FLOAT32:
                {
                    dataVal = String.valueOf(msg.getDataFloat32());
                    break;
                }
                case FLOAT64:
                {
                    dataVal = String.valueOf(msg.getDataFloat64());
                    break;
                }
                case BYTE:
                {
                    dataVal = String.valueOf(msg.getDataByte());
                    break;
                }
                case BOOL:
                {
                    dataVal = String.valueOf(msg.getDataBool());
                    break;
                }
                case STRING:
                {
                    dataVal = msg.getDataString();
                    break;
                }
                default:
                    dataVal = "N/A";
            }

            final String dataTag = msg.getDataTag();
            final String dataUnits = msg.getDataUnits();

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
    }


}
