package unal.edu.co.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the commit_diff database table.
 * @author Fernando Cortes
 *
 */
@Entity
@Table(name="commit_diff")
public class CommitDiff implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String changeset;

    @Lob()
	private byte[] diff;

	@Column(name="project_name")
	private String projectName;

    public CommitDiff() {
    }

	public String getChangeset() {
		return this.changeset;
	}

	public void setChangeset(String changeset) {
		this.changeset = changeset;
	}

	public byte[] getDiff() {
		return this.diff;
	}

	public void setDiff(byte[] diff) {
		this.diff = diff;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}