DESCRIPTION = "Agate AGL apps and resources"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r0"
SRCREV = "${AUTOREV}"
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
S = "${WORKDIR}/git"

SRC_URI += "git://github.com/enactjs/agate-apps.git;protocol=https"

SRC_URI += " \
    file://config-communication-server.xml \
    file://config-console.xml \
    file://config-copilot.xml \
    file://icon-communication-server.png \
    file://icon-console.png \
    file://icon-copilot.png \
    file://agate-communication-server.service \
"

DEPENDS += " nodejs nodejs-native zip-native"

inherit aglwgt

do_configure() {
  # changing the home directory to the working directory, the .npmrc will be created in this directory
  export HOME=${WORKDIR}

  ${STAGING_BINDIR_NATIVE}/npm update
  ${STAGING_BINDIR_NATIVE}/npm shrinkwrap

  # access npm registry using http
  ${STAGING_BINDIR_NATIVE}/npm set strict-ssl false
  ${STAGING_BINDIR_NATIVE}/npm config set registry http://registry.npmjs.org/

   # configure cache to be in working directory
  ${STAGING_BINDIR_NATIVE}/npm set cache ${WORKDIR}/npm_cache

  # clear local cache prior to each configure
  ${STAGING_BINDIR_NATIVE}/npm cache clear

  cd ${S}
  ${STAGING_BINDIR_NATIVE}/npm install -g @enact/cli

  # install components deps
  cd ${S}/components
  ${STAGING_BINDIR_NATIVE}/npm install
}

do_compile() {
  # changing the home directory to the working directory, see do_configure
  export HOME=${WORKDIR}

  # build communication-server
  cd ${S}/communication-server
  ${STAGING_BINDIR_NATIVE}/npm --no-optional install

  # build console app
  cd ${S}/console
  ${STAGING_BINDIR_NATIVE}/npm install
  ${STAGING_BINDIR_NATIVE}/npm run pack

  # build copilot app
  cd ${S}/copilot
  ${STAGING_BINDIR_NATIVE}/npm install
  ${STAGING_BINDIR_NATIVE}/npm run pack
}

do_install_append() {
  # Ugly hack to start agate-communication-server on systemd
  # Didn't have time to figure out how it should be done in AGL in right way

  install -d ${D}${systemd_user_unitdir}
  install -v -m 644 ${WORKDIR}/agate-communication-server.service ${D}${systemd_user_unitdir}/agate-communication-server.service

  install -d ${D}${sysconfdir}/systemd/user/default.target.wants
  ln -sf ${systemd_user_unitdir}/agate-communication-server.service ${D}${sysconfdir}/systemd/user/default.target.wants
}

do_aglwgt_package()  {
  install -d ${B}/package

  # package communication-server
  install -v -m 644 ${WORKDIR}/config-communication-server.xml ${S}/communication-server/config.xml
  install -v -m 644 ${WORKDIR}/icon-communication-server.png ${S}/communication-server/icon.png
  install -v -m 755 ${WORKDIR}/agate-communication-server ${S}/communication-server/agate-communication-server
  cd ${S}/communication-server
  ${STAGING_BINDIR_NATIVE}/zip -r ${B}/package/agate-communication-server.wgt *

  # package console app
  install -v -m 644 ${WORKDIR}/config-console.xml ${S}/console/dist/config.xml
  install -v -m 644 ${WORKDIR}/icon-console.png ${S}/console/dist/icon.png
  cd ${S}/console/dist
  ${STAGING_BINDIR_NATIVE}/zip -r ${B}/package/agate-console.wgt *

  # package copilot app
  install -v -m 644 ${WORKDIR}/config-copilot.xml ${S}/copilot/dist/config.xml
  install -v -m 644 ${WORKDIR}/icon-copilot.png ${S}/copilot/dist/icon.png
  cd ${S}/copilot/dist
  ${STAGING_BINDIR_NATIVE}/zip -r ${B}/package/agate-copilot.wgt *
}

FILES_${PN} += "${systemd_user_unitdir}"
