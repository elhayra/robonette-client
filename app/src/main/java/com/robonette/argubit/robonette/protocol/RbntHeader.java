package com.robonette.argubit.robonette.protocol;

import com.robonette.argubit.robonette.protocol.CellTypes.ByteCell;
import com.robonette.argubit.robonette.protocol.CellTypes.Int32Cell;

public class RbntHeader implements RbntMsg
{

    public enum MsgType
    {
        HEADER      (1),
        INFO        (2),
        IMAGE       (3),
        MAP         (4),
        COMMAND     (5);

        private int value;
        private MsgType(int value) { this.value = value; }
        public int getValue() { return value; }
        public static MsgType fromInteger(int x)
        {
            switch(x) {
                case 1:
                    return HEADER;
                case 2:
                    return INFO;
                case 3:
                    return IMAGE;
                case 4:
                    return MAP;
                case 5:
                    return COMMAND;
            }
            return null;
        }
    }

    public static final int SIZE = (Int32Cell.SIZE * 2) + ByteCell.SIZE;
    public static final int INDX_HEADER_START = 0;
    public static final int INDX_MSG_TYPE = INDX_HEADER_START + Int32Cell.SIZE;
    public static final int INDX_MSG_SIZE = INDX_MSG_TYPE + ByteCell.SIZE;

    public static final int VALID_HEADER_START = 0x01;

    private Int32Cell headerStart = new Int32Cell(INDX_HEADER_START);
    private ByteCell msgType = new ByteCell(INDX_MSG_TYPE);
    private Int32Cell msgSize = new Int32Cell(INDX_MSG_SIZE);

    public MsgType getMsgType() { return MsgType.fromInteger(msgType.getValue()); }
    public int getMsgSize() { return msgSize.getValue(); }


    /* return true if header is valid */
    public boolean fromBytes(byte[] bytes)
    {
        if (bytes.length != SIZE)
            return false;

        headerStart.fromBytes(bytes);
        msgType.fromBytes(bytes);
        msgSize.fromBytes(bytes);

        if (headerStart.getValue() != VALID_HEADER_START)
           return false;

        return true;
    }

}