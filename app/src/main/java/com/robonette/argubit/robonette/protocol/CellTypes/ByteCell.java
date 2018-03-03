package com.robonette.argubit.robonette.protocol.CellTypes;

/* unsigned byte representation */

public class ByteCell extends PacketCell
{
    public static final int SIZE = 1;
    private byte value;

    public ByteCell(int index) { super(index); }

    public ByteCell(int index, byte value)
    {
        super(index);
        setValue(value);
    }

    public boolean fromBytes(byte [] bytes)
    {
        value = bytes[getIndex()];
        return true;
    }

    public byte getValue() { return value; }

    public void setValue(byte value) { this.value = value; }
}
