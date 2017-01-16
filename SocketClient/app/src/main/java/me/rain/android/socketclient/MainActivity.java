package me.rain.android.socketclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnConnect;
    private Button mBtnSendMsg;

    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        mBtnConnect = (Button)findViewById(R.id.btn_connect_server);
        mBtnConnect.setOnClickListener(this);

        mBtnSendMsg = (Button)findViewById(R.id.btn_send_message);
        mBtnSendMsg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_connect_server) {
            connectServer();
        }else if(view.getId() == R.id.btn_send_message) {
            sendMessage();
        }
    }

    private void connectServer() {
        new Thread() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket("10.19.83.64", 9999);  //服务端所运行的设备的IP
                    Log.d("rain", "mSocket:"+mSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void sendMessage() {
        new Thread() {
            @Override
            public void run() {
                if(mSocket != null) {
                    try {
                        DataOutputStream writer = new DataOutputStream(mSocket.getOutputStream());
                        writer.writeUTF("Hello, my id is 007!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
