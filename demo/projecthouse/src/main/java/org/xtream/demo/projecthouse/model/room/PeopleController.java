package org.xtream.demo.projecthouse.model.room;

import java.io.File;
import java.net.URISyntaxException;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.demo.projecthouse.model.CSVFileWithOneKey;

public class PeopleController extends Component {
	public CSVFileWithOneKey csvData;
	
	public Port<Double> probabilityOutput = new Port<>();
	
	public PeopleController(String filename) {
		super();
		try {
			File file = new File(getClass().getResource(filename).toURI());
			csvData = new CSVFileWithOneKey(file, 1);
		} catch (URISyntaxException e) {
			throw new RuntimeException("Problems creating file for " + filename);
		}
	}

	public Expression<Double> probabilityExpression = new Expression<Double>(probabilityOutput) {
		
		@Override
		protected Double evaluate(State state, int timepoint) {
			return csvData.get(timepoint, 1);
		}
	};
}
