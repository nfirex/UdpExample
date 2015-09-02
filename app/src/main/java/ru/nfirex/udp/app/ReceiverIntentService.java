package ru.nfirex.udp.app;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ReceiverIntentService extends IntentService {
    public static final String TAG = "ReceiverIntentService";

    public static final String EXTRA_STATUS = "start";
    public static final String STATUS_START = "status.start";
    public static final String STATUS_STOP = "status.stop";

    public static final int LOCAL_PORT = 3000;
    public static final int BUFFER_SIZE = 1024;

    private volatile boolean isRunning;

    public ReceiverIntentService() {
        super(TAG);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        final String status = intent.getStringExtra(EXTRA_STATUS);
        if (TextUtils.equals(status, STATUS_START)) {
            isRunning = true;
        } else if (TextUtils.equals(status, STATUS_STOP)) {
            isRunning = false;
        } else {
            return;
        }

        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while (isRunning) {
            final DatagramSocket socket = createSocket();
            if (socket == null) {
                return;
            }

            final DatagramPacket packet = createPacket();
            if (packet == null) {
                return;
            }

            receive(socket, packet);

            final String data = getData(socket, packet);
            Log.d(TAG, "data: " + data);
        }
    }




    protected DatagramSocket createSocket() {
        try {
            return new DatagramSocket(LOCAL_PORT);
        } catch (SocketException e) {
            Log.d(TAG, "Can't create socket");
            e.printStackTrace();
        }

        return null;
    }

    protected DatagramPacket createPacket() {
        final byte[] buffer = new byte[BUFFER_SIZE];

        return new DatagramPacket(buffer, BUFFER_SIZE);
    }

    protected void receive(DatagramSocket socket, DatagramPacket packet) {
        try {
            socket.receive(packet);
        } catch (IOException e) {
            Log.d(TAG, "Can't receive");
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    protected String getData(DatagramSocket socket, DatagramPacket packet) {
        try {
            return new String(packet.getData(), 0, packet.getLength());
        } finally {
            socket.close();
        }
    }
}