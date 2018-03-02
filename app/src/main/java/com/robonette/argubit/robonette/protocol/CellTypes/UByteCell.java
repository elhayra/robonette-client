package com.robonette.argubit.robonette.protocol.CellTypes;

/* unsigned byte representation */

public class UByteCell extends PacketCell
{
    public static final int SIZE = 1;
    private byte value;

    public UByteCell(int index) { super(index); }

    public UByteCell(int index, byte value)
    {
        super(index);
        setValue(value);
    }

    public void fromBytes(byte [] bytes)
    {
        value = bytes[getIndex()];
    }

    public byte getValue() { return value; }

    public void setValue(byte value) { this.value = value; }
}
