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

import com.robonette.argubit.robonette.protocol.CellTypes.Int32Cell;
import com.robonette.argubit.robonette.protocol.CellTypes.StringCell;
import com.robonette.argubit.robonette.protocol.messages.RbntMsg;

public class CompressedImgMsg implements RbntMsg
{
    public final int INDX_DATA;
    private StringCell tag = new StringCell(0);
    private StringCell format = new StringCell(tag.getIndex() + StringCell.SIZE);
    private Int32Cell imgSize = new Int32Cell(format.getIndex() + StringCell.SIZE);
    private byte [] data;

    public CompressedImgMsg()  //TODO: SPLIT THIS MSG FOR COMPRESSED AND UNCOMPRESSED MSGS
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
