SUMMARY = "The software for DEMO platform of AGL IVI profile (WAM flavour)"
DESCRIPTION = "A set of packages belong to AGL Demo Platform (WAM web runtime)"

LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-agl-demo-platform-wam \
    "

ALLOW_EMPTY_${PN} = "1"

PREFERRED_PROVIDER_virtual/webruntime = "wam"

RDEPENDS_${PN} += "\
    packagegroup-agl-image-ivi \
    "

# add packages for demo platform (include demo apps) here
RDEPENDS_${PN} += " \
    packagegroup-agl-demo-platform \
    "

# add packages for OpenIVI-HTML5 demo
RDEPENDS_${PN} += " \
    chromium68-browser-service \
    wam \
    wamdevmode \
    afm-widget-examples \
    "
