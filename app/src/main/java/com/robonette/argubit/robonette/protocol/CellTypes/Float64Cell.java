package com.robonette.argubit.robonette.protocol.CellTypes;

public class Float64Cell extends PacketCell
{
    public final int SIZE = 8;
    private double value;

    public Float64Cell(int index) { super(index); }

    public Float64Cell(int index, double value)
    {
        super(index);
        setValue(value);
    }

    @Override
    public void fromBytes(byte [] bytes)
    {

    }

    public double getValue() { return value; }

    public void setValue(double value) { this.value = value; }
}