package com.robonette.argubit.robonette.protocol.CellTypes;

public class BoolCell extends PacketCell
{
    public final int SIZE = 1;
    private boolean value;

    public BoolCell(int index) { super(index); }

    public BoolCell(int index, boolean value)
    {
        super(index);
        setValue(value);
    }

    @Override
    public void fromBytes(byte [] bytes)
    {

    }

    public boolean isValue() { return value; }

    public void setValue(boolean value) { this.value = value; }
}
