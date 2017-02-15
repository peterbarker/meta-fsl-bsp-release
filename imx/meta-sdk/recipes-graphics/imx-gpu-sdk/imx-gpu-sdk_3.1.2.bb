SUMMARY = "Freescale GPU SDK Samples"
DESCRIPTION = "Set of sample applications for Freescale GPU"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=be67a88e9e6c841043b005ad7bcf8309"
DEPENDS = "${X11_DEPENDS} ${WL_DEPENDS} devil assimp gstreamer1.0 gstreamer1.0-plugins-base"

IMX_DEPENDS_APPEND          = ""
IMX_DEPENDS_APPEND_imxgpu2d = "virtual/libg2d virtual/libopenvg"
IMX_DEPENDS_APPEND_imxgpu3d = "virtual/libg2d virtual/libgles2"
DEPENDS_append = " ${IMX_DEPENDS_APPEND}"

X11_DEPENDS = "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'xrandr', '', d)}"
WL_DEPENDS = "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', '', d)}"

inherit fsl-eula-unpack

# For backwards compatibility
RPROVIDES_${PN} = "vivante-gpu-sdk"
RREPLACES_${PN} = "vivante-gpu-sdk"
RCONFLICTS_${PN} = "vivante-gpu-sdk"

SRC_URI = "${FSL_MIRROR}/${PN}-${PV}.bin;fsl-eula=true"

SRC_URI[md5sum] = "9815da3e439a61ef35c9575fbb16aa86"
SRC_URI[sha256sum] = "2a9f10f129c4c4c38fb06eadb69b354e3aab9a6d4c1bf32f51221996d92ebe39"


BACKEND = "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'Wayland', \
                bb.utils.contains('DISTRO_FEATURES', 'x11', 'X11', 'FB', d), d)}"

BUILD_USE_FEATURES        = "[EGL,OpenVG,G2D,OpenGLES2,OpenGLES3]"
BUILD_USE_FEATURES_mx6sl  = "[EGL,OpenVG,G2D]"
BUILD_USE_FEATURES_mx6sx  = "[EGL,OpenVG,G2D,OpenGLES2]"
BUILD_USE_FEATURES_mx7ulp = "[EGL,OpenVG,G2D,OpenGLES2]"
BUILD_USE_FEATURES_mx8    = "[EGL,OpenVG,G2D,OpenGLES2,OpenGLES3,OpenGLES3.1,OpenCL,OpenCL1.1,OpenCL1.2,OpenVX,OpenVX1.0.1,Vulkan]"

do_compile () {
    export FSL_GRAPHICS_SDK=${S}
    export FSL_PLATFORM_NAME=Yocto
    export ROOTFS=${STAGING_DIR_HOST}
    cd ${S}/.Config
    ./FslBuild.py -t sdk -u ${BUILD_USE_FEATURES} -- -j 2 EGLBackend=${BACKEND} ROOTFS=${STAGING_DIR_HOST} install
}

do_install () {
    install -d "${D}/opt/${PN}"
    cp -r ${S}/bin/* ${D}/opt/${PN}
    rm -rf ${D}/opt/${PN}/GLES2/DirectMultiSamplingVideoYUV
    rm -rf ${D}/opt/${PN}/GLES3/DirectMultiSamplingVideoYUV
    rm -rf ${D}/opt/${PN}/GLES2/DeBayer
    if [ "${IS_MX8}" = "1" ]; then
        rm -rf ${D}/opt/${PN}/G2D/EightLayers
    fi
}

FILES_${PN} += "/opt/${PN}"
FILES_${PN}-dbg += "/opt/${PN}/*/*/.debug /usr/src/debug"
INSANE_SKIP_${PN} += "already-stripped rpaths"

COMPATIBLE_MACHINE = "(mx6|mx8|mx7ulp)"