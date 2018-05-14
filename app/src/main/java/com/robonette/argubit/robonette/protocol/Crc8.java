package com.robonette.argubit.robonette.protocol;

/**
 * Created by submachine on 14/05/2018.
 */

public class Crc8
{
    byte table[] = new byte[256];
    // x8 + x7 + x6 + x4 + x2 + 1
    final byte poly = (byte)0xd5;

    public Crc8()
    {
        for (int i = 0; i < 256; ++i)
        {
            int temp = i;
            for (int j = 0; j < 8; ++j)
            {
                if ((temp & 0x80) != 0)
                {
                    temp = (temp << 1) ^ poly;
                }
                else
                {
                    temp <<= 1;
                }
            }
            table[i] = (byte)temp;
        }
    }

    public byte calcChecksum(byte [] bytes, int startIndx, int endIndx)
    {
        byte crc = 0;
        if (endIndx > startIndx && startIndx >= 0)
        {
            for (int i = startIndx; i < endIndx; i++)
            {
                crc = table[crc ^ bytes[i]];
            }
            return crc;
        }
        return 0;
    }
}
