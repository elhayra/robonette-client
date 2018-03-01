package com.robonette.argubit.robonette.protocol.CellTypes;

/* unsigned byte representation */

public class UByteCell extends PacketCell
{
    public static final int SIZE = 1;
    private char value;

    public UByteCell(int index) { super(index); }

    public UByteCell(int index, char value)
    {
        super(index);
        setValue(value);
    }

    @Override
    public void fromBytes(byte [] bytes)
    {

    }

    public char getValue() { return value; }

    public void setValue(char value) { this.value = value; }
}
