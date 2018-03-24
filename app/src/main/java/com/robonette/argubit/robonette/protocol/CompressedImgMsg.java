package com.robonette.argubit.robonette.protocol;

import com.robonette.argubit.robonette.protocol.CellTypes.Int32Cell;
import com.robonette.argubit.robonette.protocol.CellTypes.StringCell;

public class CompressedImgMsg implements RbntMsg
{
    public final int INDX_DATA;
    private StringCell tag = new StringCell(0);
    private StringCell format = new StringCell(tag.getIndex() + StringCell.SIZE);
    private Int32Cell imgSize = new Int32Cell(format.getIndex() + StringCell.SIZE);
    private byte [] data;

    CompressedImgMsg()  //TODO: SPLIT THIS MSG FOR COMPRESSED AND UNCOMPRESSED MSGS
    {
        INDX_DATA = imgSize.getIndex() + Int32Cell.SIZE;
    }

    public String getTag() { return tag.getValue(); }
    public String getFormat() { return format.getValue(); }
    public int getImgSize() { return imgSize.getValue(); }
    public byte [] getData() { return data; }

    public boolean fromBytes(byte [] bytes)
    {
        tag.fromBytes(bytes);
        format.fromBytes(bytes);
        imgSize.fromBytes(bytes);


        final int imgSize = getImgSize();
        data = new byte[imgSize];
        for (int indx = INDX_DATA; indx < imgSize; indx++)
            data[indx - INDX_DATA] = bytes[indx];

        return true;
    }
}
