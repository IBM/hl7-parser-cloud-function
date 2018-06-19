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

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.ValidationContextFactory;

import com.google.gson.JsonObject;

/**
 * HL7 file validation using the provided ValidationContext implementations
 * The class can handle any kind of HL7 trigger event, but it extract specific 
 * data type only from ADT and ORU messages
 *
 * INPUT JSON
 * {
 * "hl7":"MSH|^~\\&|SQLAB||SMS||201601081056||ORU^R01|34|P|2.2|151760005618||||||^^^^^\rPID|000|111|8505059^^^AMC^MR|CCC|ICDTENPONE^TWENTY^^^^^|DDD|19810519|M|EEE|FFF|GGG|HHH|||MMM|NNN|OOO|500084652^^^^^\rORC|RE|SO80773465-0|||||^^^201601081054|||||08443^PAUZE, DANIEL K|||||||\rOBR||SO80773465-0||LABTYSCR^TYPE + SCREEN (CONVERT)^L^TYSCR^ - ^LN|||201601081054|||||||201601081054|^|08443^PAUZE, DANIEL K||||F16050||||BB|F|TYSCR^TYSCR|^^^^^R|^~^~||||||| - ||||||||\rOBX|0|ST|%EXX^CROSSMATCH EXPIRATION^L^%EXX^^LN||01/12/2016||||||F|||||^^^^^^^^^^^|\rOBX|0|ST|%ABR^ABO/RH(D)^L^882-1^ABO + Rh group Type in Blood^LN||O`POS||||||F|||||^^^^^^^^^^^|\rOBX|0|ST|%AS^ANTIBODY SCREEN^L^890-4^Blood group antibody screen Presence in Serum or Plasma^LN||NEG||||||F|||||^^^^^^^^^^^|\r"
 * }
 * 
 * OUTPUT JSON for a valid message
 * {
 * "valid":"true",
 * "metadata":{
 * 		"messagetype":"ORU_R01",
 * 		"patientid":"8505059",
 * 		"uniqueplacerid":"SO80773465-0",
 * 		"universalserviceid":"LABTYSCR_TYPE + SCREEN (CONVERT)",
 * 		"observationidentifier":"%EXX_CROSSMATCH EXPIRATION"
 * 		}
 * }
 * 
 */
public class HL7Parser {

	/*
	 * A main method accepting and returning a JsonObject as requested by IBM Cloud function specifications
	 */
	public static JsonObject main(JsonObject obj) {

		JsonObject objOut= new JsonObject();
		JsonObject objMetadata = new JsonObject();
		
		if ( ! obj.isJsonNull()) {
			
			String hl7Message = obj.get("hl7").getAsString();
			
			System.out.println("******START MSG******");
			System.out.println(hl7Message);
			System.out.println("******END MSG******");
	
			/*
			 * We will use a parser with default settings, as defined by the DefaultValidation class
			 */
			HapiContext context = new DefaultHapiContext();
			
			context.setValidationContext(ValidationContextFactory.defaultValidation());	
			
			PipeParser parser = context.getPipeParser();
		
			try {
				Message parsedhl7Message = parser.parse(hl7Message);
			
				System.out.println("******VALID MESSAGE******");
				System.out.println(parsedhl7Message);
				
				/*
				 * Using the Terser class to access hl7 segments and data type for any kind of trigger event
				 */
				 
				Terser terser = new Terser(parsedhl7Message);
				
				objOut.addProperty("valid", "true");
				
				objMetadata.addProperty("messagetype", terser.get("/.MSH-9-1") + "_" + terser.get("/.MSH-9-2"));
					
				String type = terser.get("/.MSH-9-1");
				
				if ( type.equals("ORU") || type.equals("ADT") ) {
					
					objMetadata.addProperty("patientid", terser.get("/.PID-3-1"));
				
					if ( type.equals("ORU")) {
					
						objMetadata.addProperty("uniqueplacerid",terser.get("/.ORC-2-1"));
						objMetadata.addProperty("universalserviceid",terser.get("/.OBR-4-1") + "_" + terser.get("/.OBR-4-2"));	
					}
				
					objMetadata.addProperty("observationidentifier",terser.get("/.OBX-3-1") + "_" + terser.get("/.OBX-3-2"));
				}	
				objOut.add("metadata", objMetadata);
				
			} catch (HL7Exception e) {
				
				objOut.addProperty("error", e.getMessage());
				System.out.println("******PARSING ERROR******");
			}
			
			System.out.println("******START OUTPUT******");
			System.out.println(objOut.toString());
			System.out.println("******END JSON******");
			
			}
		else {
			objOut.addProperty("error", "ERROR: The input json object is null");
			System.out.println("******ERROR: The input json object is null******");
		}
		
		return objOut;
	}

}
