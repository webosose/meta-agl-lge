# Copyright (c) 2018 LG Electronics, Inc.

SUMMARY = "Chromium webruntime for webOS"
AUTHOR = "Lokesh Kumar Goel <lokeshkumar.goel@lge.com>"
SECTION = "webos/apps"
LICENSE = "Apache-2.0 & BSD-3-Clause & LGPL-2.0 & LGPL-2.1"
LIC_FILES_CHKSUM = "\
    file://src/LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d \
    file://src/third_party/blink/renderer/core/LICENSE-LGPL-2;md5=36357ffde2b64ae177b2494445b79d21 \
    file://src/third_party/blink/renderer/core/LICENSE-LGPL-2.1;md5=a778a33ef338abbaf8b8a7c36b6eec80 \
"

require gn-utils.inc

inherit gettext

# webos-wayland-extensions
# libwebosi18n
#
# bison-native
# xproto
# curl
DEPENDS = "virtual/gettext wayland wayland-native pixman freetype fontconfig openssl pango cairo icu libxkbcommon libexif dbus pciutils udev libcap alsa-lib virtual/egl elfutils-native libdrm atk gperf-native gconf nss nss-native nspr nspr-native"

#PROVIDES = "virtual/webruntime"
PROVIDES = "${BROWSER_APPLICATION}"

SRC_URI = "\
    git://github.com/webosose/${PN};branch=@15.agl.flounder;protocol=https \
    git://github.com/webosose/v8;destsuffix=git/src/v8 \
"
SRCREV="${AUTOREV}"

## we don't include SRCPV in PV, so we have to manually include SRCREVs in do_fetch vardeps
WEBOS_VERSION_V8 = "6.8.275.26-2_a8559356cc7652b77913e47f681ee126b97e8eaf"
do_fetch[vardeps] += "SRCREV_v8"
SRCREV_v8 = "d7b77ff7d9178acc5f6f84d27f56c0e714d28e7a"
SRCREV_FORMAT = "main_v8"

S = "${WORKDIR}/git"

SRC_DIR = "${S}/src"
OUT_DIR = "${WORKDIR}/build"
BUILD_TYPE = "Release"

WEBRUNTIME_BUILD_TARGET = "webos:weboswebruntime"
BROWSER_APP_BUILD_TARGET = "chrome"
BROWSER_APPLICATION="chromium68-browser"
BROWSER_APPLICATION_DIR = "/opt/chromium68"

TARGET = "${WEBRUNTIME_BUILD_TARGET} ${BROWSER_APP_BUILD_TARGET}"

# Skip do_install_append of webos_system_bus. It is not compatible with this component.
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"

PACKAGECONFIG ?= "jumbo use-upstream-wayland"

# Options to enable debug/debug-webcore build.
# Add the following line to local.conf (or local.dev.inc) to enable them:
#   PACKAGECONFIG_append_pn-chromium68 = " debug debug-webcore"
# By default debug is completely disabled to speed up build
PACKAGECONFIG[debug] = "is_debug=false is_component_build=false symbol_level=2, is_debug=false symbol_level=0"
PACKAGECONFIG[debug-webcore] = "remove_webcore_debug_symbols=false,remove_webcore_debug_symbols=true"

# Set a default value for jumbo file merge of 8. This should be good for build
# servers and workstations with a big number of cores. In case build is
# happening in a machine with less cores but still enough RAM a good value could
# be 50.
JUMBO_FILE_MERGE_LIMIT="8"
PACKAGECONFIG[jumbo] = "use_jumbo_build=true jumbo_file_merge_limit=${JUMBO_FILE_MERGE_LIMIT}, use_jumbo_build=false"

PACKAGECONFIG[lttng] = "use_lttng=true,use_lttng=false,lttng-ust,lttng-tools lttng-modules babeltrace"

# Chromium can use v4l2 device for hardware accelerated video decoding on such boards as Renesas R-car M3, for example.
# In case of R-car m3, additional patches are required for gstreamer and v4l2apps.
# See https://github.com/igel-oss/meta-browser-hwdecode/tree/igalia-chromium71.
PACKAGECONFIG[use-linux-v4l2] = "use_v4l2_codec=true use_v4lplugin=true use_linux_v4l2_only=true"

PACKAGECONFIG[use-upstream-wayland] = " \
        ozone_platform_wayland_external=false ozone_platform_wayland=true \
        use_system_minigbm=true, \
        ozone_platform_wayland_external=true ozone_platform_wayland=false \
"

#custom_toolchain=\"//build/toolchain/linux/unbundle:default\"
GN_ARGS = "\
    cros_host_ar=\"${BUILD_AR}\"\
    cros_host_cc=\"${BUILD_CC}\"\
    cros_host_cxx=\"${BUILD_CXX}\"\
    cros_host_extra_ldflags=\"${BUILD_LDFLAGS}\"\
    cros_target_ar=\"${AR}\"\
    cros_target_cc=\"${CC}\"\
    cros_target_cxx=\"${CXX}\"\
    enable_memorymanager_webapi=false\
    ffmpeg_branding=\"Chrome\"\
    host_os=\"linux\"\
    is_cross_linux_build=true\
    is_clang=false\
    ozone_auto_platforms=false\
    proprietary_codecs=true\
    target_os=\"linux\"\
    target_sysroot=\"${STAGING_DIR_HOST}\"\
    treat_warnings_as_errors=false\
    is_agl=true\
    use_cbe=true\
    is_chrome_cbe=true\
    use_cups=false\
    use_custom_libcxx=false\
    use_kerberos=false\
    use_neva_media=false\
    use_ozone=true\
    use_xkbcommon=true\
    use_pmlog=false\
    use_sysroot=false\
    use_system_debugger_abort=true\
    use_webos_gpu_info_collector=false\
    ${PACKAGECONFIG_CONFARGS}\
"

# We need this for cross to 32 bit architectures, as we do not have a way
# to retrieve a host gcc for 32 bits in yocto
GN_ARGS_TOOLCHAIN = "\
    is_host_clang=true\
    host_toolchain=\"//build/toolchain/yocto:clang_yocto_native\" \
    fatal_linker_warnings=false\
    use_custom_libcxx_for_host=true\
"

# But for x86-64 previous setting fails in torque, so this makes build use
# gcc on host, and use host toolchain for v8 snapshot and torque
GN_ARGS_TOOLCHAIN_x86-64 = "\
    is_host_clang=false\
    use_custom_libcxx_for_host=false\
    v8_snapshot_toolchain=\"//build/toolchain/cros:host\" \
    cros_v8_snapshot_is_clang=false\
    cros_v8_snapshot_ar=\"${BUILD_AR}\"\
    cros_v8_snapshot_cc=\"${BUILD_CC}\"\
    cros_v8_snapshot_cxx=\"${BUILD_CXX}\"\
    cros_v8_snapshot_extra_ldflags=\"${BUILD_LDFLAGS}\"\
"

GN_ARGS += "${GN_ARGS_TOOLCHAIN}"

python do_write_toolchain_file () {
    """Writes a BUILD.gn file for Yocto detailing its toolchains."""
    toolchain_dir = d.expand("${S}/src/build/toolchain/yocto")
    bb.utils.mkdirhier(toolchain_dir)
    toolchain_file = os.path.join(toolchain_dir, "BUILD.gn")
    write_toolchain_file(d, toolchain_file)
}
addtask write_toolchain_file after do_patch before do_configure
# end TODO: drop this after we migrate to ubuntu 16.04 or above

## Disable neva-media for webos
## TODO: Remove this when gmp integration is complete for new chromium
#PACKAGECONFIG_remove_webos = "neva-media"

# More options to speed up the build
GN_ARGS += "\
    enable_nacl=false\
    disable_ftp_support=true\
    enable_print_preview=false\
    enable_remoting=false\
    use_glib=true\
    use_gnome_keyring=false\
    use_pulseaudio=false\
"

# Following options help build with icecc
GN_ARGS += "\
    linux_use_bundled_binutils=false\
    use_debug_fission=false\
"

# TODO: Check if we need something like below:
# CHROMIUM_DEBUG_FLAGS = "-g1"
# GN_ARGS += “extra_cflags=' ${CHROMIUM_DEBUG_FLAGS} ‘\”
# DEBUG_FLAGS = ""

# Respect ld-is-gold in DISTRO_FEATURES when enabling gold
# Similar patch applied in meta-browser
# http://patchwork.openembedded.org/patch/77755/
EXTRA_OEGN_GOLD = "${@bb.utils.contains('DISTRO_FEATURES', 'ld-is-gold', 'use_gold=true', 'use_gold=false', d)}"
GN_ARGS += "${EXTRA_OEGN_GOLD}"

GN_ARGS_append_arm = " target_cpu=\"arm\""
GN_ARGS_append_qemux86 = " target_cpu=\"x86\""
GN_ARGS_append_aarch64 = " target_cpu=\"arm64\""

# TODO: Check if we need anything  like these for GN
# GYP_DEFINES_append_aarch64 = " target_arch=arm64"
# workaround to fix emulator issue with latest chromium
# replace with proper fix when available
# follow: https://bugreports.qt.io/browse/QTBUG-57705
# GYP_DEFINES_append_qemux86 = " generate_character_data=0"

# ARM builds need special additional flags (see ${S}/build/config/arm.gni).
ARM_FLOAT_ABI = "${@bb.utils.contains('TUNE_FEATURES', 'callconvention-hard', 'hard', 'softfp', d)}"
GN_ARGS_append_armv6 = " arm_arch=\"armv6\" arm_version=6 arm_float_abi=\"${ARM_FLOAT_ABI}\""
GN_ARGS_append_armv7a = " arm_arch=\"armv7-a\" arm_version=7 arm_float_abi=\"${ARM_FLOAT_ABI}\""
GN_ARGS_append_armv7ve = " arm_arch=\"armv7ve\" arm_version=7 arm_float_abi=\"${ARM_FLOAT_ABI}\""
# tcmalloc's atomicops-internals-arm-v6plus.h uses the "dmb" instruction that
# is not available on (some?) ARMv6 models, which causes the build to fail.
GN_ARGS_append_armv6 += 'use_allocator="none"'
# The WebRTC code fails to build on ARMv6 when NEON is enabled.
# https://bugs.chromium.org/p/webrtc/issues/detail?id=6574
GN_ARGS_append_armv6 += 'arm_use_neon=false'

# TODO: Add GN corresponding if we support dynamic injection loading
# GYP_DEFINES += "use_dynamic_injection_loading=0"

# Doesn't build for armv[45]*
COMPATIBLE_MACHINE = "(-)"
COMPATIBLE_MACHINE_aarch64 = "(.*)"
COMPATIBLE_MACHINE_armv6 = "(.*)"
COMPATIBLE_MACHINE_armv7a = "(.*)"
COMPATIBLE_MACHINE_armv7ve = "(.*)"
COMPATIBLE_MACHINE_x86 = "(.*)"
COMPATIBLE_MACHINE_x86-64 = "(.*)"

#CHROMIUM_PLUGINS_PATH = "${libdir}"
CBE_DATA_PATH = "${libdir}/cbe"
CBE_DATA_LOCALES_PATH = "${CBE_DATA_PATH}/locales"

# The text relocations are intentional -- see comments in [GF-52468]
# TODO: check if we need INSANE_SKIP on ldflags
INSANE_SKIP_${PN} = "textrel ldflags"


do_compile[progress] = "outof:^\[(\d+)/(\d+)\]\s+"
do_compile() {
    if [ ! -f ${OUT_DIR}/${BUILD_TYPE}/build.ninja ]; then
         do_configure
    fi

    export PATH="${S}/depot_tools:$PATH"
    ${S}/depot_tools/ninja -C ${OUT_DIR}/${BUILD_TYPE} ${TARGET}
}

do_configure() {
    configure_env
}

configure_env() {
    export GYP_CHROMIUM_NO_ACTION=1
    export PATH="${S}/depot_tools:$PATH"

    GN_ARGS="${GN_ARGS}"
    echo GN_ARGS is ${GN_ARGS}
    echo BUILD_TARGETS are ${TARGET}
    cd ${SRC_DIR}
    gn gen ${OUT_DIR}/${BUILD_TYPE} --args="${GN_ARGS}"
}

WINDOW_SIZE ?= "1920,1080"

configure_browser_settings() {
    USER_AGENT="Mozilla/5.0 (Linux; NetCast; U) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/${CHROMIUM_VERSION} Safari/537.31"
    echo "${USER_AGENT}" > ${D_DIR}/user_agent_conf
    #We can replace below WINDOW_SIZE values from build configuration if available
    #echo "${WINDOW_SIZE}" > ${D_DIR}/window_size_conf
}

install_chromium_browser() {
    D_DIR=${D}${BROWSER_APPLICATION_DIR}
    install -d ${D_DIR}

    # Install browser files
     if [ -e "${SRC_DIR}/webos/install" ]; then
         cd ${OUT_DIR}/${BUILD_TYPE}
         xargs --arg-file=${SRC_DIR}/webos/install/default_browser/binary.list cp -R --no-dereference --preserve=mode,links -v --target-directory=${D_DIR}
         cd ${SRC_DIR}
         xargs --arg-file=${SRC_DIR}/webos/install/default_browser/runtime.list cp -R --no-dereference --preserve=mode,links -v --target-directory=${D_DIR}
     fi

    # AGL does not have PMLOG
    sed -i.bak s/PmLogCtl.*// ${D_DIR}/run_webbrowser

    # To execute chromium in JAILER, Security Part needs permissions change
    # run_webbrowser: Script file for launching chromium
    chmod -v 755 ${D_DIR}/chrome
    chmod -v 755 ${D_DIR}/kill_webbrowser
    chmod -v 755 ${D_DIR}/run_webbrowser

    configure_browser_settings
}

MKSNAPSHOT_PATH = ""
MKSNAPSHOT_PATH_arm = "clang_x86_v8_arm/"
MKSNAPSHOT_PATH_aarch64 = "clang_x64_v8_arm64/"

install_webruntime() {
    install -d ${D}${libdir}
    install -d ${D}${includedir}/${BPN}
    install -d ${D}${CBE_DATA_PATH}
    install -d ${D}${CBE_DATA_LOCALES_PATH}

    # Install webos webview files
    if [ -e "${SRC_DIR}/webos/install" ]; then
        cd ${SRC_DIR}
        xargs --arg-file=${SRC_DIR}/webos/install/weboswebruntime/staging_inc.list cp --parents --target-directory=${D}${includedir}/${BPN}

        cd ${OUT_DIR}/${BUILD_TYPE}

        cp libcbe.so ${D}${libdir}/
        if [ "${WEBOS_LTTNG_ENABLED}" = "1" ]; then
          # use bindir if building non-cbe
          cp libchromium_lttng_provider.so ${D}${libdir}/
        fi
        xargs --arg-file=${SRC_DIR}/webos/install/weboswebruntime/binary.list cp --parents --target-directory=${D}${CBE_DATA_PATH}
        cat ${SRC_DIR}/webos/install/weboswebruntime/data_locales.list | xargs -I{} install -m 755 -p {} ${D}${CBE_DATA_LOCALES_PATH}
    fi

    # move this to separate mksnapshot-cross recipe once we figure out how to build just cross mksnapshot from chromium repository
    install -d ${D}${bindir_cross}
    gzip -c ${OUT_DIR}/${BUILD_TYPE}/${MKSNAPSHOT_PATH}mksnapshot > ${D}${bindir_cross}/${HOST_SYS}-mksnapshot.gz
}

do_install() {
    install_webruntime
    install_chromium_browser
}

WEBOS_SYSTEM_BUS_DIRS_LEGACY_BROWSER_APPLICATION = " \
    ${webos_sysbus_prvservicesdir}/${BROWSER_APPLICATION}.service \
    ${webos_sysbus_pubservicesdir}/${BROWSER_APPLICATION}.service \
    ${webos_sysbus_prvrolesdir}/${BROWSER_APPLICATION}.json \
    ${webos_sysbus_pubrolesdir}/${BROWSER_APPLICATION}.json \
"

SYSROOT_DIRS_append = " ${bindir_cross}"

PACKAGES_prepend = " \
    ${PN}-cross-mksnapshot \
    ${BROWSER_APPLICATION} \
"

FILES_${BROWSER_APPLICATION} += " \
    ${BROWSER_APPLICATION_DIR} \
    ${WEBOS_SYSTEM_BUS_DIRS_LEGACY_BROWSER_APPLICATION} \
"

RDEPENDS_${BROWSER_APPLICATION} += "${PN}"

VIRTUAL-RUNTIME_gpu-libs ?= ""
RDEPENDS_${PN} += "${VIRTUAL-RUNTIME_gpu-libs}"

# The text relocations are intentional -- see comments in [GF-52468]
# TODO: check if we need INSANE_SKIP on ldflags
INSANE_SKIP_${BROWSER_APPLICATION} += "libdir ldflags textrel"

FILES_${PN} = " \
    ${libdir}/*.so \
    ${CBE_DATA_PATH}/* \
    ${libdir}/${BPN}/*.so \
    ${WEBOS_SYSTEM_BUS_DIRS} \
"

FILES_${PN}-dev = " \
    ${includedir} \
"

FILES_${PN}-cross-mksnapshot = "${bindir_cross}/${HOST_SYS}-mksnapshot.gz"
