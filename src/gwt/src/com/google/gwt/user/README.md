GWT Patches
------------
This folder contains selected GWT source files pulled from the GWT 2.8.2
release, then modified to fix issues or add needed functionality.

This has been happening since early in RStudio development, but was not
noticed during the last few GWT updates. Thus, we were using increasingly
out-of-date copies of these files.

To upgrade to a new GWT version:

- update `updatesources.sh`, and `updatespatches.sh` to refer to new GWT
- execute `updatesources.sh` to stomp over the modified GWT files with the
latest copies and reapply patches
- resolve any conflicts, test, etc.
- run `createpatches.sh` to create updated copies of the patch files

