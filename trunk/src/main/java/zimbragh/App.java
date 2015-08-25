package zimbragh;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.DtEnd;

/**
 * Przykładowa aplikacja pokazująca wykorzystanie delegata biznesowego
 */

public class App {
	/**
	 * Aplikacja tworzy nowy obiekt delegata biznesowego podając adres serwera testowego
	 * oraz dane stworzonego wcześniej użytkownika.
	 * Następnie wypisuje wszystkie kontakty z książki adresowej tego użytkownika, dodaje nową pozycję
	 * i powtarza wypisywanie. Później pobiera kalendarz użytkownika, do którego dodaje nowy wpis. 
	 */
	public static void main(String[] args) throws IOException, ParserException {
		ZimbraDelegate zimbra = new ZimbraDelegate("testzimbra.com", "zm916ejgis", "oflvfq");
		System.out.println("contact list:");
		System.out.println(zimbra.getContacts());
		zimbra.putContact("jan@kowalski.com", getSampleContact());
		System.out.println(zimbra.getContacts());

		Calendar calendar = zimbra.getCalendar();
		playWith(calendar);
		System.out.println(calendar);
		zimbra.setCalendar(calendar);
	}

	private static Contact getSampleContact() {
		Map<String, String> infoMap = new HashMap<String,String>();
		infoMap.put("firstName", "Jan");
		infoMap.put("lastName", "Kowalski");
		return new Contact(infoMap);
	}

	private static void playWith(Calendar calendar) {
		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		TimeZone timezone = registry.getTimeZone("Europe/Warsaw");

		java.util.Calendar cal = java.util.Calendar.getInstance(timezone);
		cal.set(java.util.Calendar.YEAR, 2009);
		cal.set(java.util.Calendar.MONTH, java.util.Calendar.SEPTEMBER);
		cal.set(java.util.Calendar.DAY_OF_MONTH, 28);
		cal.set(java.util.Calendar.HOUR_OF_DAY, 15);
		cal.clear(java.util.Calendar.MINUTE);
		cal.clear(java.util.Calendar.SECOND);

		DateTime dt = new DateTime(cal.getTime());
		dt.setTimeZone(timezone);
		cal.set(java.util.Calendar.HOUR_OF_DAY, 16);
		DateTime dt2 = new DateTime(cal.getTime());
		dt2.setTimeZone(timezone);

		VEvent melbourneCup = new VEvent(dt, "Melbourne Cup");
		melbourneCup.getProperties().add(new DtEnd(dt2));

		calendar.getComponents().add(melbourneCup);
	}   
}
