package com.robonette.argubit.robonette.protocol.CellTypes;

public class StringCell extends PacketCell
{
    public static final int SIZE = 20;
    private String value;

    public StringCell(int index) { super(index); }

    public StringCell(int index, String value)
    {
        super(index);
        setValue(value);
    }

    public void fromBytes(byte [] bytes)
    {
        value = new String (bytes);
    }

    public String getValue() { return value; }

    public void setValue(String value) { this.value = value; }
}
