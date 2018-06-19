# Java Cloud Function for HL7 file parsing and data type extraction: hl7-parser-cloud-function

This directory contains a simple IBM Cloud Function in Java, HL7Parser, that uses the HAPI library to handle HL7 file format of whatever trigger event type. It validates, using a default validation rules set the file format, the HL7 input and extracts some data type belonging to different file segment. In addition there are two more classes, HL7Validation and MainHL7Validation, implementing a main class that instantiates and calls the other one, that uses a little bit more specific HAPI library classes related to the ORU_R01 HL7 trigger event.

## Java class input parameter:
 * `JsonObject` The input JSON Object required by IBM Cloud Specifications about Java Cloud Functions and actions. Refer to IBM Cloud documentation for details at this link: https://console.bluemix.net/docs/openwhisk/openwhisk_actions.html#creating-java-actions.
 * `JsonObject` sample:
 {
  "hl7" : "MSH|^~\\&|SQLAB||SMS||201601081056||ORU^R01|34|P|2.2|151760005618||||||^^^^^\r\nPID|000|111|8505059^^^AMC^MR|CCC|ICDTENPONE^TWENTY^^^^^|DDD|19810519|M|EEE|FFF|GGG|HHH|||MMM|NNN|OOO|500084652^^^^^\r\nORC|RE|SO80773465-0|||||^^^201601081054|||||08443^PAUZE, DANIEL K|||||||\r\nOBR||SO80773465-0||LABTYSCR^TYPE + SCREEN (CONVERT)^L^TYSCR^ - ^LN|||201601081054|||||||201601081054|^|08443^PAUZE, DANIEL K||||F16050||||BB|F|TYSCR^TYSCR|^^^^^R|^~^~||||||| - ||||||||\r\nOBX|0|ST|%EXX^CROSSMATCH EXPIRATION^L^%EXX^^LN||01/12/2016||||||F|||||^^^^^^^^^^^|\r\n"
 }
 * \n is not supported as HL7 segments separator. We use \r instead. 

## Java class output parameter:
* `JsonObject` The output JSOn Object required by IBM Cloud Specifications about Java Cloud Functions and actions.Refer to IBM Cloud documentation for details at this link: https://console.bluemix.net/docs/openwhisk/openwhisk_actions.html#creating-java-actions.
* `JsonObject` sample in case of a hl7 message parsed as valid of ORU_R01 trigger event type. Data type are extracted from the MSH (messagetype), PID (patientid), ORC (uniqueplacerid), OBR (universalserviceid) and OBX (observationidentifier) segments.
{
 "valid":"true",
  "metadata":
  {
  		"messagetype":"ORU_R01",
  		"patientid":"8505059",
  		"uniqueplacerid":"SO80773465-0",
  		"universalserviceid":"LABTYSCR_TYPE + SCREEN (CONVERT)",
  		"observationidentifier":"%EXX_CROSSMATCH EXPIRATION"
  }
}
* `JsonObject` sample in case of a hl7 message parsed as invalid:
{
 "error":"HL7Exception message"
}
* `JsonObject` sample in case of an empty hl7 message:
{
 "error":"ERROR: The input json object is null"
}

## 1. Register the JAVA cloud function called hl7parser inside your IBM Cloud org and space using the IBM Cloud CLI
* Run command:
  bx wsk action create hl7parser hl7parser.jar --main com.hl7.parser.hl7_ingestion_parser.HL7Parser
* Now you can see and invoke the action from the IBM Cloud UI.
* Java actions cannot be created with the IBM Cloud UI.

 * IBM Cloud CLI [download](https://console.bluemix.net/docs/cli/reference/bluemix_cli/download_cli.html#download_install)
 * Cloud functions [CLI plug-in](https://console.bluemix.net/docs/openwhisk/bluemix_cli.html#cloudfunctions_cli)
 * Licensed under the Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
