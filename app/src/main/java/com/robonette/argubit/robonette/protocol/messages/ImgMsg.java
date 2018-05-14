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

import com.robonette.argubit.robonette.protocol.CellTypes.BoolCell;
import com.robonette.argubit.robonette.protocol.CellTypes.Int32Cell;
import com.robonette.argubit.robonette.protocol.CellTypes.StringCell;
import com.robonette.argubit.robonette.protocol.Crc8;

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

    public ImgMsg()
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

        byte checksum = bytes[INDX_DATA + dataSize];
        Crc8 crc = new Crc8();
        byte calcedChecksum = crc.calcChecksum(bytes, 0, checksum);
        if (calcedChecksum != checksum)
            return false;


        return true;
    }
}
