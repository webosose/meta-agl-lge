**README.md for the 'meta-agl-lge' layer.**

meta-agl-lge, demo components and targets developed by LGE
==========================================================

The layer 'meta-agl-lge' provides a demo of the WAM web runtime
on top of AGL distribution.

Quick start guide
-----------------

Add this library with feature 'agl-demo-wam'

1. Fetch AGL eel with repo tool.

```bash
repo init -b eel -u https://gerrit.automotivelinux.org/gerrit/AGL/AGL-repo
repo sync
```

2. Clone this repository in same path of other layers.

3. Call to aglsetup.sh adding agl-demo-wam as a feature.

I.e. for Minnowboard:

```bash
source meta-agl/scripts/aglsetup.sh -m intel-corei7-64 -b build agl-devel agl-demo agl-appfw-smack agl-netboot agl-demo-wam
```

4. Build the target agl-demo-platform-wam

```bash
bitbake agl-demo-platform-wam
```

Supported Machines
------------------

Reference hardware:

* Minnowboard

Layer dependencies
------------------

* Base dependencies [agl-demo-wam]:

URI: git://git.yoctoproject.org/poky
> branch         : pyro
> tested revision: pyro-17.0.3

URI: https://github.com/meta-qt5/meta-qt5.git
> branch:   pyro
> tested revision: c6aa602d0640040b470ee81de39726276ddc0ea3

URI: https://gerrit.automotivelinux.org/gerrit/AGL/meta-agl
> branch:   eel
> tested revision: d6edc2872b1fc53f93607e7e38dc3ae87e12128c

URI: https://gerrit.automotivelinux.org/gerrit/AGL/meta-agl-devel
> branch:   eel
> tested revision: eel-5.0.2

URI: https://gerrit.automotivelinux.org/gerrit/AGL/meta-agl-demo
> branch:   eel
> tested revision: 9dfac4c2eb8291ecca449791dea66c389da1580a

URI: https://github.com/01org/meta-intel-iot-security
> tested revision: 20bbb97f6d5400b126ae96ef446c3e60c7e16285

