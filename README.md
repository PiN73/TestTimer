# TestTimer

The application is listening and displaying realtime location updates.
Service is used to keep it active when the app is in background or screen is turned off.
Location data is passed from Service to Activity using BroadcastReceiver.


# Contents

[Service](app/src/main/java/p/testtimer/MainActivity.kt) — listens location updates and sends data to reciever

[Activity](app/src/main/java/p/testtimer/MainActivity.kt) — requests permissions, starts service and displays data


# Libraries used

[Smart Location Library](https://github.com/mrmans0n/smart-location-lib) — to simplify location quering

[PermissionsDispatcher](https://github.com/permissions-dispatcher/PermissionsDispatcher) — to simplify permissions quering
