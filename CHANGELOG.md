
# Change Log

Record of version changes for developers
## 1.1.1 - Jan 2nd, 2020

* Fixed: World not cleaning up entities and other data between tests
* Changed: cleanupBetweenTests() to nuke the world when called instead of clearing a single chunk

## 1.1.0 - Dec 31, 2019

* Added: Test Manager to control world, server, and player init
* Added: Helpers to clear center chunk
* Added: Helpers to lock test to center chunk

## 1.0.0 - Dec 30, 2019

* Changed: From Junit4 to Junit5
* Changed: To support Minecraft 12
* Removed: Launcher init
* Removed: RunWith system

## Before

* Not making change logs for old versions, PR if you care