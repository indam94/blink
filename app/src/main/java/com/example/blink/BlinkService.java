package com.example.blink;

import android.util.Log;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BlinkService {
    private static final String TAG = "BlinkService";
    private static final String HOST = "10.64.6.2";
    private static final int PORT = 8080;

    private static BlinkService instance;

    private ManagedChannel channel;

    public static BlinkService getInstance() {
        if (instance == null) instance = new BlinkService(HOST, PORT);
        return instance;
    }

    private BlinkService(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
    }

    public boolean checkNickname(String nickname) {
        try {
            BlinkGrpc.BlinkBlockingStub stub = BlinkGrpc.newBlockingStub(this.channel);
            BlinkGrpc.BlinkStub asyncStub = BlinkGrpc.newStub(this.channel);

            Nickname request = Nickname.newBuilder().setNickname(nickname).build();
            NicknameResp response = stub.checkNickname(request);
            return response.getResult();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    public boolean submitNickname(String nickname) {
        try {
            BlinkGrpc.BlinkBlockingStub stub = BlinkGrpc.newBlockingStub(this.channel);

            Nickname request = Nickname.newBuilder().setNickname(nickname).build();
            NicknameResp response = stub.submitNickname(request);
            return  response.getResult();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    public boolean upLoadFileRequest(String nickname, String filename) {
        try {
            BlinkGrpc.BlinkBlockingStub stub = BlinkGrpc.newBlockingStub(this.channel);

            UploadFileRequestReq request = UploadFileRequestReq.newBuilder()
                    .setNickname(nickname).setFilename(filename).build();
            UploadFileRequestResp response = stub.uploadFileRequest(request);
            return  response.getResult();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

}
