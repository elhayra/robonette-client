package com.robonette.argubit.robonette.protocol.CellTypes;

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

    @Override
    public void fromBytes(byte [] bytes)
    {

    }

    public float getValue() { return value; }

    public void setValue(float value) { this.value = value; }
}
