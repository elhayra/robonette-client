package com.robonette.argubit.robonette.protocol;

import android.icu.text.IDNA;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.robonette.argubit.robonette.communication.TcpClient;
import com.robonette.argubit.robonette.protocol.RbntHeader.MsgType;

import java.util.ArrayList;

/* this class get requests from TcpClientListener and notify */
/* back when action is done. Using singletone allow all      */
/* activities to use the same socket connection easily       */

public class ConnectionManager extends Thread
{
    private static final String TAG = "TcpClientSingletone";
    private static ConnectionManager instance;
    private TcpClient tcpClient;
    private MsgType waitingMsgType;
    private int buffSize;

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

            if (waitingMsgType == MsgType.HEADER)
            {
                buffSize = RbntHeader.SIZE;
                byte [] bytes = new byte[buffSize];
                int bytesRead = tcpClient.readBytes(bytes, buffSize);
                RbntHeader header = new RbntHeader();
                if (header.fromBytes(bytes))
                {
                    waitingMsgType = header.getMsgType();
                    buffSize = header.getMsgSize();
                }
            }
            else
            {
                final byte [] bytes = new byte[buffSize];
                int bytesRead = tcpClient.readBytes(bytes, buffSize);
                new Thread() {
                    public void run()
                    {
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
                    }
                    }
                }.start();
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
                }
                Log.i(TAG, "connection failed");
                notifyConnectedStatus(false);
                this.start();
            }
        }.start();
    }

    public void close()
    {
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

    private void notifyIncomingInfoMsg(InfoMsg infoMsg)
    {
        for (ConnectionListener listener : subscribers)
            listener.onIncomingInfoMsg(infoMsg);
    }

}
