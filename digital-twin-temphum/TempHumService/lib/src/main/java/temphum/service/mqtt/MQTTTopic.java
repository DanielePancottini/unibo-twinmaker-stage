package temphum.service.mqtt;

public enum MQTTTopic {

    TEST("test"),
    TEMPHUMID("temphumid"),
    LED("led");

    private final String name;

    private MQTTTopic(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
