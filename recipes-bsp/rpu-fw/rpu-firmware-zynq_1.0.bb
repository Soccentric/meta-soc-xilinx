SUMMARY = "R5 firmware for Xilinx Zynq UltraScale+"
DESCRIPTION = "RPU/MCU firmware - supports pre-built ELF and source compilation"
LICENSE = "CLOSED"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Configuration: Set to "prebuilt" or "source"
RPU_BUILD_MODE ?= "prebuilt"

# For pre-built firmware
SRC_URI:append = " \
    ${@bb.utils.contains('RPU_BUILD_MODE', 'prebuilt', 'file://firmware.elf', '', d)} \
"

# For source-based firmware
SRC_URI:append = " \
    ${@bb.utils.contains('RPU_BUILD_MODE', 'source', 'git://github.com/YOUR-ORG/rpu-fw-zynq.git;protocol=https;branch=main', '', d)} \
"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

# Toolchain for compiling RPU firmware
DEPENDS = "gcc-arm-none-eabi-native"
TARGET_CC = "arm-none-eabi-gcc"
TARGET_OBJCOPY = "arm-none-eabi-objcopy"

RPU_FIRMWARE_NAME = "rpu-firmware.elf"
RPU_DEPLOY_DIR = "${DEPLOY_DIR_IMAGE}/rpu"

inherit deploy

do_compile() {
    if [ "${RPU_BUILD_MODE}" = "source" ]; then
        cd ${S}
        # Add your build commands here
        # Example: make CROSS_COMPILE=arm-none-eabi-
        bbwarn "RPU firmware source compilation not fully configured"
        bbwarn "Please update do_compile with your build commands"
    fi
}

do_install() {
    install -d ${D}${base_libdir}/firmware
    
    if [ "${RPU_BUILD_MODE}" = "prebuilt" ]; then
        install -m 0644 ${WORKDIR}/firmware.elf ${D}${base_libdir}/firmware/${RPU_FIRMWARE_NAME}
    elif [ "${RPU_BUILD_MODE}" = "source" ]; then
        # Install compiled firmware
        # Update path based on your build output
        if [ -f ${S}/build/firmware.elf ]; then
            install -m 0644 ${S}/build/firmware.elf ${D}${base_libdir}/firmware/${RPU_FIRMWARE_NAME}
        else
            bbwarn "Compiled firmware not found at expected location"
        fi
    fi
}

do_deploy() {
    install -d ${RPU_DEPLOY_DIR}
    if [ -f ${D}${base_libdir}/firmware/${RPU_FIRMWARE_NAME} ]; then
        install -m 0644 ${D}${base_libdir}/firmware/${RPU_FIRMWARE_NAME} ${RPU_DEPLOY_DIR}/
    fi
}

addtask deploy before do_build after do_install

FILES:${PN} += "${base_libdir}/firmware/*"

COMPATIBLE_MACHINE = "zynqmp-generic-custom"

# Usage notes:
# 1. For pre-built: Set RPU_BUILD_MODE = "prebuilt" and place firmware.elf in files/
# 2. For source: Set RPU_BUILD_MODE = "source" and update SRC_URI with your repo
