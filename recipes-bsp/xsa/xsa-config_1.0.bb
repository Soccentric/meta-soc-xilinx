SUMMARY = "Xilinx XSA (Xilinx Support Archive) configuration"
DESCRIPTION = "Recipe to deploy and configure XSA file for Xilinx Zynq UltraScale+"
LICENSE = "CLOSED"

# XSA file can be provided via:
# 1. Local file: file://path/to/design.xsa
# 2. HTTP/HTTPS download: https://example.com/design.xsa
# 3. Git LFS: git://...;lfs=1

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = " \
    file://system.xsa \
"

S = "${WORKDIR}"

PROVIDES = "virtual/xsa"

XSA_FILE = "system.xsa"
XSA_DEPLOY_DIR = "${DEPLOY_DIR_IMAGE}/xsa"

inherit deploy

do_install() {
    install -d ${D}${datadir}/xsa
    install -m 0644 ${WORKDIR}/${XSA_FILE} ${D}${datadir}/xsa/
}

do_deploy() {
    install -d ${XSA_DEPLOY_DIR}
    install -m 0644 ${WORKDIR}/${XSA_FILE} ${XSA_DEPLOY_DIR}/
}

addtask deploy before do_build after do_install

FILES:${PN} += "${datadir}/xsa/*"

COMPATIBLE_MACHINE = "zynqmp-generic"

# Notes for usage:
# 1. Place your XSA file in recipes-bsp/xsa/files/system.xsa
# 2. Or modify SRC_URI to point to your XSA location
# 3. The XSA will be deployed to ${DEPLOY_DIR_IMAGE}/xsa/
