# ITM_Database_Android
A series of classes that make it easier to create Querys and objects based on database tables. There are three main classes Items, Modals, and Tables. Items are Object classes that can be used with your activitys to get data. Modal Classes are Used to generate these Item classes, Lists of Item Classes or write up any other querys you want to write. Table classes are used for the inital build of the database and to set values for what does and doesn't sync up to your server when you request

### Prerequisites

minSdkVersion 19

## Getting Started
Add dependencies

```
implementation 'com.github.nmc9:ITM_Database_Android:v1.3.2-beta'
```
Note*: This code can be seen in the exmaple app that comes along

It's recommended to create a database folder in your project and inside that folder add three more
1. Item
2. Table
3. Modal
These will hold all your ITM classes.

Next create a subclass of DatabaseHelper and UpgradeHelper
### [Database](https://github.com/nmc9/ITM_Database_Android/wiki/DatabaseHelper)
### [Upgrade](https://github.com/nmc9/ITM_Database_Android/wiki/UpgradeHelper)

Now you can create ITM classes
### [Table](https://github.com/nmc9/ITM_Database_Android/wiki/Table)
### [Item](https://github.com/nmc9/ITM_Database_Android/wiki/Item)
### [Modal](https://github.com/nmc9/ITM_Database_Android/wiki/Modal)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone who's code was used
* Inspiration
* etc
