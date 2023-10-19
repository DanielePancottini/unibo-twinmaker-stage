#include "DHT.h"
#include <ArduinoJson.h>

#define DHTPIN 2     
#define DHTTYPE DHT11 

#define BUTTON_PIN 4
#define LED_PIN 8

DHT dht(DHTPIN, DHTTYPE);

//Flag to change the led value according to the mqtt led topic
int ledState = LOW;

void readLedState();

void setup() {
  Serial.begin(9600);
  Serial.println(F("TempHumid Arduino!"));

  pinMode(BUTTON_PIN, INPUT);
  pinMode(LED_PIN, OUTPUT);
  dht.begin();
}

void loop() {

  readLedState();

  //If the button is clicked, get temperature and humidity values from DHT sensor
  // and send it to Java Service via serial
  if(digitalRead(BUTTON_PIN) == HIGH) {

    DynamicJsonDocument doc(1024);

    doc["humidity"] = dht.readHumidity();
    doc["temperature"] = dht.readTemperature();
    doc["led"] = ledState;

    serializeJson(doc, Serial);
    Serial.println();

    delay(1000);
  }
  
}

//Read Serial and check for a new led state value sent by Java Service 
void readLedState() {
  if (Serial.available()) {
    StaticJsonDocument<300> doc;

    DeserializationError err = deserializeJson(doc, Serial);

    if (err == DeserializationError::Ok) {
      Serial.print("led state = ");
      Serial.println(doc["ledState"].as<int>());

      ledState = doc["ledState"];
      digitalWrite(LED_PIN, ledState);
    } 
    else {
      Serial.print("deserializeJson() returned ");
      Serial.println(err.c_str());
  
      // Flush all bytes in the serial port buffer
      while (Serial.available() > 0)
        Serial.read();
    }
  }
}