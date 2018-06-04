INHERIT += "blacklist"

PNBLACKLIST[qtwebengine] = "Superseded by wam"
RDEPENDS_packagegroup-agl-demo_remove = "qtwebengine"

PREFERRED_PROVIDER_virtual/webruntime = "wam"
