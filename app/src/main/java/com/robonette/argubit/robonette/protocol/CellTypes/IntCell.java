package com.robonette.argubit.robonette.protocol.CellTypes;

import com.robonette.argubit.robonette.protocol.InfoMsg;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class IntCell extends PacketCell
{
    public final int SIZE = 4;
    private int value;

    public IntCell(int index) { super(index); }

    public IntCell(int index, int value)
    {
        super(index);
        setValue(value);
    }

    @Override
    public void fromBytes(byte [] bytes)
    {
        byte [] trimmedArr = Arrays.copyOfRange(bytes,
                bytes.length - 4,
                bytes.length);
        ByteBuffer wrapped = ByteBuffer.wrap(trimmedArr);
        value = wrapped.getInt();
    }

    public int getValue() { return value; }

    public void setValue(int value) { this.value = value; }
}
