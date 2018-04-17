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

package com.robonette.argubit.robonette.protocol;
import android.util.Log;

import com.robonette.argubit.robonette.communication.TcpClient;
import com.robonette.argubit.robonette.protocol.messages.CompressedImgMsg;
import com.robonette.argubit.robonette.protocol.messages.ImgMsg;
import com.robonette.argubit.robonette.protocol.messages.InfoMsg;
import com.robonette.argubit.robonette.protocol.messages.MapMsg;
import com.robonette.argubit.robonette.protocol.messages.RbntHeader;
import com.robonette.argubit.robonette.protocol.messages.RbntHeader.MsgType;

import java.util.ArrayList;
import java.util.Map;

/* this class get requests from TcpClientListener and notify */
/* back when action is done. Using singletone allow all      */
/* activities to use the same socket connection easily       */

public class ConnectionManager extends Thread
{
    private static final String TAG = "ConnectionManager";
    private static ConnectionManager instance;
    private TcpClient tcpClient;
    private MsgType waitingMsgType;
    private int buffSize = 256;
    private int nextMsgSize;

    private ArrayList<ConnectionListener> subscribers;


    private ConnectionManager()
    {
        tcpClient = new TcpClient();
        subscribers = new ArrayList<ConnectionListener>();
        waitingMsgType = MsgType.HEADER;
    }

    public static ConnectionManager getInstance()
    {
        if (instance == null)
        {
            synchronized (ConnectionManager.class)
            {
                if (instance == null)
                    instance = new ConnectionManager();
            }
        }
        return instance;
    }

    public boolean isConnected()
    {
        return tcpClient.isConnected();
    }

    public void subscribe(ConnectionListener listener) { subscribers.add(listener); }
    public void unsubscribe(ConnectionListener listener) { subscribers.remove(listener); }

    public void run()
    {
        while (isConnected())
        {
            // handle header
             if (waitingMsgType == MsgType.HEADER)
            {
                byte [] bytes = new byte[RbntHeader.SIZE];
                int bytesRead = tcpClient.readBytes(bytes,0, RbntHeader.SIZE);
                RbntHeader header = new RbntHeader();
                if (header.fromBytes(bytes))
                {
                    waitingMsgType = header.getMsgType();
                    nextMsgSize = header.getMsgSize();
                }
            }
            else // handle msg
            {
                final byte [] bytes = new byte[nextMsgSize];
                int bytesRead = 0;

                while (bytesRead < nextMsgSize)
                    bytesRead += tcpClient.readBytes(bytes, bytesRead, nextMsgSize - bytesRead);


                Log.i(TAG, "got " + bytesRead + " bytes");


                if (bytesRead != nextMsgSize)
                    continue;

                /*new Thread() {
                    public void run()
                    {*/
                    switch (waitingMsgType)
                    {
                        case INFO:
                            InfoMsg info = new InfoMsg();
                            if (info.fromBytes(bytes))
                                notifyIncomingInfoMsg(info);
                            break;
                        case IMAGE:
                            ImgMsg img = new ImgMsg();
                            if (img.fromBytes(bytes))
                                notifyIncomingImgMsg(img);
                            break;
                        case COMPRESSED_IMG:
                            CompressedImgMsg compressedImgMsg = new CompressedImgMsg();
                            if (compressedImgMsg.fromBytes(bytes))
                                notifyIncomingCompressedImg(compressedImgMsg);
                            break;
                        case MAP:
                            MapMsg mapMsg = new MapMsg();
                            if (mapMsg.fromBytes(bytes))
                                notifyIncomingMapMsg(mapMsg);
                            break;
                    }
                   /* }
                }.start();*/
                waitingMsgType = MsgType.HEADER;
            }
        }
        close();
    }

    public void connect(final String srvrIp, final int srvrPort, final int buffSize)
    {
        this.buffSize = buffSize;
        new Thread() {
            public void run()
            {
                if (tcpClient.connectTo(srvrIp, srvrPort))
                {
                    Log.i(TAG, "connection success");
                    notifyConnectedStatus(true);
                    instance.start();
                }
                else
                {
                    Log.i(TAG, "connection failed");
                    notifyConnectedStatus(false);
                }
            }
        }.start();
    }

    public void close()
    {
        if (tcpClient != null)
            tcpClient.close();
        instance = null;
    }

    /* notifiers */
    private void notifyConnectedStatus(boolean state)
    {
        for (ConnectionListener listener : subscribers)
            listener.onConnectedStatusChanged(state);
    }

    private void notifyIncomingImgMsg(ImgMsg imgMsg)
    {
        for (ConnectionListener listener : subscribers)
            listener.onIncomingImgMsg(imgMsg);
    }

    private void notifyIncomingCompressedImg(CompressedImgMsg compressedImgMsg)
    {
        for (ConnectionListener listener : subscribers)
            listener.onIncomingCompressedImgMsg(compressedImgMsg);
    }

    private void notifyIncomingInfoMsg(InfoMsg infoMsg)
    {
        for (ConnectionListener listener : subscribers)
            listener.onIncomingInfoMsg(infoMsg);
    }

    private void notifyIncomingMapMsg(MapMsg mapMsg)
    {
        for (ConnectionListener listener : subscribers)
            listener.onIncomingMapMsg(mapMsg);
    }

}
