<div align="center">
    <img src="https://raw.githubusercontent.com/evrencoskun/TableViewSample/master/art/Logo-5.png" >
    <h2>TableView for Android</h2>
    <p align="center">
        <p>TableView is a powerful Android library for displaying complex data structures and rendering tabular data composed of rows, columns and cells. 
           TableView relies on a separate model object to hold and represent the data it displays. This repository also contains a sample app that is
           designed to show you how to create your own TableView in your application.</p>        
        <a href="https://youtu.be/1DWFIqrqrPk">
            <b> Full video »</b>
        </a>
    </p>

</div>

<p align="center">
    <a href="https://youtu.be/1DWFIqrqrPk">
      <img src="https://raw.githubusercontent.com/evrencoskun/TableViewSample/master/art/TableView-0_8_5_1_2.gif">
    </a>
</p>

## Features

  - [x] Each column width value can be calculated automatically considering the largest one.
  - [x] Setting your own model class to be displayed in a table view easily.
  - [x] `TableView` has an action listener interface to listen user touch interaction for each cell.
  - [x] `TableView` columns can be sorted in ascending or descending order.
  - [x] Hiding & showing the rows and columns is pretty easy.
  - [x] Filtering by more than one data.
  - [x] Pagination functionality.

## What's new

You can check new implementations of `TableView` on the [release page](https://github.com/evrencoskun/TableView/releases).
  
## Table of Contents

  - [Installation](#installation)
  - [Documentation](#documentation)
  - [Sample Apps](#sample-apps)
  - [Donations](#donations)
  - [Contributors](#contributors)
  - [License](#license)

## Installation

To use this library in your Android project, just add the following dependency into your module's `build.gradle`:

***Use Jitpack implementation***

1. Check Jitpack use : 
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

2. Add implementation in project build :
```
implementation 'com.github.evrencoskun:TableView:v0.8.9.4'
```

## Documentation 

Please check out the [project's wiki](https://github.com/evrencoskun/TableView/wiki).

## Sample Apps

- This repository has a [sample application](https://github.com/evrencoskun/TableView/tree/master/app) of `TableView`.
- [TableViewSample 2](https://github.com/evrencoskun/TableViewSample2)
- [Xently-UI](https://github.com/ajharry69/Xently-UI)
- [Price List Lite](https://pricelistlite.isolpro.in)
- [Database Client for MySQL and PostgreSQL](https://play.google.com/store/apps/details?id=dev.dhruv.databaseclient)
- ([Submit a Pull Request](https://github.com/evrencoskun/TableView/compare) to mention your app on this page)

## Donations

**This project needs you!** If you would like to support this project's further development, the creator of this project or the continuous maintenance of this project, **feel free to donate**. Your donation is highly appreciated (and I love food, coffee and beer). Thank you!

**PayPal**

- [**Donate 5 $**](https://www.paypal.me/evrencoshkun): Thank's for creating this project, here's a coffee (or some beer) for you!
- [**Donate 10 $**](https://www.paypal.me/evrencoshkun): Wow, I am stunned. Let me take you to the movies!
- [**Donate 15 $**](https://www.paypal.me/evrencoshkun): I really appreciate your work, let's grab some lunch!
- [**Donate 25 $**](https://www.paypal.me/evrencoshkun): That's some awesome stuff you did right there, dinner is on me!
- Or you can also [**choose what you want to donate**](https://www.paypal.me/evrencoshkun), all donations are awesome!

## Contributors

Contributions of any kind are welcome! I would like to thank all the [contributors](https://github.com/evrencoskun/TableView/graphs/contributors) for sharing code and
making `TableView` a better product.

If you wish to contribute to this project, please refer to our [contributing guide](.github/CONTRIBUTING.md).

## License

```
MIT License

Copyright (c) 2021 Evren Coşkun

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
