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

package com.robonette.argubit.robonette.protocol.CellTypes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Float32Cell extends PacketCell
{
    public static final int SIZE = 4;
    private float value;

    public Float32Cell(int index) { super(index); }

    public Float32Cell(int index, float value)
    {
        super(index);
        setValue(value);
    }

    public boolean fromBytes(byte [] bytes)
    {
        byte [] trimmedArr = Arrays.copyOfRange(bytes,
                getIndex(),
                getIndex() + SIZE);
        ByteBuffer wrapped = ByteBuffer.wrap(trimmedArr);
        wrapped.order(ByteOrder.LITTLE_ENDIAN);
        value = wrapped.getFloat();
        return true;
    }

    public boolean toBytes(byte [] bytes)
    {
        if (bytes.length < getIndex() + SIZE)
            return false;

        byte[] valueBytes = ByteBuffer.allocate(SIZE).putFloat(value).array();
        for (int i=0; i<SIZE; i++)
            bytes[i + getIndex()] = valueBytes[i];

        return true;
    }

    public float getValue() { return value; }

    public void setValue(float value) { this.value = value; }
}
