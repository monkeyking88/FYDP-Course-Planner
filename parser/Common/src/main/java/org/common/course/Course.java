package org.common.course;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public final class Course implements Serializable, Comparable<Course> {
	private static final long	serialVersionUID	= -706526989894737623L;

	public static final String	NAME_ANY			= "Course";
	public static final String	NAME_NONE			= "none";
	public static final Course	ANY;
	public static final Course	NONE;
	static {
		ANY = new Course(NAME_ANY, "", 0, "", "", "", new BigDecimal("0.0"));
		NONE = new Course(NAME_NONE, "", 0, "", "", "", new BigDecimal("0.0"));
	}

	@JsonProperty("subject")
	private final String		subject;

	@JsonProperty("catalog_number")
	private final String		catalogNumber;

	@JsonSerialize(using = IdSerializer.class)
	@JsonDeserialize(using = IdDeserializer.class)
	@JsonProperty("course_id")
	private final Integer		id;

	@JsonProperty("title")
	private final String		title;

	@JsonProperty("description")
	private final String		description;

	@JsonProperty("academic_level")
	private final String		academicLevel;

	@JsonSerialize(using = UnitsSerializer.class)
	@JsonDeserialize(using = UnitsDeserializer.class)
	@JsonProperty("units")
	private BigDecimal			units;

	public Course() {
		super();
		this.subject = null;
		this.catalogNumber = null;
		this.id = 0;
		this.title = null;
		this.description = null;
		this.academicLevel = null;
		this.units = null;
	}

	public String getName() {
		return subject + catalogNumber;
	}

	public static class IdSerializer extends JsonSerializer<Integer> {
		@Override
		public void serialize(Integer id, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
			// course ids in the def.jon are always 6-digit strings filled with
			// leading 0s, this method does the same
			jgen.writeString(String.format("%06d", id));
		}
	}

	public static class IdDeserializer extends JsonDeserializer<Integer> {
		@Override
		public Integer deserialize(JsonParser parser, DeserializationContext context) throws IOException {
			String idString = parser.getText();
			return Integer.valueOf(idString);
		}
	}

	public static class UnitsSerializer extends JsonSerializer<BigDecimal> {
		@Override
		public void serialize(BigDecimal units, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
			jgen.writeString(units.setScale(5, BigDecimal.ROUND_HALF_UP).toString());
		}
	}

	public static class UnitsDeserializer extends JsonDeserializer<BigDecimal> {
		@Override
		public BigDecimal deserialize(JsonParser parser, DeserializationContext context) throws IOException {
			String unitsString = parser.getText();
			return new BigDecimal(unitsString);
		}
	}

	@Override
	public int compareTo(Course o) {
		if (o == null) {
			throw new NullPointerException(); 	//this is per Java SE7 specification for Comparable<T>
		}
		if (this.equals(o)) {
			return 0;
		}
		if (!this.subject.equals(o.subject)) {
			return this.subject.compareTo(o.subject);
		}
		if (!this.catalogNumber.equals(o.catalogNumber)) {
			return this.catalogNumber.compareTo(o.catalogNumber);
		}
		if (!this.id.equals(o.id)) {
			return this.id.compareTo(o.id);
		}
		if (!this.title.equals(o.title)){
			return this.title.compareTo(o.title);
		}
		if (!this.units.equals(o.units)) {
			return this.units.compareTo(o.units);
		}
		return 0;
	}

	public Course(String subject, String catalogNumber, Integer id, String title, String description, String academicLevel, BigDecimal units) {
		super();
		this.subject = subject;
		this.catalogNumber = catalogNumber;
		this.id = id;
		this.title = title;
		this.description = description;
		this.academicLevel = academicLevel;
		this.units = units;
	}

	public String getSubject() {
		return subject;
	}

	public String getCatalogNumber() {
		return catalogNumber;
	}

	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getAcademicLevel() {
		return academicLevel;
	}

	public BigDecimal getUnits() {
		return units;
	}

	@Override
	public String toString() {
		return "Course [subject=" + subject + ", catalogNumber=" + catalogNumber + ", id=" + id + ", title=" + title + ", description=" + description + ", academicLevel=" + academicLevel + ", units="
				+ units + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((academicLevel == null) ? 0 : academicLevel.hashCode());
		result = prime * result + ((catalogNumber == null) ? 0 : catalogNumber.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((units == null) ? 0 : units.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course other = (Course) obj;
		if (academicLevel == null) {
			if (other.academicLevel != null)
				return false;
		} else if (!academicLevel.equals(other.academicLevel))
			return false;
		if (catalogNumber == null) {
			if (other.catalogNumber != null)
				return false;
		} else if (!catalogNumber.equals(other.catalogNumber))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (units == null) {
			if (other.units != null)
				return false;
		} else if (!units.equals(other.units))
			return false;
		return true;
	}

}
