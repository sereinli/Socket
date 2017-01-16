package me.rain.android.socketserver;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private final int UPDATE_MESSAGE_FROM_CLIENT = 1;

    private TextView mTextView;
    private boolean mLoop = true;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_MESSAGE_FROM_CLIENT:
                    String message = (String)msg.obj;
                    if(!TextUtils.isEmpty(message)) {
                        Log.d("rain", "handleMessage:"+message);
                        mTextView.setText(message);
                    }
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView)findViewById(R.id.tv_text);
        init();
    }

    private void init() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Thread() {
                    @Override
                    public void run() {
                        startServer();
                    }
                }.start();
            }
        }, 100);
    }

    private void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            while (mLoop) {
                reader(serverSocket.accept());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reader(final Socket socket) {
        new Thread() {
            @Override
            public void run() {
                try {
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    String messageFromClient = dataInputStream.readUTF();

                    Log.d("rain", "server received message from client:"+messageFromClient);
                    Message message = new Message();
                    message.what = UPDATE_MESSAGE_FROM_CLIENT;
                    message.obj = messageFromClient;
                    mHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void finish() {
        super.finish();
        mLoop = false;
    }
}
