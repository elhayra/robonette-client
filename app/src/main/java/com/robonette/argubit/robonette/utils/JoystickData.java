package com.robonette.argubit.robonette.utils;

/**
 * Created by submachine on 11/05/2018.
 */

public class JoystickData
{
    private float axisX;
    private float axisY;
    private int id;

    public float getAxisX()
    {
        return axisX;
    }

    public float getAxisY()
    {
        return axisY;
    }

    public int getId()
    {
        return id;
    }

    public void setAxisX(float axisX)
    {
        this.axisX = axisX;
    }

    public void setAxisY(float axisY)
    {
        this.axisY = axisY;
    }

    public void setId(int id)
    {
        this.id = id;
    }
}
