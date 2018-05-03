DESCRIPTION = "AGL Demo Platform image currently contains a simple HMI and \
demos (WAM flavour)."

LICENSE = "MIT"

require agl-demo-platform-wam.inc

IMAGE_INSTALL_append = "\
    packagegroup-agl-demo-platform-wam \
    "
