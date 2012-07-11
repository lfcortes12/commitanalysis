package unal.edu.co.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the commit_stat database table.
 * 
 */
@Entity
@Table(name="commit_stat")
public class CommitStat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String changeset;

	@Column(name="project_name")
	private String projectName;

    @Lob()
	private byte[] stat;

    public CommitStat() {
    }

	public String getChangeset() {
		return this.changeset;
	}

	public void setChangeset(String changeset) {
		this.changeset = changeset;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public byte[] getStat() {
		return this.stat;
	}

	public void setStat(byte[] stat) {
		this.stat = stat;
	}

}