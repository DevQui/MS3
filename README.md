# MS3

>>>Summary of the purpose of this repo.
This is an Android Java Application. 
It reads a csv file format and then validates the data. If the row in the csv file has a complete data, meaning no missing values,
the data is considered as VALID and it will be stored to the SQLite database, mylist.db. However, if there are missing values from the row of the csv file, it is considered as INVALID, it will be stored to another data-bad.csv.
After the all data has been read and stored to both the database and the csv, it will record 
the number of data received, the number of complete (success) data, and the number of incomplete (failed) data.

>>>Steps for getting this app running (for fellow developers).
When creating the application, I first started to read the csv file by splitting them by ","(comma) and stored them to a String Array. 
From there, I started to check per row whether it has a missing value.
If a missing value is found, I automatically added them to an Array List so that after reading all data from the given csv file,
I can call the method to create another csv file and store the incomplete data. Those data that are complete were stored in the database.
When the application finished reading all data from the csv file, I then called the method to create the log file where the number
of data received, the number of complete (success) data, and the number of incomplete (failed) data are displayed.

>>>Overview of my approach, design choices, and assumptions.
Breaking down the requirements one by one made it easier for me in creating the application.
For the reading of the csv file, I just logged the data received. When it seems like all data has been read, 
I started storing all database before I tried to validate each data. Since reading the csv file in plain text might lead to
confusions when reading them, I decided to upload the csv file to the Google Drive so that I can view it better using Spreadsheet.
So there's an option whether to save it in Google Drive or to Send it using Gmail.
Basically, I just listed what should be done first, second, third and so on.
It also helped a lot for me that I try my best to finished each requirement before I do something else so that my momentum wouldn't be lost. 
