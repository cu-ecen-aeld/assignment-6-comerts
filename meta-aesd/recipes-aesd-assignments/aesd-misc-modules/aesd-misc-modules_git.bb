# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://git@github.com/cu-ecen-aeld/assignment-7-comerts.git;protocol=ssh;branch=main"

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "ebe0cd59bb766dcf6f13b9f8a37e6b57381d82e7"

S = "${WORKDIR}/git"

inherit module

TARGET_LDFLAGS += "-pthread -lrt"

MODULES_INSTALL_TARGET = "install"
EXTRA_OEMAKE += " -C ${STAGING_KERNEL_DIR} M=${S}/misc-modules"
EXTRA_OEMAKE += "SUBDIRS=misc-modules"
EXTRA_OEMAKE += "LDDINC=${S}/include"

FILES:${PN} += "${sysconfdir}/rcS.d/S98miscmodules"
FILES:${PN} += "${libdir}/modules/${KERNEL_VERSION}/kernel/drivers/misc-modules/faulty.ko"
FILES:${PN} += "${libdir}/modules/${KERNEL_VERSION}/kernel/drivers/misc-modules/hello.ko"

do_configure() {
    :
}

do_compile() {
    oe_runmake
}

do_install() {
    oe_runmake ARCH=arm CROSS_COMPILE=${TARGET_PREFIX} INSTALL_MOD_PATH=${D} modules_install

    install -d ${D}${libdir}/modules/${KERNEL_VERSION}/kernel/drivers/misc-modules
    install -m 0644 ${S}/misc-modules/faulty.ko ${D}${libdir}/modules/${KERNEL_VERSION}/kernel/drivers/misc-modules/
    install -m 0644 ${S}/misc-modules/hello.ko ${D}${libdir}/modules/${KERNEL_VERSION}/kernel/drivers/misc-modules/

    install -d ${D}${sysconfdir}/rcS.d
    install -m 0755 ${THISDIR}/files/misc-modules_start_stop ${D}${sysconfdir}/rcS.d/S98miscmodules
}
