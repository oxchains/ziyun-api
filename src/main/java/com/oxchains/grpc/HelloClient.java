package com.oxchains.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;

/**
 * Created by song on 2017/7/3.
 */
public class HelloClient {

    private final ManagedChannel channel;
    private final GreeterGrpc.GreeterBlockingStub blockingStub;


    public HelloClient(String host,int port){
        channel = ManagedChannelBuilder.forAddress(host,port)
                .usePlaintext(true)
                .build();
        blockingStub = GreeterGrpc.newBlockingStub(channel);
    }


    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public  boolean greet(String name){
        HelloRequest request = HelloRequest.newBuilder().setGreeting("hello").build();
        HelloReply response;
        try {
            response = blockingStub.sayHello(request);
        } catch (StatusRuntimeException e) {
            String errorMsg= e.getMessage();
            if(errorMsg.indexOf("unknown service")>-1){
//                System.out.println("server on -- unknown service");
                return true;
            }
            return false;
//            System.out.println("+++++++++++++++++++++++++");
//            System.out.println(errorMsg);
//            System.out.println("+++++++++++++++++++++++++");
        }
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        HelloClient client = new HelloClient("localhost",7050);
        for(int i=0;i<5;i++){
            client.greet("world:"+i);
        }


    }
}
