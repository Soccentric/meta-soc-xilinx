SUMMARY = "IOT image for Xilinx Zynq UltraScale+"
DESCRIPTION = "Specialized image with IOT support"

require zynq-image-base.bb

# IoT Framework Configuration - set in local.conf
# IOT_PROTOCOLS = "mqtt aws-iot azure-iot coap"
IOT_PROTOCOLS ?= "mqtt"

# MQTT Support
IMAGE_INSTALL:append = " \
    ${@bb.utils.contains('IOT_PROTOCOLS', 'mqtt', 'mosquitto mosquitto-clients paho-mqtt-c paho-mqtt-cpp python3-paho-mqtt', '', d)} \
"

# AWS IoT Support
IMAGE_INSTALL:append = " \
    ${@bb.utils.contains('IOT_PROTOCOLS', 'aws-iot', 'aws-iot-device-sdk-cpp-v2 aws-iot-device-sdk-python-v2', '', d)} \
"

# Azure IoT Support
IMAGE_INSTALL:append = " \
    ${@bb.utils.contains('IOT_PROTOCOLS', 'azure-iot', 'azure-iot-sdk-c python3-azure-iot-device', '', d)} \
"

# CoAP Support
IMAGE_INSTALL:append = " \
    ${@bb.utils.contains('IOT_PROTOCOLS', 'coap', 'libcoap python3-aiocoap', '', d)} \
"

# Common IoT utilities
IMAGE_INSTALL:append = " \
    openssl \
    ca-certificates \
    curl \
    wget \
    jq \
    python3-json \
    python3-requests \
"
