# Copyright (C) 2016 Freescale Semiconductor
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "GPU G2D library and apps for imx6"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://COPYING;md5=d4f548f93b5fe0ee2bc86758c344412d"

PROVIDES += "virtual/libg2d"
RPROVIDES_${PN} += "virtual/libg2d"

SRC_URI = "${MX8_DOWNLOADS}/${PN}-${PV}.bin;fsl-eula=true"

S="${WORKDIR}/${PN}-${PV}"

inherit fsl-eula-unpack

SRC_URI[md5sum] = "a97b1ce82aa5f049672aaaa294c23145"
SRC_URI[sha256sum] = "6046319e9e8f5d0fc38fc0583e11907a5d85069b8cdae3acc9fc9cd05ff7829c"

do_install () {

    install -d ${D}${libdir}
    install -d ${D}${includedir}

    cp ${S}/g2d/usr/lib/*.so* ${D}${libdir}
    cp -Pr ${S}/g2d/usr/include/* ${D}${includedir}
    cp -r ${S}/gpu-demos/opt ${D}
}

FILES_${PN} = "${libdir}/libg2d* /opt"
FILES_${PN}-dev = "${includedir}"

COMPATIBLE_MACHINE = "(mx6|mx7ulp)"