package com.robonette.argubit.robonette.protocol.messages;

import android.nfc.Tag;
import android.util.Log;

import com.robonette.argubit.robonette.protocol.CellTypes.Float32Cell;
import com.robonette.argubit.robonette.protocol.CellTypes.Int32Cell;
import com.robonette.argubit.robonette.protocol.CellTypes.StringCell;

public class MapMsg implements RbntMsg
{
    public final String TAG = "MapMsg";
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
        Log.i(TAG, "width: " + getWidth());
        Log.i(TAG, "height: " + getHeight());
        Log.i(TAG, "img size: " + imgSize);
        Log.i(TAG, "indx data: " + INDX_DATA);
        Log.i(TAG, "bytes size: " + bytes.length);
        Log.i(TAG, "w index: " + width.getIndex());
        Log.i(TAG, "byte w 1: " + bytes[width.getIndex()]);
        Log.i(TAG, "byte w 2: " + bytes[width.getIndex()+1]);
        Log.i(TAG, "byte w 3: " + bytes[width.getIndex()+2]);
        Log.i(TAG, "byte w 4: " + bytes[width.getIndex()+3]);
        data = new byte[imgSize];

        for (int indx = 0; indx < imgSize; indx++)
            data[indx] = bytes[indx + INDX_DATA - 1];

        return true;
    }
}
