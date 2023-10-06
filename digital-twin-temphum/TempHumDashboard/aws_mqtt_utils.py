from awsiot import mqtt_connection_builder

#AWS IotCore constants in order to build an mqtt client
AWS_ENDPOINT = "ayn0g9lgl97xl-ats.iot.us-east-1.amazonaws.com"
AWS_CERT = "./res/5873f00eae53dbb972bbde3e1ca99b419c4968504f34336127aea3188f6d0361-certificate.pem.crt"
AWS_KEY = "./res/5873f00eae53dbb972bbde3e1ca99b419c4968504f34336127aea3188f6d0361-private.pem.key"
AWS_CLIENT_ID = "dht11-sensor-client2"

# Callback when connection is accidentally lost.
def on_connection_interrupted(connection, error, **kwargs):
    print("Connection interrupted. error: {}".format(error))

# Callback when an interrupted connection is re-established.
def on_connection_resumed(connection, return_code, session_present, **kwargs):
    print("Connection resumed. return_code: {} session_present: {}".format(return_code, session_present))

#Build and return a new mqtt client
def getMqttClient():
    # Create a MQTT connection from the command line data
    return mqtt_connection_builder.mtls_from_path(
        endpoint=AWS_ENDPOINT,
        cert_filepath=AWS_CERT,
        pri_key_filepath=AWS_KEY,
        on_connection_interrupted=on_connection_interrupted,
        on_connection_resumed=on_connection_resumed,
        client_id=AWS_CLIENT_ID,
        clean_session=False,
        keep_alive_secs=30)