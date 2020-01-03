
# Change Log

Record of version changes for developers

## 1.3.0 - Jan 3rd, 2020

Breaking change for previous versions

* Removed: Any calls to junit lib not inside a unit test
* Removed: test prefabs due to lack of usage and dep on junit
* Removed: Temp file creation in dedicated server due to crashes while creating the server (inconsistant)
* Added: Error handler to dedicated server to allow redirecting errors to unit test fail calls
* Changed: TestManager to require error handler 'Assertions::fail' for junit 5

## 1.2.0 - Jan 3rd, 2020

* Added: Reflection helper for removing final
* Added: Reflection helper for setting private fields
* Added: Reflection helper for setting static final fields
* Added: DummyCommandSender for testing commands
* Changed: Dedicated fake server to use temp files for property files

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