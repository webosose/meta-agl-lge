DESCRIPTION = "AGL Application Framework - Example widgets"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "\
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
"

PR = "r0"

SRC_URI += "git://github.com/iotbzh/afm-widget-examples.git;branch=master"
SRC_URI[md5sum] = "73b5290664d82b97d800fc1d24dd70fc"

SRC_URI += " \
    file://youtube-agl.wgt \
"

S = "${WORKDIR}/git"
SRCREV = "${AUTOREV}"

inherit aglwgt

do_aglwgt_package()  {
  install -d ${B}/package
  cp ${S}/memory-match.wgt ${B}/package/memory-match.wgt
  cp ${S}/annex.wgt ${B}/package/annex.wgt
  cp ${WORKDIR}/youtube-agl.wgt ${B}/package/youtube-agl.wgt
}

