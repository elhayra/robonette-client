package com.robonette.argubit.robonette.utils;

import android.util.Log;

import com.robonette.argubit.robonette.R;
import com.robonette.argubit.robonette.protocol.ConnectionManager;
import com.robonette.argubit.robonette.protocol.messages.CmdMsg;
import com.robonette.argubit.robonette.protocol.messages.RbntHeader;

import java.util.TimerTask;

/**
 * Created by submachine on 11/05/2018.
 */

public class JoystickSenderTask extends TimerTask
{
    JoystickData joyData = new JoystickData();

    public synchronized void updateJoystickState(float axisX, float axisY, int id)
    {
        Log.i("joy1: ", "x: " + axisX + "| y: " + axisY);
        joyData.setAxisX(axisX);
        joyData.setAxisY(axisY);
        joyData.setId(id);

        Log.i("joy2: ", "x: " + joyData.getAxisX() + "| y: " + joyData.getAxisY());
    }

    public JoystickSenderTask()
    {
        super();
    }

    @Override
    public void run()
    {
        float axisX = joyData.getAxisX();
        float axisY = joyData.getAxisY();
        if (axisX !=0 && axisY != 0 )
            Log.i("joy3: ", "x: " + axisX + "| y: " + axisY);


        RbntHeader header = new RbntHeader();
        header.setMsgType(RbntHeader.MsgType.COMMAND);
        header.setMsgSize(CmdMsg.SIZE);
        final byte [] headerBytes = header.toBytes();

        if (joyData.getId() == R.id.rightJoystick)
        {
                ConnectionManager.getInstance().sendBytes(headerBytes);

                CmdMsg xCmdMsg = new CmdMsg();
                xCmdMsg.id.setValue((byte)20);
                xCmdMsg.value.setValue(axisX);
                byte [] xCmdBytes = xCmdMsg.toBytes();

                ConnectionManager.getInstance().sendBytes(xCmdBytes);


                ConnectionManager.getInstance().sendBytes(headerBytes);

                CmdMsg yCmdMsg = new CmdMsg();
                yCmdMsg.id.setValue((byte)21);
                yCmdMsg.value.setValue(joyData.getAxisY());
                byte [] yCmdBytes = yCmdMsg.toBytes();

                ConnectionManager.getInstance().sendBytes(yCmdBytes);



        }
        /*else if (joyData.getId() == R.id.leftJoystick)
        {

                ConnectionManager.getInstance().sendBytes(headerBytes);

                CmdMsg xCmdMsg = new CmdMsg();
                xCmdMsg.id.setValue((byte)10);
                xCmdMsg.value.setValue(joyData.getAxisX());
                byte [] xCmdBytes = xCmdMsg.toBytes();

                ConnectionManager.getInstance().sendBytes(xCmdBytes);

                ConnectionManager.getInstance().sendBytes(headerBytes);

                CmdMsg yCmdMsg = new CmdMsg();
                yCmdMsg.id.setValue((byte)11);
                yCmdMsg.value.setValue(joyData.getAxisY());
                byte [] yCmdBytes = yCmdMsg.toBytes();

                ConnectionManager.getInstance().sendBytes(yCmdBytes);

        }*/

    }

    @Override
    public boolean cancel()
    {
        return super.cancel();
    }

    @Override
    public long scheduledExecutionTime()
    {
        return super.scheduledExecutionTime();
    }
}
