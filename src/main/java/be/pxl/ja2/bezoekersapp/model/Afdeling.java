package be.pxl.ja2.bezoekersapp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Afdeling {
	@Id
	@GeneratedValue
	private String code;
	private String naam;

	public Afdeling() {
	}

	public Afdeling(String code, String naam) {
		this.code = code;
		this.naam = naam;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}
}
