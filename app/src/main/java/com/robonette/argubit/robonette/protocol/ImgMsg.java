package com.robonette.argubit.robonette.protocol;

import com.robonette.argubit.robonette.protocol.CellTypes.BoolCell;
import com.robonette.argubit.robonette.protocol.CellTypes.ByteCell;
import com.robonette.argubit.robonette.protocol.CellTypes.StringCell;
import com.robonette.argubit.robonette.protocol.CellTypes.UInt32Cell;

public class ImgMsg implements RbntMsg
{
    public final int INDX_DATA;
    private StringCell tag = new StringCell(0);
    private StringCell encoding = new StringCell(tag.getIndex() + StringCell.SIZE);
    private UInt32Cell height = new UInt32Cell(encoding.getIndex() + StringCell.SIZE);
    private UInt32Cell width = new UInt32Cell(height.getIndex() + UInt32Cell.SIZE);
    private UInt32Cell step = new UInt32Cell(width.getIndex() + UInt32Cell.SIZE);
    private BoolCell isBigEndian = new BoolCell(step.getIndex() + UInt32Cell.SIZE);
    private byte [] data;

    ImgMsg()
    {
        INDX_DATA = isBigEndian.getIndex() + BoolCell.SIZE;
    }

    public String getTag() { return tag.getValue(); }
    public String getEncoding() { return encoding.getValue(); }
    public int getHeight() { return height.getValue(); }
    public int getWidth() { return width.getValue(); }
    public int getStep() { return step.getValue(); }
    public boolean isBigEndian() { return isBigEndian.getValue(); }

    public boolean fromBytes(byte [] bytes)
    {
        tag.fromBytes(bytes);
        encoding.fromBytes(bytes);
        height.fromBytes(bytes);
        width.fromBytes(bytes);
        step.fromBytes(bytes);
        isBigEndian.fromBytes(bytes);

        final int dataSize = step.getValue() * height.getValue();
        data = new byte[dataSize];
        for (int indx = 0; indx < dataSize; indx++)
            data[indx] = bytes[indx + INDX_DATA];

        return true;
    }
}
