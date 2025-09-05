package com.nikhil.inventory.adaptiveinventoryforecastingservice.models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class DateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();

        if (text == null || text.isBlank()) {
            return null;
        }

        // Try multiple formats
        String[] patterns = {
            "yyyy-MM-dd",    // 2025-07-15
            "MMM d, yyyy",   // Jul 15, 2025
            "MMMM d, yyyy",  // July 15, 2025
            "MMMM d",        // July 15
            "dd/MM/yyyy"     // 15/07/2025
        };

        for (String pattern : patterns) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH);
                LocalDate date = LocalDate.parse(text.replaceAll("(st|nd|rd|th)", ""), formatter);

                // If no year in pattern (like "July 15"), assume current year
                if (!pattern.contains("y")) {
                    return date.withYear(LocalDate.now().getYear());
                }
                return date;
            } catch (DateTimeParseException ignored) {}
        }

        throw new RuntimeException("Unrecognized date format: " + text);
    }
}
