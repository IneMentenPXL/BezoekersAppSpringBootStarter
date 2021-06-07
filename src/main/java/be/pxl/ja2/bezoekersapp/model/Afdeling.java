package be.pxl.ja2.bezoekersapp.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Afdeling {
	@Id
	private String code;
	private String naam;

	public Afdeling() {
		// JPA only
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Afdeling afdeling = (Afdeling) o;

		return code != null ? code.equals(afdeling.code) : afdeling.code == null;
	}

	@Override
	public int hashCode() {
		return code != null ? code.hashCode() : 0;
	}
}
