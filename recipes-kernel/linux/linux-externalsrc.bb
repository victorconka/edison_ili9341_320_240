LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

# Allows to avoid fetching, unpacking and patching, since our code is already cloned by repo
#inherit externalsrc

#SRC_URI = "git://github.com/01org/edison-linux.git;protocol=https;branch=edison-3.10.17"
SRC_URI = "git://github.com/primiano/edison-kernel.git;protocol=https;branch=master"
SRCREV = "master"

#SRC_URI += "file://0002-fbtft_ili9xxx.patch"
#SRC_URI += "file://0003-ili9341_init.patch"

SRC_URI += "file://0001-edison_fbtft.patch"
#SRC_URI += "file://0002-edison_boardc.patch"

#SRC_URI += "file://0001-Wimu-Mass-Storage-Commands.patch"
#SRC_URI += "file://0002-SPI-delay-fix.patch"
#SRC_URI += "file://0003-intel_mid.patch"
#SRC_URI += "file://0004-hsu-dma.patch"


# Don't use Yocto kernel configuration system, we instead simply override do_configure
# to copy our defconfig in the build directory just before building.
# I agree this is very ad hoc, but maybe it's good enough for our development environment
do_configure() {
  cp "${THISDIR}/files/defconfig" "${B}/.config"
}

EXTERNALSRC_pn-linux-externalsrc = "${S}"
EXTERNALSRC_BUILD_pn-linux-externalsrc = "${B}"

LINUX_VERSION ?= "3.10"
LINUX_VERSION_EXTENSION = "-edison-${LINUX_KERNEL_TYPE}"

S = "${EDISONREPO_TOP_DIR}/linux-kernel"
B = "${WORKDIR}/${BP}"

# This is required for kernel to do the build out-of-tree.
# If this is not set, most of the kernel make targets won't work properly
# as they'll be executed in the sources
export KBUILD_OUTPUT="${B}"

# The previous line should not be necessary when those 2 are added
# but it doesn't work..
KBUILD_OUTPUT = "${B}"
OE_TERMINAL_EXPORTS += "KBUILD_OUTPUT"

PR = "r2"

COMPATIBLE_MACHINE = "edison"

do_deploy() {
  kernel_do_deploy
  install ${B}/vmlinux ${DEPLOYDIR}/vmlinux
}

do_kernel_configme() {
  echo "skip this option"
}
