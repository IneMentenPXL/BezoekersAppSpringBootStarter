package be.pxl.ja2.bezoekersapp.dao;

import be.pxl.ja2.bezoekersapp.model.Afdeling;
import be.pxl.ja2.bezoekersapp.model.Bezoeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface BezoekerDao extends JpaRepository<Bezoeker, Long> {
    List<Bezoeker> findBezoekerByPatient_Afdeling_Code(String afdelingsCode);
    Bezoeker findBezoekerByPatient_Code(String patientCode);
    List<Bezoeker> findBezoekerByTijdstipAndPatient_Afdeling(LocalTime tijdstip, Afdeling afdeling);
}
