package com.robonette.argubit.robonette.communication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpClient
{
    private Socket socket;
    private BufferedReader inFromServer;
    private DataOutputStream outToServer;

    public boolean connectTo(String srvrIp, int srvrPort)
    {
        if (NetAddress.validateIp(srvrIp) &&
            NetAddress.validatePort(srvrPort))
        {
            try
            {
                InetSocketAddress inetAddress = new InetSocketAddress(srvrIp, srvrPort);
                socket = new Socket();
                socket.connect(inetAddress, 1000);
                outToServer = new DataOutputStream(socket.getOutputStream());
                inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }
            catch (IOException e)
            {
                return false;
            }
            catch (Exception e)
            {
                return false;
            }
        }
        return true;
    }

    public boolean isConnected() { return socket.isConnected(); }

    public boolean writeBytes(byte[] byteArr)
    {
        try
        {
            outToServer.write(byteArr);
        } catch (IOException e)
        {
            return false;
        }
        return true;
    }

    public int readByte()
    {
        int incoming = -1;
        try
        {
            incoming = inFromServer.read();
        } catch (IOException e)
        {
            return -1;
        }
        return incoming;
    }

    public int readBytes(char [] bytes, int size)
    {
        int bytesRead = 0;
        try
        {
            bytesRead = inFromServer.read(bytes, 0, size);
        } catch (IOException e)
        {
            return -1;
        }
        return bytesRead;
    }

    public void close()
    {
        try
        {
            socket.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
