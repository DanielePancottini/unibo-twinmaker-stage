package temphum.service;

import com.google.gson.Gson;

import temphum.service.model.HTMessage;
import temphum.service.mqtt.MQTTClientAws;
import temphum.service.mqtt.MQTTTopic;
import temphum.service.serial.CommChannel;
import temphum.service.serial.SerialCommChannel;

public class RunService {

    public static void main(String[] args) {

        final Gson gson = new Gson();

        final String portName = "COM3";
        System.out.println("Start monitoring serial port " + portName + " at 9600 boud rate");

        final MQTTClientAws mqttClient = new MQTTClientAws();
        mqttClient.connect();

        try {
            CommChannel arduinoChannel = new SerialCommChannel(portName, 9600);

            // thread for receiving msg from arduino and store it
            final Thread receiver = new Thread(() -> {
                while (true) {
                    try {
                        if (arduinoChannel.isMsgAvailable()) {
                            final String rawMsg = arduinoChannel.receiveMsg();
                            System.out.println("New Message: " + rawMsg);

                            try {
                              //raw message to pojo
                                final HTMessage message = gson.fromJson(rawMsg, HTMessage.class);

                                //send message to aws
                                mqttClient.publishMessage(MQTTTopic.TEMPHUMID, gson.toJson(message));

                            } catch(Exception e) {
                                //not a valid json
                            }

                        }
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            receiver.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
