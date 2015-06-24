package com.feistypeach.plista4j;

import com.feistypeach.pojos.*;
import junit.framework.TestCase;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class PLMarshallerTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testPListWriter() throws Exception {
        Address home = new Address();
        home.setStreet1("1835 73rd Ave NE Medina");
        home.setCity("King");
        home.setState("WA");
        home.setZipcode("98039");
        home.setCustom(new HashMap<>());
        home.getCustomFields().put("mobile", "415-5551212");
        home.getCustomFields().put("work", "415-5551313");
        home.getCustomFields().put("fax", "415-5551414");

        Location loc = new Location(26.899147, -82.037895);

        Person person1 = new Person();
        person1.setFirstName("Bill");
        person1.setLastName("Gates");
        person1.setDob(new SimpleDateFormat("MM/dd/yyyy").parse("10/28/1955"));
        person1.setAvatar("manu".getBytes());
        person1.setLocation(loc);
        person1.setAddress(home);
        person1.setWeight((float) 150.60);
        person1.setTags(new ArrayList<>());
        person1.getTags().add("Foo");
        person1.getTags().add("Bar");
        person1.getTags().add("Baz");


        PListWriter writer = new PListWriter();
        writer.write(person1, new PrintStream(new File("sample.plist")));

//        System.out.println("---------------------------------------------PRINT READ FILE BEGIN");
//        List<String> strings = IOUtils.readLines(new FileInputStream("sample.plist"));
//        strings.forEach((x) -> System.out.println(x));
//        System.out.println("---------------------------------------------PRINT READ FILE END");

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

    public void testImprovedPListParser() throws Exception {
        Address home = new Address();
        home.setStreet1("1835 73rd Ave NE Medina");
        home.setCity("King");
        home.setState("WA");
        home.setZipcode("98039");
        home.setCustom(new HashMap<>());
        home.getCustomFields().put("mobile", "415-5551212");
        home.getCustomFields().put("work", "415-5551313");
        home.getCustomFields().put("fax", "415-5551414");

        Address nearBy = new Address();
        nearBy.setCity("BillCity");
        nearBy.setState("BillState");
        home.setNearBy(new HashMap<>());
        home.getNearBy().put("one", nearBy);
        nearBy = new Address();
        nearBy.setCity("Gate City");
        nearBy.setState("Gate State");
        home.getNearBy().put("two", nearBy);


        Location loc = new Location(26.899147,-82.037895);

        Person person1 = new Person();
        person1.setFirstName("Bill");
        person1.setLastName("Gates");
        person1.setDob(new SimpleDateFormat("MM/dd/yyyy").parse("10/28/1955"));
        person1.setAvatar(loadResource("BillGates.png"));
        person1.setLocation(loc);
        person1.setAddress(home);
        person1.setWeight((float) 150.60);
        person1.setTags(new ArrayList<>());
        person1.getTags().add("Foo");
        person1.getTags().add("Bar");
        person1.getTags().add("Baz");
        person1.setLocation(loc);

        Person p1 = new Person();
        p1.setFirstName("f1"); p1.setLastName("l1");
        Address addr1 = new Address();
        addr1.setStreet1("f1Street");
        addr1.setCity("f1City");
        addr1.setState("f1State");
        addr1.setZipcode("1001");
        addr1.setCustom(new HashMap<>());
        addr1.getCustomFields().put("f1Mobile", "1234");
        addr1.getCustomFields().put("f1Work", "1678");
        addr1.setNearBy(new HashMap<>());
        nearBy = new Address();
        nearBy.setCity("f1NearCity1");
        nearBy.setState("f1NearState1");
        addr1.getNearBy().put("one", nearBy);
        nearBy = new Address();
        nearBy.setCity("f1NearCity2");
        nearBy.setState("f1NearState2");
        addr1.getNearBy().put("two", nearBy);
        p1.setAddress(addr1);
        person1.setFriends(new ArrayList<>());
        person1.getFriends().add(p1);

        Person p2 = new Person();
        p2.setFirstName("f2"); p2.setLastName("l2");
        Address addr2 = new Address();
        addr2.setStreet1("f2Street");
        addr2.setCity("f2City");
        addr2.setState("f2State");
        addr2.setZipcode("1002");
        addr2.setCustom(new HashMap<>());
        addr2.getCustomFields().put("f2Mobile", "2234");
        addr2.getCustomFields().put("f2Work", "2678");
        addr2.setNearBy(new HashMap<>());
        nearBy = new Address();
        nearBy.setCity("f2NearCity1");
        nearBy.setState("f2NearState1");
        addr2.getNearBy().put("one", nearBy);
        nearBy = new Address();
        nearBy.setCity("f2NearCity2");
        nearBy.setState("f2NearState2");
        addr2.getNearBy().put("two", nearBy);
        p2.setAddress(addr2);
        person1.getFriends().add(p2);

        Person p3 = new Person();
        p3.setFirstName("f3"); p3.setLastName("l3");
        Address addr3 = new Address();
        addr3.setStreet1("f3Street");
        addr3.setCity("f3City");
        addr3.setState("f3State");
        addr3.setZipcode("1003");
        addr3.setCustom(new HashMap<>());
        addr3.getCustomFields().put("f3Mobile", "3234");
        addr3.getCustomFields().put("f3Work", "3678");
        addr3.setNearBy(new HashMap<>());
        nearBy = new Address();
        nearBy.setCity("f3NearCity1");
        nearBy.setState("f3NearState1");
        addr3.getNearBy().put("one", nearBy);
        nearBy = new Address();
        nearBy.setCity("f3NearCity2");
        nearBy.setState("f3NearState2");
        addr3.getNearBy().put("two", nearBy);
        p3.setAddress(addr3);
        person1.getFriends().add(p3);


        PListWriter writer = new PListWriter();
        writer.write(person1, new PrintStream(new File("sample.plist")));

        PListReader reader = new PListReader();
        Person person2 = reader.read(Person.class, new FileInputStream("sample.plist"));

        assertEquals(person1.getFirstName(), person2.getFirstName());
        assertEquals(person1.getLastName(), person2.getLastName());
        assertEquals(person1.getDob(), person2.getDob());
        assertEquals(person1.getWeight(), person2.getWeight());
        assertEquals(person1.getLocation().getLatitude(), person2.getLocation().getLatitude());
        assertEquals(person1.getLocation().getLongitude(), person2.getLocation().getLongitude());
        assertEquals(person1.getAddress().getCustomFields().get("mobile"), person2.getAddress().getCustomFields().get("mobile"));
        assertEquals(person1.getAddress().getCustomFields().get("work"), person2.getAddress().getCustomFields().get("work"));
        assertEquals(person1.getAddress().getCustomFields().get("fax"), person2.getAddress().getCustomFields().get("fax"));
        assertEquals(person1.getAddress().getNearBy().get("one").getCity(), person2.getAddress().getNearBy().get("one").getCity());
        assertEquals(person1.getAddress().getNearBy().get("two").getCity(), person2.getAddress().getNearBy().get("two").getCity());
        assertEquals(person1.getAddress().getNearBy().get("one").getState(), person2.getAddress().getNearBy().get("one").getState());
        assertEquals(person1.getAddress().getNearBy().get("two").getState(), person2.getAddress().getNearBy().get("two").getState());

        for(int i=0; i<3; i++) {
            assertEquals(person1.getFriends().get(i).getFirstName(), person2.getFriends().get(i).getFirstName());
            assertEquals(person1.getFriends().get(i).getLastName(), person2.getFriends().get(i).getLastName());
            assertEquals(person1.getFriends().get(i).getAddress().getStreet1(), person2.getFriends().get(i).getAddress().getStreet1());
            assertEquals(person1.getFriends().get(i).getAddress().getCity(), person2.getFriends().get(i).getAddress().getCity());
            assertEquals(person1.getFriends().get(i).getAddress().getZipcode(), person2.getFriends().get(i).getAddress().getZipcode());
            assertEquals(person1.getFriends().get(i).getAddress().getCustomFields().get("f" + (i+1) + "Mobile"), person2.getFriends().get(i).getAddress().getCustomFields().get("f" + (i+1) + "Mobile"));
            assertEquals(person1.getFriends().get(i).getAddress().getCustomFields().get("f" + (i+1) + "Work"), person2.getFriends().get(i).getAddress().getCustomFields().get("f" + (i+1) + "Work"));
            assertEquals(person1.getFriends().get(i).getAddress().getNearBy().get("one").getCity(), person2.getFriends().get(i).getAddress().getNearBy().get("one").getCity());
            assertEquals(person1.getFriends().get(i).getAddress().getNearBy().get("one").getState(), person2.getFriends().get(i).getAddress().getNearBy().get("one").getState());
            assertEquals(person1.getFriends().get(i).getAddress().getNearBy().get("two").getCity(), person2.getFriends().get(i).getAddress().getNearBy().get("two").getCity());
            assertEquals(person1.getFriends().get(i).getAddress().getNearBy().get("two").getState(), person2.getFriends().get(i).getAddress().getNearBy().get("two").getState());
        }

    }

    /**
     * The plist to be converted has many keys, but the pojo is interested only in a subset of keys. So pojo can have only the keys in which it is interested. Other keys and values are ignored by the library
     */
    public void testIncompletePojo() {
        PListReader reader = new PListReader();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n" +
                "<plist version=\"1.0\">\n" +
                "<dict>\n" +
                "       <key>key1</key>\n" +
                "       <string>946fd849-2b65-45cc-a60b-2ffaa8e8f815</string>\n" +
                "       <key>key2</key>\n" +
                "       <dict>\n" +
                "              <key>key21</key>\n" +
                "              <integer>3</integer>\n" +
                "              <key>key22</key>\n" +
                "              <true/>\n" +
                "              <key>key23</key>\n" +
                "              <true/>\n" +
                "              <key>key24</key>\n" +
                "              <true/>\n" +
                "       </dict>\n" +
                "       <key>key3</key>\n" +
                "       <string>someValue</string>\n" +
                "       <key>UDID</key>\n" +
                "       <string>b1215196263f75c0ac33ed504f4e5132287ae273</string>\n" +
                "       <key>data</key>\n" +
                "       <data>\n" +
                "             aGVsbG8gd\n" +
                "             29ybGQ=\n" +
                "       </data>" +
                "</dict>\n" +
                "</plist>\n";
        CheckRequest request = reader.read(CheckRequest.class, new ByteArrayInputStream(xml.getBytes()));
        assertNotNull(request.getUdid());
        assertEquals("b1215196263f75c0ac33ed504f4e5132287ae273", request.getUdid());
        assertEquals("hello world", new String(request.getData()));
    }

    /**
     * Value of 'testData' can be either &lt;string&gt; or &lt;data&gt;
     */
    public void testMultipleSetters() {
        String plist = "<plist><dict><key>key1</key><string>someValue1</string><key>testData</key><string>aGVsbG8gd29ybGQ=</string><key>key2</key><string>someValue2</string><key>key3</key><string>1.0</string></dict></plist>";
        PListReader reader = new PListReader();
        MultipleSetterRequest request = reader.read(MultipleSetterRequest.class, new ByteArrayInputStream(plist.getBytes()));
        assertNotNull(request.getTestData());
        assertEquals("hello world", new String(request.getTestData()));

        plist = "<plist><dict><key>key1</key><string>someValue1</string><key>testData</key><data>aGVsbG8gd29ybGQ=</data><key>key2</key><string>someValue2</string><key>key3</key><string>1.0</string></dict></plist>";
        request = reader.read(MultipleSetterRequest.class, new ByteArrayInputStream(plist.getBytes()));
        assertNotNull(request.getTestData());
        assertEquals("hello world", new String(request.getTestData()));
    }

    public byte[] loadResource(String resource) throws Exception {
        InputStream in = getClass().getClassLoader().getResourceAsStream("BillGates.png");

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int rd;
        byte[] buf = new byte[200];
        while ((rd = in.read(buf)) > -1) {
            out.write(buf, 0, rd);
        }

        return out.toByteArray();
    }
}
