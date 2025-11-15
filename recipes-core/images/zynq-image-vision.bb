SUMMARY = "VISION image for Xilinx Zynq UltraScale+"
DESCRIPTION = "Specialized image with VISION support"

require zynq-image-base.bb

# Vision System Framework Configuration - set in local.conf
# VISION_FRAMEWORKS = "opencv gstreamer tensorflow pytorch"
VISION_FRAMEWORKS ?= "opencv gstreamer"

# OpenCV Support
IMAGE_INSTALL:append = " \
    ${@bb.utils.contains('VISION_FRAMEWORKS', 'opencv', 'opencv python3-opencv', '', d)} \
"

# GStreamer Support
IMAGE_INSTALL:append = " \
    ${@bb.utils.contains('VISION_FRAMEWORKS', 'gstreamer', 'gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-good gstreamer1.0-plugins-bad gstreamer1.0-plugins-ugly gstreamer1.0-libav python3-gst', '', d)} \
"

# TensorFlow Lite Support
IMAGE_INSTALL:append = " \
    ${@bb.utils.contains('VISION_FRAMEWORKS', 'tensorflow', 'tensorflow-lite python3-tensorflow-lite', '', d)} \
"

# PyTorch Support (if available)
IMAGE_INSTALL:append = " \
    ${@bb.utils.contains('VISION_FRAMEWORKS', 'pytorch', 'pytorch python3-torch', '', d)} \
"

# Common vision utilities
IMAGE_INSTALL:append = " \
    v4l-utils \
    ffmpeg \
    python3-numpy \
    python3-pillow \
    python3-scipy \
"

# Camera and video device support
IMAGE_INSTALL:append = " \
    kernel-module-uvcvideo \
"
