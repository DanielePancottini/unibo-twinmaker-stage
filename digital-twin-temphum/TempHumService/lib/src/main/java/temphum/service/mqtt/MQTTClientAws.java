package temphum.service.mqtt;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.MqttMessage;
import software.amazon.awssdk.crt.mqtt.QualityOfService;
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder;

public class MQTTClientAws {

    //AWS IotCore configuration constants
    private final static String CLIENT_ENDPOINT = "ayn0g9lgl97xl-ats.iot.us-east-1.amazonaws.com";
    private final static String CLIENT_ID = "dht11-sensor-client1";
    private final static String CERTIFICATE_FILE = "5873f00eae53dbb972bbde3e1ca99b419c4968504f34336127aea3188f6d0361-certificate.pem.crt";
    private final static String PRIVATE_KEY_FILE = "5873f00eae53dbb972bbde3e1ca99b419c4968504f34336127aea3188f6d0361-private.pem.key";

    private MqttClientConnection client;

    //Build a new AWS MQTT Client with given configuration data
    public MQTTClientAws() {
        ClassLoader cl = this.getClass().getClassLoader();

        //Create the MQTT connection from the builder
        try {
            this.client = AwsIotMqttConnectionBuilder.newMtlsBuilder(
                        cl.getResourceAsStream(MQTTClientAws.CERTIFICATE_FILE).readAllBytes(),
                        cl.getResourceAsStream(MQTTClientAws.PRIVATE_KEY_FILE).readAllBytes())
                    .withClientId(MQTTClientAws.CLIENT_ID)
                    .withEndpoint(MQTTClientAws.CLIENT_ENDPOINT)
                    .withCleanSession(true)
                    .withProtocolOperationTimeoutMs(60000)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Connect this client to the aws mqtt broker
    public void connect() {
        CompletableFuture<Boolean> connected = this.client.connect();
        try {
            boolean sessionPresent = connected.get();
            System.out.println("[MQTT Client] Connected to " + (!sessionPresent ? "new" : "existing") + " session!");
        } catch (Exception ex) {
            throw new RuntimeException("[MQTT Client] Exception occurred during connect", ex);
        }
    }

    //Publish message to a given topic
    public void publishMessage(final MQTTTopic topic, final String message) {
        this.client.publish(new MqttMessage(topic.getName(), message.getBytes(), QualityOfService.AT_LEAST_ONCE));
        System.out.println("[MQTT Client] Send message - " + message + " to " + topic.getName());
    }

    //Subscribe this client to a given topic
    public void subscribeToTopic(final MQTTTopic topic, final Consumer<MqttMessage> callback) {
        this.client.subscribe(topic.getName(), QualityOfService.AT_LEAST_ONCE, callback);
        System.out.println("[MQTT Client] Subscribed to " + topic.getName());
    }

}
