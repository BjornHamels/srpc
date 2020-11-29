# srpc

SnowRunner profile copier.

## Reason

When playing SnowRunner with friends who whomever hosts the multiplayer game on a custom map needs to start out with an empty savegame.
Other players that join can select their stored trucks on the custom map.
This copier makes it so the host is no longer penalized by copying all stored trucks over to the new savegame.

## Backups

This program backs up all files it changes. But it is best that you create your own backups before using this tool. (At your own risk I might add!)

## Example usage

Below is an example of the use.

```console
[!!] Found the following savegame files. Please review them carefully!
 1 |CompleteSave.dat |   level_ru_03_01| 21r|   41160xp|   48500$|  16gtrs|
 2 |CompleteSave1.dat|           143692|  1r|       0xp|    5000$|   4gtrs|
 3 |CompleteSave2.dat|   level_us_02_01| 30r|   91680xp|  732250$|  41gtrs|

[??] Input the SOURCE of the profile. Enter the linenumber.
SOURCE: 3

[??] Now input the DESTINATION of the savegame to get the profile form the source. Enter the linenumber.
DESTINATION: 2

[!!] Review the information below. To confirm, enter the number 5985!
SOURCE      |CompleteSave2.dat|   level_us_02_01| 30r|   91680xp|  732250$|  41gtrs|   >---this profile---\   This file is unaffected.
DESTINATION |CompleteSave1.dat|           143692|  1r|       0xp|    5000$|   4gtrs|   <---goes here------/   This file loses the profile.
5985

[!!] Backing up and injecting the profile from source into destination.

[<3] DONE. Press enter.
```

After which the save games look like below. See line 2 compared to above.

```console
 1 |CompleteSave.dat |   level_ru_03_01| 21r|   41160xp|   48500$|  16gtrs|
 2 |CompleteSave1.dat|           143692| 30r|   91680xp|  732250$|  41gtrs|
 3 |CompleteSave2.dat|   level_us_02_01| 30r|   91680xp|  732250$|  41gtrs|
```
