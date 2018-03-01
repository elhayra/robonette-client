package com.robonette.argubit.robonette.communication;

public interface TcpClientListener
{
    public void OnTcpConnected(boolean connected);
    public void OnTcpIncoming(byte [] bytes);

}
