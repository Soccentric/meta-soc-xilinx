SUMMARY = "RUST image for Xilinx Zynq UltraScale+"
DESCRIPTION = "Specialized image with RUST support"

require zynq-image-base.bb

# Rust Programming Language Support

# Rust toolchain
IMAGE_INSTALL:append = " \
    rust \
    rust-dev \
    cargo \
    cargo-bin \
"

# Configurable Rust crates - set in local.conf
# RUST_CRATES = "tokio serde actix-web"
RUST_CRATES ?= ""

# Popular Rust crates (examples - add recipes as needed)
IMAGE_INSTALL:append = " \
    ${@bb.utils.contains('RUST_CRATES', 'tokio', 'rust-tokio', '', d)} \
    ${@bb.utils.contains('RUST_CRATES', 'serde', 'rust-serde', '', d)} \
    ${@bb.utils.contains('RUST_CRATES', 'actix-web', 'rust-actix-web', '', d)} \
"

# Development tools
IMAGE_INSTALL:append = " \
    rust-tools-clippy \
    rust-tools-rustfmt \
    llvm \
"

# Note: Individual Rust crate recipes need to be created
# in recipes-rust/ directory or use cargo-bitbake tool
