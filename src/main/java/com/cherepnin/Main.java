package com.cherepnin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws ParseException {
        String sourceFileName = args[0];
        String resultFileName = args[1];

        Map<String, List<Ticket>> mappedFile = readFile(sourceFileName);

        List<Ticket> tickets = selectionByFlight(mappedFile, "VVO", "TLV");
        List<Long> flightsDuration = getFlightsDuration(tickets);

        String averageFlightTime = getAverageFlightTime(flightsDuration);
        String percentileFlightTime = getPercentileFlightTime(90, flightsDuration);

        writeToFile(resultFileName, averageFlightTime, percentileFlightTime);
    }

    private static String getAverageFlightTime (List<Long> allFlightsDuration) {
        long totalFlightTime = allFlightsDuration
                .stream()
                .reduce((long) 0, Long::sum);

        long averageFlightTime = totalFlightTime / allFlightsDuration.size();

        return (new SimpleDateFormat("hh:mm:ss")).format(new Date(averageFlightTime));
    }

    private static String getPercentileFlightTime(int percentile, List<Long> flightsDuration) {
        Collections.sort(flightsDuration);

        int index = (int) Math.ceil(percentile / 100.0 * flightsDuration.size());
        long result = flightsDuration.get(index - 1);

        return (new SimpleDateFormat("hh:mm:ss")).format(new Date(result));
    }


    private static Map<String, List<Ticket>> readFile(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<Ticket>> object;

        try {
            object = mapper.readValue(new File(filePath), new TypeReference <>() {});

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    private static void writeToFile(String fileName, String averageFlightTime, String percentileFlightTime) {
        try(FileWriter writer = new FileWriter(fileName, false)) {

            writer.write("Average flight time: " + averageFlightTime + "\n");
            writer.write("90th percentile of flight time: " + percentileFlightTime);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Ticket> selectionByFlight(Map<String, List<Ticket>> file, String origin, String destination) {
        List<Ticket> tickets = new ArrayList<>();

        for (Map.Entry<String, List<Ticket>> entry : file.entrySet()) {
            if (entry.getKey().equals("tickets")) {
                tickets = entry.getValue();
            }
        }

        tickets.stream()
                .filter(ticket -> ticket.getOrigin().equals(origin))
                .filter(ticket -> ticket.getDestination().equals(destination))
                .collect(Collectors.toList());

        return tickets;
    }

    private static List<Long> getFlightsDuration(List<Ticket> tickets) throws ParseException {
        Date departure;
        Date arrival;
        long flightDuration;

        List<Long> allFlightsDuration = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm");

        for (Ticket ticket : tickets) {
            departure = dateFormat.parse(ticket.getDeparture_date() + " " + ticket.getDeparture_time());
            arrival = dateFormat.parse(ticket.getArrival_date() + " " + ticket.getArrival_time());
            flightDuration = arrival.getTime() - departure.getTime();

            allFlightsDuration.add(flightDuration);
        }

        return allFlightsDuration;
    }
}
