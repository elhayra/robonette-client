package com.robonette.argubit.robonette.protocol;

import com.robonette.argubit.robonette.protocol.CellTypes.BoolCell;
import com.robonette.argubit.robonette.protocol.CellTypes.Float32Cell;
import com.robonette.argubit.robonette.protocol.CellTypes.Float64Cell;
import com.robonette.argubit.robonette.protocol.CellTypes.IntCell;
import com.robonette.argubit.robonette.protocol.CellTypes.StringCell;
import com.robonette.argubit.robonette.protocol.CellTypes.UByteCell;

import java.util.Arrays;

public class InfoMsg
{
    /* TODO: MAYBE TURN THIS INTO IMMUTABLE */

    public static final int DATA_SIZE = 8;
    public static final int SIZE = 49;

    /* packet cells */

    UByteCell dataType = new UByteCell(0);
    StringCell dataTag = new StringCell(dataType.getIndex() + UByteCell.SIZE);
    StringCell dataUnits = new StringCell(dataTag.getIndex() + StringCell.SIZE);
    int DATA_INDEX = dataUnits.getIndex() + StringCell.SIZE;

    /* one of this data types will be used dynamically */
    /* according to incoming package data type         */

    IntCell dataInt;
    Float32Cell dataFloat32;
    Float64Cell dataFloat64;
    UByteCell dataByte;
    BoolCell dataBool;
    StringCell dataString;

    private enum DataType
    {
        INT32   (1),
        FLOAT32 (2),
        FLOAT64 (3),
        UBYTE   (4),
        BOOL    (5),
        STRING  (6);
        private int value;
        private DataType(int value) { this.value = value; }
        public int getValue() { return value; }
        public static DataType fromInteger(int x) {
            switch(x) {
                case 1:
                    return INT32;
                case 2:
                    return FLOAT32;
                case 3:
                    return FLOAT64;
                case 4:
                    return UBYTE;
                case 5:
                    return BOOL;
                case 6:
                    return STRING;
            }
            return null;
        }
    }

    public boolean fromBytes(byte [] bytes)
    {
        if (bytes.length == SIZE)
        {
            /* fetch single byte info from bytes array */
            dataType.setValue((char)bytes[dataType.getIndex()]);

            /* build sub-arrays for cells size bigger than 1 byte */
            byte [] dataTagArr = Arrays.copyOfRange(bytes,
                    dataTag.getIndex(),
                    dataTag.getIndex() + StringCell.SIZE);
            dataTag.fromBytes(dataTagArr);

            byte [] dataUnitsArr = Arrays.copyOfRange(bytes,
                    dataUnits.getIndex(),
                    dataUnits.getIndex() + StringCell.SIZE);
            dataUnits.fromBytes(dataUnitsArr);

            byte [] dataArr = Arrays.copyOfRange(bytes,
                    DATA_INDEX,
                    DATA_INDEX + DATA_SIZE);

            switch (DataType.fromInteger(dataType.getValue()))
            {
                case INT32:
                {
                    dataInt = new IntCell(DATA_INDEX);
                    dataInt.fromBytes(dataArr);
                    break;
                }
                case FLOAT32:
                {
                    dataFloat32 = new Float32Cell(DATA_INDEX);
                    break;
                }
                case FLOAT64:
                {
                    dataFloat64 = new Float64Cell(DATA_INDEX);
                    break;
                }
                case UBYTE:
                {
                    dataByte = new UByteCell(DATA_INDEX);
                    break;
                }
                case BOOL:
                {
                    dataBool = new BoolCell(DATA_INDEX);
                    break;
                }
                case STRING:
                {
                    dataString = new StringCell(DATA_INDEX);
                    break;
                }
            }
            return true;
        }
        return false;
    }

}
