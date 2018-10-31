# Copyright (c) 2016-2017 LG Electronics, Inc.

DEPENDS = "virtual/gettext wayland wayland-native pixman freetype fontconfig openssl pango cairo icu libxkbcommon libexif dbus pciutils udev libcap alsa-lib virtual/egl elfutils-native libdrm atk gperf-native gconf"

# 'wgtpkg-pack' in af-main-native is required.
DEPENDS_append = " af-main-native"
# for bindings  af-binder is required.
DEPENDS_append = " af-binder"

SRC_URI = "git://github.com/webosose/${PN}.git;branch=@1.agl.flounder;protocol=https"
S = "${WORKDIR}/git"
SRCREV = "${AUTOREV}"

CHROMIUM_DEBUG_FLAGS = "-g1"

inherit gettext

GYP_DEFINES = "\
    agl=1\
    chromeos=0\
    clang=0\
    component=static_library\
    disable_ftp_support=1\
    disable_nacl=1\
    enable_media_router=0\
    enable_plugin_installation=1\
    enable_plugins=1\
    enable_printing=0\
    enable_webrtc=1\
    ffmpeg_branding=Chrome\
    host_clang=0\
    no_gc_sections=1\
    ozone_auto_platforms=0\
    ozone_platform_caca=0\
    ozone_platform_dri=0\
    ozone_platform_drm=0\
    ozone_platform_eglfs=1\
    ozone_platform_egltest=0\
    ozone_platform_gbm=0\
    ozone_platform_ozonex=0\
    ozone_platform_test=0\
    ozone_platform_wayland_external=1\
    proprietary_codecs=1\
    python_ver=2.7\
    qt_patch=1\
    release_extra_cflags=' ${CHROMIUM_DEBUG_FLAGS} '\
    remoting=0\
    remove_webcore_debug_symbols=0\
    strip_tests=1\
    swig_defines=-DOS_LINUX\
    sysroot=${STAGING_DIR_HOST}\
    system_libdir=lib\
    use_ash=0\
    use_aura=1\
    use_cups=0\
    use_gnome_keyring=0\
    use_ibus=0\
    use_kerberos=0\
    use_nss_certs=0\
    use_nss_verifier=0\
    use_openssl=1\
    use_openssl_certs=1\
    use_ozone=1\
    use_pulseaudio=0\
    use_self_signed_certificates=1\
    use_system_icu=0\
    use_xkbcommon=1\
    webos=0\
    webos_ewam=0\
    werror=''\
"

# Respect ld-is-gold in DISTRO_FEATURES when enabling gold
# Similar patch applied in meta-browser
# http://patchwork.openembedded.org/patch/77755/
#EXTRA_OEGYP_GOLD = "${@bb.utils.contains('DISTRO_FEATURES', 'ld-is-gold', 'linux_use_gold_binary=1 linux_use_gold_flags=1', 'linux_use_gold_binary=0 linux_use_gold_flags=0', d)}"
EXTRA_OEGYP_GOLD = "linux_use_gold_binary=1 linux_use_gold_flags=1"

# Use system binutils, bundled binutils (GNU gold (GNU Binutils 2.24) 1.11)
# aren't compatible with (GNU gold (GNU Binutils for Ubuntu 2.25.90.20160101) 1.11) included in Ubuntu-16.04 Alpha
# and building native libvpx fails with:
# chromium38/38.0.2125.122-107-r11.3/git/src/third_party/binutils/Linux_x64/Release/bin/ld: error: /usr/lib/gcc/x86_64-linux-gnu/5/../../../x86_64-linux-gnu/crti.o: unsupported reloc 42 against global symbol __gmon_start__
GYP_DEFINES_append = " linux_use_bundled_gold=0"
GYP_DEFINES_append = " linux_use_bundled_binutils=0"

# WORKAROUNDS
GYP_DEFINES_append = " linux_fpic=1"

GYP_DEFINES += "${EXTRA_OEGYP_GOLD}"

GYP_DEFINES_append_arm = " target_arch=arm arm_float_abi=${TUNE_CCARGS_MFLOAT}"
GYP_DEFINES_append_aarch64 = " target_arch=arm64"
GYP_DEFINES_append_qemux86 = " target_arch=ia32"
# workaround to fix emulator issue with latest chromium
# replace with proper fix when available
# follow: https://bugreports.qt.io/browse/QTBUG-57705
GYP_DEFINES_append_qemux86 = " generate_character_data=0"
GYP_DEFINES_append_armv7a = " arm_version=7"
GYP_DEFINES_append_armv7ve = " arm_version=7 arm_override_arch='armv7ve'"
GYP_DEFINES_append_hardware = " use_directmedia2=1"

# Doesn't build for armv[45]*
COMPATIBLE_MACHINE = "(-)"
COMPATIBLE_MACHINE_aarch64 = "(.*)"
COMPATIBLE_MACHINE_armv6 = "(.*)"
COMPATIBLE_MACHINE_armv7a = "(.*)"
COMPATIBLE_MACHINE_armv7ve = "(.*)"
COMPATIBLE_MACHINE_x86 = "(.*)"
COMPATIBLE_MACHINE_x86-64 = "(.*)"

# The text relocations are intentional -- see comments in [GF-52468]
INSANE_SKIP_${PN} = "textrel"

SRC_DIR = "${S}/src"
OUT_DIR = "${WORKDIR}/build"
BUILD_TYPE = "Release"

WEBOS_SYSTEM_BUS_FILES_LOCATION = "${OUT_DIR}/${BUILD_TYPE}/services"

do_compile[progress] = "outof:^\[(\d+)/(\d+)\]\s+"
do_compile() {
    if [ ! -f ${OUT_DIR}/${BUILD_TYPE}/build.ninja ]; then
         do_configure
    fi

    ${S}/depot_tools/ninja -C ${OUT_DIR}/${BUILD_TYPE} chromium
}

do_configure() {
    configure_env
    echo GYP_DEFINES is $GYP_DEFINES
    echo GYP_GENERATOR_FLAGS is $GYP_GENERATOR_FLAGS
    ${SRC_DIR}/build/gyp_chromium
}

DEBUG_FLAGS = ""

configure_env() {
    CC_host="${BUILD_CC}"
    CXX_host="${BUILD_CXX}"
    LD_host="${BUILD_LD}"
    AR_host="${BUILD_AR}"

    # GYP options
    GYP_DEFINES="${GYP_DEFINES}"
    GYP_GENERATOR_FLAGS="output_dir=${OUT_DIR} config=${BUILD_TYPE}"

    # Lg default build environment
    if [ -e "${SRC_DIR}/lg" ]; then
        export GYP_INCLUDE_FIRST=${SRC_DIR}/lg/build/lg_common.gypi
        export GYP_INCLUDE_LAST=${SRC_DIR}/lg/build/lg_defines.gypi
    fi

    export CC_host CXX_host LD_host AR_host GYP_DEFINES GYP_GENERATOR_FLAGS
}

WINDOW_SIZE ?= "1920,1080"
CACHE_DIR ?= "${webos_homedir}/webbrowser"

configure_browser_settings() {
    echo "${USER_AGENT}" > ${D_DIR}/user_agent_conf

    echo "${CACHE_DIR}" > ${D_DIR}/user_cachedir_conf
    #We can replace below WINDOW_SIZE values from build configuration if available
    echo "${WINDOW_SIZE}" > ${D_DIR}/window_size_conf
}

install_chromium_browser() {
    D_DIR=${D}${BROWSER_APPLICATION_DIR}
    install -d ${D_DIR}
    cp -R --no-dereference --preserve=mode,links -v ${OUT_DIR}/${BUILD_TYPE}/install/* ${D_DIR}/

    # To execute chromium in JAILER, Security Part needs permissions change
    # run_webbrowser: Script file for launching chromium
    chmod -v 755 ${D_DIR}/chrome
    chmod -v 755 ${D_DIR}/kill_webbrowser
    chmod -v 755 ${D_DIR}/run_webbrowser

    # disble remote debugging feature for production
    if [ "${WEBOS_DISTRO_PRERELEASE}" = "" ]; then
        sed -i 's/ENABLE_INSPECTOR=1/ENABLE_INSPECTOR=0/' ${D_DIR}/run_webbrowser
    fi

    configure_browser_settings
}

do_install() {
    install_chromium_browser
}
