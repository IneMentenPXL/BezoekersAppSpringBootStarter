package be.pxl.ja2.bezoekersapp.service;

import be.pxl.ja2.bezoekersapp.dao.BezoekerDao;
import be.pxl.ja2.bezoekersapp.dao.PatientDao;
import be.pxl.ja2.bezoekersapp.model.Bezoeker;
import be.pxl.ja2.bezoekersapp.model.Patient;
import be.pxl.ja2.bezoekersapp.model.PatientBuilder;
import be.pxl.ja2.bezoekersapp.rest.resources.RegistreerBezoekerResource;
import be.pxl.ja2.bezoekersapp.util.exception.BezoekersAppException;
import be.pxl.ja2.bezoekersapp.util.exception.OngeldigTijdstipException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BezoekerServiceRegistreerBezoekerTest {
    private static final String PATIENT_CODE = "P001";

    @Mock
    private BezoekerDao bezoekerDao;

    @Mock
    private PatientDao patientDao;

    @InjectMocks
    private BezoekersService bezoekersService;

    @Captor
    private ArgumentCaptor<Bezoeker> bezoekerArgumentCaptor;

    private RegistreerBezoekerResource registreerBezoekerResource;

    @BeforeEach
    public void init() {
        registreerBezoekerResource = new RegistreerBezoekerResource();
        registreerBezoekerResource.setNaam("Vanaken");
        registreerBezoekerResource.setVoornaam("Max");
        registreerBezoekerResource.setTelefoonnummer("0489787458");
        registreerBezoekerResource.setTijdstip(LocalTime.of(15, 30));
        registreerBezoekerResource.setPatientCode(PATIENT_CODE);
    }


    @Test
    public void throwsBezoekersAppExceptionWhenPatientCodeInvalid() {
        when(patientDao.findById(PATIENT_CODE)).thenReturn(Optional.empty());
        assertThrows(BezoekersAppException.class, () -> bezoekersService.registreerBezoeker(registreerBezoekerResource));
    }

    @Test
    public void throwsBezoekersAppExceptionWhenPatientAlreadyHasVisitor() {
        when(patientDao.findById(PATIENT_CODE)).thenReturn(Optional.of(PatientBuilder.aPatient().withCode(PATIENT_CODE).build()));
        when(bezoekerDao.findBezoekerByPatient_Code(PATIENT_CODE)).thenReturn(new Bezoeker());
        assertThrows(BezoekersAppException.class, () -> bezoekersService.registreerBezoeker(registreerBezoekerResource));
    }

    @Test
    public void bezoekerIsSavedCorrectly() throws BezoekersAppException, OngeldigTijdstipException {
        Patient patient = PatientBuilder.aPatient().withCode(PATIENT_CODE).build();
        when(patientDao.findById(PATIENT_CODE)).thenReturn(Optional.of(patient));
        when(bezoekerDao.findBezoekerByPatient_Code(PATIENT_CODE)).thenReturn(null);
        when(bezoekerDao.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        bezoekersService.registreerBezoeker(registreerBezoekerResource);

        verify(bezoekerDao).save(bezoekerArgumentCaptor.capture());
        Bezoeker bezoeker = bezoekerArgumentCaptor.getValue();

        assertEquals(patient, bezoeker.getPatient());
        assertEquals(registreerBezoekerResource.getTijdstip(), bezoeker.getTijdstip());
        assertNull(bezoeker.getAanmelding());
        assertEquals(registreerBezoekerResource.getVoornaam(), bezoeker.getVoornaam());
        assertEquals(registreerBezoekerResource.getNaam(), bezoeker.getNaam());
    }
}
