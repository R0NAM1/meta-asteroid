SUMMARY = "A light version of meegotouch, pulling in useful parts without requiring the full heavy library."
HOMEPAGE = "https://github.com/sailfishos/mlite"
LICENSE = "LGPL-2.1+"
LIC_FILES_CHKSUM = "file://src/mnotification.cpp;beginline=1;endline=18;md5=5dd5d73b0225b3099a82e6ad93283ea1"

SRC_URI = "git://github.com/sailfishos/mlite.git;protocol=https \
           file://0001-MDesktopEntry-Allow-dynamic-locale-switch-by-disabli.patch \
           file://0002-mlite-Disable-tests.patch "
SRCREV = "37ded5a0ca8f0770e3347f3711c7c4603d2a5b25"
PR = "r1"
PV = "+git${SRCPV}"
S = "${WORKDIR}/git"

DEPENDS:append = "qtbase glib-2.0 dconf"
inherit qmake5
B = "${WORKDIR}/git" 
# Out of dir build breaks mlite5.pc installation

do_configure:prepend() {
    sed -i "s@\$\$\[QT_INSTALL_BINS\]@${OE_QMAKE_PATH_EXTERNAL_HOST_BINS}@" src/src.pro
}

do_install:append() {
    cd src/
    cp *.h MDConfGroup MNotificationGroup MRemoteAction MExport MDesktopEntry MNotification MGConfItem  ${D}/usr/include/mlite5/
}

FILES:${PN}-dbg += "/opt"
