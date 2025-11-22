SUMMARY = "Custom device trees for Xilinx Zynq UltraScale+"
DESCRIPTION = "Device tree sources with override and overlay support"
LICENSE = "GPL-2.0-only | MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

inherit devicetree

DEPENDS = "dtc-native"

# Source can be local files or git repository
# Update FILESEXTRAPATHS and SRC_URI with your sources
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = " \
    file://custom-base.dts \
    file://overlays/custom-overlay-1.dts \
    file://overlays/custom-overlay-2.dts \
"

S = "${WORKDIR}"

# Device tree configuration
DT_FILES_PATH = "${S}"
DT_INCLUDE = "${STAGING_KERNEL_DIR}/include"

# List of device tree files to compile
DEVICE_TREE_SOURCES = " \
    custom-base.dts \
    overlays/custom-overlay-1.dts \
    overlays/custom-overlay-2.dts \
"

do_compile() {
    for dts in ${DEVICE_TREE_SOURCES}; do
        dtc_file="${DT_FILES_PATH}/${dts}"
        if [ -f "${dtc_file}" ]; then
            dtb_file="$(basename ${dts} .dts).dtb"
            dtc -I dts -O dtb -o ${B}/${dtb_file} \
                -i ${DT_FILES_PATH} \
                -i ${DT_INCLUDE} \
                ${dtc_file}
        fi
    done
}

do_install() {
    install -d ${D}/boot/devicetree
    install -d ${D}/boot/devicetree/overlays
    
    # Install base device trees
    for dtb in ${B}/*.dtb; do
        if [ -f "${dtb}" ]; then
            install -m 0644 ${dtb} ${D}/boot/devicetree/
        fi
    done
    
    # Install overlays if any
    if [ -d "${B}/overlays" ]; then
        for dtb in ${B}/overlays/*.dtb; do
            if [ -f "${dtb}" ]; then
                install -m 0644 ${dtb} ${D}/boot/devicetree/overlays/
            fi
        done
    fi
}

FILES:${PN} += "/boot/devicetree/*"

COMPATIBLE_MACHINE = "zynqmp-generic-custom"
