DESCRIPTION = "Agate AGL apps and resources"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "\
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://LICENSE;md5=67727fe009fb4997198fd45a42b06b99 \
"

PR = "r0"
SRCREV = "${AUTOREV}"
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
S="${WORKDIR}"

SRC_URI += " \
    file://20-agate-apps-install.sh \
    file://agate-agl-app.wgt \
    file://agate-comm-server.service \
    file://LICENSE \
"

inherit aglwgt

do_install_append() {
  install -d ${D}/${sysconfdir}/agl-postinsts
  install -v -m 775 ${WORKDIR}/20-agate-apps-install.sh ${D}/${sysconfdir}/agl-postinsts/20-agate-apps-install.sh

  install -d ${D}${systemd_user_unitdir}
  install -v -m 644 ${WORKDIR}/agate-comm-server.service ${D}${systemd_user_unitdir}/agate-comm-server.service

  install -d ${D}${sysconfdir}/systemd/user/default.target.wants
  ln -sf ${systemd_user_unitdir}/agate-comm-server.service ${D}${sysconfdir}/systemd/user/default.target.wants
}

do_aglwgt_package()  {
  install -d ${B}/package
  cp ${WORKDIR}/agate-agl-app.wgt ${B}/package/agate-agl-app.wgt
}

FILES_${PN} += "${sysconfdir}/agl-postinsts ${systemd_user_unitdir}"
