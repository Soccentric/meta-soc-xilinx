# meta-soc-xilinx

Yocto meta-layer for Xilinx Zynq UltraScale+ - Yocto scarthgap

## Overview

This layer provides custom images, kernel, bootloader, and device tree support for Xilinx Zynq UltraScale+ platforms.

**Generated on:** 2025-11-15

## Layer Dependencies

This layer depends on:
- `openembedded-core` (meta)
- `meta-xilinx`
- `meta-openembedded/meta-oe`
- `meta-openembedded/meta-python`

## Quick Start

### 1. Setup Build Environment

```bash
# Clone Yocto Poky
git clone -b scarthgap git://git.yoctoproject.org/poky.git
cd poky

# Clone dependencies
git clone -b scarthgap git://git.openembedded.org/meta-openembedded
git clone -b scarthgap <BSP_LAYER_URL>

# Clone this layer
git clone <YOUR_REPO_URL> meta-soc-xilinx

# Initialize build environment
source oe-init-build-env build-zynq
```

### 2. Configure Build

Add layers to `conf/bblayers.conf`:

```
BBLAYERS ?= " \
  /path/to/poky/meta \
  /path/to/poky/meta-poky \
  /path/to/poky/meta-yocto-bsp \
  /path/to/meta-openembedded/meta-oe \
  /path/to/meta-openembedded/meta-python \
  /path/to/meta-xilinx \
  /path/to/meta-soc-xilinx \
"
```

Configure `conf/local.conf`:

```
MACHINE = "zynqmp-generic"
DISTRO = "poky"

# Accept licenses
LICENSE_FLAGS_ACCEPTED = "commercial"

# Enable parallel builds
BB_NUMBER_THREADS = "8"
PARALLEL_MAKE = "-j 8"
```

### 3. Build Images

```bash
# Build base image
bitbake zynq-image-base

# Build specialized images
bitbake zynq-image-ros2
bitbake zynq-image-iot
bitbake zynq-image-grpc
bitbake zynq-image-vision
bitbake zynq-image-dds
bitbake zynq-image-rust
```

## Layer Structure

```
meta-soc-xilinx/
├── conf/
│   └── layer.conf                    # Layer configuration
├── recipes-kernel/linux/             # Custom kernel recipes
├── recipes-bsp/
│   ├── u-boot/                       # Custom U-Boot recipes
│   ├── device-tree/                  # Device tree recipes
│   ├── xsa/                          # XSA configuration (Xilinx)
│   └── rpu-fw/                       # RPU firmware recipes
├── recipes-core/images/              # Image recipes
├── recipes-ros2/                     # ROS2 packages
├── recipes-iot/                      # IoT packages
├── recipes-grpc/                     # gRPC packages
├── recipes-vision/                   # Vision system packages
├── recipes-dds/                      # DDS packages
├── recipes-rust/                     # Rust packages
└── README.md                         # This file
```

## Customization Guide

### Kernel Configuration

The layer uses an out-of-tree kernel hosted on GitHub.

**Update kernel source:**
Edit `recipes-kernel/linux/linux-custom_6.10.bb`:

```
SRC_URI = "git://github.com/YOUR-ORG/linux-zynq.git;protocol=https;branch=main"
SRCREV = "YOUR_COMMIT_HASH"  # or use ${AUTOREV} for latest
```

**Custom kernel config:**
```bash
bitbake linux-custom -c menuconfig
bitbake linux-custom -c savedefconfig
# Copy .config to your kernel repo
```

### U-Boot Configuration

**Update U-Boot source:**
Edit `recipes-bsp/u-boot/u-boot-custom_2024.07.bb`:

```
SRC_URI = "git://github.com/YOUR-ORG/u-boot-zynq.git;protocol=https;branch=main"
SRCREV = "YOUR_COMMIT_HASH"
```

### Device Tree Management

The layer supports both device tree override and overlays.

**Add custom device trees:**
1. Place `.dts` files in `recipes-bsp/device-tree/files/`
2. Update `DEVICE_TREE_SOURCES` in the recipe
3. For overlays, place in `files/overlays/` directory

**Example structure:**
```
recipes-bsp/device-tree/files/
├── custom-base.dts
└── overlays/
    ├── spi-overlay.dts
    ├── i2c-overlay.dts
    └── uart-overlay.dts
```

### XSA Configuration (Xilinx Specific)

The Xilinx Support Archive (XSA) contains hardware design information.

**Add your XSA file:**
1. Place `system.xsa` in `recipes-bsp/xsa/files/`
2. Or update SRC_URI to download from remote location

**Alternative sources:**
```
# HTTP download
SRC_URI = "https://example.com/path/to/design.xsa"

# Git LFS
SRC_URI = "git://github.com/YOUR-ORG/hw-designs.git;lfs=1;branch=main"
```

The XSA file will be deployed to `${DEPLOY_DIR_IMAGE}/xsa/`

### RPU/R5 Firmware

This layer supports both pre-built and source-compiled RPU firmware.

**Pre-built firmware (default):**
1. Place `firmware.elf` in `recipes-bsp/rpu-fw/files/`
2. Build: `bitbake rpu-firmware-zynq`

**Source-compiled firmware:**
1. Set in `local.conf`:
   ```
   RPU_BUILD_MODE = "source"
   ```
2. Update recipe with your firmware repository URL
3. Implement build commands in `do_compile()`

**Deployment:**
- Target: `/lib/firmware/rpu-firmware.elf`
- Deploy: `${DEPLOY_DIR_IMAGE}/rpu/`

## Specialized Images

### Base Image: zynq-image-base

Full command-line system with development tools, based on `core-image-full-cmdline`.

**Includes:**
- Custom kernel and U-Boot
- Device tree support
- Development tools (gcc, gdb, python3)
- Network utilities
- SSH server

### ROS2 Image: zynq-image-ros2

**Configuration in local.conf:**
```
ROS2_DISTRO = "humble"  # Options: humble, iron, jazzy
```

**Includes:**
- ROS2 base packages
- colcon build system
- rosdep, vcstool
- Network time synchronization

**Additional layer required:** `meta-ros`

### IoT Image: zynq-image-iot

**Configuration in local.conf:**
```
IOT_PROTOCOLS = "mqtt aws-iot azure-iot coap"
```

**Supported protocols:**
- **MQTT**: Mosquitto broker/client, Paho MQTT
- **AWS IoT**: AWS IoT Device SDK
- **Azure IoT**: Azure IoT SDK
- **CoAP**: libcoap, aiocoap

### gRPC Image: zynq-image-grpc

**Includes:**
- gRPC C++ libraries
- gRPC Python libraries
- Protocol Buffers (protobuf)
- Development tools

**Use cases:**
- Microservices communication
- High-performance RPC
- Cross-language APIs

### Vision System Image: zynq-image-vision

**Configuration in local.conf:**
```
VISION_FRAMEWORKS = "opencv gstreamer tensorflow pytorch"
```

**Supported frameworks:**
- **OpenCV**: Computer vision library
- **GStreamer**: Multimedia framework
- **TensorFlow Lite**: ML inference
- **PyTorch**: Deep learning (if available)

**Includes:**
- V4L2 utilities
- FFmpeg
- Camera driver support
- NumPy, Pillow, SciPy

### DDS Image: zynq-image-dds

**Configuration in local.conf:**
```
DDS_IMPLEMENTATION = "fastdds"  # Options: fastdds, cyclonedds, connextdds
```

**Supported implementations:**
- **Fast-DDS** (eProsima): Default, high performance
- **CycloneDDS** (Eclipse): Lightweight
- **Connext DDS** (RTI): Enterprise (requires license)

**Use cases:**
- Real-time data distribution
- IIoT applications
- ROS2 backend

### Rust Image: zynq-image-rust

**Configuration in local.conf:**
```
RUST_CRATES = "tokio serde actix-web"
```

**Includes:**
- Rust compiler and cargo
- rustfmt, clippy
- LLVM toolchain

**Note:** Individual Rust crate recipes need to be created or use `cargo-bitbake` tool.

## Configuration Examples

### Example local.conf

```bash
# Machine configuration
MACHINE = "zynqmp-generic"

# Distribution
DISTRO = "poky"

# Build optimization
BB_NUMBER_THREADS = "${@oe.utils.cpu_count()}"
PARALLEL_MAKE = "-j ${@oe.utils.cpu_count()}"

# Disk space monitoring
BB_DISKMON_DIRS = "\
    STOPTASKS,$/{TMPDIR},1G,100K \
    STOPTASKS,$/{DL_DIR},1G,100K \
    STOPTASKS,$/{SSTATE_DIR},1G,100K \
    STOPTASKS,/tmp,100M,100K \
    HALT,$/{TMPDIR},100M,1K \
    HALT,$/{DL_DIR},100M,1K \
    HALT,$/{SSTATE_DIR},100M,1K \
    HALT,/tmp,10M,1K"

# Package management
PACKAGE_CLASSES = "package_rpm"
EXTRA_IMAGE_FEATURES = "debug-tweaks package-management"

# Specialized image configurations
ROS2_DISTRO = "humble"
IOT_PROTOCOLS = "mqtt"
VISION_FRAMEWORKS = "opencv gstreamer"
DDS_IMPLEMENTATION = "fastdds"
RUST_CRATES = "tokio serde"

# RPU firmware mode
RPU_BUILD_MODE = "prebuilt"

# Network configuration for downloads
PREMIRRORS:prepend = "\
git://.*/.*   https://mirrors.kernel.org/sources.cff/ \
ftp://.*/.*   https://mirrors.kernel.org/sources.cff/ \
http://.*/.*  https://mirrors.kernel.org/sources.cff/ \
https://.*/.* https://mirrors.kernel.org/sources.cff/ \
"

# Accept licenses
LICENSE_FLAGS_ACCEPTED = "commercial"
```

## Troubleshooting

### Common Issues

**1. Kernel build fails**
```bash
# Check kernel defconfig exists
bitbake linux-custom -c listtasks
# Try with verbose output
bitbake linux-custom -v
```

**2. U-Boot configuration errors**
```bash
# Verify U-Boot defconfig
bitbake u-boot-custom -c menuconfig
```

**3. Device tree compilation fails**
```bash
# Check DTS syntax
dtc -I dts -O dtb your-file.dts
```

**4. Missing dependencies**
```bash
# Verify all layers are added
bitbake-layers show-layers
# Add missing layers to bblayers.conf
```

## Development Workflow

### Iterative Development

```bash
# Clean specific recipe
bitbake <recipe-name> -c cleansstate

# Force rebuild
bitbake <recipe-name> -f

# Build with specific task
bitbake <recipe-name> -c compile
bitbake <recipe-name> -c install

# Debug recipe
bitbake <recipe-name> -e | grep ^SRC_URI=
bitbake <recipe-name> -e | grep ^WORKDIR=
```

### SDK Generation

```bash
# Generate SDK for application development
bitbake zynq-image-base -c populate_sdk

# Install SDK
./tmp/deploy/sdk/<sdk-installer>.sh
```

## Testing

### QEMU Testing (if supported)

```bash
runqemu zynqmp-generic nographic
```

### Hardware Testing

1. Flash image to SD card/eMMC:
   ```bash
   sudo dd if=tmp/deploy/images/zynqmp-generic/<image>.wic \
           of=/dev/sdX bs=4M status=progress
   sync
   ```

2. Boot and verify:
   ```bash
   # Check kernel version
   uname -a
   
   # Verify device tree
   ls /sys/firmware/devicetree/base/
   
   # Check firmware
   ls /lib/firmware/
   ```

## Contributing

### Adding New Recipes

1. Create recipe directory: `recipes-<category>/<package-name>/`
2. Add recipe: `<package-name>_<version>.bb`
3. Test build: `bitbake <package-name>`
4. Add to image: Update `IMAGE_INSTALL:append` in image recipe

### Recipe Template

```bash
SUMMARY = "Brief description"
DESCRIPTION = "Detailed description"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=..."

SRC_URI = "git://...;protocol=https;branch=main"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit <classes>

do_compile() {
    # Build commands
}

do_install() {
    # Install commands
}

FILES:${PN} += "/path/to/files"
```

## Resources

- **Yocto Project**: https://www.yoctoproject.org/
- **Xilinx Zynq UltraScale+ BSP**: [Add BSP documentation URL]
- **Layer Index**: https://layers.openembedded.org/

## License

This layer is provided under the MIT license. Individual recipes may have different licenses.

## Support

For issues and questions:
- GitHub Issues: [Add your repository URL]
- Mailing List: [Add mailing list]
- Documentation: [Add documentation URL]

---

**Last Updated:** 2025-11-15
**Yocto Version:** scarthgap
**Maintainer:** [Your Name/Organization]
