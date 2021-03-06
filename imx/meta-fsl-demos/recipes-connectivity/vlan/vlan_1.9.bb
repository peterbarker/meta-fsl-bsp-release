DESCRIPTION = "VLAN provides vconfig utility"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://vconfig.c;md5=293ef69c4e88434d37a5ee577a5288cb"

SRC_URI = "http://vlan.sourcearchive.com/downloads/1.9-3ubuntu9/vlan_1.9.orig.tar.gz"

S = "${WORKDIR}/vlan"

SRC_URI[md5sum] = "5f0c6060b33956fb16e11a15467dd394"
SRC_URI[sha256sum] = "3b8f0a1bf0d3642764e5f646e1f3bbc8b1eeec474a77392d9aeb4868842b4cca"

do_install () {
     install -d ${D}/${exec_prefix}/bin
     cp ${S}/vconfig ${D}/${exec_prefix}/bin
}

# ignore strip to avoid yocto errors in stripping
do_compile () {
    oe_runmake PLATFORM=ARM 'STRIP=echo' all
}

# comment out MakeInclude in Makefile which sets build environment
do_configure_append () {
    sed -i 's/^ include/#^include/' ${S}/Makefile
}
