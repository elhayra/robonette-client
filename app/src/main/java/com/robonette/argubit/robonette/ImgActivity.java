package com.robonette.argubit.robonette;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.util.Preconditions;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.robonette.argubit.robonette.protocol.CompressedImgMsg;
import com.robonette.argubit.robonette.protocol.ConnectionListener;
import com.robonette.argubit.robonette.protocol.ConnectionManager;
import com.robonette.argubit.robonette.protocol.ImgMsg;
import com.robonette.argubit.robonette.protocol.InfoMsg;

import java.io.ByteArrayInputStream;

public class ImgActivity extends AppCompatActivity implements ConnectionListener
{
    ImageView imgView;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_control);

        imgView = findViewById(R.id.imgView);
        ConnectionManager.getInstance().subscribe(this);
    }

    public void onIncomingImgMsg(ImgMsg imgMsg)
    {
        byte [] imgBytes = imgMsg.getData();
        int width = imgMsg.getWidth();
        int height = imgMsg.getHeight();
        final Bitmap bitmap = Bitmap.createBitmap  (width, height, Bitmap.Config.ARGB_8888);

        int[] colors = new int[imgBytes.length];
        int r,g,b;
        for (int ci = 0; ci < colors.length-2; ci++)
        {
            b = (int)(0xFF & imgBytes[ci]);
            g = (int)(0xFF & imgBytes[ci+1]);
            r = (int)(0xFF & imgBytes[ci+2]);

            colors[ci] = Color.rgb(r, g, b);

            int red = Color.red(colors[ci]);
            int green = Color.green(colors[ci]);
            int blue = Color.blue(colors[ci]);

        }

        bitmap.setPixels(colors, 0, width, 0, 0, width, height);

        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                imgView.setImageBitmap(bitmap);
            }
        });
    }

    public void onIncomingCompressedImgMsg(CompressedImgMsg compressedImgMsg)
    {
        final byte [] imgBytes = compressedImgMsg.getData();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imgBytes);
        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                imgView.setImageBitmap(bitmap);
            }
        });
    }

    public void onIncomingInfoMsg(InfoMsg infoMsg)
    {

    }

    @Override
    public void onConnectedStatusChanged(boolean status)
    {

    }
}
