package com.feistypeach.plista4j;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;

import junit.framework.TestCase;

import com.feistypeach.pojos.Address;
import com.feistypeach.pojos.Location;
import com.feistypeach.pojos.Person;

public class PLMarshallerTest extends TestCase {
	
	@SuppressWarnings("unchecked")
	public void testPListWriter() throws Exception {
		Address home = new Address();
		home.setStreet1("1835 73rd Ave NE Medina");
		home.setCity("King");
		home.setState("WA");
		home.setZipcode("98039");
		home.getCustomFields().put("mobile", "415-5551212");
		home.getCustomFields().put("work", "415-5551313");
		home.getCustomFields().put("fax", "415-5551414");
		
		Location loc = new Location(26.899147,-82.037895);
		
		Person rodney = new Person();
		rodney.setFirstName("Bill");
		rodney.setLastName("Gates");
		rodney.setDob(new SimpleDateFormat("MM/dd/yyyy").parse("10/28/1955"));
		rodney.setAvatar(loadResource("BillGates.png"));
		rodney.setLocation(loc);
		rodney.setAddress(home);
		rodney.setWeight((float)150.60);
		rodney.getTags().add("Foo");
		rodney.getTags().add("Bar");
		rodney.getTags().add("Baz");
		
		
		PListWriter writer = new PListWriter();
		writer.write(rodney, new PrintStream(new File("sample.plist")));
	}
	
	public byte[] loadResource(String resource) throws Exception {
		InputStream in = getClass().getResourceAsStream("BillGates.png");
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		int rd;
		byte[] buf = new byte[200];
		while ((rd = in.read(buf)) > -1) {
			out.write(buf, 0, rd);
		}
		
		return out.toByteArray();
	}
}
