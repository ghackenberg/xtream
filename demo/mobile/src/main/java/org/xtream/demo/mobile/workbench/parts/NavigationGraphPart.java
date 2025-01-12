package org.xtream.demo.mobile.workbench.parts;

import java.awt.Color;
import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.xtream.core.model.Component;
import org.xtream.core.model.Container;
import org.xtream.core.model.Port;
import org.xtream.core.workbench.Event;
import org.xtream.core.workbench.Part;
import org.xtream.core.workbench.controls.ImagePanel;
import org.xtream.core.workbench.events.JumpEvent;
import org.xtream.core.workbench.events.SelectionEvent;
import org.xtream.demo.mobile.datatypes.Edge;
import org.xtream.demo.mobile.datatypes.Graph;
import org.xtream.demo.mobile.datatypes.Node;

public class NavigationGraphPart<T extends Component> extends Part<T>
{
	
	private ImagePanel image;
	
	private Graph graph;
	private Set<Float> colorValues;
	private Map<Port<?>,Float> map = new HashMap<Port<?>,Float>();
	
	private Container root = null;
	private int timepoint = 0;
	
	public NavigationGraphPart(Graph graph)
	{
		this(graph, 0, 0);
	}
	public NavigationGraphPart(Graph graph, int x, int y)
	{
		this(graph, x, y, 1, 1);
	}
	public NavigationGraphPart(Graph graph, int x, int y, int width, int height)
	{
		super("Navigation graph", NavigationGraphPart.class.getClassLoader().getResource("parts/navigation_graph.png"), x, y, width, height);
		
		this.graph = graph;
		
		image = new ImagePanel();
		
		getPanel().add(image);
	}
	
	@Override
	public void handle(Event<T> event)
	{
		if (event instanceof SelectionEvent)
		{
			SelectionEvent<T> selection = (SelectionEvent<T>) event;
			
			root = selection.getElementByClass(Container.class);
			
			update();
		}
		else if (event instanceof JumpEvent)
		{
			JumpEvent<T> jump = (JumpEvent<T>) event;
			
			timepoint = jump.getTimepoint();
			
			update();
		}
	}
	
	public void update() 
	{
		try
		{
			colorValues = new HashSet<Float>();
			
			PrintStream dot = new PrintStream(new File("MobilityGraph.dot"));
			
			dot.append("digraph G {\n");
			dot.append("\tnormalize = 2.;\n");
			dot.append("\toverlap = false;\n");
			dot.append("\trankdir = LR;\n");
			dot.append("\tfontname = \"Calibri\";\n");
			dot.append("\tfontsize = 12;\n");
			dot.append("\tnode [fontname = \"Calibri\", fontsize = 10];\n");
			dot.append("\tedge [fontname = \"Calibri\", fontsize = 9];\n");
			
			if (graph != null)
			{	
				dot.append("subgraph Map {\n");
				
				for (Edge e : graph.getEdges())
				{
					Node source = graph.getNode(e.getSource());
					Node target = graph.getNode(e.getTarget());
					
					double length = graph.getEdgeDistance(source, target);
					
					if (!source.equals(target))
					{
						dot.append("\t\"" + e.getSource() + "\" -> \"" + e.getTarget() + "\" [len = \"" + length + "\", color = grey, style = invis, arrowhead = vee, penwidth = " + e.getWeight() + "];\n");
					}
				}
				
				double nodeMaxWeight = 0.0;
				
				for (Node v : graph.getNodes()) 
				{
					if (nodeMaxWeight < Double.parseDouble(v.getWeight())) 
					{
						nodeMaxWeight = Double.parseDouble(v.getWeight());
					}
				}
				
				for (Node v : graph.getNodes()) 
				{
					int r, g, b;
					r = g = b = (int) (127+(Double.parseDouble(v.getWeight())/nodeMaxWeight)*128);
					//String color = "#" + Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b);
					String color = String.format("#%02x%02x%02x", r, g, b);
					
					if (v.getName().equals("Origin") || v.getName().equals("Destination"))
					{
						dot.append("\t\"" + v.getName() + "\" [label = \"" + v.getName() + "\", shape = ellipse, color = black, fillcolor=\"" + color +"\"];\n");
					}
					else 
					{
						dot.append("\t\"" + v.getName() + "\" [label = \"\", shape = point, pendwidth = 0.25, color = black, fillcolor=\"" + color +"\"];\n");
					}
					// dot.append("\t\"" + v.getName() + "\" [label = \"\", shape = point, color=\"" + color +"\", pos=\""+ v.getXpos() + "," + v.getYpos() + "" + "\"];\n");
				}
				
				dot.append("}\n");
					
				dot.append("subgraph Vehicle {\n");
	
				// TODO [Dominik] Bug: not rendering when sequence is not yet complete
	
				for (Component component : root.getChildrenByClass(Component.class)) 	
				{
					Set<String> set = new HashSet<String>();
					
					for (Port<?> port : component.getChildrenByClass(Port.class))
					{
						if (port.getName().equals("positionOutput") && !(set.contains(component.getName())))
						{	
							set.add(component.getName());
							
							if (!map.containsKey(port))
							{
								map.put(port, generateColor(0));
							}
							
							double width = 0.1;
							
							Edge previous = (Edge) port.get(getState(), 0);
							
							@SuppressWarnings("unused")
							int step = 0;
							
							for (int i = 0; i < timepoint; i++)
							{	
								Edge current = (Edge) port.get(getState(), i);
								
								if (previous != current || ((i) == (timepoint-1)))
								{	
									width+= 0.1;
									step++;
									
									Color colorPartitioning = new Color(Color.HSBtoRGB(map.get(port), 1.f, 0.75f));
									String color = String.format("#%02x%02x%02x", colorPartitioning.getRed(), colorPartitioning.getGreen(), colorPartitioning.getBlue());
									
									if (!previous.getSource().equals(previous.getTarget())) 
									{
										dot.append("\t\"" + previous.getSource() + "\" -> \"" + previous.getTarget() + "\" [arrowsize = 0.4, color = \"" + color + "\", arrowhead = vee, fontcolor = \"" + color + "\", penwidth = " + width + "];\n");
										//dot.append("\t\"" + previous.getSource() + "\" -> \"" + previous.getTarget() + "\" [label = \"" + "(" + ((i-step)>0?(i-step):0) + "-" + (i-1) + ")" + "\", color = \"" + color + "\", fontcolor = \"" + color + "\", penwidth = " + width + "];\n");
									}
									width = 0.1;
									step = 0;
									previous = current;
								}
								else 
								{
									width+= 0.1;
									step++;
								}
							}
						}
					}
				}
				
				dot.append("}\n");
			}
			
			dot.append("}");
			
			dot.close();
		
			Runtime.getRuntime().exec("dot -Kneato -Gdpi=190 -Gratio=1.0 -Tpng -oMobilityGraph.png MobilityGraph.dot").waitFor();
			Runtime.getRuntime().exec("dot -Kneato -Gdpi=190 -Gratio=1.0 -Tsvg -oMobilityGraph.svg MobilityGraph.dot").waitFor();
			
			image.setImage(ImageIO.read(new File("MobilityGraph.png")));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public float generateColor(float threshold) 
	{
		float color = (float) Math.random();
		
		for (float item : colorValues)
		{
			if (Math.abs(color-item) <= threshold)
			{
				return generateColor(threshold);
			}
		}
		
		colorValues.add(color);
		
		return color;
	}

}
