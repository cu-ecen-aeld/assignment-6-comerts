# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE
LICENSE = "Unknown"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f098732a73b5f6f3430472f5b094ffdb"

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
FILES:${PN} += "${libdir}/modules/${KERNEL_VERSION}/kernel/drivers/misc-modules/faulty"
FILES:${PN} += "${libdir}/modules/${KERNEL_VERSION}/kernel/drivers/misc-modules/hello"

do_configure() {
    :
}

do_compile() {
    oe_runmake
}

do_install() {
    oe_runmake ARCH=arm CROSS_COMPILE=${TARGET_PREFIX} INSTALL_MOD_PATH=${D} modules_install

    install -d ${D}${libdir}/modules/${KERNEL_VERSION}/kernel/drivers/misc-modules
    install -m 0644 ${S}/faulty ${D}${libdir}/modules/${KERNEL_VERSION}/kernel/drivers/misc-modules/
    install -m 0644 ${S}/hello ${D}${libdir}/modules/${KERNEL_VERSION}/kernel/drivers/misc-modules/

    install -d ${D}${sysconfdir}/rcS.d
    install -m 0755 ${THISDIR}/files/misc-modules_start_stop ${D}${sysconfdir}/rcS.d/S98miscmodules
}
