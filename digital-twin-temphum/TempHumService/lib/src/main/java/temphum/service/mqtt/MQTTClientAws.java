package temphum.service.mqtt;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.MqttMessage;
import software.amazon.awssdk.crt.mqtt.QualityOfService;
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder;

public class MQTTClientAws {

    String clientEndpoint = "ayn0g9lgl97xl-ats.iot.us-east-1.amazonaws.com";
    String clientId = "dht11-sensor-client1";
    String certificateFile = "5873f00eae53dbb972bbde3e1ca99b419c4968504f34336127aea3188f6d0361-certificate.pem.crt";
    String privateKeyFile = "5873f00eae53dbb972bbde3e1ca99b419c4968504f34336127aea3188f6d0361-private.pem.key";

    private MqttClientConnection client;

    public MQTTClientAws() {
        ClassLoader cl = this.getClass().getClassLoader();

        /**
         * Create the MQTT connection from the builder
         */
        try {
            this.client = AwsIotMqttConnectionBuilder.newMtlsBuilder(
                        cl.getResourceAsStream(this.certificateFile).readAllBytes(),
                        cl.getResourceAsStream(this.privateKeyFile).readAllBytes())
                    .withClientId(this.clientId)
                    .withEndpoint(this.clientEndpoint)
                    .withCleanSession(true)
                    .withProtocolOperationTimeoutMs(60000)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void connect() {
        CompletableFuture<Boolean> connected = this.client.connect();
        try {
            boolean sessionPresent = connected.get();
            System.out.println("Connected to " + (!sessionPresent ? "new" : "existing") + " session!");
        } catch (Exception ex) {
            throw new RuntimeException("Exception occurred during connect", ex);
        }
    }

    public void publishMessage(final MQTTTopic topic, final String message) {
        this.client.publish(new MqttMessage(topic.getName(), message.getBytes(), QualityOfService.AT_LEAST_ONCE));
        System.out.println("MQTT Client: send message - " + message + " to " + topic.getName());
    }

}
