SUMMARY = "Lightweight http(s) proxy daemon"
HOMEPAGE = "https://tinyproxy.github.io/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI = "git://github.com/Igalia/${PN}.git;branch=wam-proxy;protocol=https;rev=feb6e92d593e8b073c33cb2719b0f4c97b8626b0 \
		   file://disable-documentation.patch"
S = "${WORKDIR}/git"

EXTRA_OECONF += " \
	--enable-filter \
	--enable-transparent \
	--enable-reverse \
	--enable-upstream \
	--enable-xtinyproxy \
	"

inherit autotools

FILES_${PN} += "${datadir}/tinyproxy/*"
