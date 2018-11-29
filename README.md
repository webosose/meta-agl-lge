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
repo init -b eel -m eel_5.1.0.xml -u https://gerrit.automotivelinux.org/gerrit/AGL/AGL-repo
repo sync
```

2. Clone this repository in same path of other layers.

3. Call to aglsetup.sh adding agl-demo-wam as a feature.

I.e. for MinnowBoard:

```bash
source meta-agl/scripts/aglsetup.sh -m intel-corei7-64 -b build agl-devel agl-demo agl-appfw-smack agl-netboot agl-demo-wam
```

I.e. for Renesas R-car M3 board:

```bash
source meta-agl/scripts/aglsetup.sh -m m3ulcb -b build agl-devel agl-demo agl-appfw-smack agl-netboot agl-demo-wam
```

Please note that the environment requires R-car M3 drivers to be placed into the $HOME/Downloads folder.
The following drivers are required by the AGL eel branch:

```
R-Car_Gen3_Series_Evaluation_Software_Package_for_Linux-weston2-20170904.zip
R-Car_Gen3_Series_Evaluation_Software_Package_of_Linux_Drivers-weston2-20170904.zip
```

The drivers can be downloaded from https://www.renesas.com/en-us/solutions/automotive/rcar-demoboard-2.html.

4. Build the target agl-demo-platform-wam

```bash
bitbake agl-demo-platform-wam
```

5. Flash the resulting build.

I.e. for MinnowBoard use [meta-agl/scripts/mkefi-agl.sh](https://gerrit.automotivelinux.org/gerrit/#/admin/projects/AGL/meta-agl) by replacing /dev/sdX with your actual SD card device.
```bash
sudo ../meta-agl/scripts/mkefi-agl.sh tmp/deploy/images/intel-corei7-64/agl-demo-platform-wam-intel-corei7-64.wic.xz /dev/sdX
```

Supported Machines
------------------

Reference hardware:

* MinnowBoard
* Renesas R-car M3 board

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
> tested revision: eel_5.1.0

URI: https://gerrit.automotivelinux.org/gerrit/AGL/meta-agl-devel
> branch:   eel
> tested revision: eel_5.1.0

URI: https://gerrit.automotivelinux.org/gerrit/AGL/meta-agl-demo
> branch:   eel
> tested revision: eel_5.1.0

URI: https://github.com/01org/meta-intel-iot-security
> tested revision: 20bbb97f6d5400b126ae96ef446c3e60c7e16285

