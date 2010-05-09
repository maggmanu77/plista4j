package com.feistypeach.plista4j;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
		
		Person person1 = new Person();
		person1.setFirstName("Bill");
		person1.setLastName("Gates");
		person1.setDob(new SimpleDateFormat("MM/dd/yyyy").parse("10/28/1955"));
		person1.setAvatar(loadResource("BillGates.png"));
		person1.setLocation(loc);
		person1.setAddress(home);
		person1.setWeight((float)150.60);
		person1.getTags().add("Foo");
		person1.getTags().add("Bar");
		person1.getTags().add("Baz");
		
		
		PListWriter writer = new PListWriter();
		writer.write(person1, new PrintStream(new File("sample.plist")));
		
		PListReader reader = new PListReader();
		Person person2 = reader.read(Person.class, new FileInputStream("sample.plist"));
		
		assertEquals(person1.getFirstName(), person2.getFirstName());
		assertEquals(person1.getLastName(), person2.getLastName());
		assertEquals(person1.getDob(), person2.getDob());
		assertEquals(person1.getAvatar().length, person2.getAvatar().length);
		assertEquals(person1.getWeight(), person2.getWeight());
		assertEquals(person1.getLocation().getLatitude(), person2.getLocation().getLatitude());
		assertEquals(person1.getLocation().getLongitude(), person2.getLocation().getLongitude());
		
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
