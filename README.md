# Tax Printer Connector

## Motivation

A tax printing device is a government issued device to collect businesses billing and tax information. I developed this as an easy way to interact with those printers from any http client (even CURL or your own browser) so you can easily print invoices from your web or desktop application.

## Extending

Create a new package under com.taxprinter.driver and implement the TaxPrinterDriver interface, then change the
loaded implementation on the main Guice module for dependency injection, that's it. I used dropwizard because
that was the only sane http micro framework I knew back then when I started this, feel free to port this to
SpringBoot, I think it can be easily done, I tried to use JEE APIs as much as I could.

## Supported devices

* BixolonSRP350

* Epsonxxx <- WIP and will be supported in a future release (I don't have a date for that).

## Configuration

Check the taxprinter.yml config, change the port number to match the port used by your device, Windows, Mac OS,
and Linux are supported (may require assign port access permissions to your current user).

## Build

* Java 8+ is required
* Run ./gradlew uberJar
* Find the executable fat jar on the build folder

## Notice

This interface is not an official nor endorsed Government Tax Collection Agency (DGII) product.
I wouldn't be in any way liable or responsible to any Obligor who is a Party or is a party to a Finance Document
to which this clause applies for any loss or liability arising from any act, default, omission or misconduct
due to the usage of this source code. Ask your local DGII representative for license or permissions to interface
with a government issued printer device.
