DESCRIPTION = "forcerdate triggers forced NTP update of time regularly"
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
    file://forcerdate.sh \
    file://forcerdate.service \
    file://LICENSE \
"

do_install_append() {
  install -d ${D}${bindir}
  install -v -m 775 ${WORKDIR}/forcerdate.sh ${D}${bindir}/forcerdate.sh

  install -d ${D}${systemd_system_unitdir}
  install -v -m 644 ${WORKDIR}/forcerdate.service ${D}${systemd_system_unitdir}/forcerdate.service

  install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants
  ln -snf /lib/systemd/system/forcerdate.service ${D}${sysconfdir}/systemd/system/multi-user.target.wants
}

FILES_${PN} += "{bindir} ${systemd_system_unitdir}"

