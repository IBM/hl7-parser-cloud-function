/**
 * Copyright 2017 IBM Corp. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 

package com.hl7.parser.hl7_ingestion_parser;

import com.google.gson.JsonObject;

public class MainHL7Validation {

	public static void main(String[] args) {
		
		String oruMessage = "MSH|^~\\&|SQLAB||SMS||201601081056||ORU^R01|34|P|2.2|151760005618||||||^^^^^\r\n"
		+ "PID|000|111|8505059^^^AMC^MR|CCC|ICDTENPONE^TWENTY^^^^^|DDD|19810519|M|EEE|FFF|GGG|HHH|||MMM|NNN|OOO|500084652^^^^^\r\n"
		+ "ORC|RE|SO80773465-0|||||^^^201601081054|||||08443^PAUZE, DANIEL K|||||||\r\n"
		+ "OBR||SO80773465-0||LABTYSCR^TYPE + SCREEN (CONVERT)^L^TYSCR^ - ^LN|||201601081054|||||||201601081054|^|08443^PAUZE, DANIEL K||||F16050||||BB|F|TYSCR^TYSCR|^^^^^R|^~^~||||||| - ||||||||\r\n"
		+ "OBX|0|ST|%EXX^CROSSMATCH EXPIRATION^L^%EXX^^LN||01/12/2016||||||F|||||^^^^^^^^^^^|\r\n"
		+ "OBX|0|ST|%ABR^ABO/RH(D)^L^882-1^ABO + Rh group Type in Blood^LN||O`POS||||||F|||||^^^^^^^^^^^|\r\n"
		+ "OBX|0|ST|%AS^ANTIBODY SCREEN^L^890-4^Blood group antibody screen Presence in Serum or Plasma^LN||NEG||||||F|||||^^^^^^^^^^^|\r\n";
		
		
		String adtMessage = "MSH|^~\\&|SOARIAN|E_AMC|||20151220101501||ADT^A10|154030586|T|2.4|||||||||\r\n"
		+ "ZSH|SIERX||||\r\n"
		+ "EVN|A10|20151220101501|||||\r\n"
		+ "PID|||8504352^^^^MR||DISCHARGE^ONE^^^^^L||19670324000000|F||D|90 STATE STREET^^EAST GREENBUSH^NY^12061^US^P^^38||^^^^1^465^6565666||ADA|C|NOV|500084629^^^^ECD\r\n"
		+ "PV1||E|EDWR^^EDWR-M65|||||||ED||||||N||ER||||||||||||||||||||||||||20151220101400||||||4388723|||\r\n"
		+ "ZPV|||||||||      ||||||||||||||||||||||||||||||      |      |      |||||||||||||||||^^^^^^^^^^|^^^^^^^^^^^^^^^^||^^UserDefinedString20^^Ambulatory~^^UserDefinedString22^^12/20/2015 10:14~^^UserDefinedString23^^12/20/2015 10:15|||\r\n"
		+ "ZP1||||||||||||||||||||||||||||||||||||^^UserDefinedString8^^E-Emancipated Youth Information Provided||||||||\r\n";
	
		
		JsonObject objIn = new JsonObject();
		
		objIn.addProperty("hl7", oruMessage);
		
		System.out.println(objIn.toString());
		
		HL7Validation messageValidator = new HL7Validation();
		
		JsonObject objOut = new JsonObject();
		
		objOut = messageValidator.validator(objIn);
		
		String outMessage = objOut.toString();
		
		System.out.println(outMessage);
		
	}

}
