package com.hl7.parser.hl7_ingestion_parser;

import com.google.gson.JsonObject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	String oruMessage = "MSH|^~\\&|SQLAB||SMS||201601081056||ORU^R01|34|P|2.2|151760005618||||||^^^^^\r"
    			+ "PID|000|111|8505059^^^AMC^MR|CCC|ICDTENPONE^TWENTY^^^^^|DDD|19810519|M|EEE|FFF|GGG|HHH|||MMM|NNN|OOO|500084652^^^^^\r"
    			+ "ORC|RE|SO80773465-0|||||^^^201601081054|||||08443^PAUZE, DANIEL K|||||||\r"
    			+ "OBR||SO80773465-0||LABTYSCR^TYPE + SCREEN (CONVERT)^L^TYSCR^ - ^LN|||201601081054|||||||201601081054|^|08443^PAUZE, DANIEL K||||F16050||||BB|F|TYSCR^TYSCR|^^^^^R|^~^~||||||| - ||||||||\r"
    			+ "OBX|0|ST|%EXX^CROSSMATCH EXPIRATION^L^%EXX^^LN||01/12/2016||||||F|||||^^^^^^^^^^^|\r"
    			+ "OBX|0|ST|%ABR^ABO/RH(D)^L^882-1^ABO + Rh group Type in Blood^LN||O`POS||||||F|||||^^^^^^^^^^^|\r"
    			+ "OBX|0|ST|%AS^ANTIBODY SCREEN^L^890-4^Blood group antibody screen Presence in Serum or Plasma^LN||NEG||||||F|||||^^^^^^^^^^^|\r";
    			
    	JsonObject objIn = new JsonObject();
		
		objIn.addProperty("hl7", oruMessage);
		
		System.out.println(objIn.toString());
		
		HL7Parser messageValidator = new HL7Parser();
		
		JsonObject objOut = new JsonObject();
		
		objOut = messageValidator.main(objIn);
		
        assertTrue( objOut.get("valid").getAsString().equals("true"));
        assertTrue( objOut.getAsJsonObject("metadata").get("messagetype").getAsString().equals("ORU_R01"));
        assertTrue( objOut.getAsJsonObject("metadata").get("patientid").getAsString().equals("8505059")); 
        assertTrue( objOut.getAsJsonObject("metadata").get("uniqueplacerid").getAsString().equals("SO80773465-0"));
        assertTrue( objOut.getAsJsonObject("metadata").get("universalserviceid").getAsString().equals("LABTYSCR_TYPE + SCREEN (CONVERT)"));
        assertTrue( objOut.getAsJsonObject("metadata").get("observationidentifier").getAsString().equals("%EXX_CROSSMATCH EXPIRATION"));
    }
    
    public void testAppFailure()
    {
    	String invalidOruMessage = "|^~\\&|SQLAB||SMS||201601081056||ORU^R01|34|P|2.2|151760005618||||||^^^^^\r"
    			+ "PID|000|111|8505059^^^AMC^MR|CCC|ICDTENPONE^TWENTY^^^^^|DDD|19810519|M|EEE|FFF|GGG|HHH|||MMM|NNN|OOO|500084652^^^^^\r"
    			+ "ORC|RE|SO80773465-0|||||^^^201601081054|||||08443^PAUZE, DANIEL K|||||||\r"
    			+ "OBR||SO80773465-0||LABTYSCR^TYPE + SCREEN (CONVERT)^L^TYSCR^ - ^LN|||201601081054|||||||201601081054|^|08443^PAUZE, DANIEL K||||F16050||||BB|F|TYSCR^TYSCR|^^^^^R|^~^~||||||| - ||||||||\r"
    			+ "OBX|0|ST|%EXX^CROSSMATCH EXPIRATION^L^%EXX^^LN||01/12/2016||||||F|||||^^^^^^^^^^^|\r"
    			+ "OBX|0|ST|%ABR^ABO/RH(D)^L^882-1^ABO + Rh group Type in Blood^LN||O`POS||||||F|||||^^^^^^^^^^^|\r"
    			+ "OBX|0|ST|%AS^ANTIBODY SCREEN^L^890-4^Blood group antibody screen Presence in Serum or Plasma^LN||NEG||||||F|||||^^^^^^^^^^^|\r";
    			
    	JsonObject objIn = new JsonObject();
		
		objIn.addProperty("hl7", invalidOruMessage);
		
		System.out.println(objIn.toString());
		
		HL7Parser messageValidator = new HL7Parser();
		
		JsonObject objOut = new JsonObject();
		
		objOut = messageValidator.main(objIn);
		
        assertTrue( objOut.has("error"));
        
    }
    
    public void testAppADT()
    {
    	String adtMessage = "MSH|^~\\&|SOARIAN|E_AMC|||20151220101501||ADT^A10|154030586|T|2.4|||||||||\r\n"
    			+ "ZSH|SIERX||||\r\n"
    			+ "EVN|A10|20151220101501|||||\r\n"
    			+ "PID|||8504352^^^^MR||DISCHARGE^ONE^^^^^L||19670324000000|F||D|90 STATE STREET^^EAST GREENBUSH^NY^12061^US^P^^38||^^^^1^465^6565666||ADA|C|NOV|500084629^^^^ECD\r\n"
    			+ "PV1||E|EDWR^^EDWR-M65|||||||ED||||||N||ER||||||||||||||||||||||||||20151220101400||||||4388723|||\r\n"
    			+ "ZPV|||||||||      ||||||||||||||||||||||||||||||      |      |      |||||||||||||||||^^^^^^^^^^|^^^^^^^^^^^^^^^^||^^UserDefinedString20^^Ambulatory~^^UserDefinedString22^^12/20/2015 10:14~^^UserDefinedString23^^12/20/2015 10:15|||\r\n"
    			+ "ZP1||||||||||||||||||||||||||||||||||||^^UserDefinedString8^^E-Emancipated Youth Information Provided||||||||\r\n";
    		
	JsonObject objIn = new JsonObject();
	
	objIn.addProperty("hl7", adtMessage);
	
	System.out.println(objIn.toString());
	
	HL7Parser messageValidator = new HL7Parser();
	
	JsonObject objOut = new JsonObject();
	
	objOut = messageValidator.main(objIn);
	
    assertTrue( objOut.get("valid").getAsString().equals("true"));
    assertTrue( objOut.getAsJsonObject("metadata").get("messagetype").getAsString().equals("ADT_A10"));
    assertTrue( objOut.getAsJsonObject("metadata").get("patientid").getAsString().equals("8504352")); 
    assertTrue( ! objOut.getAsJsonObject("metadata").get("observationidentifier").getAsString().contains("null"));
    }
}
