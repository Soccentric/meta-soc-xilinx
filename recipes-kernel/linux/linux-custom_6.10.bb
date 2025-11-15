SUMMARY = "Custom Linux kernel 6.10 for Xilinx Zynq UltraScale+"
DESCRIPTION = "Out-of-tree Linux kernel hosted on GitHub"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel

LINUX_VERSION = "6.10"
LINUX_VERSION_EXTENSION = "-custom-zynq"

# Placeholder GitHub repository - update with your actual repo
SRC_URI = "git://github.com/YOUR-ORG/linux-zynq.git;protocol=https;branch=main"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

DEPENDS += "openssl-native util-linux-native"
DEPENDS += "bison-native flex-native"

# Kernel configuration
KERNEL_DEFCONFIG ?= "defconfig"
KBUILD_DEFCONFIG ?= "${KERNEL_DEFCONFIG}"

do_configure:prepend() {
    if [ -f ${S}/arch/${ARCH}/configs/${KERNEL_DEFCONFIG} ]; then
        cp ${S}/arch/${ARCH}/configs/${KERNEL_DEFCONFIG} ${B}/.config
    else
        bbwarn "Defconfig ${KERNEL_DEFCONFIG} not found, using default"
        oe_runmake -C ${S} O=${B} defconfig
    fi
}

COMPATIBLE_MACHINE = "zynqmp-generic"
