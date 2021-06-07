package be.pxl.ja2.bezoekersapp.service;

import be.pxl.ja2.bezoekersapp.dao.BezoekerDao;
import be.pxl.ja2.bezoekersapp.dao.PatientDao;
import be.pxl.ja2.bezoekersapp.model.Bezoeker;
import be.pxl.ja2.bezoekersapp.model.Patient;
import be.pxl.ja2.bezoekersapp.rest.resources.RegistreerBezoekerResource;
import be.pxl.ja2.bezoekersapp.util.exception.BezoekerstijdstipUtil;
import be.pxl.ja2.bezoekersapp.util.exception.BezoekersAppException;
import be.pxl.ja2.bezoekersapp.util.exception.OngeldigTijdstipException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BezoekersService {
    private final PatientDao patientDao;
    private final BezoekerDao bezoekerDao;
    private static final Logger LOGGER = LogManager.getLogger(BezoekersService.class);
    public static final int BEZOEKERS_PER_TIJDSTIP_PER_AFDELING = 2;

    public BezoekersService(PatientDao patientDao, BezoekerDao bezoekerDao) {
        this.patientDao = patientDao;
        this.bezoekerDao = bezoekerDao;
    }

    public Long registreerBezoeker(RegistreerBezoekerResource registreerBezoekerResource) {
        BezoekerstijdstipUtil.controleerBezoekerstijdstip(registreerBezoekerResource.getTijdstip());
        Patient patient = patientDao.findById(registreerBezoekerResource.getPatientCode()).orElse(null);
        if (patient == null) {
            throw new BezoekersAppException("Patiënt " + registreerBezoekerResource.getPatientCode() + " niet gekend");
        }

        Bezoeker existingVisitor = bezoekerDao.findBezoekerByPatient_Code(patient.getCode());
        if (existingVisitor != null) {
            throw new BezoekersAppException("Er is reeds een bezoeker geregistreerd voor patiënt " + patient.getCode());
        }

        List<Bezoeker> visitorsByTimeAndWard = bezoekerDao.findBezoekerByTijdstipAndPatient_Afdeling(registreerBezoekerResource.getTijdstip(), patient.getAfdeling());
        if (visitorsByTimeAndWard.size() >= BEZOEKERS_PER_TIJDSTIP_PER_AFDELING) {
            throw new BezoekersAppException("Kies een ander tijdstip");
        }

        Bezoeker visitor = new Bezoeker();
        visitor.setNaam(registreerBezoekerResource.getNaam());
        visitor.setVoornaam(registreerBezoekerResource.getVoornaam());
        visitor.setTelefoonnummer(registreerBezoekerResource.getTelefoonnummer());
        visitor.setPatient(patient);
        visitor.setTijdstip(registreerBezoekerResource.getTijdstip());
        visitor = bezoekerDao.save(visitor);
        return visitor.getId();
    }

    public void controleerBezoek(Long bezoekerId, LocalDateTime aanmelding) {
        Bezoeker bezoeker = bezoekerDao.findById(bezoekerId).orElse(null);
        if (bezoeker == null) {
            LOGGER.error("Geen bezoeker gevonden met id " + bezoekerId);
            throw new BezoekersAppException("Er ging iets mis");
        }

        if (bezoeker.isReedsAangemeld(aanmelding.toLocalDate())) {
            LOGGER.error("Bezoeker is vandaag reeds aangemeld");
            throw new BezoekersAppException("Bezoeker is vandaag reeds aangemeld");
        }

        BezoekerstijdstipUtil.controleerAanmeldingstijdstip(aanmelding, bezoeker.getTijdstip());
        LOGGER.info("Bezoeker voor " + bezoeker.getPatient().getCode() + " toegelaten");
        bezoeker.setAanmelding(aanmelding);
        bezoekerDao.save(bezoeker);
    }

    public List<Bezoeker> getBezoekersVoorAfdeling(String afdelingCode) {
        return bezoekerDao.findBezoekerByPatient_Afdeling_Code(afdelingCode);
    }
}
