package com.robonette.argubit.robonette.protocol.CellTypes;

import java.util.Arrays;

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

    public boolean fromBytes(byte [] bytes)
    {
        /* build sub-arrays for cells size bigger than 1 byte */
        byte [] dataTagArr = Arrays.copyOfRange(bytes,
                getIndex(),
                getIndex() + StringCell.SIZE);
        value = new String (dataTagArr);

        return true;
    }

    public String getValue() { return value; }

    public void setValue(String value) { this.value = value; }
}
