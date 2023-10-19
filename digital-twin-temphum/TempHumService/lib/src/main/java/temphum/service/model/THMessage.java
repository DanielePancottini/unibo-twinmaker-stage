package temphum.service.model;

public class THMessage {

    private double temperature;
    private double humidity;
    private int led;

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(final double humidity) {
        this.humidity = humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(final double temperature) {
        this.temperature = temperature;
    }

    public int getLed() {
        return led;
    }

    public void setLed(final int led) {
        this.led = led;
    }

}
