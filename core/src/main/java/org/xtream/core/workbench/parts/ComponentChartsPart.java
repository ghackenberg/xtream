package org.xtream.core.workbench.parts;

import java.awt.BasicStroke;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.RectangleInsets;
import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Container;
import org.xtream.core.model.Port;
import org.xtream.core.model.charts.Histogram;
import org.xtream.core.model.charts.Series;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.workbench.Event;
import org.xtream.core.workbench.Part;
import org.xtream.core.workbench.events.JumpEvent;
import org.xtream.core.workbench.events.SelectionEvent;

public class ComponentChartsPart<T extends Component> extends Part<T>
{
	
	private static int PADDING = 0;
	private static int STROKE = 3;
	
	private JPanel panel;
	private Map<Chart, JFreeChart> charts = new HashMap<>();
	private Map<Chart, ValueMarker> markers = new HashMap<>();
	private Container container;
	private int timepoint = -1;
	
	public ComponentChartsPart()
	{
		this(0, 0);
	}
	public ComponentChartsPart(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public ComponentChartsPart(int x, int y, int width, int height)
	{
		super("Component charts", ComponentChartsPart.class.getClassLoader().getResource("parts/component_charts.png"), x, y, width, height);
		
		panel = new JPanel();
		
		getPanel().add(panel);
	}
	
	@Override
	public void handle(Event<T> event)
	{
		if (event instanceof SelectionEvent)
		{
			SelectionEvent<T> selection = (SelectionEvent<T>) event;
			
			Container container = selection.getElementByClass(Container.class);
			
			update(container);
		}
		else if (event instanceof JumpEvent)
		{
			JumpEvent<T> jump = (JumpEvent<T>) event;
			
			int timepoint = jump.getTimepoint();
			
			update(timepoint);
		}
	}
	
	public void update(Container container)
	{
		this.container = container;
		
		// Remove old charts
		
		panel.removeAll();

		// Charts
		
		if (container.getChildrenByClass(Timeline.class).size() > 0)
		{	
			// Calculate grid layout
			
			int cols = (int) Math.ceil(Math.sqrt(container.getChildrenByClass(Timeline.class).size()));
			int rows = (int) Math.ceil(Math.sqrt(container.getChildrenByClass(Timeline.class).size()));
			
			panel.setLayout(new GridLayout(cols, rows));
			
			// Show charts
			
			for (Chart definition : container.getChildrenByClass(Chart.class))
			{
				JFreeChart jfreechart = getChart(definition);
				
				panel.add(new ChartPanel(jfreechart));
				
				charts.put(definition, jfreechart);
			}
			
			// Update datasets
			
			update(timepoint);
		}
		
		// Repaint frame
		
		panel.updateUI();
	}
	
	public void update(int timepoint)
	{
		if (timepoint == -1 || container == null)
		{
			return;
		}
		
		this.timepoint = timepoint;
		
		for (Chart definition : container.getChildrenByClass(Chart.class))
		{
			if (definition instanceof Timeline)
			{
				DefaultXYDataset dataset = new DefaultXYDataset();
				
				Timeline timeline = (Timeline) definition;
				
				for (Series<Double> series : timeline.getSeries())
				{
					double[][] data = new double[2][getState().getTimepoint() + 1];
					
					for (int i = 0; i <= getState().getTimepoint(); i++)
					{
						data[0][i] = i;
						data[1][i] = series.getPort().get(getState(), i);
					}
					
					dataset.addSeries(series.getLabel(), data);
				}
				
				getChart(definition).getXYPlot().setDataset(dataset);
				
				getMarker(definition).setValue(timepoint);
			}
			else if (definition instanceof Histogram)
			{
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				
				Histogram<?> histogram = (Histogram<?>) definition;
				
				for (Port<?> port : histogram.getPorts())
				{
					Map<String, Integer> map = new HashMap<String, Integer>();
					
					for (int i = 0; i <= getState().getTimepoint(); i++)
					{
						if (!(map.containsKey(port.get(getState(), i).toString())))
						{
							map.put(port.get(getState(), i).toString(), 1);
						}
						else 
						{
							map.put(port.get(getState(), i).toString(), 1 + map.get(port.get(getState(), i).toString()));
						}
					}
					
					for (String i : map.keySet())
					{
						dataset.setValue(map.get(i), i, "value");
					}
				}
				
				getChart(definition).getCategoryPlot().setDataset(dataset);
			}
			else
			{
				throw new IllegalStateException();
			}
		}
		
		// Repaint frame
		
		panel.updateUI();
	}
	
	private JFreeChart getChart(Chart definition)
	{
		JFreeChart jfreechart = charts.get(definition);
		
		if (jfreechart == null)
		{
			String label = definition.getLabel() != null ? definition.getLabel() : definition.getName();
			String domain = definition.getDomain();
			String range = definition.getRange();
					
			if (definition instanceof Timeline)
			{
				
				jfreechart = ChartFactory.createXYLineChart(label, domain, range, new DefaultXYDataset(), PlotOrientation.VERTICAL, true, true, false);
				
				Timeline timeline = (Timeline) definition;
				
				for (int series = 0; series < timeline.getSeries().length; series++)
				{
					jfreechart.getXYPlot().getRenderer().setSeriesStroke(series, new BasicStroke(STROKE));
				}
				
				((NumberAxis) jfreechart.getXYPlot().getRangeAxis()).setAutoRangeIncludesZero(false);
				
				jfreechart.getXYPlot().addDomainMarker(getMarker(definition));
			}
			else if (definition instanceof Histogram)
			{
				jfreechart = ChartFactory.createBarChart(label, domain, range, new DefaultCategoryDataset(), PlotOrientation.VERTICAL, true, true, false);
			}
			else
			{
				throw new IllegalStateException();
			}
			
			jfreechart.setAntiAlias(true);
			jfreechart.setTextAntiAlias(true);
			jfreechart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
			
			charts.put(definition, jfreechart);
		}
			
		return jfreechart;
	}
	
	private ValueMarker getMarker(Chart chart)
	{
		ValueMarker marker = markers.get(chart);
		
		if (marker == null)
		{
			marker = new ValueMarker(0);
			marker.setStroke(new BasicStroke(STROKE));
			
			markers.put(chart, marker);
		}
		
		return marker;
	}

}
