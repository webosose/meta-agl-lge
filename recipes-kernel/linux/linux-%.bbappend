# Enables finding the cpu_governor.cfg.
FILESEXTRAPATHS_prepend := "${THISDIR}/linux:"
SRC_URI_append = " file://cpu_governor.cfg"

KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/cpu_governor.cfg"

