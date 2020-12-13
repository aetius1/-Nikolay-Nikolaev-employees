package longestPeriod;


import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.javatuples.Pair;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

/**
 * @author NN
 *
 *the main class where the logic is implemented
 *basically it reads the lines of the file, puts them in Item object
 *then  checks for each two Item objects if they worked on the same project on for
 *how long.
 *The last step is to sum the times of all pairs who worked on common project and print
 *them
 */
public class MainClass {

	static List<Item> allItems;

	public static void main(String[] args) throws IOException {

		allItems = new ArrayList<Item>();

		Scanner sc = new Scanner(System.in);
		String file = sc.nextLine();
		Scanner scanner = new Scanner(new File(file));

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] items = line.split(",");
			Item currentItem = new Item(items[2], items[3], Integer.parseInt(items[0]), Integer.parseInt(items[1]));
			allItems.add(currentItem);

		}

		scanner.close();
		sc.close();
		// Here we use Guava MultiMap to store all pairs of Employees who worked
		// on common project
		Multimap<Pair<Integer, Integer>, Long> commonTimeOnProjects = getCommonPairsTime(allItems);
		Map<Pair<Integer, Integer>, Long> finalResult = new HashMap<>();

		for (Pair firstName : commonTimeOnProjects.keySet()) {
			@SuppressWarnings("unchecked")
			List<Long> lastNames = (List<Long>) commonTimeOnProjects.get(firstName);
			long sum = lastNames.stream().mapToLong(Long::longValue).sum();
			finalResult.put(firstName, sum);

		}
		// get the maximum time worked from 2 employees on a single project
		Long max = Collections.max(finalResult.values());

		List<Pair<Integer, Integer>> keys = new ArrayList<>();
		for (Entry<Pair<Integer, Integer>, Long> entry : finalResult.entrySet()) {
			if (entry.getValue().longValue() == max.longValue()) {
				keys.add(entry.getKey());
			}
		}
		// prints all pair Employee numbers that have maksimum time worked on a project
		keys.stream().forEach(p -> System.out.println(p.getValue0() + " and the second one " + p.getValue1()));

	}

	// method to get the diferences in days between two sets of end and start dates
	public static long getDateDifference(String startDate, String endDate, String secondStartDate,
			String secondEndDate) {

		// using Natty Library to give us the date regardless of formatting
		Parser parser = new Parser();

		List<DateGroup> secondItemStartDate = parser.parse(secondStartDate);
		List<DateGroup> firstItemEndDate = parser.parse(endDate);
		List<DateGroup> firstItemStartDate = parser.parse(startDate);
		List<DateGroup> secondItemEndDate = parser.parse(secondEndDate);

		Date secondItemStart = secondItemStartDate.get(0).getDates().get(0);
		Date firstItemEnd = firstItemEndDate.get(0).getDates().get(0);
		Date secondItemEnd = secondItemEndDate.get(0).getDates().get(0);
		Date firstItemStart = firstItemStartDate.get(0).getDates().get(0);

		if (firstItemEnd.getTime() <= secondItemStart.getTime()) {

			return 0;
		}

		if (secondItemEnd.getTime() <= firstItemStart.getTime()) {

			return 0;
		}

		if (firstItemStart.getTime() >= (secondItemStart.getTime())
				&& firstItemEnd.getTime() <= secondItemEnd.getTime()) {
			return Math.abs(ChronoUnit.DAYS.between(convertToLocalDateViaInstant(firstItemEnd),
					convertToLocalDateViaInstant(firstItemStart)));
		}
		if (secondItemStart.getTime() >= (firstItemStart.getTime())
				&& secondItemEnd.getTime() <= firstItemEnd.getTime()) {
			return Math.abs(ChronoUnit.DAYS.between(convertToLocalDateViaInstant(secondItemStart),
					convertToLocalDateViaInstant(secondItemEnd)));
		}

		long difference = Math.abs(ChronoUnit.DAYS.between(convertToLocalDateViaInstant(firstItemEnd),
				convertToLocalDateViaInstant(firstItemStart)));

		return difference;

	}

	// the actual implementation of common time worked on a single project

	public static Multimap<Pair<Integer, Integer>, Long> getCommonPairsTime(List<Item> listche) {

		Multimap<Pair<Integer, Integer>, Long> result = MultimapBuilder.hashKeys().arrayListValues().build();
		for (int i = 0; i < listche.size(); i++) {
			for (int j = i + 1; j < listche.size(); j++) {
				if (listche.get(i).getProjectId() == listche.get(j).getProjectId()) {
					Pair<Integer, Integer> pair;
					if (listche.get(i).getEmployeId() > listche.get(j).getEmployeId()) {

						pair = new Pair<Integer, Integer>(listche.get(i).getEmployeId(), listche.get(j).getEmployeId());
					} else {
						pair = new Pair<Integer, Integer>(listche.get(j).getEmployeId(), listche.get(i).getEmployeId());

					}

					// we give current time to all values of NULL
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
					for (Item item : listche) {

						if (item.getEndDate().equals("NULL")) {
							LocalDateTime currentDate = LocalDateTime.now();
							item.setEndDate(dtf.format(currentDate));
						}
						if (item.getStartDate().equals("NULL")) {
							LocalDateTime currentDate = LocalDateTime.now();
							item.setStartDate(dtf.format(currentDate));
						}

					}

					long commonTime = getDateDifference(listche.get(i).getStartDate(), listche.get(i).getEndDate(),
							listche.get(j).getStartDate(), listche.get(j).getEndDate());
					result.put(pair, commonTime);

				}
			}
		}

		return result;

	}

	// Utility method for converting java.util.Date to LocalDate
	public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

}
