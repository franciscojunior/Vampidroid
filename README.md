## Vampidroid

[![N|Solid](http://www.vekn.net/images/dark_pact.png)](https://whitewolf.box.com/v/DarkPack)

Vampidroid is a mobile management solution for your Vampire: The Eternal Struggle™ card game cards.

With it you will be able to search card text, manage your deck and many other things.


## Material Design

Version 2.0 is a complete remake of the app using the Material Design guidelines. It is currently in BETA stage.

## Play Store

[You can find the current release (2.x) on play store (Beta Testing).](https://play.google.com/store/apps/details?id=name.vampidroid.beta)

[Also, there is the previous version (1.x) on play store.](https://play.google.com/store/apps/details?id=name.vampidroid)

You can have both installed side-by-side. 

## Current limitations

The 2.0 BETA version doens't have all the functionality of the 1.x version yet. So, it may be a good idea to have both installed if you need some features not availabe on 2.x


## How to compile

In order to compile Vampidroid, just open the project in AndroidStudio and select `Build -> Make Project`. All dependencies are already setup.

## How to update the database file
The database is located in the \app\src\main\assets\VampiDroid.mp3 file.

1. [Download and extract the vtescsv archive](http://www.vekn.net/images/stories/downloads/vtescsv_utf8.zip)
2. [Download DB Browser for SQL Lite](https://sqlitebrowser.org/dl/)
3. Run DB Browser for SQL Lite
4. File > Open Database and choose the VampiDroid.mp3 file
5. Delete the tables library and crypt by right-clicking on them and choosing Delete Table
6. File > Import > Table from CSV file
7. Choose the vteslib.csv file for the library table or the vtescrypt.csv file for the crypt table
8. Set the following import values:
   * Table name: library (for vteslib.csv) or crypt (for vtescrypt.csv)
   * Column names in first line: true
   * Field separator: ,
   * Quote character: "
   * Encoding: UTF-8
   * Trim fields: true
9. Click on OK to import the data
10. Right-click on the table created > Modify Table. Answer Save when prompted to save the table.
11. Check the PK checkbox for the line Id and click on OK
12. Repeat steps 6-11 for the other table
13. Tools > Compact Database > select Main and click on OK
14. File > Close Database.
## Copyright notice

Portions of the materials are the copyrights and trademarks of White Wolf Entertainment AB, and are used with permission. All rights reserved. For more information please visit white-wolf.com.


## License

Vampidroid is licensed under GPL2 

See the LICENSE file for the full license text.
