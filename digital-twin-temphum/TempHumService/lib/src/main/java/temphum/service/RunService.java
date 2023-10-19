package temphum.service;

import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

import temphum.service.model.THMessage;
import temphum.service.mqtt.MQTTClientAws;
import temphum.service.mqtt.MQTTTopic;
import temphum.service.serial.CommChannel;
import temphum.service.serial.SerialCommChannel;

public class RunService {

    public static void main(String[] args) {

        final Gson gson = new Gson();

        final String portName = "COM3";
        System.out.println("Start monitoring serial port " + portName + " at 9600 boud rate");

        //Get a new AWS MQTT client and connect it to the broker
        final MQTTClientAws mqttClient = new MQTTClientAws();
        mqttClient.connect();

        try {
            CommChannel arduinoChannel = new SerialCommChannel(portName, 9600);

            //Receive messages from arduino, and if a message contains DHT sensor data and led state
            //publish those values to temphumid mqtt topic
            final Thread receiver = new Thread(() -> {
                while (true) {
                    try {
                        if (arduinoChannel.isMsgAvailable()) {
                            final String rawMsg = arduinoChannel.receiveMsg();
                            System.out.println("[Arduino Receiver] New Message: " + rawMsg);

                            try {
                                final THMessage message = gson.fromJson(rawMsg, THMessage.class);
                                mqttClient.publishMessage(MQTTTopic.TEMPHUMID, gson.toJson(message));

                            } catch(Exception e) {
                                System.err.println("[Arduino Receiver] Not a significant message");
                            }

                        }
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            //Subscribe to led mqtt topic to monitor new led value and send it to arduino
            mqttClient.subscribeToTopic(MQTTTopic.LED, message -> {
                final String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
                System.out.println("[MQTT Led Topic]: " + payload);
                arduinoChannel.sendMsg(payload);
            });

            receiver.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
