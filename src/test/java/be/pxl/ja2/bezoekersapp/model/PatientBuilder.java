package be.pxl.ja2.bezoekersapp.model;

import java.time.LocalDateTime;

public final class PatientBuilder {
    private String code;
    private LocalDateTime opname;
    private Afdeling afdeling;

    public PatientBuilder() {
    }

    public static PatientBuilder aPatient() {
        return new PatientBuilder();
    }

    public PatientBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public PatientBuilder withOpname(LocalDateTime opname) {
        this.opname = opname;
        return this;
    }

    public PatientBuilder withAfdeling(Afdeling afdeling) {
        this.afdeling = afdeling;
        return this;
    }

    public Patient build() {
        Patient patient = new Patient();
        patient.setAfdeling(afdeling);
        patient.setCode(code);
        patient.setOpname(opname);
        return patient;
    }
}
