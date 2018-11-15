**README.md for the 'meta-agl-lge' layer.**

meta-agl-lge, demo components and targets developed by LGE
==========================================================

The layer 'meta-agl-lge' provides a demo of the WAM web runtime
on top of AGL distribution.

Quick start guide
-----------------

Add this library with feature 'agl-demo-wam'

1. Fetch AGL flounder with repo tool.

```bash
repo init -b flounder -m flounder_6.0.2.xml -u https://gerrit.automotivelinux.org/gerrit/AGL/AGL-repo
repo sync
```

2. Clone this repository in same path of other layers.

3. Call to aglsetup.sh adding agl-demo-wam as a feature.

I.e. for Minnowboard : Not verified yet [TODO]

```bash
source meta-agl/scripts/aglsetup.sh -m intel-corei7-64 -b build agl-devel agl-netboot agl-appfw-smack agl-demo agl-localdev agl-audio-4a-framework agl-hmi-framework agl-demo-wam
```

I.e. for Renesas R-car M3 board:

```bash
source meta-agl/scripts/aglsetup.sh -m m3ulcb -b build agl-devel agl-netboot agl-appfw-smack agl-demo agl-localdev agl-audio-4a-framework agl-hmi-framework agl-demo-wam
```

Please note that the environment requires R-car M3 drivers to be placed into the $HOME/Downloads folder.
The following drivers are required by the AGL eel branch:

```
R-Car_Gen3_Series_Evaluation_Software_Package_for_Linux-20180423.zip
R-Car_Gen3_Series_Evaluation_Software_Package_of_Linux_Drivers-20180423.zip
```

The drivers can be downloaded from https://www.renesas.com/eu/en/solutions/automotive/rcar-download/rcar-demoboard.html

4. Build the target agl-demo-platform-wam

```bash
bitbake agl-demo-platform-wam
```

Supported Machines
------------------

Reference hardware:

* Renesas R-car M3 board
* Minnowboard : Not verified yet [TODO]

Layer dependencies
------------------

* Base dependencies [agl-demo-wam]:

URI: git://git.yoctoproject.org/poky
> branch         : rocko
> tested revision: 05711ba18587aaaf4a9c465a1dd4537f27ceda93

URI: https://github.com/meta-qt5/meta-qt5.git
> branch:   rocko
> tested revision: 682ad61c071a9710e9f9d8a32ab1b5f3c14953d1

URI: https://gerrit.automotivelinux.org/gerrit/AGL/meta-agl
> tag:   m/flounder,
> tested revision: 0fde830e52253e877ac23d625416e8f20e440965

URI: https://gerrit.automotivelinux.org/gerrit/AGL/meta-agl-devel
> tag:   m/flounder
> tested revision: b5df3d646894d0ee798a1949dbda9620131dbb4e

URI: https://gerrit.automotivelinux.org/gerrit/AGL/meta-agl-demo
> tag:   m/flounder
> tested revision: 00e92bf3daa085120abee26af42920df100ecf57

