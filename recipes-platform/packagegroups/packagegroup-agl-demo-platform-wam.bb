SUMMARY = "The software for DEMO platform of AGL IVI profile (WAM flavour)"
DESCRIPTION = "A set of packages belong to AGL Demo Platform (WAM web runtime)"

LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-agl-demo-platform-wam \
    "

ALLOW_EMPTY_${PN} = "1"

PREFERRED_PROVIDER_virtual/webruntime = "wam"
EXTRA_APPS = ""

RDEPENDS_${PN} += "\
    packagegroup-agl-image-ivi \
    "

# add packages for demo platform (include demo apps) here
RDEPENDS_${PN} += " \
    packagegroup-agl-demo \
    "

# add packages for OpenIVI-HTML5 demo
RDEPENDS_${PN} += " \
    chromium53-browser \
    wam \
    "
