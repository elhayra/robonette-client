package com.robonette.argubit.robonette.protocol;

import com.robonette.argubit.robonette.protocol.CellTypes.BoolCell;
import com.robonette.argubit.robonette.protocol.CellTypes.Float32Cell;
import com.robonette.argubit.robonette.protocol.CellTypes.Float64Cell;
import com.robonette.argubit.robonette.protocol.CellTypes.Int32Cell;
import com.robonette.argubit.robonette.protocol.CellTypes.StringCell;
import com.robonette.argubit.robonette.protocol.CellTypes.ByteCell;

public class InfoMsg
{
    /* TODO: MAYBE TURN THIS INTO IMMUTABLE */

    public static final int DATA_SIZE = 8;
    public static final int SIZE = 49;

    /* packet cells */

    ByteCell dataType = new ByteCell(0);
    StringCell dataTag = new StringCell(dataType.getIndex() + ByteCell.SIZE);
    StringCell dataUnits = new StringCell(dataTag.getIndex() + StringCell.SIZE);
    int DATA_INDEX = dataUnits.getIndex() + StringCell.SIZE;

    /* one of this data types will be used dynamically */
    /* according to incoming package data type         */

    Int32Cell dataInt;
    Float32Cell dataFloat32;
    Float64Cell dataFloat64;
    ByteCell dataByte;
    BoolCell dataBool;
    StringCell dataString;

    public DataType getDataType() { return DataType.fromInteger(dataType.getValue()); }
    public String getDataTag() { return dataTag.getValue(); }
    public String getDataUnits() { return dataUnits.getValue(); }

    public int getDataInt() { return dataInt.getValue(); }
    public float getDataFloat32() { return dataFloat32.getValue(); }
    public double getDataFloat64() { return dataFloat64.getValue(); }
    public byte getDataByte() { return dataByte.getValue(); }
    public boolean getDataBool() { return dataBool.getValue(); }
    public String getDataString() { return dataString.getValue(); }

    public enum DataType
    {
        INT32   (1),
        FLOAT32 (2),
        FLOAT64 (3),
        BYTE    (4),
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
                    return BYTE;
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
            dataType.fromBytes(bytes);

            dataTag.fromBytes(bytes);

            dataUnits.fromBytes(bytes);

            switch (DataType.fromInteger(dataType.getValue()))
            {
                case INT32:
                {
                    dataInt = new Int32Cell(DATA_INDEX);
                    dataInt.fromBytes(bytes);
                    break;
                }
                case FLOAT32:
                {
                    dataFloat32 = new Float32Cell(DATA_INDEX);
                    dataFloat32.fromBytes(bytes);
                    break;
                }
                case FLOAT64:
                {
                    dataFloat64 = new Float64Cell(DATA_INDEX);
                    break;
                }
                case BYTE:
                {
                    dataByte = new ByteCell(DATA_INDEX);
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
