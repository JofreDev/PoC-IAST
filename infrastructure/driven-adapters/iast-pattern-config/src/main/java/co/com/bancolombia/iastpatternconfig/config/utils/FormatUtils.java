package co.com.bancolombia.iastpatternconfig.config.utils;

import co.com.bancolombia.model.validation.exceptions.TechnicalException;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import static co.com.bancolombia.model.validation.exceptions.message.TechnicalErrorMessage.INVALID_DATE;
import static co.com.bancolombia.model.validation.exceptions.message.TechnicalErrorMessage.INVALID_NUMBER;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class FormatUtils {

    /// DecimalFormatSymbols-> Define los formatos numericos que peuden ser usados
    public static final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);

    public static String getNumberWithFormat(String valueOfJson, String format, boolean removeDot) {
        try {
            var number = new DecimalFormat(format, symbols).format(Double.parseDouble(valueOfJson));
            return removeDot ? number.replace(".", "") : number;
        } catch (IllegalArgumentException exception) {
            throw new TechnicalException(INVALID_NUMBER, exception);
        }
    }

    public static String getDateWithFormat(String valueOfJson, String format) {
        try {
            var dateTime = LocalDateTime.parse(valueOfJson, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            return DateTimeFormatter.ofPattern(format).format(dateTime);
        } catch (DateTimeParseException | IllegalArgumentException exception) {
            throw new TechnicalException(INVALID_DATE, exception);
        }
    }
}
