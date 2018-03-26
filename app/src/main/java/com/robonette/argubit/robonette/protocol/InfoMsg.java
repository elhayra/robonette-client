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

package com.robonette.argubit.robonette.protocol;

import android.support.v4.util.Preconditions;

import com.robonette.argubit.robonette.protocol.CellTypes.BoolCell;
import com.robonette.argubit.robonette.protocol.CellTypes.Float32Cell;
import com.robonette.argubit.robonette.protocol.CellTypes.Float64Cell;
import com.robonette.argubit.robonette.protocol.CellTypes.Int32Cell;
import com.robonette.argubit.robonette.protocol.CellTypes.StringCell;
import com.robonette.argubit.robonette.protocol.CellTypes.ByteCell;

public class InfoMsg implements RbntMsg
{
    /* TODO: MAYBE TURN THIS INTO IMMUTABLE */

    public static final int DATA_SIZE = 8;
    public static final int SIZE = 49;

    /* packet cells */

    private ByteCell dataType = new ByteCell(0);
    private StringCell dataTag = new StringCell(dataType.getIndex() + ByteCell.SIZE);
    private StringCell dataUnits = new StringCell(dataTag.getIndex() + StringCell.SIZE);
    private int DATA_INDEX = dataUnits.getIndex() + StringCell.SIZE;

    /* one of this data types will be used dynamically */
    /* according to incoming package data type         */

    private Int32Cell dataInt;
    private Float32Cell dataFloat32;
    private Float64Cell dataFloat64;
    private ByteCell dataByte;
    private BoolCell dataBool;
    private StringCell dataString;

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
