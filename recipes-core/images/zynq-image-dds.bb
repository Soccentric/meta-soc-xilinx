SUMMARY = "DDS image for Xilinx Zynq UltraScale+"
DESCRIPTION = "Specialized image with DDS support"

require zynq-image-base.bb

# DDS Implementation Configuration - set in local.conf
# DDS_IMPLEMENTATION = "fastdds" (or "cyclonedds", "connextdds")
DDS_IMPLEMENTATION ?= "fastdds"

# Fast-DDS (eProsima)
IMAGE_INSTALL:append = " \
    ${@bb.utils.contains('DDS_IMPLEMENTATION', 'fastdds', 'fastdds fastdds-gen python3-fastdds', '', d)} \
"

# CycloneDDS (Eclipse)
IMAGE_INSTALL:append = " \
    ${@bb.utils.contains('DDS_IMPLEMENTATION', 'cyclonedds', 'cyclonedds cyclonedds-cxx python3-cyclonedds', '', d)} \
"

# RTI Connext DDS (requires license)
IMAGE_INSTALL:append = " \
    ${@bb.utils.contains('DDS_IMPLEMENTATION', 'connextdds', 'connextdds', '', d)} \
"

# Common DDS utilities
IMAGE_INSTALL:append = " \
    python3-xml \
"

# Note: May require additional layer dependencies
# For Fast-DDS: meta-fastdds (if available)
# For CycloneDDS: meta-ros or custom layer
