#include "DHT.h"
#include <ArduinoJson.h>

#define DHTPIN 2     
#define DHTTYPE DHT11 

#define BUTTON_PIN 4

DHT dht(DHTPIN, DHTTYPE);

void setup() {
  Serial.begin(9600);
  Serial.println(F("DHTxx test!"));

  pinMode(BUTTON_PIN, INPUT);
  dht.begin();
}

void loop() {
  if(digitalRead(BUTTON_PIN) == HIGH) {

    DynamicJsonDocument doc(1024);

    doc["humidity"] = dht.readHumidity();
    doc["temperature"] = dht.readTemperature();

    serializeJson(doc, Serial);
    // Start a new line
    Serial.println();

    delay(5000);
  }
  
}
