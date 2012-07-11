package unal.edu.co.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the commit database table.
 * 
 */
@Entity
@Table(name="commit")
public class Commit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String changeset;

	private String author;

	@Column(name="commit_date")
	private Timestamp commitDate;

    @Lob()
	private String description;

	@Column(name="email_author")
	private String emailAuthor;

	@Column(name="project_name")
	private String projectName;

    public Commit() {
    }

	public String getChangeset() {
		return this.changeset;
	}

	public void setChangeset(String changeset) {
		this.changeset = changeset;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Timestamp getCommitDate() {
		return this.commitDate;
	}

	public void setCommitDate(Timestamp commitDate) {
		this.commitDate = commitDate;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmailAuthor() {
		return this.emailAuthor;
	}

	public void setEmailAuthor(String emailAuthor) {
		this.emailAuthor = emailAuthor;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}