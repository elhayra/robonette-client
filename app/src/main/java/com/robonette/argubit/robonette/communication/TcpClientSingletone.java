package com.robonette.argubit.robonette.communication;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

/* this class get requests from TcpClientListener and notify */
/* back when action is done. Using singletone allow all      */
/* activities to use the same socket connection easily       */

public class TcpClientSingletone extends Thread
{
    private static final String TAG = "TcpClientSingletone";
    private static TcpClientSingletone instance;
    private TcpClient tcpClient;
    private ArrayList<TcpClientListener> subscribers;
    private Thread srvrThread;

    private volatile String srvrIp;
    private volatile int srvrPort;
    private volatile int buffSize;
    private volatile boolean connectReq = false;
    private volatile boolean shutdownReq = false;

    private TcpClientSingletone()
    {
        tcpClient = new TcpClient();
        subscribers = new ArrayList<TcpClientListener>();
    }

    public static TcpClientSingletone getInstance()
    {
        if (instance == null)
        {
            synchronized (TcpClientSingletone.class)
            {
                if (instance == null)
                {
                    instance = new TcpClientSingletone();
                    instance.start();
                }
            }
        }
        return instance;
    }

    public boolean isConnected()
    {
        return tcpClient.isConnected();
    }

    public void run()
    {
        boolean connected = false;
        while (!shutdownReq)
        {
            if (connectReq)
            {
                connected = connect();
                if (connected)
                    notifyConnected(true);
                connectReq = false;
            }
            /* listen to incoming bytes */
            if (connected)
            {
                byte [] bytes = new byte[buffSize];
                int bytesRead = tcpClient.readBytes(bytes, buffSize);
                if (bytesRead > 0)
                {
                    notifyIncoming(bytes);
                }
            }
        }

        tcpClient.close();
        instance = null;
    }

    public void subscribe(TcpClientListener listener)
    {
        subscribers.add(listener);
    }

    public void unsubscribe(TcpClientListener listener)
    {
        subscribers.remove(listener);
    }

    /* actions */
    private boolean connect()
    {
        if (tcpClient.connectTo(srvrIp, srvrPort))
        {
            Log.i(TAG, "connection success");
            return true;
        }
        Log.i(TAG, "connection failed");
        return false;
    }

    /* requests */
    public void requestConnect(String srvrIp, int srvrPort, int buffSize)
    {
        this.srvrIp = srvrIp;
        this.srvrPort = srvrPort;
        this.buffSize = buffSize;
        connectReq = true;
    }

    public void requestClose()
    {
        shutdownReq = true;
    }

    /* notifiers */
    private void notifyConnected(boolean state)
    {
        for (TcpClientListener listener : subscribers)
            listener.OnTcpConnected(state);
    }

    private void notifyIncoming(byte[] bytes)
    {
        int a = bytes.length;
        for (TcpClientListener listener : subscribers)
            listener.OnTcpIncoming(bytes);
    }
}
