# Copyright (c) 2016-2018 LG Electronics, Inc.

SUMMARY = "Chromium v53 browser for AGL"
AUTHOR = "Jose Dapena Paz <jose.dapena@lge.com>"
SECTION = "apps"
LICENSE = "Apache-2.0 & BSD-3-Clause & LGPL-2.0 & LGPL-2.1"
LIC_FILES_CHKSUM = "\
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://src/LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d \
    file://src/third_party/WebKit/Source/core/LICENSE-LGPL-2;md5=36357ffde2b64ae177b2494445b79d21 \
    file://src/third_party/WebKit/Source/core/LICENSE-LGPL-2.1;md5=a778a33ef338abbaf8b8a7c36b6eec80 \
"

require chromium53.inc

BROWSER_APPLICATION ?= "chromium53-browser"

GYP_DEFINES += "use_chromium_cbe=1 use_dynamic_injection_loading=0"

# For WAM we need glib mainloop
GYP_DEFINES += "use_glib=1"

# Disable components not needed for AGL
GYP_DEFINES += "use_cups=0 use_gconf=0 use_gio=0"

# Disable special allocators
GYP_DEFINES += "use_allocator=none"

# Do not use system cross toolchain
GYP_DEFINES += "posix_use_system_cross_toolchain=0"

# This variable should be removed.
GYP_DEFINES_append = " platform_apollo=1"

# Avoid failing on link stage
GYP_DEFINES += "disable_fatal_linker_warnings=1"

BROWSER_APPLICATION_DIR = "/opt/chromium53"
CHROMIUM_PLUGINS_PATH = "${libdir}"
CBE_DATA_PATH = "${libdir}/cbe"
CBE_DATA_LOCALES_PATH = "${CBE_DATA_PATH}/locales"
GYP_DEFINES += "cbe_data=${CBE_DATA_PATH}"

do_install_append() {
    install -d ${D}${libdir}
    install -d ${D}${includedir}/${BPN}
    cd ${SRC_DIR}
    xargs --arg-file=${SRC_DIR}/build/cbe_staging_inc.list cp --parents --target-directory=${D}${includedir}/${BPN}
    ln -snf ${BPN} ${D}${includedir}/chromium
    cd ${OUT_DIR}/${BUILD_TYPE}
    xargs --arg-file=${SRC_DIR}/build/cbe_staging_res.list cp --parents --target-directory=${D}${includedir}/${BPN}
    cat ${SRC_DIR}/build/cbe_staging_lib.list | xargs -I{} install -m 755 -p {} ${D}${libdir}
    mkdir -p ${D}${CBE_DATA_PATH}
    cat ${SRC_DIR}/webos/install/cbe/cbe_data.list | xargs -I{} install -m 755 -p {} ${D}${CBE_DATA_PATH}
    mkdir -p ${D}${CBE_DATA_LOCALES_PATH}
    cat ${SRC_DIR}/webos/install/cbe/cbe_data_locales.list | xargs -I{} install -m 755 -p {} ${D}${CBE_DATA_LOCALES_PATH}

    # move this to separate mksnapshot-cross recipe once we figure out how to build just cross mksnapshot from chromium repository
    install -d ${D}${bindir_cross}
    gzip -c ${OUT_DIR}/${BUILD_TYPE}/mksnapshot > ${D}${bindir_cross}/${HOST_SYS}-mksnapshot.gz
}

pkg_postinst_${PN}_append() {
    chsmack -a "*" /usr/lib/libcbe.so
    chsmack -a "*" /usr/lib/cbe/icudtl.dat
    chsmack -a "*" /usr/lib/cbe/natives_blob.bin
    chsmack -a "*" /usr/lib/cbe/snapshot_blob.bin
    chsmack -a "*" /usr/lib/cbe/locales/en-US.pak
}

pkg_postinst_${BROWSER_APPLICATION}_append() {
    chsmack -a "*" /opt/chromium53/chrome
    chsmack -a "*" /opt/chromium53/run_webbrowser
    chsmack -a "*" /opt/chromium53/chrome_100_percent.pak
    chsmack -a "*" /opt/chromium53/chrome_200_percent.pak
    chsmack -a "*" /opt/chromium53/resources.pak
    chsmack -a "*" /opt/chromium53/locales/en-US.pak
}

SYSROOT_DIRS_append = " ${bindir_cross}"

PROVIDES = "${BROWSER_APPLICATION}"

PACKAGES += " \
    ${PN}-cross-mksnapshot \
    ${BROWSER_APPLICATION} \
"

FILES_${BROWSER_APPLICATION} += " \
    ${datadir} \
    ${BROWSER_APPLICATION_DIR} \
    ${AFM_INSTALL_DIR} \
    ${SYSTEMD_USER_DIR} \
    ${SMACK_ACCESSES_DIR} \
"

RDEPENDS_${BROWSER_APPLICATION} += "${PN}"

VIRTUAL-RUNTIME_gpu-libs ?= ""
RDEPENDS_${PN} += "${VIRTUAL-RUNTIME_gpu-libs}"

# Drop all security flags, at least for now, to reduce
# toolchain conflicts in Ubuntu 14.04.
SECURITY_CFLAGS = ""
SECURITY_LDFLAGS = ""

INSANE_SKIP_${BROWSER_APPLICATION} += "libdir"

FILES_${PN} = " \
    ${libdir}/*.so \
    ${CBE_DATA_PATH}/* \
    ${libdir}/${BPN}/*.so \
"

FILES_${PN}-dev = " \
    ${includedir} \
"

FILES_${PN}-cross-mksnapshot = "${bindir_cross}/${HOST_SYS}-mksnapshot.gz"

