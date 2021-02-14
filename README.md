# Hospital Management System

For english translation see below. It will be posted eventually.

# About the project

This projects creates a system for updating and tracking changes in the free beds of hospitals and their departments. It allows the hospital administration to easily make changes to their current resources and gives any user (potential client of the hospital) a quick and easy way to determine which hospital has avaliale beds and how many.
This project was made because of the increasing number of people in Bulgaria who have to cycle between 4 or more hospitals before getting accepted or dying. Even the emergency responders have no way of knowing which hospital has avaliable beds.

## Possible clients

This project is targeted towards countries, provinces or states with no present system that would quickly relay this kind of information to both the general public and emergency responders.

## Build with:

* [Google Cloud](https://cloud.google.com/)

* [Firebase](https://firebase.google.com/)

* [Spring](https://spring.io/)

# Getting startd:

This porject is not local and is completely cloud-based.

## Prerequisites

Before using this project you will need a Firebase database and a Google Cloud project.

* Service account
  * Log into Firebase and open your project
  * Go to Project Settings (a cog next to Project Overview)
  * Go to Service Accounts
  * Click generate new private key and download it

# Installation

//TODO

# Usage

Before opening the application at least two firebase collections need to be created - "hospitals", "users" and "audit".

* Hospitals
  * At least the names of the **departments** of the hospitals need to be added as documents.
  * Additionally the names of the parent hospital need to be added as text fields called "hospital".
  * If the hospital has a single department, then the hospital name and department may be the same.
  * (optional) The type of department may be added as a number field mapping to the following:
    * 0: Empty - used when no other number is added
    * 1: Covid Specific - a deparment opened specifically for COVID patients
    * 2: Emergency - the hospital's emergency department
    * 3: Converted - any other department converted for use during the pandemic
  * The other fields may be added and changed through the frontend.
* Users
  * The users (hospital administrators) should be added as documents *using their __emails__*
  * Each user should have 2 text fields:
    * "email" - their email (as a field)
    * "hospital" - the hospital which they are managing
    * (Optional) ID - either their ID in the hospital or their given OAuth2 ID
* Audit
  * This collection may be left empty as the program will generate the rest.
  * __It still has to be created even if left blank__
  
Once the the Spring application is up the following links are of importance:

*for the sake of simplicity the first part of the URL is omitted*

* "/Admin/index.html" - this is the administrator's portal. 
  * Once opened the user will be prompted to log in with their Google account. *Please use the account whose email has been added to the database*
  * If the user logs in with the appropriate email, the application will aoutomatically load the hospital which has been assigned to the user (in the database)
  * That includes all the departments which have been added to that hospital (with the field "hospital" in the database)
  * The user may then alter any of the 3 fields "Total Beds", "Occupied Beds" and "Free Beds". For the ease of the user they can also click "Recalculate Free Beds", which will calculate the free beds from the total and occupied. They may also click "Occupy" or "Free a bed", which changes the values of the occupied and free.
  * *If none of these fields were added to the database, then they will be blank and need to be entered here*
  * Once finished the user must click "Upload Information" to upload the entered information.
* "/User/index.html" - this is a public link and can be accessed by anyone.
  * Once opened the user will **not** be prompted to log in.
  * A list with all departments with free beds (freebeds != 0) will be listed in descending order of free beds.
  * This page should be avaliable to the general public as well as emergency responders.
* "/Util/Audit/index.html" - this is an audit log of all changes to hospitals.
  * Once opened the user will not be prompted to log in, though there is a button that allows them to do so.
  * A list of the last X (10 by default) changes in hospitals will open. 
  * The value X may be adjusted by the user and they can turn to the next page of changes.
  * If the user's email is in the database then the page will also show who made the change. *this is done to preserve personal information*
* "/Util/PermError" - if a user logs in, but their email is not in the database and they attempt to open any sensitive information (the Admin portal or the Audit when logged) they will be sent to this link.
  * Once here the user may log out and try to log in again

All information is stored in Firebase, so restarting the application will not lead to the loss of information.

# License

Distributed under the GNU Affero General Public License. See `LICENSE` for more information.

# Contact

Ivan Spirov - [ispirovjr@gmail.com](ispirovjr@gmail.com)

Project Link: [https://github.com/ispirovjr/Onlympiad2020](https://github.com/ispirovjr/Onlympiad2020)
