package be.pxl.ja2.bezoekersapp.dao;

import be.pxl.ja2.bezoekersapp.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BezoekerDaoTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private BezoekerDao bezoekerDao;

    private Afdeling afdeling;

    @BeforeEach
    public void init() {
        afdeling = new Afdeling("A", "Afdeling A");
        testEntityManager.persist(afdeling);
        Patient patient1 = PatientBuilder.aPatient().withCode("P001").withAfdeling(afdeling).build();
        Patient patient2 = PatientBuilder.aPatient().withCode("P002").withAfdeling(afdeling).build();
        testEntityManager.persist(patient1);
        testEntityManager.persist(patient2);
        Bezoeker bezoeker = BezoekerBuilder.aBezoeker().withPatient(patient2).withTijdstip(LocalTime.of(14, 0)).build();
        testEntityManager.persist(bezoeker);
        testEntityManager.flush();
        testEntityManager.clear();
    }

    @Test
    public void returnsBezoekersOpAfdelingVoorTijdstip() {
        List<Bezoeker> bezoekers = bezoekerDao.findBezoekerByTijdstipAndPatient_Afdeling(LocalTime.of(14, 0), afdeling);

        assertNotNull(bezoekers);
        assertEquals(1, bezoekers.size());
        assertEquals(afdeling, bezoekers.get(0).getPatient().getAfdeling());
        assertEquals(LocalTime.of(14, 0), bezoekers.get(0).getTijdstip());
    }

    @Test
    public void returnsGeenBezoekersVoorTijdstipOpAfdeling() {
        List<Bezoeker> bezoekers = bezoekerDao.findBezoekerByTijdstipAndPatient_Afdeling(LocalTime.of(14, 10), afdeling);

        assertNotNull(bezoekers);
        assertTrue(bezoekers.isEmpty());
    }
}
