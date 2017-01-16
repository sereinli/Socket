# Socket
Android Socket 进程间通信
通常也称作"套接字"，用于描述IP地址和端口，是一个通信链的句柄。在Internet上的主机一般运行了多个服务软件，同时提供几种服务。
每种服务都打开一个Socket，并绑定到一个端口上，不同的端口对应于不同的服务。

网络上的两个程序通过一个双向的通讯连接实现数据的交换，这个双向链路的一端称为一个Socket。Socket通常用来实现客户方和服务方的连接。
Socket是TCP/IP协议的一个十分流行的编程界面，一个Socket由一个IP地址和一个端口号唯一确定。

在java中，Socket和ServerSocket类库位于java .net包中。ServerSocket用于服务器端，Socket是建立网络连接时使用的。
在连接成功时，应用程序两端都会产生一个Socket实例，操作这个实例，完成所需的会话。

服务端的代码，在服务端特定的端口9999监听客户端请求，一旦有请求，便会执行，而后继续监听。
使用accept()这个阻塞函数，就是该方法被调用后一直等待客户端的请求，直到有请求且连接到同一个端口，accept()返回一个对应于客户端的Socket。

#客户端发送数据主要代码：
```
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
```

#服务器端接收数据主要代码：
```
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
 ```
