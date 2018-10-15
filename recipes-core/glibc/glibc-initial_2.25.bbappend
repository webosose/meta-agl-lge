do_configure () {
        (cd ${S} && gnu-configize) || die "failure in running gnu-configize"
        find ${S} -name "configure" | xargs touch
        cfgscript=`python3 -c "import os; print(os.path.relpath('${S}', '.'))"`/configure
        $cfgscript --host=${TARGET_SYS} --build=${BUILD_SYS} \
                --prefix=/usr \
                --without-cvs --disable-sanity-checks \
                --with-headers=${STAGING_DIR_TARGET}${includedir} \
                --enable-hacker-mode --enable-addons --disable-werror
}
