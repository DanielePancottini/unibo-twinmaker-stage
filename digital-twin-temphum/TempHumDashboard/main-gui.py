import tkinter as tk
import json
from awscrt import mqtt
import aws_mqtt_utils as mqtt_utils

#On temphumid message show both temperature and humidity last values 
def on_message_received(topic, payload, dup, qos, retain, **kwargs):
    print("Received message from topic '{}': {}".format(topic, payload))
    data = json.loads(payload)
    temperatureValue.set(data["temperature"])
    humidityValue.set(data["humidity"])
    
#On ledon button click publish to led topic the high value  
def on_ledon_click():
    mqttClient.publish(
        topic="led",
        payload='{"ledState": 1}',
        qos=mqtt.QoS.AT_LEAST_ONCE)
    
#On ledoff button click publish to led topic the low value  
def on_ledoff_click():
    mqttClient.publish(
        topic="led",
        payload='{"ledState": 0}',
        qos=mqtt.QoS.AT_LEAST_ONCE)

#Get a new aws mqtt client and connect to the broker
mqttClient = mqtt_utils.getMqttClient()
mqttClient.connect().result()

#Subscribe to temphumid topic in order to get humidity and temperature values
mqttClient.subscribe(
        topic="temphumid",
        qos=mqtt.QoS.AT_LEAST_ONCE,
        callback=on_message_received)

#Create the tkinter master object
root = tk.Tk()
root.title('Temphumid Dashboard')
root.geometry("300x200")  
root.maxsize(500, 300)  

root.columnconfigure(0, weight = 1)
root.columnconfigure(1, weight = 1)
root.columnconfigure(2, weight = 1)
root.columnconfigure(3, weight = 1)

root.rowconfigure(0, weight = 1)
root.rowconfigure(1, weight = 1)
root.rowconfigure(2, weight = 1)
root.rowconfigure(3, weight = 1)

#Temperature label
tk.Label(root, text="Temperature:").grid(row=0, column=1)
temperatureValue = tk.StringVar(value="Undefined")
tk.Label(root, textvariable=temperatureValue, text="Undefined").grid(row=0, column=2)

#Humidity label
tk.Label(root, text="Humidity:" ).grid(row=1, column=1)
humidityValue = tk.StringVar(value="Undefined")
tk.Label(root, textvariable=humidityValue).grid(row=1, column=2)

#LedOn button
tk.Button(root, text="LED ON", command=on_ledon_click).grid(row = 3, column=1)

#LedOff button
tk.Button(root, text="LED OFF", fg="green", command=on_ledoff_click).grid(row=3, column=2)

#Threads start
root.mainloop()


    