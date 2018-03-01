package com.robonette.argubit.robonette;

public class InfoItem
{
    private String label;
    private String data;
    private String unit;

    public InfoItem(String label, String data, String unit)
    {
        this.label = label;
        this.data = data;
        this.unit = unit;
    }

    public String getLabel() { return label; }
    public String getData() { return data; }
    public String getUnit() { return unit; }

    public void setLabel(String label) { this.label = label; }
    public void setData(String data) { this.data = data; }
    public void setUnit(String unit) { this.unit = unit; }
}
