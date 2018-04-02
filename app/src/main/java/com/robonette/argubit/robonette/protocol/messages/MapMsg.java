package com.robonette.argubit.robonette.protocol.messages;

import com.robonette.argubit.robonette.protocol.CellTypes.Float32Cell;
import com.robonette.argubit.robonette.protocol.CellTypes.Int32Cell;
import com.robonette.argubit.robonette.protocol.CellTypes.StringCell;

public class MapMsg implements RbntMsg
{
    public final int INDX_DATA;
    private Float32Cell resolution = new Float32Cell(0);
    private Int32Cell width = new Int32Cell(resolution.getIndex() + Float32Cell.SIZE);
    private Int32Cell height = new Int32Cell(width.getIndex() + Int32Cell.SIZE);
    private byte [] data;

    public MapMsg()  //TODO: SPLIT THIS MSG FOR COMPRESSED AND UNCOMPRESSED MSGS
    {
        INDX_DATA = height.getIndex() + Int32Cell.SIZE;
    }

    public float getResolution() { return resolution.getValue(); }
    public int getWidth() { return width.getValue(); }
    public int getHeight() { return height.getValue(); }
    public byte [] getData() { return data; }

    public boolean fromBytes(byte [] bytes)
    {
        resolution.fromBytes(bytes);
        width.fromBytes(bytes);
        height.fromBytes(bytes);

        final int imgSize = getWidth() * getHeight();
        data = new byte[imgSize];
        for (int indx = INDX_DATA; indx < imgSize; indx++)
            data[indx - INDX_DATA] = bytes[indx];

        return true;
    }
}
