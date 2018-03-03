package com.robonette.argubit.robonette.protocol.CellTypes;

import com.robonette.argubit.robonette.protocol.InfoMsg;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Int32Cell extends PacketCell
{
    public final int SIZE = 4;
    private int value;

    public Int32Cell(int index) { super(index); }

    public Int32Cell(int index, int value)
    {
        super(index);
        setValue(value);
    }

    public boolean fromBytes(byte [] bytes)
    {
        byte [] trimmedArr = Arrays.copyOfRange(bytes,
                bytes.length - 8,
                bytes.length - 4);
        ByteBuffer wrapped = ByteBuffer.wrap(trimmedArr);
        wrapped.order(ByteOrder.LITTLE_ENDIAN);
        value = wrapped.getInt();
        return true;
    }

    public int getValue() { return value; }

    public void setValue(int value) { this.value = value; }
}
