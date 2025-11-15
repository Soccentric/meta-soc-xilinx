# RPU Firmware Directory

This recipe supports two modes for R5 firmware:

## Pre-built Mode (default)
1. Place your pre-built firmware ELF file here as `firmware.elf`
2. The recipe will package and deploy it

## Source Mode
1. Set `RPU_BUILD_MODE = "source"` in your local.conf or recipe
2. Update the git repository URL in the recipe
3. Implement the build commands in `do_compile()`

The firmware will be deployed to:
- `/lib/firmware/` on the target
- `${DEPLOY_DIR_IMAGE}/rpu/` for deployment
