SUMMARY = "ROS2 image for Xilinx Zynq UltraScale+"
DESCRIPTION = "Specialized image with ROS2 support"

require zynq-image-base.bb

# ROS2 Distribution - configurable via local.conf
# Set: ROS2_DISTRO = "humble" (or "iron", "jazzy")
ROS2_DISTRO ?= "humble"

IMAGE_INSTALL:append = " \
    ros-base \
    ros-core \
    python3-colcon-common-extensions \
    python3-rosdep \
    python3-vcstool \
"

# Additional ROS2 tools
IMAGE_INSTALL:append = " \
    ros2-launch \
    ros2-lifecycle \
    ros2-diagnostic-updater \
"

# Network time for ROS2
IMAGE_INSTALL:append = " \
    chrony \
"

# Note: Add meta-ros layer dependency to layer.conf
# LAYERDEPENDS_{layer_name} += "meta-ros"
