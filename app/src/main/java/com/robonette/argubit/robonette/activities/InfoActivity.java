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

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.robonette.argubit.robonette.utils.InfoItem;
import com.robonette.argubit.robonette.utils.InfoListAdapter;
import com.robonette.argubit.robonette.R;
import com.robonette.argubit.robonette.protocol.messages.CompressedImgMsg;
import com.robonette.argubit.robonette.protocol.ConnectionListener;
import com.robonette.argubit.robonette.protocol.ConnectionManager;
import com.robonette.argubit.robonette.protocol.messages.ImgMsg;
import com.robonette.argubit.robonette.protocol.messages.InfoMsg;
import com.robonette.argubit.robonette.protocol.messages.MapMsg;

import java.util.ArrayList;
import java.util.HashMap;

public class InfoActivity extends AppCompatActivity implements ConnectionListener
{
    private static final String TAG = "InfoActivity";
    private ListView listView;
    private HashMap<String, Integer> listViewHash;
    ArrayList<InfoItem> infoArrayList;
    InfoListAdapter listAdapter;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle barToggle;

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

        drawerLayout = findViewById(R.id.drawerLayout);
        barToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(barToggle);
        barToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        final String selectedTitle = menuItem.getTitle().toString();
                        navigateTo(selectedTitle);

                        return true;
                    }
                });


    }

    private void navigateTo(final String title)
    {
        if (title.equals("Image Control View"))
        {
            Intent activityIntent = new Intent(this, ImgActivity.class);
            startActivity(activityIntent);
        }
        else if (title.equals("Map View"))
        {
            Intent activityIntent = new Intent(this, MapActivity.class);
            startActivity(activityIntent);
        }
        else if (title.equals("Disconnect"))
        {
            ConnectionManager.getInstance().unsubscribe(this);
            ConnectionManager.getInstance().close();

            Intent activityIntent = new Intent(this, ConnectActivity.class);
            startActivity(activityIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (barToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
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
    public void onIncomingMapMsg(MapMsg mapMsg)
    {

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
