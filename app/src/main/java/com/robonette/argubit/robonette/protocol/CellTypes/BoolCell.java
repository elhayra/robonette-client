package com.robonette.argubit.robonette.protocol.CellTypes;

public class BoolCell extends PacketCell
{
    public static final int SIZE = 1;
    private boolean value;

    public BoolCell(int index) { super(index); }

    public BoolCell(int index, boolean value)
    {
        super(index);
        this.value = value;
    }

    public boolean fromBytes(byte [] bytes)
    {
        return true;
    }

    public boolean isValue() { return value; }

    public boolean getValue() { return value; }
}
