SUMMARY = "WAM"
AUTHOR = "Jani Hautakangas <jani.hautakangas@lge.com>"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit qmake5

DEPENDS = "qtbase glib-2.0 jsoncpp boost chromium53 wayland-ivi-extension libhomescreen libwindowmanager"


PR="r0"

PROVIDES += "virtual/webruntime"
RPROVIDES_${PN} += "virtual/webruntime"

SRC_URI = "git://github.com/webosose/${PN}.git;branch=@1.agl.flounder;protocol=https"
S = "${WORKDIR}/git"
SRCREV = "${AUTOREV}"

EXTRA_QMAKEVARS_PRE += "CONFIG_BUILD+=agl_service"
EXTRA_QMAKEVARS_PRE += "PREFIX=/usr"
EXTRA_QMAKEVARS_PRE += "PLATFORM=${@'PLATFORM_' + '${DISTRO}'.upper().replace('-', '_')}"
EXTRA_QMAKEVARS_PRE += "CHROMIUM_SRC_DIR=${STAGING_INCDIR}/chromium53"
OE_QMAKE_CXXFLAGS += "-Wno-unused-variable"

do_install_append() {
    install -d ${D}${sysconfdir}/wam
    install -v -m 644 ${S}/files/launch/security_policy.conf ${D}${sysconfdir}/wam/security_policy.conf
    install -d ${D}${systemd_user_unitdir}
    install -v -m 644 ${S}/files/launch/WebAppMgr.service ${D}${systemd_user_unitdir}/WebAppMgr.service
    install -d ${D}${sysconfdir}/default/
    install -v -m 644 ${S}/files/launch/WebAppMgr.env ${D}${sysconfdir}/default/WebAppMgr.env
    ln -snf WebAppMgr ${D}${bindir}/web-runtime
    install -d ${D}${sysconfdir}/systemd/user/default.target.wants
    ln -sf ${systemd_user_unitdir}/WebAppMgr.service ${D}${sysconfdir}/systemd/user/default.target.wants
}

pkg_postinst_${PN}_append() {
    chsmack -a "*" /usr/bin/WebAppMgr
    chsmack -a "*" /usr/lib/libWebAppMgr.so.1.0.0
    chsmack -a "*" /usr/lib/libWebAppMgrCore.so.1.0.0
    chsmack -a "*" /usr/lib/webappmanager/plugins/libwebappmgr-default-plugin.so
}

RDEPENDS_${PN} += "boost-filesystem wam-tinyproxy"
FILES_${PN} += "${sysconfdir}/init ${sysconfdir}/wam ${libdir}/webappmanager/plugins/*.so ${systemd_user_unitdir}"

