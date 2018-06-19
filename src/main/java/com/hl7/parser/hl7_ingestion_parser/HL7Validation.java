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
import ca.uhn.hl7v2.model.v22.message.ORU_R01;
import ca.uhn.hl7v2.model.v22.segment.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

/**
 * HL7 file validation using the provided ValidationContext implementations
 * ORU_R01 v2.2 MessageType implemented
 */
public class HL7Validation {

	public JsonObject validator(JsonObject obj) {

		JsonObject objOut= new JsonObject();
		JsonArray objMetadata = new JsonArray();
		//JsonObject objMetadata = new JsonObject();
		JsonObject objMsh= new JsonObject();
		JsonObject objPid= new JsonObject();
		JsonObject objOrc= new JsonObject();
		JsonObject objObr= new JsonObject();
		JsonObject objObx= new JsonObject();
		
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
				
				Terser terser = new Terser(parsedhl7Message);
				
				objOut.addProperty("valid", "true");
				//objMsh.addProperty("messagetype", terser.get("/.MSH-9-1") + "_" + terser.get("/.MSH-9-2"));
				//objMetadata.addProperty("messagetype", terser.get("/.MSH-9-1") + "_" + terser.get("/.MSH-9-2"));
					
				String type = terser.get("/.MSH-9-1") + "_" + terser.get("/.MSH-9-2");
				
				//if ( type.equals("ORU") || type.equals("ADT") ) {
					
					//objPid.addProperty("patientid", terser.get("/.PID-3-1"));
					//System.out.println("patientid " + terser.get("/.PID-3-1"));
					//objMetadata.addProperty("patientid", terser.get("/.PID-3-1"));
				
				if ( type.equals("ORU_R01")) {
					
					ORU_R01 oruMessage = (ORU_R01) parsedhl7Message;
					MSH msh = oruMessage.getMSH();
					PID pid = oruMessage.getPATIENT_RESULT().getPATIENT().getPID();
					ORC orc = oruMessage.getPATIENT_RESULT().getORDER_OBSERVATION().getORC();
					OBR obr = oruMessage.getPATIENT_RESULT().getORDER_OBSERVATION().getOBR();
					OBX obx = oruMessage.getPATIENT_RESULT().getORDER_OBSERVATION().getOBSERVATION().getOBX();	
									
					if (msh != null) {
						objMsh.addProperty("messagetype", msh.getMsh9_MessageType().getCm_msg1_MessageType().getValue() + "_" + msh.getMsh9_MessageType().getCm_msg2_TriggerEvent().getValue());
						objMetadata.add(objMsh); 
					}
					
					if (pid != null) {
						objPid.addProperty("patientid", pid.getPatientIDInternalID(0).getCm_pat_id1_IDNumber().getValue());
						objMetadata.add(objPid);
					}
					
					//objOrc.addProperty("uniqueplacerid",terser.get("/.ORC-2-1"));
					//objMetadata.addProperty("uniqueplacerid",terser.get("/.ORC-2-1"));
					//objObr.addProperty("universalserviceid",terser.get("/.OBR-4-1") + "_" + terser.get("/.OBR-4-2"));
					//objMetadata.addProperty("universalserviceid",terser.get("/.OBR-4-1") + "_" + terser.get("/.OBR-4-2"));
					if (orc != null) {
						objOrc.addProperty("uniqueplacerid", orc.getOrc2_PlacerOrderNumber().getCm_placer1_UniquePlacerId().getValue());
						objMetadata.add(objOrc);
					}
					
					if (obr != null) {
						objObr.addProperty("universalserviceid", obr.getUniversalServiceID().getCe1_Identifier().getValue());
						objMetadata.add(objObr);
					}
					
				//}
				
				//objObx.addProperty("observationidentifier",terser.get("/.OBX-3-1") + "_" + terser.get("/.OBX-3-2"));	
				//objMetadata.addProperty("observationidentifier",terser.get("/.OBX-3-1") + "_" + terser.get("/.OBX-3-2"));
					
					 if (obx != null) {
						objObx.addProperty("observationidentifier", obx.getObx3_ObservationIdentifier().getCe2_Text().getValue());
						objMetadata.add(objObx);
					}
					
					objOut.add("metadata", objMetadata);
				}	
				
				
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




