package com.robonette.argubit.robonette.protocol.CellTypes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Float32Cell extends PacketCell
{
    public final int SIZE = 4;
    private float value;

    public Float32Cell(int index) { super(index); }

    public Float32Cell(int index, float value)
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
        value = wrapped.getFloat();
        return true;
    }

    public float getValue() { return value; }

    public void setValue(float value) { this.value = value; }
}
