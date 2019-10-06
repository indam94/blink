package com.example.blink.utils;

import android.util.Log;
import com.example.blink.*;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

public class BlinkService {
    private static final String TAG = "BlinkService";
    private static final int PORT = 8080;

    private static BlinkService instance;

    private ManagedChannel channel;

    public static BlinkService getInstance() {
        if (instance == null) instance = new BlinkService(App.prefs.getMyIp(), PORT);
        return instance;
    }

    private BlinkService(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
    }

    public boolean checkNickname(String nickname) {
        try {
            BlinkGrpc.BlinkBlockingStub stub = BlinkGrpc.newBlockingStub(this.channel);
            //BlinkGrpc.BlinkStub asyncStub = BlinkGrpc.newStub(this.channel);

            Nickname request = Nickname.newBuilder().setNickname(nickname).build();

            NicknameResp response = stub.checkNickname(request);

            Log.d("gRPC", response.getResult() ? "true" : "false");

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

    public ReceiveRequest setReceiverStream(String nickname, float lat, float lng){
        try {
            BlinkGrpc.BlinkBlockingStub stub = BlinkGrpc.newBlockingStub(this.channel);

            ReceiverInfo request = ReceiverInfo.newBuilder()
                    .setNickname(nickname).setLocation(
                            Location.newBuilder().setLatitude(lat).setLongitude(lng).build()
                    ).build();

            ReceiveRequest response = stub.setReceiverStream(request);
            if (response.getUuid().equals("Nil")) return null;
            return response;
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public String uploadFileRequest(String nickname, String filename) {
        try {
            BlinkGrpc.BlinkBlockingStub stub = BlinkGrpc.newBlockingStub(this.channel);

            UploadFileRequestReq request = UploadFileRequestReq.newBuilder()
                    .setNickname(nickname).setFilename(filename).build();
            UploadFileRequestResp response = stub.uploadFileRequest(request);
            if (response.getResult()) {
                return response.getUuid();
            } else {
                return "";
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return "";
        }//if(""==)
    }

    public ArrayList<Client> getClientsByLocation(float latitude, float longitude){
        ArrayList<Client> clientList = new ArrayList<>();
        try {
            BlinkGrpc.BlinkBlockingStub stub = BlinkGrpc.newBlockingStub(this.channel);

            Location request = Location.newBuilder().setLatitude(latitude).setLongitude(longitude).build();
            Iterator<Client> response = stub.getClientsByLocation(request);

            while (response.hasNext()) {
                Client cli = response.next();
                clientList.add(cli);
            }

            return clientList;
        } catch (Exception e) {
            return clientList;
        }
    }


    public ArrayList<Client> getClientsByName (String nickname){
        ArrayList<Client> clientList = new ArrayList<>();
        try{
            BlinkGrpc.BlinkBlockingStub stub = BlinkGrpc.newBlockingStub(this.channel);

            Nickname request = Nickname.newBuilder().setNickname(nickname).build();
            Iterator<Client> response = stub.getClientsByName(request);

            while (response.hasNext()){
                Client cli = response.next();
                clientList.add(cli);
            }

            return clientList;

        } catch (Exception e){

            return clientList;
        }
    }

    public UploadFileResp uploadFile (File file, String uuid){
        try {
            BlinkGrpc.BlinkStub stub = BlinkGrpc.newStub(this.channel);
            final CountDownLatch finishLatch = new CountDownLatch(1);
            final UploadFileResp.Builder builder = UploadFileResp.newBuilder();

            StreamObserver<UploadFileResp> responseObserver = new StreamObserver<UploadFileResp>() {

                @Override
                public void onNext(UploadFileResp value) {
                    builder.setCode(value.getCode()).setUuid(value.getUuid());
                }

                @Override
                public void onError(Throwable t) {
                    finishLatch.countDown();
                }

                @Override
                public void onCompleted() {
                    finishLatch.countDown();
                }
            };

            StreamObserver<FileChunk> requestObserver = stub.uploadFile(responseObserver);

            try {
                BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));

                final int chunkSize = 1024 * 1024;
                final long chunkLength = file.length() / chunkSize;

                Log.d("UploadFile", "Chunk size: " + chunkSize);
                Log.d("UploadFile", "Chunk length: " + chunkLength);

                byte[] buf = new byte[chunkSize];
                int count;
                while ((count = stream.read(buf)) > 0) {
                    FileChunk chunk = FileChunk.newBuilder()
                            .setUuid(uuid).setChunk(ByteString.copyFrom(ByteBuffer.wrap(buf), count))
                            .build();

                    requestObserver.onNext(chunk);

                    if (finishLatch.getCount() == 0) {
                        // Error occurred before the file send ends
                        builder.setUuid("").setCode(UploadStatusCode.Unknown);
                        break;
                    }

                }
                //FileChunk chunk = FileChunk.newBuilder().setUuid(uuid).setChunk(ByteBuffer.wrap())

            } catch (Exception e) {
                requestObserver.onError(e);
                e.printStackTrace();
                throw e;
            }

            requestObserver.onCompleted();
            Log.d("BlinkService", "File upload complete");

            UploadFileResp result = builder.build();
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public Boolean sendRequest (String nickname, String receiverNickname, String uuid){
        try{
            BlinkGrpc.BlinkBlockingStub stub = BlinkGrpc.newBlockingStub(this.channel);
            SendRequestReq request = SendRequestReq.newBuilder()
                    .setNickname(nickname).setReceiverNickname(receiverNickname).setUuid(uuid)
                    .build();

            SendRequestResp response = stub.sendRequest(request);
            return response.getResult();


        } catch (Exception e){

            return false;
        }
    }

    public String respondGrant (String nickname, String receiverNickname, String filename, String uuid){
        try{
            BlinkGrpc.BlinkBlockingStub stub = BlinkGrpc.newBlockingStub(this.channel);
            ReceiveRequest request = ReceiveRequest.newBuilder()
                    .setNickname(nickname).setReceiverNickname(receiverNickname)
                    .setFilename(filename).setUuid(uuid).build();

            FileLink response = stub.respondGrant(request);

            return response.getLink();


        } catch (Exception e){

            return "";
        }
    }

    public boolean respondDenial (String nickname, String receiverNickname, String filename, String uuid) {
        try{
            BlinkGrpc.BlinkBlockingStub stub = BlinkGrpc.newBlockingStub(this.channel);
            ReceiveRequest request = ReceiveRequest.newBuilder().setNickname(nickname).setReceiverNickname(receiverNickname)
                    .setFilename(filename).setUuid(uuid).build();
            stub.respondDenial(request);

            return true;
        } catch (Exception e){

            return false;
        }
    }

    public int makeSpot (String nickname, Location location, String uuid)  {
        try{
            BlinkGrpc.BlinkBlockingStub stub = BlinkGrpc.newBlockingStub(this.channel);
            MakeSpotReq request = MakeSpotReq.newBuilder()
                    .setNickname(nickname).setLocation(location).setUuid(uuid).build();
            MakeSpotResp response = stub.makeSpot(request);

            return response.getId();

        } catch (Exception e){
            //?
            return 0;
        }
    }

    public Spot getSpotById (int getSpotByIdReq) {
        try{
            BlinkGrpc.BlinkBlockingStub stub = BlinkGrpc.newBlockingStub(this.channel);
            GetSpotByIdReq request = GetSpotByIdReq.newBuilder().setId(getSpotByIdReq).build();
            Spot response = stub.getSpotById(request);

            return response;
        } catch (Exception e){

            return null;
        }
    }

    public ArrayList<Spot> getSpotsByLocation (float latitude, float longitude) {
        ArrayList<Spot> spotList = new ArrayList<>();
        try{
            BlinkGrpc.BlinkBlockingStub stub = BlinkGrpc.newBlockingStub(this.channel);
            Location request = Location.newBuilder().setLongitude(longitude).setLatitude(latitude).build();

            Iterator<Spot> response = stub.getSpotsByLocation(request);

            while(response.hasNext()){
                Spot spot = response.next();
                spotList.add(spot);
            }

            return spotList;

        } catch (Exception e){

            return spotList;
        }
    }

    public String getFileFromSpot (int id, String nickname, Location location, String uuid) {
        try{
            BlinkGrpc.BlinkBlockingStub stub = BlinkGrpc.newBlockingStub(this.channel);
            Spot request = Spot.newBuilder()
                    .setId(id).setNickname(nickname).setLocation(location).setUuid(uuid).build();
            FileLink response = stub.getFileFromSpot(request);

            return response.getLink();

        } catch (Exception e){

            return "";
        }
    }
}
