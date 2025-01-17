SUMMARY = "Plugin-based filesystem to access and change the state of the system "
HOMEPAGE = "https://github.com/sailfishos/statefs"
LICENSE = "LGPL-2.1+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f16c63ad20517dd81888a9ee32c4a0d4"

SRC_URI = "git://github.com/sailfishos/statefs.git;protocol=https \
           file://statefs.service"
SRCREV = "ef73da6fc0721eebedc25c5a7d09ee4b5dc3ba85"
PR = "r1"
PV = "+git${SRCPV}"
S = "${WORKDIR}/git"

DEPENDS += "boost fuse cor"
RDEPENDS:${PN} += "statefs-providers util-linux-getopt"
EXTRA_OECMAKE=" -DVERSION=0.3.0 -DENABLE_USER_SESSION=ON -DSYSTEMD_USER_UNIT_DIR=/usr/lib/systemd/user/"

inherit cmake

B = "${WORKDIR}/git"

do_configure:prepend() {
    sed -i "/examples/d" CMakeLists.txt
    cp ${WORKDIR}/statefs.service ${S}/statefs.service
}

do_install:append() {
    install -d ${D}/etc/sysconfig/statefs/
    echo "STATEFS_GID=1007" > ${D}/etc/sysconfig/statefs/system.conf
    echo "STATEFS_UMASK=0002" >> ${D}/etc/sysconfig/statefs/system.conf
    echo "STATEFS_GID=1007" > ${D}/etc/sysconfig/statefs/session.conf
    echo "STATEFS_UMASK=0002" >> ${D}/etc/sysconfig/statefs/session.conf

    install -d ${D}/var/lib/statefs/system

    install -d ${D}/lib/systemd/system/multi-user.target.wants/
    install -d ${D}/usr/lib/systemd/user/
    install -d ${D}/usr/lib/systemd/user/default.target.wants/
    mv ${D}/usr/lib/systemd/system/statefs-system.service ${D}/lib/systemd/system/statefs-system.service
    if [ ! -f ${D}/usr/lib/systemd/user/default.target.wants/statefs.service ]; then
        ln -s /usr/lib/systemd/user/statefs.service ${D}/usr/lib/systemd/user/default.target.wants/statefs.service
    fi
    if [ ! -f ${D}/lib/systemd/system/multi-user.target.wants/statefs-system.service ]; then
        ln -s /lib/systemd/system/statefs-system.service ${D}/lib/systemd/system/multi-user.target.wants/statefs-system.service
    fi
}

pkg_postinst:${PN}() {
    setcap CAP_SYS_ADMIN=ep $D/usr/bin/statefs
}

pkg_postinst_ontarget:${PN}() {
    /usr/lib/statefs/loader-action register /usr/lib/statefs/libloader-default.so
    /usr/lib/statefs/loader-action register /usr/lib/statefs/libloader-inout.so
    /usr/lib/statefs/loader-action register /usr/lib/statefs/libloader-qt5.so

    /usr/lib/statefs/provider-action register /usr/lib/statefs/libprovider-power_udev.so default --system
    /usr/lib/statefs/provider-action register /usr/lib/statefs/libprovider-bluez.so qt5 --system
    /usr/lib/statefs/provider-action register /usr/lib/statefs/libprovider-connman.so qt5 --system
    /usr/lib/statefs/provider-action register /usr/lib/statefs/libprovider-mce.so qt5 --system
    /usr/lib/statefs/provider-action register /usr/lib/statefs/libprovider-profile.so qt5
    /usr/lib/statefs/provider-action register /etc/timed-statefs.conf inout
}

PACKAGE_WRITE_DEPS = "libcap-native"
FILES:${PN} += "/lib/systemd/ /usr/lib/systemd /var/lib/statefs/ /etc/sysconfig/statefs/ /usr/lib/systemd/user/default.target.wants/"
FILES:${PN}-dbg += "/opt/"
