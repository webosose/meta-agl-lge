SUMMARY = "Lightweight http(s) proxy daemon"
HOMEPAGE = "https://tinyproxy.github.io/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI = "git://github.com/Igalia/${PN}.git;branch=wam-proxy;protocol=https"
SRC_URI += "\
    file://disable-documentation.patch \
"
S = "${WORKDIR}/git"
SRCREV = "${AUTOREV}"

EXTRA_OECONF += " \
	--enable-filter \
	--enable-transparent \
	--enable-reverse \
	--enable-upstream \
	--enable-xtinyproxy \
	"

inherit autotools

FILES_${PN} += "${datadir}/tinyproxy/*"
