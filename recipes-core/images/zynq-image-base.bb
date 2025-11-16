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

COMPATIBLE_MACHINE = "(k24-smk|k24-smk-kd|k24-smk-sdt|k24c-sm|k24c-sm-sdt|k24i-sm|k24i-sm-sdt|k26-sm|k26-sm-sdt|k26-smk|k26-smk-kr|k26-smk-kr-sdt|k26-smk-kv|k26-smk-kv-sdt|k26-smk-sdt|kria-zynqmp-generic|microzed-zynq7|minized-zynq7|picozed-zynq7|zc702-zynq7|zc706-zynq7|zedboard-zynq7|zybo-linux-bd-zynq7|zybo-zynq7|zynqmp-cg-generic|zynqmp-dr-generic|zynqmp-eg-generic|zynqmp-ev-generic|zynqmp-generic|zcu102-zynqmp|zcu104-zynqmp|zcu106-zynqmp|zcu111-zynqmp|zcu208-zynqmp|zcu208-sdfec-zynqmp|zcu216-zynqmp|zcu670-zynqmp|zcu1275-zynqmp|zcu1285-zynqmp|versal-2ve-2vm-generic|versal-ai-core-generic|versal-ai-edge-generic|versal-generic|versal-hbm-generic|versal-net-generic|versal-premium-generic|versal-prime-generic|vck190-emmc-versal|vck190-ospi-versal|vck190-versal|vek280-versal|vhk158-versal|vmk180-emmc-versal|vmk180-ospi-versal|vmk180-versal|vpk120-versal|vpk180-versal)"
