#include "DHT.h"
#include <ArduinoJson.h>

#define DHTPIN 2     
#define DHTTYPE DHT11 

DHT dht(DHTPIN, DHTTYPE);

void setup() {
  Serial.begin(9600);
  Serial.println(F("DHTxx test!"));

  dht.begin();
}

void loop() {

    DynamicJsonDocument doc(1024);

    doc["humidity"] = dht.readHumidity();
    doc["temperature"] = dht.readTemperature();

    serializeJson(doc, Serial);
    // Start a new line
    Serial.println();

    delay(360000);
  
}
