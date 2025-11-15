SUMMARY = "GRPC image for Xilinx Zynq UltraScale+"
DESCRIPTION = "Specialized image with GRPC support"

require zynq-image-base.bb

# gRPC with C++ and Python support

IMAGE_INSTALL:append = " \
    grpc \
    grpc-dev \
    protobuf \
    protobuf-dev \
    protobuf-c \
"

# C++ support
IMAGE_INSTALL:append = " \
    grpc-cpp \
    abseil-cpp \
"

# Python support
IMAGE_INSTALL:append = " \
    python3-grpcio \
    python3-grpcio-tools \
    python3-protobuf \
"

# Development tools
IMAGE_INSTALL:append = " \
    cmake \
    pkgconfig \
"
