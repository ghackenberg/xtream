package org.xtream.demo.street.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;

public class NetComponent extends Component
{
	
	// Ports
	
	public Port<Double> balance = new Port<Double>();
	
	public Port<Double> load_00 = new Port<Double>();
	public Port<Double> load_01 = new Port<Double>();
	
	// Charts
	
	public Chart balanceChart = new Chart(balance);
	
	public Chart balancePreview = new Chart(balance); 
	
	// Expressions
	
	public Expression<Double> balanceExpr = new Expression<Double>(balance)
	{
		@Override public Double evaluate(int timepoint)
		{
			return load_00.get(timepoint) + load_01.get(timepoint);
		}
	};

}