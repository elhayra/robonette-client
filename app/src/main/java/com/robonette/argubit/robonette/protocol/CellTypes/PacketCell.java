package com.robonette.argubit.robonette.protocol.CellTypes;

public abstract class PacketCell
{
    private final int index;
    public PacketCell (int index) { this.index = index; }
    public int getIndex() { return index; }
    public abstract void fromBytes(byte [] bytes);
}
