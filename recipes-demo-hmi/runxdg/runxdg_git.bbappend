FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
            file://0002-backport-Force-set-unset-keyboard-focus.patch \
            file://0001-Fix-memory-corruption-issue-when-unregitering-surfac.patch \
"
