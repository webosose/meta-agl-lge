SUMMARY = "Lightweight http(s) proxy daemon"
HOMEPAGE = "https://tinyproxy.github.io/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI = "git://github.com/Igalia/${PN}.git;branch=wam-proxy;protocol=https;rev=917ef9eec23b63015b5e20e9c7b9350c08c55512 \
		   file://disable-documentation.patch \
		   file://tinyproxy.service"
S = "${WORKDIR}/git"

EXTRA_OECONF += " \
	--enable-filter \
	--enable-transparent \
	--disable-regexcheck \
	--enable-reverse \
	--enable-upstream \
	--enable-xtinyproxy \
	"

inherit autotools systemd useradd

#User specific
USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "nobody"
GROUPADD_PARAM_${PN} = "--system tinyproxy"

SYSTEMD_PACKAGES += "${BPN}"
SYSTEMD_SERVICE_${PN} = "tinyproxy.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_install_append() {
	if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
		install -d ${D}${systemd_system_unitdir}
		install -m 0644 ${WORKDIR}/tinyproxy.service ${D}${systemd_system_unitdir}
	fi
}
