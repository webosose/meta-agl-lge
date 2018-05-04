SUMMARY = "Chromium browser widget"
DESCRIPTION = "Wgt packaging for running chromium53 installed browser"
HOMEPAGE = "https://webosose.org"
SECTION = "apps"
LICENSE = "CLOSED"

SRC_URI = "gitsm://auto-gitlab.lgsvl.net/jose.dapena/chromium53-browser-service.git;protocol=ssh"
SRCREV = "${AUTOREV}"

PV = "1.0+git${SRCPV}"
S = "${WORKDIR}/git"

#build-time dependencies
DEPENDS += "af-binder af-main-native chromium53"

inherit cmake aglwgt

RDEPENDS_${PN} += "chromium53-browser runxdg"
