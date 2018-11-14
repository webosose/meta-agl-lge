LICENSE = "Apache-2.0"

do_install() {
    install -D /dev/null ${D}/var/luna/preferences/debug_system_apps
    install -D /dev/null ${D}/var/luna/preferences/devmode_enabled
}

