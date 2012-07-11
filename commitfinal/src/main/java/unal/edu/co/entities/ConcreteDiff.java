package unal.edu.co.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the concrete_diff database table.
 * 
 */
@Entity
@Table(name="concrete_diff")
public class ConcreteDiff implements Serializable {
	private static final long serialVersionUID = 1L;

	/*@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String changeset;*/
	
	@Id
	@Column(columnDefinition = "BINARY(32)", length = 32)
	private String changeset;
	
    @Lob()
	@Column(name="processed_diff")
	private byte[] processedDiff;

    public ConcreteDiff() {
    }

	public String getChangeset() {
		return this.changeset;
	}

	public void setChangeset(String changeset) {
		this.changeset = changeset;
	}

	public byte[] getProcessedDiff() {
		return this.processedDiff;
	}

	public void setProcessedDiff(byte[] processedDiff) {
		this.processedDiff = processedDiff;
	}

}