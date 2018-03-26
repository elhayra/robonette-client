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

package com.robonette.argubit.robonette.communication;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpClient
{
    private Socket socket;
    private DataInputStream inFromServer;
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
                socket.connect(inetAddress, 6000);
                outToServer = new DataOutputStream(socket.getOutputStream());
                inFromServer = new DataInputStream(socket.getInputStream());
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

    public int readBytes(byte [] bytes, int offset, int size)
    {
        int bytesRead = 0;
        try
        {
            bytesRead = inFromServer.read(bytes, offset, size);
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
