# Tax Printer Connector

## How to generate a License file

1 - Create a json license request file, an example is provided in the source directory:

```json
{
    "deviceIdentifier": "5979e0a741313127bd669856d24513d8d7503507800c5f69473e677f143b64e7",
    "emissionDate": "2018-02-01",
    "validForDays": 90
}
```

Where deviceIdentifier is the unique hardware identifier shown on the first log when you boot the driver
emissionDate is the day which the license starts counting
validForDays is the number of days that the license will be valid from the emissionDate

2 - Generate a binary license file encrypted from the driver public key:

```bash
cat license-request.json | openssl rsautl -encrypt -pubin -inkey public.pem > license.bin
```

3 - Store the license.bin binary file in the root folder where the driver jar resides

Done.

Notes: The driver will refuse to boot if an invalid license is provided. In the future invalid attempts will be sent
to a telemetry server to monitor misuse and tampering.
