FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
            file://0001-wayland-egl-initialize-window-surface-size-to-window.patch \
            file://0001-wayland-egl-update-surface-size-on-window-resize.patch \
            file://0001-wayland-egl-Resize-EGL-surface-on-update-buffer-for-.patch \
"
