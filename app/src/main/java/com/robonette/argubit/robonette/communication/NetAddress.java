package com.robonette.argubit.robonette.communication;

public class NetAddress
{
    public static boolean validateIp(final String ip) {
        //String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        //return ip.matches(PATTERN);
        return true;
    }

    public static boolean validatePort(final int port)
    {
        if (port >= 0 && port <= 65535)
            return true;
        return false;
    }

}


