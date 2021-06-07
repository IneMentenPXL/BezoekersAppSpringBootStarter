package be.pxl.ja2.bezoekersapp.util.exception;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class BezoekerstijdstipUtil {
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
	public static final LocalTime STARTTIJD = LocalTime.of(8, 30);
	public static final LocalTime EINDTIJD = LocalTime.of(19, 30);

	public static void controleerBezoekerstijdstip(LocalTime bezoekerstijdstip) throws OngeldigTijdstipException {
		if (bezoekerstijdstip.isBefore(STARTTIJD)) {
			throw new OngeldigTijdstipException("Het gekozen tijdstip moet na " + TIME_FORMATTER.format(bezoekerstijdstip));
		}
		if (bezoekerstijdstip.isAfter(EINDTIJD)) {
			throw new OngeldigTijdstipException("Het gekozen tijdstip moet voor " + TIME_FORMATTER.format(bezoekerstijdstip));
		}
		if (bezoekerstijdstip.getMinute() % 10 != 0) {
			throw new OngeldigTijdstipException("Het gekozen tijdstip kan enkel per 10 minuten");
		}
	}


	public static void controleerAanmeldingstijdstip(LocalDateTime aanmeldingstijdstip, LocalTime geregistreerdTijdstip) throws OngeldigTijdstipException {
		LocalDateTime geldigAanmeldingstijdstip = LocalDateTime.of(aanmeldingstijdstip.toLocalDate(), geregistreerdTijdstip);
		LocalDateTime kwartierVoor = geldigAanmeldingstijdstip.minusMinutes(15);
		LocalDateTime kwartierNa = geldigAanmeldingstijdstip.plusMinutes(15);

		if (aanmeldingstijdstip.isBefore(kwartierVoor)) {
			throw new OngeldigTijdstipException("U bent te vroeg. Wacht nog even buiten.");
		}
		if (aanmeldingstijdstip.isAfter(kwartierNa)) {
			throw new OngeldigTijdstipException("U bent te laat. Wend u tot de bezoekersbalie.");
		}
	}

}
