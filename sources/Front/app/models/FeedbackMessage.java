package models;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name="Feedback")
public class FeedbackMessage implements Serializable {
    private static final long serialVersionUID = 5029341666511429262L;
    
    private UUID id;
	private String from;
	private String message;
	private String respondTo;
	
	public FeedbackMessage(String from, String message, String respondTo){
		id = UUID.randomUUID();
		this.from = from;
		this.message = message;
		this.respondTo = respondTo;
	}
	
	//NOTE: constructor for DBDriver
	public FeedbackMessage() {}
	
	@Id
	@Type(type = "uuid-binary")
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name = "id", unique = true, nullable = false, insertable = true, updatable = false, length = 32, precision = 0)
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Column(name="_from", length=50, nullable = false)
	public String getFrom(){
		return from;
	}
	
	public void setFrom(String from){
		this.from = from;
	}
	
	@Column(name="message", length=1024, nullable = false)
	public String getMessage(){
		return message;
	}
	
	public void setMessage(String message){
		this.message = message;
	}
	
	@Column(name="respondTo", length=50, nullable = true)
	public String getRespondTo(){
		return respondTo;
	}
	
	public void setRespondTo(String respondTo){
		this.respondTo = respondTo;
	}
}
