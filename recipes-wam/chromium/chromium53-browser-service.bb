SUMMARY = "Chromium browser widget"
DESCRIPTION = "Wgt packaging for running chromium53 installed browser"
HOMEPAGE = "https://webosose.org"
SECTION = "apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "gitsm://auto-gitlab.lgsvl.net/jose.dapena/chromium53-browser-service.git;protocol=ssh"
SRCREV = "${AUTOREV}"

PV = "1.0+git${SRCPV}"
S = "${WORKDIR}/git"

#build-time dependencies
DEPENDS += "af-binder af-main-native chromium53"

inherit cmake aglwgt

RDEPENDS_${PN} += "chromium53-browser runxdg"
