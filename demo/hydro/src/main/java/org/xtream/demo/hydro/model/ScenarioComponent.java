package org.xtream.demo.hydro.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.containers.Component;

import au.com.bytecode.opencsv.CSVReader;

public class ScenarioComponent extends Component
{
	
	// Constructors
	
	public ScenarioComponent(String inflowFile, String priceFile)
	{
		try
		{
			// Read inflows
			
			CSVReader inflowReader = new CSVReader(new FileReader(inflowFile), ';');
			
			for (String[] line : inflowReader.readAll())
			{
				inflows.add(Double.parseDouble(line[0].replace(',','.')));
			}
			
			inflowReader.close();
			
			// Read prices
			
			CSVReader priceReader = new CSVReader(new FileReader(priceFile), ';');
			
			for (String[] line : priceReader.readAll())
			{
				prices.add(Double.parseDouble(line[0].replace(',','.')));
			}
			
			priceReader.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	// Parameters
	
	protected Vector<Double> inflows = new Vector<>();
	protected Vector<Double> prices = new Vector<>();
	
	// Ports
	
	public Port<Double> inflowOutput = new Port<>();
	public Port<Double> priceOutput = new Port<>();
	
	// Charts
	
	public Chart inflowChart = new Timeline(inflowOutput);
	
	// Expressions
	
	public Expression<Double> inflowExpression = new Expression<Double>(inflowOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return inflows.get(timepoint);
		}
	};
	public Expression<Double> priceExpression = new Expression<Double>(priceOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return prices.get(timepoint);
		}

	};

}
