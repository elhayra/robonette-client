package com.robonette.argubit.robonette.protocol;

import com.robonette.argubit.robonette.protocol.CellTypes.BoolCell;
import com.robonette.argubit.robonette.protocol.CellTypes.ByteCell;
import com.robonette.argubit.robonette.protocol.CellTypes.Int32Cell;
import com.robonette.argubit.robonette.protocol.CellTypes.StringCell;

public class ImgMsg implements RbntMsg
{
    public final int INDX_DATA;
    private StringCell tag = new StringCell(0);
    private StringCell encoding = new StringCell(tag.getIndex() + StringCell.SIZE);
    private Int32Cell height = new Int32Cell(encoding.getIndex() + StringCell.SIZE);
    private Int32Cell width = new Int32Cell(height.getIndex() + Int32Cell.SIZE);
    private Int32Cell step = new Int32Cell(width.getIndex() + Int32Cell.SIZE);
    private BoolCell isBigEndian = new BoolCell(step.getIndex() + Int32Cell.SIZE);
    private byte [] data;

    ImgMsg()  //TODO: SPLIT THIS MSG FOR COMPRESSED AND UNCOMPRESSED MSGS
    {
        INDX_DATA = isBigEndian.getIndex() + BoolCell.SIZE;
    }

    public String getTag() { return tag.getValue(); }
    public String getEncoding() { return encoding.getValue(); }
    public int getHeight() { return height.getValue(); }
    public int getWidth() { return width.getValue(); }
    public int getStep() { return step.getValue(); }
    public boolean isBigEndian() { return isBigEndian.getValue(); }
    public byte [] getData() { return data; }

    public boolean fromBytes(byte [] bytes)
    {
        tag.fromBytes(bytes);
        encoding.fromBytes(bytes);
        height.fromBytes(bytes);
        width.fromBytes(bytes);
        step.fromBytes(bytes);
        isBigEndian.fromBytes(bytes);

        final int dataSize = /*step.getValue() * */ height.getValue(); //TODO:  RETURN THIS TI HEIGHT * STEP --FOR UNCOMPRESSED IMAGES
        data = new byte[dataSize];
        for (int indx = INDX_DATA; indx < dataSize; indx++)
            data[indx - INDX_DATA] = bytes[indx];

        return true;
    }
}
