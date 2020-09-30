package com.babysitter.rateCalculator;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class RateCalculator {

	private final static LocalTime OFFICIAL_START = LocalTime.of(17, 00);
	private final static LocalTime OFFICIAL_END = LocalTime.of(4, 00);

	public static void main(String[] args) {

		EmpContactInformation contactInformation = new EmpContactInformation();

		// "The program is designed to calculate pay rate for hours logged in different
		// times during the shift. Each change "

		contactInformation.setName("Name: Betty'\n'"); // This line of code is included to show
		// the program can display employee's information also.
		System.out.println(contactInformation.getName());

		System.out.println(payCalculator("05:00 PM", "04:00 AM", "09:00 PM"));// 136 All shift hours
		System.out.println(payCalculator("04:00 PM", "04:00 AM", "09:00 PM"));// 136 Early clock in + all shift hours

		System.out.println(payCalculator("06:00 PM", "06:00 AM", "09:00 PM"));// 124 Start -1 & clock out + 2 hours
		System.out.println(payCalculator("06:00 PM", "11:00 PM", "09:00 PM"));// 52 Start -1 +2 hours bed time

		System.out.println(payCalculator("06:00 PM", "09:00 PM", "09:00 PM"));// 36 Start-1 to bed time
		System.out.println(payCalculator("06:00 PM", "08:00 PM", "09:00 PM"));// 24 two hours start time

		System.out.println(payCalculator("06:00 PM", "01:00 AM", "09:00 PM"));// 76 start-1 + Midnight + 1 hours
		System.out.println(payCalculator("06:00 PM", "12:00 AM", "09:00 PM"));// 60 start-1 + bed time hours
	}

	public static String payCalculator(String startTime, String endTime, String bedTime) {
		LocalTime start = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("hh:mm a"));
		LocalTime end = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("hh:mm a"));
		LocalTime bed = LocalTime.parse(bedTime, DateTimeFormatter.ofPattern("hh:mm a"));

		if (start.isBefore(OFFICIAL_START)) {
			System.out.println("Clocked in before 5 PM. Adjusted to 5 PM");
			start = OFFICIAL_START;
		}

		if (end.isAfter(OFFICIAL_END) && end.isBefore(OFFICIAL_START)) {
			System.out.println("Clockedout after 4 AM. Adjusted to 4 AM");
			end = OFFICIAL_END;
		}

		double total = 0.0;
		if ((OFFICIAL_END.isBefore(end) && end.isBefore(bed)) || end.equals(bed)) {
			return String.format("$%.2f", 12 * Math.ceil(ChronoUnit.MINUTES.between(start, end) / 60.0));
		}
		total += 12 * Math.ceil(ChronoUnit.MINUTES.between(start, bed) / 60.0);
		LocalTime temp = OFFICIAL_END.isBefore(end) ? end : LocalTime.MAX;
		total += 8 * Math.ceil(ChronoUnit.MINUTES.between(bed, temp) / 60.0);
		if (end.isAfter(LocalTime.MIDNIGHT) && !end.isAfter(OFFICIAL_END)) {
			total += 16 * Math.ceil(ChronoUnit.MINUTES.between(LocalTime.MIDNIGHT, end) / 60.0);
		}
		return String.format("$%.2f", total);
	}

}
