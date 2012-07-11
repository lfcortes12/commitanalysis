package unal.edu.co.main;

import java.util.List;

import unal.edu.co.dao.utils.Tokenizer;
import unal.edu.co.dao.utils.Utils;
import unal.edu.co.entities.CommitDiff;
import unal.edu.co.entities.ConcreteDiff;
import unal.edu.co.service.CommitDiffService;
import unal.edu.co.service.ConcreteDiffService;

public class Main {
	
	static CommitDiffService service;
	static ConcreteDiffService serviceConcreteCommitDiff;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		service = CommitDiffService.getInstance();
		serviceConcreteCommitDiff = ConcreteDiffService.getInstance();
		for (int i = 1; i <= 4172; i++ ) {
			List<CommitDiff> searched = service.findByChangeSetPaging(i, 50);
			for (CommitDiff commitDiff : searched) {
				String proccessedDiff = Tokenizer.getTokens(Utils.getAddedLines(new String(commitDiff.getDiff())));
				ConcreteDiff concreteDiff = new ConcreteDiff();
				concreteDiff.setChangeset(commitDiff.getChangeset());
				concreteDiff.setProcessedDiff(proccessedDiff.getBytes());
				serviceConcreteCommitDiff.save(concreteDiff);
			}
		}

	}

}
