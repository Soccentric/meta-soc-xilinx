SUMMARY = "Base image for Xilinx Zynq UltraScale+"
DESCRIPTION = "Custom image based on core-image-full-cmdline with platform-specific additions"

LICENSE = "MIT"

inherit core-image

# Start with full command-line base
IMAGE_FEATURES += "ssh-server-openssh"
IMAGE_FEATURES += "package-management"
IMAGE_FEATURES += "debug-tweaks"

# Core packages
IMAGE_INSTALL:append = " \
    kernel-modules \
    u-boot-fw-utils \
    dtc \
    i2c-tools \
    spitools \
    mtd-utils \
    can-utils \
    ethtool \
    iperf3 \
    tcpdump \
    strace \
    gdbserver \
    python3 \
    python3-pip \
    git \
    vim \
    htop \
"

# Platform-specific kernel and bootloader
IMAGE_INSTALL:append = " \
    linux-custom \
    u-boot-custom \
    device-tree-zynq \
"

# RPU firmware support
IMAGE_INSTALL:append = " \
    rpu-firmware-zynq \
"

# XSA configuration
IMAGE_INSTALL:append = " \
    xsa-config \
"

# Additional development tools
IMAGE_INSTALL:append = " \
    cmake \
    make \
    gcc \
    g++ \
"

# Set root password for debugging (remove for production)
# Password: root
EXTRA_USERS_PARAMS = "usermod -P root root;"

COMPATIBLE_MACHINE = "zynqmp-generic"
