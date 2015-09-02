package ru.nfirex.udp.app;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SenderIntentService extends IntentService {
    public static final String TAG = "SenderIntentService";

    public static final String EXTRA_DATA = "data";

    public static final String LOCAL_HOST = "127.0.0.1";
    public static final int LOCAL_PORT = 3000;

    public SenderIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String data = intent.getStringExtra(EXTRA_DATA);
        if (data == null) {
            return;
        }

        final DatagramSocket socket = createSocket();
        if (socket == null) {
            return;
        }

        final InetAddress address = createAddress(LOCAL_HOST);
        if (address == null) {
            return;
        }

        final DatagramPacket packet = createPacket(data, address);
        if (packet == null) {
            return;
        }

        execute(socket, packet);
    }




    protected DatagramSocket createSocket() {
        try {
            return new DatagramSocket();
        } catch (SocketException e) {
            Log.d(TAG, "Can't create socket");
            e.printStackTrace();
        }

        return null;
    }

    protected InetAddress createAddress(String host) {
        try {
            return InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            Log.d(TAG, "Can't create address");
            e.printStackTrace();
        }

        return null;
    }

    protected DatagramPacket createPacket(String source, InetAddress address) {
        return new DatagramPacket(source.getBytes(), source.length(), address, LOCAL_PORT);
    }

    protected void execute(DatagramSocket socket, DatagramPacket packet) {
        try {
            socket.send(packet);
        } catch (IOException e) {
            Log.d(TAG, "Can't execute");
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}