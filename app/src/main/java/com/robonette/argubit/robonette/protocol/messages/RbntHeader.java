/*******************************************************************************
 * Copyright (c) 2018, Elchay Rauper
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of Elchay Rauper nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/

package com.robonette.argubit.robonette.protocol.messages;

import com.robonette.argubit.robonette.protocol.CellTypes.ByteCell;
import com.robonette.argubit.robonette.protocol.CellTypes.Int32Cell;

public class RbntHeader implements RbntMsg
{

    public enum MsgType
    {
        HEADER          (1),
        INFO            (2),
        IMAGE           (3),
        COMPRESSED_IMG  (4),
        MAP             (5),
        COMMAND         (6);

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
                    return COMPRESSED_IMG;
                case 5:
                    return MAP;
                case 6:
                    return COMMAND;
            }
            return null;
        }
    }

    public static final int SIZE = (Int32Cell.SIZE * 2) + ByteCell.SIZE;
    public static final int INDX_HEADER_START = 0;
    public static final int INDX_MSG_TYPE = INDX_HEADER_START + Int32Cell.SIZE;
    public static final int INDX_MSG_SIZE = INDX_MSG_TYPE + ByteCell.SIZE;

    public static final int VALID_HEADER_START = 0x446535D0; //1147483600

    private Int32Cell headerStart = new Int32Cell(INDX_HEADER_START);
    public ByteCell msgType = new ByteCell(INDX_MSG_TYPE);
    public Int32Cell msgSize = new Int32Cell(INDX_MSG_SIZE);

    public MsgType getMsgType() { return MsgType.fromInteger(msgType.getValue()); }
    public int getSize() { return msgSize.getValue(); }
    public void setMsgType(MsgType type) { msgType.setValue((byte)type.getValue()); }
    public void setMsgSize(int size) { msgSize.setValue(size); }

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

    public byte [] toBytes()
    {
        byte bytes[] = new byte[SIZE];

        headerStart.setValue(VALID_HEADER_START);
        headerStart.toBytes(bytes);
        msgType.toBytes(bytes);
        msgSize.toBytes(bytes);

        return bytes;
    }

}
