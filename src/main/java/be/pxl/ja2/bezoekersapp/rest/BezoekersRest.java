package be.pxl.ja2.bezoekersapp.rest;

import be.pxl.ja2.bezoekersapp.rest.resources.RegistreerBezoekerResource;
import be.pxl.ja2.bezoekersapp.service.BezoekersService;
import be.pxl.ja2.bezoekersapp.util.exception.BezoekersAppException;
import be.pxl.ja2.bezoekersapp.util.exception.OngeldigTijdstipException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping(path = "bezoekers")
public class BezoekersRest {
    private final BezoekersService bezoekersService;
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHH:mm");
    private static final Logger LOGGER = LogManager.getLogger(BezoekersRest.class);

    public BezoekersRest(BezoekersService bezoekersService) {
        this.bezoekersService = bezoekersService;
    }

    @PostMapping
    public ResponseEntity<Long> registerVisitor(@RequestBody @Valid RegistreerBezoekerResource bezoekerResource) {
        Long result = bezoekersService.registreerBezoeker(bezoekerResource);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("{bezoekerId}/{entranceTimeStamp}")
    public ResponseEntity<Void> checkVisitor(@PathVariable("bezoekerId") Long bezoekerId, @PathVariable("entranceTimeStamp") String entranceTimeStamp) {
        bezoekersService.controleerBezoek(bezoekerId, LocalDateTime.parse(entranceTimeStamp, TIMESTAMP_FORMAT));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
