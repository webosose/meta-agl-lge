SUMMARY = "WAM"
AUTHOR = "Jani Hautakangas <jani.hautakangas@lge.com>"
LICENSE = "CLOSED"

inherit qmake5

DEPENDS = "qtbase glib-2.0 chromium53"

PR="r0"

SRC_URI = "git://gpro.lgsvl.com/webos-pro/${PN}.git;branch=@webosose1.agl;protocol=ssh"
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
    install -d ${D}${systemd_system_unitdir}
    install -v -m 644 ${S}/files/launch/WebAppMgr.service ${D}${systemd_system_unitdir}/WebAppMgr.service
    install -d ${D}${sysconfdir}/default/
    install -v -m 644 ${S}/files/launch/WebAppMgr.env ${D}${sysconfdir}/default/WebAppMgr.env
}

pkg_postinst_${PN}_append() {
    chsmack -a "*" /usr/bin/WebAppMgr
    chsmack -a "*" /usr/lib/libWebAppMgr.so.1.0.0
    chsmack -a "*" /usr/lib/libWebAppMgrCore.so.1.0.0
    chsmack -a "*" /usr/lib/webappmanager/plugins/libwebappmgr-default-plugin.so
}

FILES_${PN} += "${sysconfdir}/init ${sysconfdir}/wam ${libdir}/webappmanager/plugins/*.so ${systemd_system_unitdir}"

