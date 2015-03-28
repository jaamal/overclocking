package models;

public class TeamMember
{
	private final String fullName;
	private final String birthday;
	private final String email;
	private final String post;
	private final String photoName;

	public TeamMember(String fullName, String birthday, String email, String post, String photoName) {
		this.fullName = fullName;
		this.birthday = birthday;
		this.email = email;
		this.post = post;
		this.photoName = photoName;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public String getBirthday() {
		return birthday;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPost() {
		return post;
	}
	
	public String getPhotoName() {
		return photoName;
	}
}