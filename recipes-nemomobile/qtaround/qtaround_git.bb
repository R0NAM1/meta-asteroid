SUMMARY = "C++ library providing miscelaneous handy wrappers around Qt classes/functionality. Initially it was created as the port of cutes-js to C++ to port (the-)vault to C++."
HOMEPAGE = "https://github.com/sailfishos/qtaround"
LICENSE = "LGPL-2.1+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9d1d95673c01a94e06648bf72ecda12d"

SRC_URI = "git://github.com/sailfishos/qtaround.git;protocol=https \
        file://0001-Disable-tests.patch"
SRCREV = "41126faba34ff7ccf96e5eca64fd5754d521648a"
PR = "r1"
PV = "+git${SRCPV}"
S = "${WORKDIR}/git"

DEPENDS += "qtbase cor tut-dev"
inherit cmake_qt5

EXTRA_OECMAKE=" -DVERSION=0.2.6 -DOE_QMAKE_PATH_EXTERNAL_HOST_BINS=${STAGING_DIR_NATIVE}/usr/bin/"

FILES:${PN}-dbg += "/opt"
